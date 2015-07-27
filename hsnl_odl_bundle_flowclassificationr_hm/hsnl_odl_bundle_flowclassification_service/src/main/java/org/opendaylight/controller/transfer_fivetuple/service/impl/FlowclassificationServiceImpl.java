package org.opendaylight.controller.transfer_fivetuple.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import net.floodlightcontroller.authentication.UserData;
import net.floodlightcontroller.flowclassification.FlowRuleTuple;

import org.opendaylight.controller.flowclassification.FlowClassificationManual;
import org.opendaylight.controller.sal.binding.api.AbstractBindingAwareConsumer;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ConsumerContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareConsumer;
import org.opendaylight.controller.transfer_fivetuple.service.api.FlowclassificationServiceAPI;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.FlowclassificationService;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInputBuilder;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class FlowclassificationServiceImpl extends AbstractBindingAwareConsumer implements BundleActivator, BindingAwareConsumer, FlowclassificationServiceAPI{
	
	private FlowclassificationService FiveTupleService;
    private ConsumerContext session;
    public static String authIP;
    
    public static Object flowRuleLock = new Object();
    public static HashMap <String, String> appPriority = new HashMap <String, String>();
    public static List<FlowRuleTuple> flowRule = new ArrayList<>();
    public static HashMap <String, String> appPercent = new HashMap <String, String>();
    
	@Override
    protected void startImpl(BundleContext context) {
		context.registerService(FlowclassificationServiceAPI.class, this, null);
		FileReader in;
		try {
			in = new FileReader("AuthenticationServer_IP");
		
        BufferedReader br = new BufferedReader(in);
        authIP = br.readLine();
        in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		FlowClassificationService flowClassificationService = new FlowClassificationService();
        flowClassificationService.start();
    }


	@Override
	public boolean Transfer_fivetuple(String SRC_IP, String DST_IP,
			String SRC_PORT, String DST_PORT, String APP, String IPPort, String APP_Percent) {
		// TODO Auto-generated method stub
		
		System.out.println(SRC_IP+DST_IP+SRC_PORT+DST_PORT+APP+IPPort+APP_Percent);
		System.out.println("Get userlist");
        InetAddress host = null;
        Socket link = null;

        List<UserData> userList = new ArrayList<>();
        try {
			host = InetAddress.getByName(authIP);
			link = new Socket(host, 8001);
        
			// User Register info.
			ObjectInputStream inFromServer;
			inFromServer = new ObjectInputStream(link.getInputStream());
        	userList = (List<UserData>) inFromServer.readObject();
            link.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Get userlist2");
        int match_user = 1;
        for(int i = 0;i<userList.size();i++){
        	if(userList.get(i).getIpAddress().equalsIgnoreCase(SRC_IP)){
        		match_user = 1;
        		break;
        	}
        	if(userList.get(i).getIpAddress().equalsIgnoreCase(DST_IP)){
        		match_user = 1;
        		break;
        	}
        }
        System.out.println("Get userlist3" + match_user);
        match_user = 1;
        synchronized (flowRuleLock) {
	        int flagMatch = 0;
	        if(match_user == 1){
	        	System.out.println("INFO [net.floodlightcontroller.flowclassification.QoSServiceRemoteRequest]");
	        	System.out.println("add");
	            // Check whether the tuple exists
	            for (int i = 0; i < flowRule.size(); i++) {
	
	                // Update the FlowRuleTuple if only the application name of the
	                // entry differs
	                if (flowRule.get(i).getProtocol().equalsIgnoreCase(IPPort)
	                        && flowRule.get(i).getSrcIP().equalsIgnoreCase(SRC_IP)
	                        && flowRule.get(i).getSrcPort().equalsIgnoreCase(SRC_PORT)
	                        && flowRule.get(i).getDstIP().equalsIgnoreCase(DST_IP)
	                        && flowRule.get(i).getDstPort().equalsIgnoreCase(DST_PORT)) {
	                    flowRule.get(i).setAppName(APP);
	                    flagMatch = 1;
	                    System.out.println("MATCH");
	                    break;
	                }
	            }
	
	            if (flagMatch == 0) {
	                // No matches found, append the new FlowRuleTuple to the list
	                flowRule.add(new FlowRuleTuple(IPPort, SRC_IP, SRC_PORT, DST_IP, DST_PORT, APP));
	                FlowclassificationServiceImpl.appPercent.put(APP, APP_Percent);
	                System.out.println("NOT MATCH");
	            }
	           
	            System.out.println("        FlowRule Size   -> " + flowRule.size());
	            
	            if(FlowclassificationServiceImpl.appPriority.get(APP)!=null){
	            	 System.out.println("Get userlist4");
	                new FlowClassificationManual("localhost" , IPPort, SRC_IP, SRC_PORT, DST_IP, DST_PORT, APP, Integer.parseInt(this.appPriority.get(APP)));
	            }
            }
        }
		return true;
	}


	@Override
	public void onSessionInitialized(ConsumerContext arg0) {
		// TODO Auto-generated method stub
		this.session = arg0;
	}
    
}
