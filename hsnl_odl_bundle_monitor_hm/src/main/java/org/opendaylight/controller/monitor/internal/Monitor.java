package org.opendaylight.controller.monitor.internal;

import org.opendaylight.controller.monitor.MonitorService;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.flowprogrammer.Flow;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerListener;
import org.opendaylight.controller.statisticsmanager.IStatisticsManager;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Monitor implements IFlowProgrammerListener{
    private static final Logger log = LoggerFactory.getLogger(Monitor.class);
    private MonitorService monitorService;
    static public ISwitchManager switchManager;
    static public IStatisticsManager statisticsManager;
    static public IFlowProgrammerListener FlowProgrammerListener;
    public Monitor() {
    }

    void setSwitchManagerService(ISwitchManager s) {
        switchManager = s;
    }

    void unsetSwitchManagerService(ISwitchManager s) {
        if (switchManager == s) {
            switchManager = null;
        }
    }

    void setStatisticsManager(IStatisticsManager s) {
        statisticsManager = s;
    }

    void unsetStatisticsManager(IStatisticsManager s) {
        if (statisticsManager == s) {
            statisticsManager = null;
        }
    }
    void setListener(IFlowProgrammerListener s) {
    	FlowProgrammerListener = s;
    }

    void unsetListener(IFlowProgrammerListener s) {
        if (FlowProgrammerListener == s) {
        	FlowProgrammerListener = null;
        }
    }
    public void init() {
        System.out.println("monitor init");
    }

    public void destroy() {
        System.out.println("monitor destroy");
    }

    public void start() {
        System.out.println("monitor start");
        monitorService = new MonitorService();
        monitorService.start();
    }

    public void stop() {
        System.out.println("monitor stop");
    }

	@Override
	public void flowErrorReported(Node arg0, long arg1, Object arg2) {
		// TODO Auto-generated method stub
		System.out.println(">>>>>>>>>>>>>>>>>"+arg0.toString() + arg1 + arg2);
	}

	@Override
	public void flowRemoved(Node arg0, Flow arg1) {
		// TODO Auto-generated method stub
		System.out.println(">>>>>>>>>>>>>>>>>"+arg0.toString() + arg1 );
	}
}
