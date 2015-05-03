package org.opendaylight.controller.Mac_Mapping.internal;

import java.util.HashMap;
import java.util.Set;

import org.opendaylight.controller.hosttracker.IfIptoHost;
import org.opendaylight.controller.hosttracker.hostAware.HostNodeConnector;
import org.opendaylight.controller.sal.core.NodeConnector;
import org.opendaylight.controller.sal.utils.ServiceHelper;

public class Mac_Mapping {
    public HashMap<String, NodeConnector> mac_node_mapping = new HashMap<String, NodeConnector>();
    public String containerName = "default";
    public IfIptoHost hostTracker ;
    private Thread MapHandlingThread;
    
    public void setHostTracker(IfIptoHost hostTracker) {
        this.hostTracker = hostTracker;
    }

    public void unsetHostTracker(IfIptoHost hostTracker) {
        if (this.hostTracker == hostTracker) {
            this.hostTracker = null;
        }
    }
    public Mac_Mapping() {
    }

    public void init() {
        System.out.println("MAC_MAPPING init");
    }

    public void destroy() {
        System.out.println("MAC_MAPPING destroy");
    }

    public void start() {
        MapHandling();
        System.out.println("MAC_MAPPING start");
    }

    public void stop() {

        System.out.println("MAC_MAPPING stop");
    }

    public void MapHandling() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        MapHandlingThread = new Thread(new Runnable() {
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    
                    if (null == hostTracker) {
                        System.out.println("mac null");
                    }
                    else{
                        Set<HostNodeConnector> host_set = hostTracker
                                .getAllHosts();
                        for (HostNodeConnector hostnode : host_set) {
                            if (mac_node_mapping.get(getMacAddress(hostnode
                                    .getDataLayerAddressBytes())) == null) {
                                mac_node_mapping.put(getMacAddress(hostnode
                                        .getDataLayerAddressBytes()), hostnode
                                        .getnodeConnector());
                            } else {
                                String n1 = hostnode.getnodeConnector()
                                        .toString();
                                NodeConnector n2 = mac_node_mapping
                                        .get(getMacAddress(hostnode
                                                .getDataLayerAddressBytes()));
                                if (n1.equalsIgnoreCase(n2.toString()) == false) {
                                    mac_node_mapping.put(getMacAddress(hostnode
                                            .getDataLayerAddressBytes()),
                                            hostnode.getnodeConnector());
                                }
                            }
                        }
                    }
                } while (true);
            }
        });
        MapHandlingThread.start();
    }

    public static String getMacAddress(byte[] Addr) {
        byte[] mac = Addr;
        if (mac == null)
            return null;

        StringBuilder sb = new StringBuilder(18);
        for (byte b : mac) {
            if (sb.length() > 0)
                sb.append(':');
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public NodeConnector get_nodeconnector(String dstMAC) {
        return mac_node_mapping.get(dstMAC);
    }

    public HashMap<String, NodeConnector> getmaclist() {
        return mac_node_mapping;
    }
}
