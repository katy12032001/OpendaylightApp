
/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.monitor.internal;

import org.apache.felix.dm.Component;
import org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerListener;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.statisticsmanager.IStatisticsManager;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends ComponentActivatorAbstractBase {
    protected static final Logger logger = LoggerFactory
            .getLogger(Activator.class);

    public void init() {
    }

    public void destroy() {
    }

    @Override
    public Object[] getImplementations() {
        Object[] res = { Monitor.class };
        return res;
    }

    @Override
    public void configureInstance(Component c, Object imp, String containerName) {
        if (imp.equals(Monitor.class)) {
            c.setInterface(new String[] { Monitor.class.getName()}, null);
            
            c.add(createContainerServiceDependency(containerName)
                    .setService(IStatisticsManager.class)
                    .setCallbacks("setStatisticsManager",
                            "unsetStatisticsManager").setRequired(true));
            
            c.add(createContainerServiceDependency(containerName)
                    .setService(ISwitchManager.class)
                    .setCallbacks("setSwitchManagerService",
                            "unsetSwitchManagerService").setRequired(true));
            
            c.add(createContainerServiceDependency(containerName)
            		.setService(IFlowProgrammerListener.class)
            		.setCallbacks("setListener", "unsetListener")
            		.setRequired(true));
        }
    }
}
