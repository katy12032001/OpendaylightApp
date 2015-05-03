/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.dynamic_route.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.dm.Component;
import org.opendaylight.controller.hosttracker.IfIptoHost;
import org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerListener;
import org.opendaylight.controller.sal.flowprogrammer.IFlowProgrammerService;
import org.opendaylight.controller.sal.packet.IDataPacketService;
import org.opendaylight.controller.sal.packet.IListenDataPacket;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.controller.topologymanager.ITopologyManager;
import org.opendaylight.controller.Mac_Mapping.internal.Mac_Mapping;
import org.opendaylight.controller.forwardingrulesmanager.IForwardingRulesManager;
import org.opendaylight.controller.forwardingrulesmanager.IForwardingRulesManagerAware;
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
        Object[] res = { ForwardingPkt.class };
        return res;
    }

    @Override
    public void configureInstance(Component c, Object imp, String containerName) {
        if (imp.equals(ForwardingPkt.class)) {
            // Define exported and used services for dynamic_route component.
        	String interfaces[] = null;
            Dictionary<String, Object> props = new Hashtable<String, Object>();
            props.put("salListenerName", "dynamic_route");
            
            interfaces = new String[] {IListenDataPacket.class.getName() };
            
            // Export IListenDataPacket interface to receive packet-in events.
            c.setInterface(interfaces,props);

            // Need the DataPacketService for encoding, decoding, sending data
            // packets
            c.add(createContainerServiceDependency(containerName)
                    .setService(IDataPacketService.class)
                    .setCallbacks("setDataPacketService",
                            "unsetDataPacketService").setRequired(true));

            // Need FlowProgrammerService for programming flows
            c.add(createContainerServiceDependency(containerName)
                    .setService(IFlowProgrammerService.class)
                    .setCallbacks("setFlowProgrammerService",
                            "unsetFlowProgrammerService").setRequired(true));

            // Need SwitchManager service for enumerating ports of switch
            c.add(createContainerServiceDependency(containerName)
                    .setService(ISwitchManager.class)
                    .setCallbacks("setSwitchManagerService",
                            "unsetSwitchManagerService").setRequired(true));
            c.add(createContainerServiceDependency(containerName)
                    .setService(IfIptoHost.class)
                    .setCallbacks("setHostTracker", "unsetHostTracker")
                    .setRequired(false));
            c.add(createContainerServiceDependency(containerName)
                    .setService(ITopologyManager.class)
                    .setCallbacks("setTopologyManagerService",
                            "unsetTopologyManagerService").setRequired(true));

            c.add(createContainerServiceDependency(containerName)
                    .setService(IForwardingRulesManager.class)
                    .setCallbacks("setForwardingRulesManagerService",
                            "unsetForwardingRulesManagerService")
                    .setRequired(true));

            c.add(createContainerServiceDependency(containerName)
                    .setService(Mac_Mapping.class)
                    .setCallbacks("setMac_Mapping_Service",
                            "unsetMac_Mapping_Service")
                    .setRequired(true));
        }
    }
}
