package org.opendaylight.controller.pushmeter.internal;


import org.opendaylight.controller.pushmeter.PushMeterService;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ConsumerContext;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.service.rev130918.SalMeterService;

public class PushMeter {
    NotificationService notificationService;
    private ConsumerContext context;
    public static SalMeterService flowService;
    
    public void setContext(ConsumerContext session) {
        this.context = session;
    }

    public void setFlowService(SalMeterService flowService) {
        PushMeter.flowService = flowService;
    }
    
    public void start() {
        
        notificationService = context.getSALService(NotificationService.class);
        PushMeterService pushflowService = new PushMeterService();
        pushflowService.start();
    }

}
