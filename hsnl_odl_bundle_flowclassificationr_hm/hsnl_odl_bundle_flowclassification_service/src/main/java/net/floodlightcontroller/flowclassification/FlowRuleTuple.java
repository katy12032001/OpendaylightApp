package net.floodlightcontroller.flowclassification;

import java.io.Serializable;

public class FlowRuleTuple implements Serializable {

    private String protocol;
    private String srcIP;
    private String srcPort;
    private String dstIP;
    private String dstPort;
    private String appName;

    public FlowRuleTuple(String protocol, String srcIP, String srcPort,
            String dstIP, String dstPort, String appName) {
        this.protocol = protocol;
        this.srcIP = srcIP;
        this.srcPort = srcPort;
        this.dstIP = dstIP;
        this.dstPort = dstPort;
        this.appName = appName;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setSrcIP(String srcIP) {
        this.srcIP = srcIP;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public void setDstIP(String dstIP) {
        this.dstIP = dstIP;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getSrcIP() {
        return srcIP;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public String getDstIP() {
        return dstIP;
    }

    public String getDstPort() {
        return dstPort;
    }

    public String getAppName() {
        return appName;
    }
}

