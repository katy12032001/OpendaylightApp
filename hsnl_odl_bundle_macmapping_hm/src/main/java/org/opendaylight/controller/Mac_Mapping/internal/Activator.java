package org.opendaylight.controller.Mac_Mapping.internal;

import org.apache.felix.dm.Component;
import org.opendaylight.controller.hosttracker.IfIptoHost;
import org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase;
import org.opendaylight.controller.sal.packet.IDataPacketService;
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
        Object[] res = { Mac_Mapping.class };
        return res;
    }

    @Override
    public void configureInstance(Component c, Object imp, String containerName) {
        if (imp.equals(Mac_Mapping.class)) {
            c.setInterface(new String[] { Mac_Mapping.class.getName() }, null);
            
            c.add(createContainerServiceDependency(containerName)
                    .setService(IfIptoHost.class)
                    .setCallbacks("setHostTracker",
                            "unsetHostTracker").setRequired(true));
        }
    }
}