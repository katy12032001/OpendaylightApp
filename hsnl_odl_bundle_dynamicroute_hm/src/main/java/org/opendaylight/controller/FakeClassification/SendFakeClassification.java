package org.opendaylight.controller.FakeClassification;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SendFakeClassification {
	
	private InetAddress host = null;
    private Socket link = null;
	
	public SendFakeClassification(String dstIP, String srcIP, String dstPort, String srcPort, String ipProt) {
		System.out.println("[FakeClassification]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        try {
            host = InetAddress.getByName("192.168.0.114");
            link = new Socket(host, 9002);
            
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);
			String message;
			
			// Message Sequence
			message = ipProt;
			output.println(message);
				
			message = srcIP;
			output.println(message);
			
			message = srcPort;
			output.println(message);
			
			message = dstIP;
			output.println(message);
			
			message = dstPort;
			output.println(message);
			
			System.out.println("[FakeClassification]");
			System.out.println("Flow Sent.");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			try {
				link.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
            
        
	}
}
