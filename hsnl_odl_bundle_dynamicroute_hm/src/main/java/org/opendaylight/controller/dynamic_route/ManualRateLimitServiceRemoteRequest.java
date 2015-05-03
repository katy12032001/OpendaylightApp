package org.opendaylight.controller.dynamic_route;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import org.opendaylight.controller.dynamic_route.internal.ForwardingPkt;
import org.opendaylight.controller.sal.core.NodeConnector;

import net.floodlightcontroller.flowclassification.FlowRulePriorityTuple;
import net.floodlightcontroller.flowclassification.FlowInfo;

public class ManualRateLimitServiceRemoteRequest {
	private String TarNode;
    private int priority;
    private int index;
    private int have_exist;
    private FlowRulePriorityTuple flowRulePriorityTuple;
    public ManualRateLimitServiceRemoteRequest(Socket client, HashMap<String, List<FlowInfo>> map_outQueue) {

        try {

            ObjectInputStream inFromServer = new ObjectInputStream(client.getInputStream());

            try {

                FlowRulePriorityTuple flowRulePriorityTuple = (FlowRulePriorityTuple) inFromServer
                        .readObject();
                this.flowRulePriorityTuple = flowRulePriorityTuple;
                
                System.out.println("INFO [org.opendaylight.controller.dynamic_route.ManualRateLimitServiceRemoteRequest]\n"
                        + "flowRule Protocol: " + flowRulePriorityTuple.getProtocol() +"\n"
                        + "flowRule SrcIP: " + flowRulePriorityTuple.getSrcIP() +"\n"
                        + "flowRule SrcPort: " + flowRulePriorityTuple.getSrcPort() +"\n"
                        + "flowRule DstIP: " + flowRulePriorityTuple.getDstIP() +"\n"
                        + "flowRule DstPort: " + flowRulePriorityTuple.getDstPort());
                
                TarNode = ForwardingPkt.mac_node_mapping.get(ForwardingPkt.ip_mac_mapping.get(flowRulePriorityTuple.getDstIP())).getNode().toString();
                List<FlowInfo> outQueue = map_outQueue.get(TarNode);
                
                index = -1;
                have_exist = -1;
                for (int i = 0; i < outQueue.size(); i++) {
                    if (outQueue.get(i).getSrcIP()
                            .equalsIgnoreCase(flowRulePriorityTuple.getSrcIP())
                            && outQueue.get(i).getDstIP()
                                    .equalsIgnoreCase(flowRulePriorityTuple.getDstIP())) {
                    	index = i;
                        priority = flowRulePriorityTuple.getPriority();
                        //break;
                        
                        if(outQueue.get(i).getSrcPort().equalsIgnoreCase(flowRulePriorityTuple.getSrcPort())&& outQueue.get(i).getDstPort().equalsIgnoreCase(flowRulePriorityTuple.getDstPort())){
                        	have_exist = i;
                        	break;
                        }
                    }
                }
                inFromServer.close();

            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public int getIndex() {
        return index;
    }
    
    public int get_have_exist_Index() {
        return have_exist;
    }
    
    public int getPriority() {
        return priority;
    }
    public FlowRulePriorityTuple getflowRulePriorityTuple() {
        return flowRulePriorityTuple;
    }
    public String getTarNode() {
        return TarNode;
    }

}
