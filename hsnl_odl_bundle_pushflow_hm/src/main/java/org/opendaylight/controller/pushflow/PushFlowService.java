package org.opendaylight.controller.pushflow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ClientService extends Thread { 
	private Socket client;
	public ClientService(Socket c) throws IOException{ 
		client = c;
	}  
	public void run() { 
		try {
			if(client!=null) new PushFlowServiceLocalRequest(client);
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
public class PushFlowService extends Thread {
    public static Object messageLock = new Object();
    private static ServerSocket pudhflowServiceLoSock;
    private static final int pudhflowServiceLoPort = 9090;
    private static Socket loClient;

    public void run() {

        // Wait for connection

        Thread loServerThread = new Thread(new Runnable() {

            public void run() {

                try {
                    System.out.println("push 1");
                    pudhflowServiceLoSock = new ServerSocket(
                            pudhflowServiceLoPort);
                    System.out.println("push 2");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                do {
                    try {
                        System.out.println("push 4");
                        loClient = pudhflowServiceLoSock.accept();
                        System.out.println("push 3");
                        ClientService service = new ClientService(loClient);
                        service.start();
                    } catch (IOException e) {
                        // TODO Auto-generated catch bloc
                        e.printStackTrace();
                    }
                } while (true);
            }
        });
        loServerThread.start();
    }
}