package org.opendaylight.controller.pushmeter.internal;

import org.opendaylight.controller.sal.binding.api.AbstractBindingAwareConsumer;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ConsumerContext;
import org.opendaylight.controller.sal.binding.api.data.DataBrokerService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.SalFlowService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.service.rev130918.SalMeterService;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractBindingAwareConsumer {

	PushMeter helloworld;
	private SalMeterService meterService;

	@Override
	protected void startImpl(BundleContext context) {
		helloworld = new PushMeter();
	}

	@Override
	public void onSessionInitialized(ConsumerContext session) {
		helloworld.setContext(session);
		meterService = session.getRpcService(SalMeterService.class);
		helloworld.setFlowService(meterService);
		helloworld.start();

	}
}
