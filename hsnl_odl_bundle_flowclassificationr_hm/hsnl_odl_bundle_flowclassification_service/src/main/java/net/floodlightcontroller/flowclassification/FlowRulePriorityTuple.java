package net.floodlightcontroller.flowclassification;

import java.io.Serializable;

public class FlowRulePriorityTuple extends FlowRuleTuple implements
        Serializable {

    private static final long serialVersionUID = 1L;
    public int priority;

    public FlowRulePriorityTuple(String ipProtocol, String srcIP,
            String srcPort, String dstIP, String dstPort, String appName,
            int priority) {
        super(ipProtocol, srcIP, srcPort, dstIP, dstPort, appName);
        this.priority = priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

}
