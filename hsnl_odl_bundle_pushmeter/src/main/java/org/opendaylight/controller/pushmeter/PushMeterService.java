package org.opendaylight.controller.pushmeter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PushMeterService extends Thread{
	public static Object messageLock = new Object();
	private static ServerSocket pudhMeterServiceLoSock;
	private static final int pushMeterServiceLoPort = 9091;
	private static Socket loClient;

	public void run() {

		// Wait for connection

		Thread loServerThread = new Thread(new Runnable() {

			public void run() {

				try {
					System.out.println("push 1_meter");
					pudhMeterServiceLoSock = new ServerSocket(
							pushMeterServiceLoPort);
					System.out.println("push 2_meter");
				} catch (IOException e) {
					e.printStackTrace();
				}

				do {
					try {
						System.out.println("push 4_meter");
						loClient = pudhMeterServiceLoSock.accept();
						System.out.println("push 3_meter");
						synchronized (messageLock) {
							Thread thread = new Thread(new Runnable() {
								public void run() {
									new PushMeterServiceLocalRequest(loClient);
								}
							});
							thread.start();
						}
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
