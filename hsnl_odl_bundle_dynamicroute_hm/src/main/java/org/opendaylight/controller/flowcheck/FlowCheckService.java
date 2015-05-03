package org.opendaylight.controller.flowcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.opendaylight.controller.dynamic_route.internal.ForwardingPkt;
import org.opendaylight.controller.pushflow.PushFlowInfo;


public class FlowCheckService extends Thread {
    public static Object messageLock = new Object();
    public static Object messageLock2 = new Object();
    public void run() {

        // Wait for connection

        Thread loServerThread = new Thread(new Runnable() {

            public void run() {
            	while (true) {
                	try {
                		Thread.sleep(10000);
						
                		File file=new File("remove_index");        
    	                
    		            //给該文件加锁     
                		/*
    		            RandomAccessFile fis = new RandomAccessFile(file, "rw");   
    		            FileChannel fcin=fis.getChannel();    
    		            FileLock flin=null;    
    		            while(true){    
    		                try {  
    		                    flin = fcin.tryLock(); 
    		                    break;  
    		                } catch (Exception e) {  
    		                     System.out.println("有其他線程正在操作該文件，當前線程休眠10毫秒");   
    		                     Thread.sleep(10);    
    		                }  
    		                  
    		            } 
                		*/
                		BufferedReader reader = new BufferedReader(new FileReader("remove_index"));
                		String file_name = null;
                		String line = reader.readLine();
                		if(line.equalsIgnoreCase("0")){
                			file_name = "removed1.txt";
                		}else file_name = "removed0.txt";
                		reader.close();
                		BufferedReader reader2 = new BufferedReader(new FileReader(file_name));
                		String flow = null;
                		while ((flow = reader2.readLine()) != null){
                			String[] tokens = flow.split(",");
					
						    FlowCheckInfo flowcheckinfo_get = new FlowCheckInfo(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6]);
						
				            System.out.println("[Info] org.opendaylight.controller.flowcheck\n"
				            		+"->"+flowcheckinfo_get.GetNode()+"->"+flowcheckinfo_get.GetSrcIP()+"->"+flowcheckinfo_get.GetDstIP()
				            		+"->"+flowcheckinfo_get.GetSrcMAC()+"->"+flowcheckinfo_get.GetDstMAC()
				            		+"->"+flowcheckinfo_get.GetSrcPort()+"->"+flowcheckinfo_get.GetDstPort());
						 
						 
							synchronized (ForwardingPkt.mapqueueLock) {
								FlowCheck flowcheck = new FlowCheck(flowcheckinfo_get.GetNode(), flowcheckinfo_get.GetSrcIP(), flowcheckinfo_get.GetDstIP(), flowcheckinfo_get.GetSrcMAC(),flowcheckinfo_get.GetDstMAC(), flowcheckinfo_get.GetSrcPort(),flowcheckinfo_get.GetDstPort());
							 	
								int i = flowcheck.CheckRemoveFlow();
								System.out.println("[Info]org.opendaylight.controller.flowcheck.FlowCheckServicepush 0\n"+"->"+"check"+i);
							    System.out.println("[Info]org.opendaylight.controller.flowcheck.FlowCheckServicepush 1\n"+"->"+ForwardingPkt.map_outQueue.get(flowcheckinfo_get.GetNode().toString()).size());
								if(i != -1)ForwardingPkt.map_outQueue.get(flowcheckinfo_get.GetNode().toString()).remove(i);
								System.out.println("[Info]org.opendaylight.controller.flowcheck.FlowCheckServicepush 2\n"+"->"+ForwardingPkt.map_outQueue.get(flowcheckinfo_get.GetNode().toString()).size());
							}
						 
							
                		}
                		reader2.close();
                		PrintWriter writer = new PrintWriter(file_name);
                		writer.print("");
                		writer.close();
                		/*
                		flin.release();    
    		            fcin.close();    
    		            fis.close();    
    		            fis=null;
                		*/
					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
                         
        });
        loServerThread.start();
    }
}