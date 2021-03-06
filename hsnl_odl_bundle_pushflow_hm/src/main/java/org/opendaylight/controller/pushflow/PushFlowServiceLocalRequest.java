package org.opendaylight.controller.pushflow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.MeterCaseBuilder;
import org.opendaylight.controller.pushflow.internal.PushFlow;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.Ipv4Prefix;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.PortNumber;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.Uri;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev100924.MacAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.DropActionCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.DropActionCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.OutputActionCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.OutputActionCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.drop.action._case.DropAction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.drop.action._case.DropActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.output.action._case.OutputAction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.output.action._case.OutputActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.list.ActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.list.ActionKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.AddFlowInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.AddFlowOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.FlowModFlags;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.InstructionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.Match;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.MatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.ApplyActionsCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.apply.actions._case.ApplyActionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.meter._case.MeterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.Instruction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnectorKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.l2.types.rev130827.EtherType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.MeterId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.ethernet.match.fields.EthernetDestinationBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.ethernet.match.fields.EthernetSourceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.ethernet.match.fields.EthernetTypeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.match.EthernetMatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.match.IpMatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.match.layer._3.match.Ipv4MatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.match.layer._4.match.TcpMatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.match.layer._4.match.UdpMatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.list.Action;
import org.opendaylight.yangtools.yang.common.RpcResult;

public class PushFlowServiceLocalRequest {
    
    public PushFlowServiceLocalRequest(Socket client) {
        try {
            ObjectInputStream inFromServer = new ObjectInputStream(
                    client.getInputStream());

            PushFlowInfo flowinfo_get = null;
            flowinfo_get = (PushFlowInfo) inFromServer.readObject();
            client.close();

            if (flowinfo_get.get_index() == 0) {
                if (flowinfo_get.get_type() == 0) {
                    // Arp
                    NodeRef a_node = org.opendaylight.controller.sal.compatibility.NodeMapping
                            .toNodeRef(flowinfo_get.get_node());
                    //pushArpflow(a_node , flowinfo_get.get_src_mac());
                } else if (flowinfo_get.get_type() == 1) {
                    //Output
                    NodeRef o_node = org.opendaylight.controller.sal.compatibility.NodeMapping
                            .toNodeRef(flowinfo_get.get_node());
                    System.out.println("port" + flowinfo_get.get_outport());
                    NodeConnectorRef out_port = org.opendaylight.controller.sal.compatibility.NodeMapping
                            .toNodeConnectorRef(flowinfo_get.get_outport());
                    pushOutflow(o_node, out_port, flowinfo_get.get_src_ip(),
                            flowinfo_get.get_dst_ip(),
                            flowinfo_get.get_src_mac(),
                            flowinfo_get.get_dst_mac(),flowinfo_get.getQueue());
                    
                }
                if (flowinfo_get.get_type() == 2) {
                    // RateLimite
                    NodeRef o_node = org.opendaylight.controller.sal.compatibility.NodeMapping
                            .toNodeRef(flowinfo_get.get_node());
                    System.out.println("port" + flowinfo_get.get_outport());
                    NodeConnectorRef out_port = org.opendaylight.controller.sal.compatibility.NodeMapping
                            .toNodeConnectorRef(flowinfo_get.get_outport());
                    
                    pushratelimiteflow(o_node, out_port, flowinfo_get.get_src_ip(),
                            flowinfo_get.get_dst_ip(),
                            flowinfo_get.get_src_mac(),
                            flowinfo_get.get_dst_mac(),flowinfo_get.get_src_port(),flowinfo_get.get_dst_port(),flowinfo_get.getProtocal(),flowinfo_get.getQueue());
                }
                
                if (flowinfo_get.get_type() == 3) {
                    // flood
                    NodeRef a_node = org.opendaylight.controller.sal.compatibility.NodeMapping
                            .toNodeRef(flowinfo_get.get_node());
                    //pushFloodflow(a_node , flowinfo_get.get_src_mac());
                }
            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void pushOutflow(NodeRef node, NodeConnectorRef outport,
            String src_ip, String dst_ip, String src_mac, String dst_mac, long queue) {
        System.out.println(node+","+outport+","+src_ip+","+dst_ip+","+src_mac+","+dst_mac+"," + queue);
        AddFlowInputBuilder ret = new AddFlowInputBuilder();

        ret.setNode(node);

        Ipv4MatchBuilder ipv4Match = new Ipv4MatchBuilder();
        ipv4Match.setIpv4Source(new Ipv4Prefix(src_ip+"/32"));
        ipv4Match.setIpv4Destination(new Ipv4Prefix(dst_ip+"/32"));

        EthernetSourceBuilder ethSourceBuilder = new EthernetSourceBuilder();
        ethSourceBuilder.setAddress(new MacAddress(src_mac));
        EthernetDestinationBuilder ethDestBuilder = new EthernetDestinationBuilder();
        ethDestBuilder.setAddress(new MacAddress(dst_mac));
        EthernetTypeBuilder ethTypeBuilder = new EthernetTypeBuilder();
        ethTypeBuilder.setType(new EtherType(0x0800L));

        EthernetMatchBuilder eth = new EthernetMatchBuilder();
        eth.setEthernetType(ethTypeBuilder.build());
        eth.setEthernetSource(ethSourceBuilder.build());
        eth.setEthernetDestination(ethDestBuilder.build());

        MatchBuilder matchbuilder = new MatchBuilder();
        matchbuilder.setLayer3Match(ipv4Match.build());
        matchbuilder.setEthernetMatch(eth.build());

        Match match = matchbuilder.build();
        ret.setMatch(match);

        Uri outputPort = outport.getValue()
                .firstKeyOf(NodeConnector.class, NodeConnectorKey.class)
                .getId();

        OutputActionBuilder outputActionBuilder = new OutputActionBuilder();
        outputActionBuilder.setMaxLength(new Integer(0xffff));
        outputActionBuilder.setOutputNodeConnector(outputPort);
        OutputAction outputAction = outputActionBuilder.build();

        OutputActionCaseBuilder outputActionCaseBuilder = new OutputActionCaseBuilder();
        outputActionCaseBuilder.setOutputAction(outputAction);
        OutputActionCase outputActionCase = outputActionCaseBuilder.build();

        ActionBuilder action = new ActionBuilder();
        action.setAction(outputActionCase);
        action.setKey(new ActionKey(0));
        action.setOrder(0);

        
        DropActionBuilder dropactionbuilder = new DropActionBuilder();
        DropAction dropaction = dropactionbuilder.build();
        
        DropActionCaseBuilder dropactioncasebuilder = new DropActionCaseBuilder();
        dropactioncasebuilder.setDropAction(dropaction);
        DropActionCase dropactioncase = dropactioncasebuilder.build();
        
        ActionBuilder action_d = new ActionBuilder();
        action_d.setAction(dropactioncase);
        action_d.setKey(new ActionKey(0));
        action_d.setOrder(0);

        List<Action> actionList = new ArrayList<Action>();
        
        if(queue == -1){
            actionList.add(action_d.build());
        }
        else{
            actionList.add(action.build());
        }
        
        ApplyActionsCaseBuilder aaBldr = new ApplyActionsCaseBuilder();
        aaBldr.setApplyActions(new ApplyActionsBuilder().setAction(actionList)
                .build());

        InstructionBuilder instructionBldr = new InstructionBuilder();
        instructionBldr.setInstruction(aaBldr.build());
        instructionBldr.setOrder(0);
        List<Instruction> isntructions = new ArrayList<Instruction>();
        isntructions.add(instructionBldr.build());
        
        //meter
        if(queue != -1 && queue != 0){
	        MeterBuilder aab2 = new MeterBuilder();
	        aab2.setMeterId(new MeterId(new Long(queue)));
	        InstructionBuilder ib = new InstructionBuilder();
	        ib.setInstruction(new MeterCaseBuilder().setMeter(aab2.build()).build());
	        ib.setOrder(1);
	        isntructions.add(ib.build());
        }
        InstructionsBuilder instructionsBldr = new InstructionsBuilder();
        instructionsBldr.setInstruction(isntructions);

        ret.setInstructions(instructionsBldr.build());
        
        if(queue == -1){
            ret.setIdleTimeout(0);
            ret.setHardTimeout(0);
        }
        
        else ret.setIdleTimeout(15);
        ret.setPriority(10);
        ret.setFlags(new FlowModFlags(false, false, false, false, true));
        Future<RpcResult<AddFlowOutput>> result = PushFlow.flowService
                .addFlow(ret.build());

        try {
            System.out
                    .println("<<Log org.opendaylight.controller.pushflow.PushFlowServiceLocalRequest.pushOutflow1>>+\n"
                            + result.get(5, TimeUnit.SECONDS).isSuccessful());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void pushratelimiteflow(NodeRef node, NodeConnectorRef outport,
            String src_ip, String dst_ip, String src_mac, String dst_mac, int src_port, int dst_port, int pro, long queue) {
        AddFlowInputBuilder ret = new AddFlowInputBuilder();
        System.out.println("node : "+node +" "+outport+
        		"\n src_ip  dst_ip: "+src_ip +" "+dst_ip+
        		"\n src_mac  dst_mac: "+src_mac +" "+dst_mac+
        		"\n src_port dst_port : "+src_port +" "+dst_port+
        		"\n pro : "+pro+
        		"\n queue : "+queue);
        ret.setNode(node);

        Ipv4MatchBuilder ipv4Match = new Ipv4MatchBuilder();
        ipv4Match.setIpv4Source(new Ipv4Prefix(src_ip+"/32"));
        ipv4Match.setIpv4Destination(new Ipv4Prefix(dst_ip+"/32"));
        
        EthernetSourceBuilder ethSourceBuilder = new EthernetSourceBuilder();
        ethSourceBuilder.setAddress(new MacAddress(src_mac));
        EthernetDestinationBuilder ethDestBuilder = new EthernetDestinationBuilder();
        ethDestBuilder.setAddress(new MacAddress(dst_mac));
        EthernetTypeBuilder ethTypeBuilder = new EthernetTypeBuilder();
        ethTypeBuilder.setType(new EtherType(0x0800L));

        EthernetMatchBuilder eth = new EthernetMatchBuilder();
        eth.setEthernetType(ethTypeBuilder.build());
        eth.setEthernetSource(ethSourceBuilder.build());
        eth.setEthernetDestination(ethDestBuilder.build());
        
        MatchBuilder matchbuilder = new MatchBuilder();
        IpMatchBuilder ipmatch = new IpMatchBuilder(); // ipv4 version
        
        
        UdpMatchBuilder udpmatch = new UdpMatchBuilder();
        TcpMatchBuilder tcpmatch = new TcpMatchBuilder();
        
        if(pro == 17){
             //Udp
            PortNumber udpsrcport = new PortNumber(src_port);
            PortNumber udpdstport = new PortNumber(dst_port);
            
            udpmatch.setUdpDestinationPort(udpdstport);
            udpmatch.setUdpSourcePort(udpsrcport);
            
            matchbuilder.setLayer4Match(udpmatch.build());
            ipmatch.setIpProtocol((short) 17);
        }
        else{
         // Tcp
            PortNumber tcpsrcport = new PortNumber(src_port);
            PortNumber tcpdstport = new PortNumber(dst_port);
            
            tcpmatch.setTcpDestinationPort(tcpdstport);
            tcpmatch.setTcpSourcePort(tcpsrcport);
            
            matchbuilder.setLayer4Match(tcpmatch.build());
            ipmatch.setIpProtocol((short) 6);
        }
        
        matchbuilder.setLayer3Match(ipv4Match.build());
        matchbuilder.setEthernetMatch(eth.build());
        matchbuilder.setIpMatch(ipmatch.build());

        Match match = matchbuilder.build();
        ret.setMatch(match);

        Uri outputPort = outport.getValue()
                .firstKeyOf(NodeConnector.class, NodeConnectorKey.class)
                .getId();

        OutputActionBuilder outputActionBuilder = new OutputActionBuilder();
        outputActionBuilder.setMaxLength(new Integer(0xffff));
        outputActionBuilder.setOutputNodeConnector(outputPort);
        OutputAction outputAction = outputActionBuilder.build();

        OutputActionCaseBuilder outputActionCaseBuilder = new OutputActionCaseBuilder();
        outputActionCaseBuilder.setOutputAction(outputAction);
        OutputActionCase outputActionCase = outputActionCaseBuilder.build();

        ActionBuilder action = new ActionBuilder();
        action.setAction(outputActionCase);
        action.setKey(new ActionKey(0));
        action.setOrder(0);

        DropActionBuilder dropactionbuilder = new DropActionBuilder();
        DropAction dropaction = dropactionbuilder.build();
        
        DropActionCaseBuilder dropactioncasebuilder = new DropActionCaseBuilder();
        dropactioncasebuilder.setDropAction(dropaction);
        DropActionCase dropactioncase = dropactioncasebuilder.build();
        
        ActionBuilder action_d = new ActionBuilder();
        action_d.setAction(dropactioncase);
        action_d.setKey(new ActionKey(0));
        action_d.setOrder(0);

        List<Action> actionList = new ArrayList<Action>();
        
        if(queue == -1){
            actionList.add(action_d.build());
        }
        else{
            actionList.add(action.build());
        }
        
        ApplyActionsCaseBuilder aaBldr = new ApplyActionsCaseBuilder();
        aaBldr.setApplyActions(new ApplyActionsBuilder().setAction(actionList)
                .build());

        InstructionBuilder instructionBldr = new InstructionBuilder();
        instructionBldr.setInstruction(aaBldr.build());
        instructionBldr.setOrder(0);
        List<Instruction> isntructions = new ArrayList<Instruction>();
        isntructions.add(instructionBldr.build());
        
        //meter
        if(queue != -1 && queue != 0){
	        MeterBuilder aab = new MeterBuilder();
	        aab.setMeterId(new MeterId(new Long(queue)));
	        InstructionBuilder ib = new InstructionBuilder();
	        ib.setInstruction(new MeterCaseBuilder().setMeter(aab.build()).build());
	        ib.setOrder(1);
	        isntructions.add(ib.build());
        }
        
        InstructionsBuilder instructionsBldr = new InstructionsBuilder();
        instructionsBldr.setInstruction(isntructions);

        ret.setInstructions(instructionsBldr.build());
        
        if(queue == -1){
            ret.setIdleTimeout(0);
            ret.setHardTimeout(0);
        }
        else ret.setIdleTimeout(15);
        ret.setPriority(25);
        ret.setFlags(new FlowModFlags(false, false, false, false, true));
        Future<RpcResult<AddFlowOutput>> result = PushFlow.flowService
                .addFlow(ret.build());

        try {
            System.out
                    .println("<<Log org.opendaylight.controller.pushflow.PushFlowServiceLocalRequest.pushOutflow3>>+\n"
                            + result.get(5, TimeUnit.SECONDS).isSuccessful());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
  
}
