package org.opendaylight.controller.md.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.concurrent.Future;

import org.opendaylight.controller.sal.common.util.Futures;
import org.opendaylight.controller.sal.common.util.Rpcs;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.FlowclassificationService;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleInput;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutput.EchoResult;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.Transmit5TupleOutputBuilder;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
@SuppressWarnings("deprecation")
public class FlowclassificationImp implements FlowclassificationService{
	
	
	@SuppressWarnings("finally")
	@Override
	public Future<RpcResult<Transmit5TupleOutput>> transmit5Tuple(
			Transmit5TupleInput input) {
		// TODO Auto-generated method stub
		// Connection
		/*
				InetAddress host = null;
				Socket link = null;
				
				try {
					host = InetAddress.getByName("127.0.0.1");
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					
					// Connect to the server
					link = new Socket(host, 9003);
					
					PrintWriter output = new PrintWriter(link.getOutputStream(), true);
					String message;
					
					// Message Sequence
					message = input.getIpPort();
					output.println(message);
						
					message = input.getSrcIp();
					output.println(message);
					
					message = input.getSrcPort();
					output.println(message);
					
					message = input.getDstIp();
					output.println(message);
					
					message = input.getDstPort();
					output.println(message);
					
					message = input.getAppName();
					output.println(message);*/
					
					System.out.println("Flow Sent."+input.getAppName()+input.getDstIp());
					/*
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Transmit5TupleOutputBuilder ob = new Transmit5TupleOutputBuilder();
		            ob.setEchoResult(EchoResult.Error);
		            RpcResult<Transmit5TupleOutput> rpcResult =
		                    Rpcs.<Transmit5TupleOutput> getRpcResult(true, ob.build(),
		                            Collections.<RpcError> emptySet());

		            return Futures.immediateFuture(rpcResult);
				} finally {
					
					try {
						link.close();
						 /* Build the result and return it. */
					/*
						Transmit5TupleOutputBuilder ob = new Transmit5TupleOutputBuilder();
			            ob.setEchoResult(EchoResult.Reachable);
			            RpcResult<Transmit5TupleOutput> rpcResult =
			                    Rpcs.<Transmit5TupleOutput> getRpcResult(true, ob.build(),
			                            Collections.<RpcError> emptySet());

			            return Futures.immediateFuture(rpcResult);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Transmit5TupleOutputBuilder ob = new Transmit5TupleOutputBuilder();
						ob.setEchoResult(EchoResult.Error);
			            RpcResult<Transmit5TupleOutput> rpcResult =
			                    Rpcs.<Transmit5TupleOutput> getRpcResult(true, ob.build(),
			                            Collections.<RpcError> emptySet());

			            return Futures.immediateFuture(rpcResult);
					}
					
				}*/	Transmit5TupleOutputBuilder ob = new Transmit5TupleOutputBuilder();
	            ob.setEchoResult(EchoResult.Reachable);
	            RpcResult<Transmit5TupleOutput> rpcResult =
	                    Rpcs.<Transmit5TupleOutput> getRpcResult(true, ob.build(),
	                            Collections.<RpcError> emptySet());

	            return Futures.immediateFuture(rpcResult);
	}

}
