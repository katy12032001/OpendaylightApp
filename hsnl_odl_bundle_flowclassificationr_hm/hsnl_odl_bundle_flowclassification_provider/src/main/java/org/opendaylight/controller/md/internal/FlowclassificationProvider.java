package org.opendaylight.controller.md.internal;

import org.opendaylight.controller.sal.binding.api.AbstractBindingAwareProvider;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.yang.gen.v1.urn.nthu.hsnl.md.example.rev150105.FlowclassificationService;
import org.osgi.framework.BundleContext;

public class FlowclassificationProvider extends AbstractBindingAwareProvider{
	FlowclassificationImp flowclassificationImp;
	
	public FlowclassificationProvider(){
		flowclassificationImp = new FlowclassificationImp();
	}
	@Override
    protected void startImpl(BundleContext context) {
    }

    
	@Override
	public void onSessionInitiated(ProviderContext session) {
		// TODO Auto-generated method stub
		session.addRpcImplementation(FlowclassificationService.class, flowclassificationImp);
	}
    
}
