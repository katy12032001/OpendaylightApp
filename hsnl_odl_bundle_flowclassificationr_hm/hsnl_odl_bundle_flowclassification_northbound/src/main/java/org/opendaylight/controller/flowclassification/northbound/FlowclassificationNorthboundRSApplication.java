package org.opendaylight.controller.flowclassification.northbound;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class FlowclassificationNorthboundRSApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(FlowclassificationNorthbound.class);
        return classes;
    }
}