package org.opendaylight.controller.pushflow.internal;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.opendaylight.controller.pushflow.PushFlowService;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ConsumerContext;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.Uri;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.OutputActionCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.output.action._case.OutputAction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.output.action._case.OutputActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.list.Action;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.list.ActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.list.ActionKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.AddFlowInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.AddFlowOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.SalFlowService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.InstructionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.ApplyActionsCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.apply.actions._case.ApplyActionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.Instruction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yangtools.yang.common.RpcResult;

public class PushFlow {

    NotificationService notificationService;
    private ConsumerContext context;
    public static SalFlowService flowService;

    public void setContext(ConsumerContext session) {
        this.context = session;
    }

    public void setFlowService(SalFlowService flowService) {
        PushFlow.flowService = flowService;
    }

    public void start() {
        
        notificationService = context.getSALService(NotificationService.class);
        PushFlowService pushflowService = new PushFlowService();
        pushflowService.start();
        
        //For Send to controller (pkt_in) 
        ini();
    }
    
    public void ini(){
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String containerName = "default";
        ISwitchManager switchManager = (ISwitchManager) ServiceHelper
                .getInstance(ISwitchManager.class, containerName, this);
        for (Node nn : switchManager.getNodes()) {
                NodeRef o_node = org.opendaylight.controller.sal.compatibility.NodeMapping.toNodeRef(nn);
                SendToController(o_node);
        }
    }
    public void SendToController(NodeRef node){
        AddFlowInputBuilder ret = new AddFlowInputBuilder();

        ret.setNode(node);

        Uri outputPort = new Uri("CONTROLLER");

        OutputActionBuilder outputActionBuilder = new OutputActionBuilder();
        outputActionBuilder.setMaxLength(new Integer(0xffff));
        outputActionBuilder.setOutputNodeConnector(outputPort);
        OutputAction outputAction = outputActionBuilder.build();

        ActionBuilder action = new ActionBuilder();
        action.setAction(new OutputActionCaseBuilder().setOutputAction(outputAction).build());
        action.setKey(new ActionKey(0));
        action.setOrder(0);
        
        List<Action> actionList = new ArrayList<Action>();
        
        actionList.add(action.build());
        
        ApplyActionsCaseBuilder aaBldr = new ApplyActionsCaseBuilder();
        aaBldr.setApplyActions(new ApplyActionsBuilder().setAction(actionList).build());

        InstructionBuilder instructionBldr = new InstructionBuilder();
        instructionBldr.setInstruction(aaBldr.build());

        List<Instruction> isntructions = Collections.singletonList(instructionBldr.build());
        InstructionsBuilder instructionsBldr = new InstructionsBuilder();
        instructionsBldr.setInstruction(isntructions);

        ret.setInstructions(instructionsBldr.build());
        
        ret.setIdleTimeout(0);
        ret.setHardTimeout(0);
        ret.setPriority(0);
        
        Future<RpcResult<AddFlowOutput>> result = PushFlow.flowService.addFlow(ret.build());

        try {
            System.out.println("<<Log org.opendaylight.controller.pushflow.PushFlowServiceLocalRequest.pushfloodflow00>>+\n"
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
