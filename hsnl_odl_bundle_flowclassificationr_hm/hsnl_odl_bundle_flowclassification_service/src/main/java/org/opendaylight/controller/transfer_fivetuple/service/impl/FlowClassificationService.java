package org.opendaylight.controller.transfer_fivetuple.service.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import net.floodlightcontroller.flowclassification.AppPriority;

import org.opendaylight.controller.flowclassification.FlowClassificationServiceLocalRequest;

public class FlowClassificationService extends Thread {
	
	private static ServerSocket flowClaServiceLoSock;
    private static final int flowClaServiceLoPort = 9002;
    private static Socket flowClaLoClient;
    
    private static ServerSocket flowClaServiceAppSock;
    private static final int flowClaServiceAppPort = 9005;
    private static Socket flowClaAppClient;
	
	public void run() {

        Thread loServerThread = new Thread(new Runnable() {

            public void run() {

                System.out
                        .println("INFO [net.floodlightcontroller.flowclassification.FlowClassificationService.loServerThread]\n"
                                + "    >> Waiting for FlowClassificationService Local Request on: "
                                + flowClaServiceLoPort);
                try {
                    flowClaServiceLoSock = new ServerSocket(
                            flowClaServiceLoPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                do {
                    try {

                        flowClaLoClient = flowClaServiceLoSock.accept();
                        System.out
                                .println("INFO [net.floodlightcontroller.flowclassification.FlowClassificationService.loServerThread]\n"
                                        + "    >> FlowClassificationService Local Connection Accepted.");
                        synchronized (FlowclassificationServiceImpl.flowRuleLock) {
                            Thread thread = new Thread(new Runnable() {
                                public void run() {
                                    new FlowClassificationServiceLocalRequest(flowClaLoClient, FlowclassificationServiceImpl.flowRule, FlowclassificationServiceImpl.appPercent);
                                }
                            });
                            thread.start();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } while (true);

            }
        });
        loServerThread.start();
        
        
        Thread appServiceThread = new Thread(new Runnable() {

            public void run() {

                System.out
                        .println("INFO [net.floodlightcontroller.flowclassification.FlowClassificationService.flowClaServiceThread]\n"
                                + "    >> Waiting for FlowClassificationService App Request on: "
                                + flowClaServiceAppPort);
                try {
                    flowClaServiceAppSock = new ServerSocket(
                            flowClaServiceAppPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                do {
                    try {

                        flowClaAppClient = flowClaServiceAppSock.accept();
                        System.out
                                .println("INFO [net.floodlightcontroller.flowclassification.FlowClassificationService.flowClaServiceThread]\n"
                                        + "    >> FlowClassificationService App Connection Accepted.");
                        synchronized (FlowclassificationServiceImpl.flowRuleLock) {
                            Thread thread = new Thread(new Runnable() {
                                public void run() {
                                    
                                    ObjectInputStream inFromGUI;
                                    try {
                                        inFromGUI = new ObjectInputStream(flowClaAppClient.getInputStream());
                                        AppPriority ap = (AppPriority)inFromGUI.readObject();
                                        if(ap.getPriority().equalsIgnoreCase("0"))
                                        	FlowclassificationServiceImpl.appPriority.put(ap.getAppName(), null);
                                        else
                                        	FlowclassificationServiceImpl.appPriority.put(ap.getAppName(), ap.getPriority());
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } while (true);

            }
        });
        appServiceThread.start();
    }
}
