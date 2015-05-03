package org.opendaylight.controller.pushflow.internal;

import org.opendaylight.controller.sal.binding.api.AbstractBindingAwareConsumer;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ConsumerContext;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.SalFlowService;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractBindingAwareConsumer {

    PushFlow helloworld;
    private SalFlowService flowService;

    @Override
    protected void startImpl(BundleContext context) {
        helloworld = new PushFlow();
    }

    @Override
    public void onSessionInitialized(ConsumerContext session) {
        helloworld.setContext(session);
        flowService = session.getRpcService(SalFlowService.class);
        helloworld.setFlowService(flowService);
        helloworld.start();

    }
}
