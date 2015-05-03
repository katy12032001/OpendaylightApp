package org.opendaylight.controller.dynamic_route.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opendaylight.controller.Mac_Mapping.internal.Mac_Mapping;
import org.opendaylight.controller.dynamic_route.DynamicRoute;
import org.opendaylight.controller.dynamic_route.HandlerServiceLocalRequest;
import org.opendaylight.controller.dynamic_route.HandlerServicePriorityList;
import org.opendaylight.controller.dynamic_route.ManualRateLimitServiceRemoteRequest;
import org.opendaylight.controller.evaluator.EventDataTuple;

import net.floodlightcontroller.flowclassification.FlowInfo;
import net.floodlightcontroller.flowclassification.FlowRuleTuple;

import org.opendaylight.controller.flowcheck.FlowCheckService;
import org.opendaylight.controller.forwardingrulesmanager.IForwardingRulesManager;

import net.floodlightcontroller.authentication.UserData;

import org.opendaylight.controller.hosttracker.IfIptoHost;
import org.opendaylight.controller.monitor.SwitchPortStatTuple;
import org.opendaylight.controller.pushflow.PushFlowInfo;
import org.opendaylight.controller.sal.core.Edge;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.ARP;
import org.opendaylight.controller.sal.packet.Ethernet;
import org.opendaylight.controller.sal.packet.ICMP;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.sal.packet.IPv4;
import org.opendaylight.controller.sal.packet.Packet;
import org.opendaylight.controller.sal.packet.PacketResult;
import org.opendaylight.controller.sal.packet.RawPacket;
import org.opendaylight.controller.sal.packet.TCP;
import org.opendaylight.controller.sal.utils.NetUtils;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.topologymanager.ITopologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForwardingPkt implements IListenDataPacket{

    private static final Logger log = LoggerFactory
            .getLogger(ForwardingPkt.class);
    
    private final static String MAC_ADDRESS_BROADCAST = "ff:ff:ff:ff:ff:ff";
    // SAL service
    private IDataPacketService dataPacketService;
    private IFlowProgrammerService flowProgrammerService;
    private ISwitchManager switchManager;
    private ITopologyManager topologyManager;
    private IfIptoHost hostTracker;
    private IForwardingRulesManager forwardingRulesManager;
    private Mac_Mapping macmappingservice;
    // Topology Data
    private Map<Node, Set<Edge>> Topo;
    static public List<NodeConnector> srcSwPortList = new ArrayList<NodeConnector>();
    static public HashMap<NodeConnector, List<NodeConnector>> allLinkPairs = new HashMap<NodeConnector, List<NodeConnector>>();
    private DynamicRoute dr = new DynamicRoute();

    // Monitor info.
    public static Object switchPortLock = new Object();
    public List<SwitchPortStatTuple> switchPortStat = new ArrayList<>();

    // User Register info.
    public List<UserData> userList = new ArrayList<>();

    // Handler
    private static ServerSocket handlerServiceLoSock;
    private static final int handlerServiceLoPort = 9004;
    private static Socket handlerClient;
    public static HashMap<String, List<FlowInfo>> map_outQueue = new HashMap<String, List<FlowInfo>>();
    public static Object mapqueueLock = new Object();

    private static ServerSocket manualRateLimitServerSock;
    private static final int manualRateLimitServerPort = 9010;
    private static Socket manualRateLimitServerClient;
    
    public static HashMap<String, NodeConnector> mac_node_mapping = new HashMap<String, NodeConnector>();
    public static HashMap<String, String> ip_mac_mapping = new HashMap<String, String>();
    
    public static Object ratelimite_addflowLock = new Object();
    //authentication server ip
    public static String authIP;
    
    // Convert address
    static private InetAddress intToInetAddress(int i) {
        byte b[] = new byte[] { (byte) ((i >> 24) & 0xff),
                (byte) ((i >> 16) & 0xff), (byte) ((i >> 8) & 0xff),
                (byte) (i & 0xff) };
        InetAddress addr;
        try {
            addr = InetAddress.getByAddress(b);
        } catch (UnknownHostException e) {
            return null;
        }
        return addr;
    }

    public ForwardingPkt() {
    }

    /**
     * Sets a reference to the requested DataPacketService
     */
    void setDataPacketService(IDataPacketService s) {
        log.trace("Set DataPacketService.");

        dataPacketService = s;
    }

    /**
     * Unsets DataPacketService
     */
    void unsetDataPacketService(IDataPacketService s) {
        log.trace("Removed DataPacketService.");

        if (dataPacketService == s) {
            dataPacketService = null;
        }
    }

    /**
     * Sets a reference to the requested FlowProgrammerService
     */
    void setFlowProgrammerService(IFlowProgrammerService s) {
        log.trace("Set FlowProgrammerService.");

        flowProgrammerService = s;
    }

    /**
     * Unsets FlowProgrammerService
     */
    void unsetFlowProgrammerService(IFlowProgrammerService s) {
        log.trace("Removed FlowProgrammerService.");

        if (flowProgrammerService == s) {
            flowProgrammerService = null;
        }
    }

    /**
     * Sets a reference to the requested SwitchManagerService
     */
    void setSwitchManagerService(ISwitchManager s) {
        log.trace("Set SwitchManagerService.");

        switchManager = s;
    }

    /**
     * Unsets SwitchManagerService
     */
    void unsetSwitchManagerService(ISwitchManager s) {
        log.trace("Removed SwitchManagerService.");

        if (switchManager == s) {
            switchManager = null;
        }
    }

    /**
     * Sets a reference to the requested SwitchManagerService
     */
    void setTopologyManagerService(ITopologyManager s) {
        log.trace("Set TopologyManagerService.");

        topologyManager = s;
    }

    /**
     * Unsets SwitchManagerService
     */
    void unsetTopologyManagerService(ITopologyManager s) {
        log.trace("Removed TopologyManagerService.");

        if (topologyManager == s) {
            topologyManager = null;
        }
    }

    /**
     * Sets a reference to the requested HostTrackerService
     */
    public void setHostTracker(IfIptoHost hostTracker) {
        log.debug("Setting HostTracker");
        this.hostTracker = hostTracker;
    }
    
    /**
     * Unsets HostTrackerService
     */
    public void unsetHostTracker(IfIptoHost hostTracker) {
        if (this.hostTracker == hostTracker) {
            this.hostTracker = null;
        }
    }

    /**
     * Sets a reference to the requested ForwardingRulesManagerService
     */
    public void setForwardingRulesManager(
            IForwardingRulesManager forwardingRulesManager) {
        log.debug("Setting forwardingRulesManager");
        this.forwardingRulesManager = forwardingRulesManager;
    }

    /**
     * Unsets ForwardingRulesManagerService
     */
    public void unsetForwardingRulesManager(
            IForwardingRulesManager forwardingRulesManager) {
        if (this.forwardingRulesManager == forwardingRulesManager) {
            this.forwardingRulesManager = null;
        }
    }

    /**
     * Sets a reference to the requested Mac_Mapping_Service
     */
    public void setMac_Mapping_Service(Mac_Mapping mac_mapping_service) {
        this.macmappingservice = mac_mapping_service;
    }

    /**
     * Unsets Mac_Mapping_Service
     */
    public void unsetMac_Mapping_Service(Mac_Mapping mac_mapping_service) {
        if (this.macmappingservice == mac_mapping_service) {
            this.macmappingservice = null;
        }
    }
    
    // Parse Topology
    public void ParseTopology() {
        // Init.
        srcSwPortList.clear();
        allLinkPairs.clear();

        // Get topology
        Topo = topologyManager.getNodeEdges();
        Iterator<Node> iter = Topo.keySet().iterator();
        
        System.out.println("<<Log org.opendaylight.controller.dynamic_route.internal.ForwardingPkt>>+\n");
        System.out.println("Topology Parse=>");
        // Load topology into database
        while (iter.hasNext()) {
            Object key = iter.next();
            Set<Edge> val = Topo.get(key);
            for (Edge eg : val) {
                NodeConnector n1 = eg.getHeadNodeConnector();
                NodeConnector n2 = eg.getTailNodeConnector();
                System.out.println("1." + n1.getNode() + " "
                        + n1.getNodeConnectorIDString());
                System.out.println("2." + n2.getNode() + " "
                        + n2.getNodeConnectorIDString());
                System.out
                        .println("===========================================");
                if (srcSwPortList.contains(n1) == false)
                    srcSwPortList.add(n1);
                List<NodeConnector> allDestsLR = allLinkPairs.get(n1);
                if (allDestsLR == null) {
                    allDestsLR = new ArrayList<NodeConnector>();
                    allLinkPairs.put(n1, allDestsLR);
                }
                if (allDestsLR.contains(n2) == false)
                    allDestsLR.add(n2);
                if (srcSwPortList.contains(n2) == false)
                    srcSwPortList.add(n2);
                List<NodeConnector> allDestsRL = allLinkPairs.get(n2);
                if (allDestsRL == null) {
                    allDestsRL = new ArrayList<NodeConnector>();
                    allLinkPairs.put(n2, allDestsRL);
                }
                if (allDestsRL.contains(n1) == false)
                    allDestsRL.add(n1);
            }
        }
    }

    public HashMap<NodeConnector, List<NodeConnector>> getParseTopo() {
        return ForwardingPkt.allLinkPairs;
    }

    public List<NodeConnector> getsrcSwPortList() {
        return ForwardingPkt.srcSwPortList;
    }

    @Override
    public PacketResult receiveDataPacket(RawPacket inPkt) {
        // get time
        long dijkstraRoute1 = System.nanoTime();

        // The connector, the packet came from ("port")
        // The node that received the packet ("switch")
        NodeConnector ingressConnector = inPkt.getIncomingNodeConnector();
        Node node = ingressConnector.getNode();
        System.out.println("[INFO] org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal+\n"
                        + "Packet from => Switch:" + node.getNodeIDString()+ " ; Port:"+ ingressConnector.getNodeConnectorIDString());

        // Use DataPacketService to decode the packet.
        Packet pkt = dataPacketService.decodeDataPacket(inPkt);
        if (pkt instanceof Ethernet) {
            Object nextPak = pkt.getPayload();
            
            //handle arp pkt
            if (nextPak instanceof ARP) {
                boolean check = handleARPPkt((ARP) nextPak, inPkt.getIncomingNodeConnector());
                
                System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal");
                System.out.println("Arp Trans");
                NodeConnector b = mac_node_mapping.get(getMacAddress(((Ethernet) pkt).getDestinationMACAddress()));
                
                //broadcast
                if ((check == true) ||(check == false && b==null)){
                	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"+"->ARP Broadcast");
                	for (Node nn : switchManager.getNodes()) {
	                    for(NodeConnector nc : switchManager.getUpNodeConnectors(nn)){
	                        if(!nc.toString().contains("SW")&&topologyManager.isInternal(nc)==false && !nc.toString().equalsIgnoreCase(inPkt.getIncomingNodeConnector().toString())){                            
	                        	
	                        	System.out.println("(BroadCast)Send ARP Pkt to " + nc);
	                            inPkt.setOutgoingNodeConnector(nc);
	                            dataPacketService.transmitDataPacket(inPkt);
	                        }
	                    }
	                }
                }
                
                //unicast
                else{       
                	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal");
                	System.out.println("Send ARP Pkt to " + b);
                	inPkt.setOutgoingNodeConnector(b);
                	dataPacketService.transmitDataPacket(inPkt);
                }
                
                
            }
            
            //handle ipv4 : broadcast
            else if((nextPak instanceof IPv4) && (MAC_ADDRESS_BROADCAST.equalsIgnoreCase(getMacAddress(((Ethernet) pkt).getDestinationMACAddress())))){
            	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"+"->IPv4 BroadCast");
            	for (Node nn : switchManager.getNodes()) {
                    for(NodeConnector nc : switchManager.getUpNodeConnectors(nn)){
                        if(!nc.toString().contains("SW")&&topologyManager.isInternal(nc)==false && !nc.toString().equalsIgnoreCase(inPkt.getIncomingNodeConnector().toString())){                            
                        	
                        	System.out.println("(BroadCast)Send IPV4 Pkt to " + nc);
                            inPkt.setOutgoingNodeConnector(nc);
                            dataPacketService.transmitDataPacket(inPkt);
                        }
                    }
                }
            }
            
            //handle ipv4
            else if (nextPak instanceof IPv4) {
            	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                                + "-> Ipv4 Trans");
                IPv4 pkt_v4 = (IPv4) nextPak;

                // Get ipv4 src & ipv4 dst address
                String nwSrc = ((pkt_v4.getSourceAddress() >> 24) & 0xFF) + "."
                        + ((pkt_v4.getSourceAddress() >> 16) & 0xFF) + "."
                        + ((pkt_v4.getSourceAddress() >> 8) & 0xFF) + "."
                        + (pkt_v4.getSourceAddress() & 0xFF);
                String nwDst = ((pkt_v4.getDestinationAddress() >> 24) & 0xFF)
                        + "." + ((pkt_v4.getDestinationAddress() >> 16) & 0xFF)
                        + "." + ((pkt_v4.getDestinationAddress() >> 8) & 0xFF)
                        + "." + (pkt_v4.getDestinationAddress() & 0xFF);

                // Check whether user have registered
                int matchFlag = 0;

                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getIpAddress().equalsIgnoreCase(nwDst)
                            || userList.get(i).getIpAddress()
                                    .equalsIgnoreCase(nwSrc) ){
                        matchFlag = 1;
                        break;
                    }
                }

                // wifi
                String str_tmp[] = ingressConnector.toString().split("@");
                
                System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                		+"-> Check incoming port : " +str_tmp[0]);
                
                if (str_tmp[0].contains("22@") || str_tmp[0].contains("1@")) {
                    matchFlag = 1;
                    System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                                    + "-> Wifi Match");
                }

                if (matchFlag == 1 || nwDst.equalsIgnoreCase(authIP)
                        || nwSrc.equalsIgnoreCase(authIP) || nwSrc.equalsIgnoreCase("0.0.0.0") || nwDst.equalsIgnoreCase("255.255.255.255")) {
                	
                	boolean check;
                	
                	//broadcast(dst=255.255.255.255)
                	if(nwDst.equalsIgnoreCase("255.255.255.255")) {
                		System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                                + "-> IP for broadcast");
                		check = false;
                	}
                	
                	//others
                	else{
                		System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                                + "-> IP in the UserList Or Pkt send to authentication server");
                		check = handleIPv4Pkt((IPv4) nextPak,
                            inPkt.getIncomingNodeConnector(),
                            ((Ethernet) pkt).getDestinationMACAddress(),
                            ((Ethernet) pkt).getSourceMACAddress());
                	}
                	
                	//unicast (check - true)
                    if (check == true) {
                    	inPkt.setOutgoingNodeConnector((mac_node_mapping.get(getMacAddress(((Ethernet) pkt).getDestinationMACAddress()))));
                        dataPacketService.transmitDataPacket(inPkt);
                        
                    } 
                    //broadcast (check - false)
                    else {
                       
                    	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                                + "(BroadCast)IPV4 Pkt");
                        for (Node nn : switchManager.getNodes()) {
                            for(NodeConnector nc : switchManager.getUpNodeConnectors(nn)){
                                if(!nc.toString().contains("SW")&&topologyManager.isInternal(nc)==false && !nc.toString().equalsIgnoreCase(inPkt.getIncomingNodeConnector().toString())){                            
                                	System.out.println("(BroadCast)Send IPV4 Pkt to " + nc);
                                    inPkt.setOutgoingNodeConnector(nc);
                                    dataPacketService.transmitDataPacket(inPkt);
                                }
                            }
                        }
                    }
                    long dijkstraRoute2 = System.nanoTime();
                    System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                                    + "-> whole_time : "
                                    + (dijkstraRoute2 - dijkstraRoute1));
                    
                    
                    
                    return PacketResult.IGNORED;
                }
            }
            else if (nextPak instanceof ICMP) {
                System.out.println("ICMP");
            }
            
            else if (nextPak instanceof org.opendaylight.controller.sal.packet.LLDP) {
                System.out.println("LLDP");
            }
        }
        return PacketResult.IGNORED;

    }

    private boolean handleARPPkt(ARP pkt, NodeConnector incomingNodeConnector) {
        String src_adr = null;  
        src_adr = getMacAddress(pkt.getSenderHardwareAddress());
        if (mac_node_mapping.get(src_adr) == null) {
        	/*
        	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
        			+" -> " + src_adr + " == mac byte== " + incomingNodeConnector.toString());*/
            mac_node_mapping.put(src_adr, incomingNodeConnector);
        } else {
        	/*
        	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
        			+" -> " + src_adr + " == mac byte2== " + incomingNodeConnector.toString());*/
            String n1 = incomingNodeConnector.toString();
            NodeConnector n2 = mac_node_mapping.get(src_adr);
            if (n1.equalsIgnoreCase(n2.toString()) == false) {
                mac_node_mapping.put(src_adr, incomingNodeConnector);
            }
        }
        if(pkt.getOpCode()== ARP.REQUEST) return true;
        else return false;
    }

    private boolean handleIPv4Pkt(IPv4 pkt, NodeConnector incomingNodeConnector, byte[] dstMAC, byte[] srcMAC) {
    	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                        + "-> Dst MAC : "+ getMacAddress(dstMAC)+"\n"        
                        + "-> Forwarding map (PORT): " + mac_node_mapping.get(getMacAddress(dstMAC)) +"\n"
                        + "-> Map_Mapping map (PORT): " + macmappingservice.get_nodeconnector(getMacAddress(dstMAC)) +"\n");
        
        if (mac_node_mapping.get(getMacAddress(dstMAC)) != null || macmappingservice.get_nodeconnector(getMacAddress(dstMAC))!=null) {
        	
        	//store ip address &mac address mapping
        	if(ip_mac_mapping.get(NetUtils.getInetAddress(pkt.getDestinationAddress()).getHostAddress())==null){
        		ip_mac_mapping.put(NetUtils.getInetAddress(pkt.getDestinationAddress()).getHostAddress(),getMacAddress(dstMAC));
        	}
        	if(ip_mac_mapping.get(NetUtils.getInetAddress(pkt.getSourceAddress()).getHostAddress())==null){
        		ip_mac_mapping.put(NetUtils.getInetAddress(pkt.getSourceAddress()).getHostAddress(),getMacAddress(srcMAC));
        	}
        	
        	//check whether dst mac address is mapped to defined port
        	//true : handle & false : broadcast
        	NodeConnector dst_nc = mac_node_mapping.get(getMacAddress(dstMAC));
            if (macmappingservice.get_nodeconnector(getMacAddress(dstMAC))!=null) 
                dst_nc = macmappingservice.get_nodeconnector(getMacAddress(dstMAC));
            if (incomingNodeConnector.toString().compareTo(dst_nc.toString()) == 0)
                return false;
            if (incomingNodeConnector.getNode().toString().compareTo(dst_nc.getNode().toString()) != 0) {
                // Update Topology
                Topo.clear();
                ParseTopology();

                // Get Routing Path
                System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                                + "-> HandleIPv4Pkt : Cross two upper");
                //long dynamicRoute1 = System.nanoTime();
                dr.generatePath(incomingNodeConnector, dst_nc, switchPortStat);
                //long dynamicRoute2 = System.nanoTime();
               
                synchronized(mapqueueLock){
	                if(map_outQueue.get(dst_nc.getNode().toString())==null){
	                	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
	                            + "-> Save FlowIno to database_0\n");
	                	List<NodeConnector> f_path = dr.getPath();
	                    programFlow(pkt, incomingNodeConnector, f_path.get(0), dstMAC,
	                            srcMAC, false, 0);
	                    for (int i = 1; i < f_path.size() - 1; i = i + 2) {
	                        programFlow(pkt, f_path.get(i), f_path.get(i + 1), dstMAC,
	                                srcMAC, false, 0);
	                    }
	                    programFlow(pkt, f_path.get(f_path.size() - 1), dst_nc, dstMAC,
	                            srcMAC, false, 0);
	                    
	                    List<NodeConnector> f_path2 = new ArrayList<NodeConnector>();
	                    f_path2.add(incomingNodeConnector);
	                    f_path2.addAll(f_path);
	                    f_path2.add(dst_nc);
	                	List<FlowInfo> outQueue = new ArrayList<FlowInfo>();
	                	outQueue.add(new FlowInfo(NetUtils.getInetAddress(pkt.getDestinationAddress()).getHostAddress(), 
	                			NetUtils.getInetAddress(pkt.getSourceAddress()).getHostAddress(), 
	                			f_path2, false, incomingNodeConnector, dst_nc, dstMAC, srcMAC, "Null", "Null"));
	                	map_outQueue.put(dst_nc.getNode().toString(), outQueue);
	                }
	                else {
	                	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
	                            + "-> Save FlowIno to database_1\n");
	                    List<FlowInfo> outQueue_exist = map_outQueue.get(dst_nc.getNode().toString());
	                    int check = -1;
	                    for(int k =0 ; k<outQueue_exist.size() ; k++){
	                    	if(outQueue_exist.get(k).getSrcIP().equalsIgnoreCase(NetUtils.getInetAddress(pkt.getSourceAddress()).getHostAddress())
	                    			&& outQueue_exist.get(k).getDstIP().equalsIgnoreCase(NetUtils.getInetAddress(pkt.getDestinationAddress()).getHostAddress())
	                    			&& getMacAddress(outQueue_exist.get(k).getSrcMAC()).equalsIgnoreCase(getMacAddress(srcMAC))
	                    			&& getMacAddress(outQueue_exist.get(k).getDstMAC()).equalsIgnoreCase(getMacAddress(dstMAC))
	                    			&& outQueue_exist.get(k).getSrcPort().equalsIgnoreCase("Null")
	                    			&& outQueue_exist.get(k).getDstPort().equalsIgnoreCase("Null")){
	                    		check = 1;
	                    		break;
	                    	}
	                    }
	                    if(check != 1) {
	                    	List<NodeConnector> f_path = dr.getPath();
		                    programFlow(pkt, incomingNodeConnector, f_path.get(0), dstMAC,
		                            srcMAC, false, 0);
		                    for (int i = 1; i < f_path.size() - 1; i = i + 2) {
		                        programFlow(pkt, f_path.get(i), f_path.get(i + 1), dstMAC,
		                                srcMAC, false, 0);
		                    }
		                    programFlow(pkt, f_path.get(f_path.size() - 1), dst_nc, dstMAC,
		                            srcMAC, false, 0);
		                    
		                    List<NodeConnector> f_path2 = new ArrayList<NodeConnector>();
		                    f_path2.add(incomingNodeConnector);
		                    f_path2.addAll(f_path);
		                    f_path2.add(dst_nc);
	                    	map_outQueue.get(dst_nc.getNode().toString()).add(new FlowInfo(NetUtils.getInetAddress(pkt.getDestinationAddress()).getHostAddress(), 
	                			NetUtils.getInetAddress(pkt.getSourceAddress()).getHostAddress(), 
	                			f_path2, false, incomingNodeConnector, dst_nc, dstMAC, srcMAC, "Null", "Null"));
	                    }
	                }
                }
                
                
                // cross only one switch
            } else if (incomingNodeConnector.getNode().toString().compareTo(dst_nc.getNode().toString()) == 0) {
            	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                        + "-> HandleIPv4Pkt : Cross one");

                synchronized(mapqueueLock){
	                if(map_outQueue.get(dst_nc.getNode().toString())==null){
	                	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
	                            + "-> Save FlowIno to database_0\n");
	                	programFlow(pkt, incomingNodeConnector, dst_nc, dstMAC, srcMAC,false, 0);
	                    
	                    List<NodeConnector> f_path = new ArrayList<NodeConnector>();
	                    f_path.add(incomingNodeConnector);
	                    f_path.add(dst_nc);
	                	List<FlowInfo> outQueue = new ArrayList<FlowInfo>();
	                	outQueue.add(new FlowInfo(NetUtils.getInetAddress(pkt.getDestinationAddress()).getHostAddress(), 
	                			NetUtils.getInetAddress(pkt.getSourceAddress()).getHostAddress(), 
	                			f_path, false, incomingNodeConnector, dst_nc, dstMAC, srcMAC, "Null", "Null"));
	                	map_outQueue.put(dst_nc.getNode().toString(), outQueue);
	                }
	                else {
	                	System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
	                            + "-> Save FlowIno to database_1\n");
	                	List<FlowInfo> outQueue_exist = map_outQueue.get(dst_nc.getNode().toString());
	                	int check = -1;
	                    for(int k =0 ; k<outQueue_exist.size() ; k++){
	                    	if(outQueue_exist.get(k).getSrcIP().equalsIgnoreCase(NetUtils.getInetAddress(pkt.getSourceAddress()).getHostAddress())
	                    			&& outQueue_exist.get(k).getDstIP().equalsIgnoreCase(NetUtils.getInetAddress(pkt.getDestinationAddress()).getHostAddress())
	                    			&& getMacAddress(outQueue_exist.get(k).getSrcMAC()).equalsIgnoreCase(getMacAddress(srcMAC))
	                    			&& getMacAddress(outQueue_exist.get(k).getDstMAC()).equalsIgnoreCase(getMacAddress(dstMAC))
	                    			&& outQueue_exist.get(k).getSrcPort().equalsIgnoreCase("Null")
	                    			&& outQueue_exist.get(k).getDstPort().equalsIgnoreCase("Null")){
	                    		check = 1;
	                    		break;
	                    	}
	                    }
	                	if(check != 1){ 
	                		programFlow(pkt, incomingNodeConnector, dst_nc, dstMAC, srcMAC,false, 0);
	                        
	                        List<NodeConnector> f_path = new ArrayList<NodeConnector>();
	                        f_path.add(incomingNodeConnector);
	                        f_path.add(dst_nc);
	                		map_outQueue.get(dst_nc.getNode().toString()).add(new FlowInfo(NetUtils.getInetAddress(pkt.getDestinationAddress()).getHostAddress(), 
	                			NetUtils.getInetAddress(pkt.getSourceAddress()).getHostAddress(), 
	                			f_path, false, incomingNodeConnector, dst_nc, dstMAC, srcMAC, "Null", "Null"));
	                	}
	                }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void programFlow(IPv4 formattedPak,
            NodeConnector incoming_connector, NodeConnector outgoing_connector,
            byte[] dstMAC, byte[] srcMAC, boolean f_c, int queue) {
    	
        InetAddress dstAddr = intToInetAddress(formattedPak.getDestinationAddress());
        InetAddress srcAddr = intToInetAddress(formattedPak.getSourceAddress());
        
        System.out.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
            + "-> ProgramFlow FOR Output: +\n"
            + "in_port : " + incoming_connector.toString() + " ; out_port : " + outgoing_connector.toString()+"\n"
            + "src_ip : " + srcAddr+" ; dst_ip : " + dstAddr+"\n"
            + "src_mac : " + getMacAddress(srcMAC)+"; dst_mac : " + getMacAddress(dstMAC));
        
        PushFlowInfo pf = new PushFlowInfo(0, 1, incoming_connector.getNode(),
                outgoing_connector, srcAddr.getHostAddress(),
                dstAddr.getHostAddress(), 0, 0, 0, getMacAddress(srcMAC),
                getMacAddress(dstMAC), 0L);

        InetAddress hostHandler = null;
        Socket linkHandler = null;
        try {
        	//synchronized(ratelimite_addflowLock){
            hostHandler = InetAddress.getByName("localhost");
            linkHandler = new Socket(hostHandler, 9090);

            ObjectOutputStream outToClient = new ObjectOutputStream(
                    linkHandler.getOutputStream());
            outToClient.writeObject(pf);
            outToClient.flush();
            outToClient.close();
            linkHandler.close();
            //}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Object l4Datagram = formattedPak.getPayload();

        if (l4Datagram instanceof TCP) {
            TCP tcpDatagram = (TCP) l4Datagram;
            PrintWriter writer;
			try {
				writer = new PrintWriter(new FileWriter(new File("flow_sent"),true));
				writer.println("6"+","+srcAddr.getHostAddress()+","+dstAddr.getHostAddress()+","+Integer.toString(NetUtils.getUnsignedShort(tcpDatagram.getSourcePort()))+
					","+Integer.toString(NetUtils.getUnsignedShort(tcpDatagram.getDestinationPort())));
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		System.out.println("Flow Sent.");
        }
        
    }

    private void programFlow_ratelimit(String src_ip, String dst_ip,
            String src_port, String dst_port, byte[] dstMAC, byte[] srcMAC,
            NodeConnector node, int queue, String pro) {
    	PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter(new File("flow_sent"),true));
			writer.println("[INFO]  org.opendaylight.controller.dynamic_route.internal.ForwardingPkt.internal\n"
                + "-> ProgramFlow FOR Ratelimite (app): +\n"
                + "out_port : " + node.toString()+"\n"
                + "src_ip : " + src_ip+" ; dst_ip : " + dst_ip+"\n"
                + "src_mac : " + getMacAddress(srcMAC)+"; dst_mac : " + getMacAddress(dstMAC)
                + "queue : " + queue+ " ; protocol" + pro);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        PushFlowInfo pf = null ;
        if(Integer.parseInt(pro)==0)
            pf = new PushFlowInfo(0, 1, node.getNode(), node, src_ip,
                dst_ip, 0, 0, Integer.parseInt(pro), getMacAddress(srcMAC), getMacAddress(dstMAC),
                (long) queue);
        else
            pf = new PushFlowInfo(0, 2, node.getNode(), node, src_ip,
                    dst_ip, Integer.parseInt(src_port), Integer.parseInt(dst_port), Integer.parseInt(pro), getMacAddress(srcMAC), getMacAddress(dstMAC),
                    (long) queue);

        InetAddress hostHandler = null;
        Socket linkHandler = null;
        try {
        	//synchronized(ratelimite_addflowLock){
	        hostHandler = InetAddress.getByName("localhost");
	        linkHandler = new Socket(hostHandler, 9090);
	
	        ObjectOutputStream outToClient = new ObjectOutputStream(linkHandler.getOutputStream());
	        outToClient.writeObject(pf);
	        outToClient.flush();
	        linkHandler.close();
        	//}
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	try {
				writer = new PrintWriter(new FileWriter(new File("flow_sent_error"),true));
				e.printStackTrace();
				writer.println(e);
				writer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
        }
    }

    // GUI for Management
    public void ManualRateLimitService_get() {
        Thread ManualRateLimitServiceThread = new Thread(new Runnable() {
            public void run() {
                System.out.println("INFO [org.opendaylight.Forwarding.ManualRateLimitServiceThread]\n"
                                + "    >> Waiting for Manual Request on: "
                                + handlerServiceLoPort);
                try {
                    manualRateLimitServerSock = new ServerSocket(manualRateLimitServerPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                do {
                    try {

                        manualRateLimitServerClient = manualRateLimitServerSock.accept();
                        System.out.println("INFO [org.opendaylight.Forwarding.ManualRateLimitServiceThread]\n"
                                        + "    >> Manual Request Connection Accepted.");

                        Thread thread = new Thread(new Runnable() {
                            public void run() {
                            	synchronized(mapqueueLock){
	                                ManualRateLimitServiceRemoteRequest flowRecordServiceRemoteRequest = new ManualRateLimitServiceRemoteRequest(
	                                        manualRateLimitServerClient, map_outQueue);
	                                System.out.println("INFO [org.opendaylight.Forwarding.ManualRateLimitServiceThread]\n"+"-> index ="
	                                        +flowRecordServiceRemoteRequest.getIndex());
	                                if(flowRecordServiceRemoteRequest.getIndex() != -1){
		                                String src_ip = flowRecordServiceRemoteRequest.getflowRulePriorityTuple().getSrcIP();
		                                String dst_ip = flowRecordServiceRemoteRequest.getflowRulePriorityTuple().getDstIP();
		                                
		                                String src_port = flowRecordServiceRemoteRequest.getflowRulePriorityTuple().getSrcPort();
		                                String dst_port = flowRecordServiceRemoteRequest.getflowRulePriorityTuple().getDstPort();
		                                
		                                byte[] srcMAC = map_outQueue.get(flowRecordServiceRemoteRequest.getTarNode()).get(flowRecordServiceRemoteRequest.getIndex()).getSrcMAC();
		                                byte[] dstMAC = map_outQueue.get(flowRecordServiceRemoteRequest.getTarNode()).get(flowRecordServiceRemoteRequest.getIndex()).getDstMAC();
		                                
		                                NodeConnector out_node = map_outQueue.get(flowRecordServiceRemoteRequest.getTarNode()).get(flowRecordServiceRemoteRequest.getIndex()).getOutNode();
		                               
		                                int queue = flowRecordServiceRemoteRequest.getPriority();
		                                String pro = flowRecordServiceRemoteRequest.getflowRulePriorityTuple().getProtocol();
		                                //synchronized(ratelimite_addflowLock){
		                                programFlow_ratelimit(src_ip, dst_ip, src_port, dst_port, dstMAC, srcMAC, out_node, queue, pro);
		                                //}
		                                if(flowRecordServiceRemoteRequest.get_have_exist_Index() == -1)
			                                map_outQueue.get(flowRecordServiceRemoteRequest.getTarNode()).add(new FlowInfo(dst_ip, 
			                                		src_ip, map_outQueue.get(flowRecordServiceRemoteRequest.getTarNode()).get(flowRecordServiceRemoteRequest.getIndex()).getRoute(), 
			                            			false, map_outQueue.get(flowRecordServiceRemoteRequest.getTarNode()).get(flowRecordServiceRemoteRequest.getIndex()).getInNode(),
			                            			out_node, dstMAC, srcMAC, dst_port, src_port));
	                                }
	                                try {
										manualRateLimitServerClient.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                                }
                            }
                        });
                        thread.start();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } while (true);

            }
        });
        ManualRateLimitServiceThread.start();
    }

    public static String getMacAddress(byte[] Addr) {

        byte[] mac = Addr;
        if (mac == null) return null;

        StringBuilder sb = new StringBuilder(18);
        for (byte b : mac) {
            if (sb.length() > 0)
                sb.append(':');
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public void AuthService_get() {
    	System.out.println("INFO [org.opendaylight.Forwarding.AuthServiceThread]");
        Thread MapHandlingThread = new Thread(new Runnable() {
            public void run() {
                do {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    
                    InetAddress host = null;
                    Socket link = null;

                    try {
                        host = InetAddress.getByName(authIP);
                        link = new Socket(host, 8001);

                        ObjectInputStream inFromServer = new ObjectInputStream(link.getInputStream());

                        try {
                            userList = (List<UserData>) inFromServer.readObject();
                            /*
                            System.out.println("INFO [org.opendaylight.Forwarding.AuthServiceThread]\n" 
                            		+ "-> Get userlist"
                            		+ "-> userlist size : "+userList.size());
                            */
                            inFromServer.close();
                            link.close();
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();
                        	System.out.println("INFO [org.opendaylight.Forwarding.AuthServiceThread]\n" 
                        			+ " -> cannot get userlist");
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                    	System.out.println("INFO [org.opendaylight.Forwarding.AuthServiceThread]\n" 
                    				+ " -> cannot get userlist & cannot connect to authenication server");
                    }

                } while (true);
            }
        });
        MapHandlingThread.start();
    }

    public void Handler() {
        Thread handlerServerThread = new Thread(new Runnable() {

            List<EventDataTuple> evalEventData = new ArrayList<>();
            List<EventDataTuple> currEventData = new ArrayList<>();
            List<EventDataTuple> rEventData = new ArrayList<>();
            List<FlowRuleTuple> flowRule = new ArrayList<>();
            HashMap <String, String> appPercent = new HashMap <String, String>();
            HashMap<String, String> priorityList = new HashMap<>();
            List<FlowInfo> outQueue = new ArrayList<>();

            public void run() {

                // Initialize to empty list
                evalEventData.clear();
                currEventData.clear();
                rEventData.clear();

                System.out.println("INFO [net.floodlightcontroller.forwarding.Forwarding.handlerServerThread]\n"
                                + "    >> Waiting for HandlerService Local Request on: "
                                + handlerServiceLoPort);
                try {
                    handlerServiceLoSock = new ServerSocket(handlerServiceLoPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                do {
                    try {

                        // Waiting for request from Evaluator Module
                        handlerClient = handlerServiceLoSock.accept();
                        System.out.println("INFO [net.floodlightcontroller.forwarding.Forwarding.handlerServerThread]\n"
                                        + "    >> HandlerService Local Connection Accepted.");

                        // Get data from EvaluatorModule
                        HandlerServiceLocalRequest handlerServiceLocalRequest = new HandlerServiceLocalRequest(handlerClient);
                        evalEventData = handlerServiceLocalRequest.getEventData();
                        System.out.println("INFO [net.floodlightcontroller.forwarding.Forwarding.handlerServerThread]\n"
                                        + "    >> HandlerService Local geteventdata. "
                                        + evalEventData
                                        + " size : "
                                        + evalEventData.size());
                        // Get data from FlowClassificationModule
                        InetAddress hostFlowClassification = null;
                        Socket linkFlowClassification = null;

                        try {
                            hostFlowClassification = InetAddress
                                    .getByName("localhost");
                            linkFlowClassification = new Socket(
                                    hostFlowClassification, 9002);

                            try {
                                ObjectInputStream inFromServer = new ObjectInputStream(linkFlowClassification.getInputStream());
                                flowRule = (List<FlowRuleTuple>) inFromServer.readObject();
                                appPercent = (HashMap <String, String>) inFromServer.readObject();
                                linkFlowClassification.close();
                                /*
                                System.out.println("INFO [net.floodlightcontroller.forwarding.Forwarding.handlerServerThread]\n"
                                                + "    >> HandlerService Local getflowrule."
                                                + flowRule
                                                + " size: "
                                                + flowRule.size());
                                */
                            } catch (ClassNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        // Get data from PriorityList
                        HandlerServicePriorityList handlerServicePriorityList = new HandlerServicePriorityList();
                        priorityList = handlerServicePriorityList.getPriorityList();
                        System.out.println("INFO [net.floodlightcontroller.forwarding.Forwarding.handlerServerThread]\n"
                                + "    >> currEventData Size \n"
                        		+currEventData.size());
                        // For received EventData
                        for (int i = 0; i < evalEventData.size(); i++) {

                            int flagMatch = 0;
                            // Check if the received event data exists already
                            for (int j = 0; j < currEventData.size(); j++) {
                                if (evalEventData.get(i).getEventName()
                                        .equalsIgnoreCase(currEventData.get(j).getEventName())) {
                                    String currEventName = currEventData.get(j).getEventName();
                                    currEventData.get(j).setEventName("Valid_" + currEventName);
                                    flagMatch = 1;
                                    break;
                                }
                            }
                            System.out.println("INFO [net.floodlightcontroller.forwarding.Forwarding.handlerServerThread]\n");
                            System.out.println("flagMatch : "+flagMatch);
                            if (flagMatch == 0) {

                                currEventData.add(evalEventData.get(i));
                                String currEventName = currEventData.get(currEventData.size() - 1).getEventName();
                                currEventData.get(currEventData.size() - 1).setEventName("Valid_" + currEventName);

                                System.out.println("INFO [net.floodlightcontroller.forwarding.Forwarding.handlerServerThread]\n"
                                                + "    >> QoS Event occurred.");
                            }
                            
                            // Adjust flows which covers target Sw/Port to
                            // rate-limit mode
                            
                            for (String key : map_outQueue.keySet()){
                            	outQueue = map_outQueue.get(key);
	                            for (int j = 0; j < outQueue.size(); j++) {
	                            	
	                                // For every Sw/Port pair
	                                for (int k = 0; k < outQueue.get(j).getRoute().size(); k++) {
	                                    // Check if there is any Sw/Port pair along
	                                    // the path matches target Sw/Port
	                                    if (outQueue.get(j).getRoute().get(k).getNode().toString()
	                                            .equalsIgnoreCase(evalEventData.get(i).getTarSwitchDPID())
	                                            && outQueue.get(j).getRoute().get(k).toString()
	                                                    .equalsIgnoreCase(evalEventData.get(i).getTarPortNumber())
	                                        
	                                    	&& outQueue.get(j).getRateLimit() == false) {
	                                        // Match the data from
	                                        // FlowClassification Module to get the
	                                        // AppName
	                                        for (int m = 0; m < flowRule.size(); m++) {
	                                        	if (flowRule.get(m).getSrcIP().equalsIgnoreCase(outQueue.get(j).getSrcIP())
	                                                    && flowRule.get(m).getDstIP().equalsIgnoreCase(outQueue.get(j).getDstIP())) {
	                                            	PrintWriter writer;
	                                    			try {
	                                    				writer = new PrintWriter(new FileWriter(new File("flow_sent2"),true));
	                                    				writer.println("INFO [net.floodlightcontroller.forwarding.Forwarding.handlerServerThread]\n"
	                                                        + "    >> Rate-limit:"
	                                                        + "\n           Protocol    -> "
	                                                        + flowRule.get(m).getProtocol()
	                                                        + "\n           SrcIP       -> "
	                                                        + flowRule.get(m).getSrcIP()
	                                                        + "\n           DstIP       -> "
	                                                        + flowRule.get(m).getDstIP()
	                                                        + "\n           Application -> "
	                                                        + flowRule.get(m).getAppName()
	                                                        + "\n           Priority    -> "
	                                                        + priorityList.get(flowRule.get(m).getAppName()));
	                                    				writer.close();
	                                    			} catch (IOException e) {
	                                    				// TODO Auto-generated catch block
	                                    				e.printStackTrace();
	                                    			}
	                                                String priority = priorityList.get(flowRule.get(m).getAppName());
	                                                if(priority==null) priority = "10";
	                                                outQueue.get(j).setRateLimit(true);
	     
		                                            programFlow_ratelimit(
		                                            		outQueue.get(j).getSrcIP(),
		                                                    outQueue.get(j).getDstIP(),
		                                                    "0",
		                                                    "0",
		                                                    outQueue.get(j).getDstMAC(),
		                                                    outQueue.get(j).getSrcMAC(),
		                                                    outQueue.get(j).getOutNode(),
		                                                    Integer.parseInt(priority),
		                                                    "0");
	                                                break;
	                                            }
	                                        }
	                                        break;
	                                    }
	                                }
	                            }
                            }
                        }

                        for (int i = 0; i < currEventData.size(); i++) {
                        	System.out.println(currEventData.size());
                            // Update the event list
                            if (currEventData.get(i).getEventName()
                                    .startsWith("Valid_")) {
                                String currEventName = currEventData.get(i)
                                        .getEventName();
                                rEventData.add(currEventData.get(i));
                                rEventData.get(rEventData.size() - 1)
                                        .setEventName(
                                                currEventName.substring(6));
                                System.out.println("d0");
                            } else {

                                System.out
                                        .println("INFO [net.floodlightcontroller.forwarding.Forwarding.handlerServerThread]\n"
                                                + "    >> QoS Event diminished.");

                                // Restore flows to original mode if the event
                                // is no longer in the list
                                for (String key : map_outQueue.keySet()){
                                	outQueue = map_outQueue.get(key);
                                
	                                for (int j = 0; j < outQueue.size(); j++) {
	                                    for (int k = 0; k < outQueue.get(j)
	                                            .getRoute().size(); k++) {
	                                    	System.out.println(outQueue.get(j).getRoute().get(k).getNode().toString());
	                                    	System.out.println(currEventData.get(i).getTarSwitchDPID());
	                                    	System.out.println(outQueue.get(j).getRoute().get(k).toString());
	                                    	System.out.println(currEventData.get(i).getTarPortNumber());
	                                        if (outQueue.get(j).getRoute().get(k).getNode().toString()
	                                                .equalsIgnoreCase(currEventData.get(i).getTarSwitchDPID())
	                                                && outQueue.get(j).getRoute().get(k).toString()
	                                                        .equalsIgnoreCase(currEventData.get(i).getTarPortNumber())
	                                                          	&&outQueue.get(j).getRateLimit()==true) {
	                                            outQueue.get(j).setRateLimit(false);
	                                            
		                                        programFlow_ratelimit(
		                                                outQueue.get(j).getSrcIP(),
		                                                outQueue.get(j).getDstIP(),
		                                                "0",
		                                                "0",
		                                                outQueue.get(j).getDstMAC(),
		                                                outQueue.get(j).getSrcMAC(),
		                                                outQueue.get(j).getOutNode(), 0,
		                                                "0");
	                                            
	                                            break;
	                                        }
	                                    }
	                                }
                                }
                            }
                        }
                        
                        currEventData = new ArrayList<EventDataTuple>(rEventData);
                        System.out.println(currEventData.size());
                        rEventData.clear();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } while (true);

            }
        });
        handlerServerThread.start();
    }
    public void Monitor(){
	    Thread monitorServiceThread = new Thread(new Runnable(){
	  		
	  		public void run(){
	    		
	    		do{
	    			
	    			try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			
	    			InetAddress host = null;
	    			Socket link = null;
	    			
	    			try {
	    				host = InetAddress.getByName("localhost");
	    				link = new Socket(host, 9001);
	    				
	    				ObjectInputStream inFromServer = new ObjectInputStream(link.getInputStream());
	    				
	    				try {
	    					synchronized(switchPortLock){
	    						switchPortStat = (List<SwitchPortStatTuple>) inFromServer.readObject();
	    					}
	    					
	    					link.close();
	    				} catch (ClassNotFoundException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    				
	    			} catch (IOException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			
	    			
	    		}while(true);
	      		
	      	}
	  	});
	  	monitorServiceThread.start();
    }
    
    public void init() {
        System.out.println("dynamic_route init");
    }

    public void destroy() {
        System.out.println("dynamic_route destroy");
    }

    public void start() {
        System.out.println("dynamic_route start");
        
        FileReader in;
		try {
			in = new FileReader("AuthenticationServer_IP");
		
	        BufferedReader br = new BufferedReader(in);
	        authIP = br.readLine();
	        in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        ParseTopology();
        AuthService_get();
        ManualRateLimitService_get();
        Handler();
        Monitor();
        FlowRemoved();
    }

    public void stop() {
        System.out.println("dynamic_route stop");
    }

	public void FlowRemoved() {
		// TODO Auto-generated method stub
		System.out.println("INFO [org.opendaylight.forwarding.Forwarding]\n"
                + "    >> flowRemoved.");
		FlowCheckService flowcheckservice = new FlowCheckService();
		flowcheckservice.start();
	}	
}
