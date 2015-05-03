package org.opendaylight.controller.flowcheck;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SendFlowCheckInfo {
	public SendFlowCheckInfo(Socket client, FlowCheckInfo flowcheckinfo){
		
		try {
			
			ObjectOutputStream outToClient = new ObjectOutputStream(client.getOutputStream());
			
			outToClient.writeObject(flowcheckinfo);
			outToClient.flush();
			client.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}
