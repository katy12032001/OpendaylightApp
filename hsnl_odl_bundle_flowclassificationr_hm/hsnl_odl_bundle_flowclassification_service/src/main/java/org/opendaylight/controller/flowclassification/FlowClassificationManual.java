package org.opendaylight.controller.flowclassification;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import net.floodlightcontroller.flowclassification.FlowRulePriorityTuple;

public class FlowClassificationManual {
    public FlowClassificationManual(String controller_ip , String protocol, String srcIP, String srcPort, String dstIP, String dstPort, 
            String appName, int priority){
        
        InetAddress host = null;
        Socket link = null;
        
        
        try {
            host = InetAddress.getByName(controller_ip);
            link = new Socket(host, 9010);
            System.out.println("Get userlist5-1");
            ObjectOutputStream outToClient = new ObjectOutputStream(link.getOutputStream());
            outToClient.writeObject(new FlowRulePriorityTuple(protocol, srcIP, srcPort, dstIP, dstPort, appName, priority));
            outToClient.flush();
            link.close();
            System.out.println("Get userlist5");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
