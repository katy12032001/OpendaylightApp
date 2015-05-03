package net.floodlightcontroller.flowclassification;

import java.util.List;

import org.opendaylight.controller.sal.core.NodeConnector;

public class FlowInfo {

    private List<NodeConnector> rt;
    private boolean rL;

    private String protocol;
    private String srcIP;
    private String dstIP;

    private byte[] dstMAC;
    private byte[] srcMAC;

    private NodeConnector in;
    private NodeConnector out;
    private String dstPort;
    private String srcPort;
    private String IPPort;

    public FlowInfo(String dstIP, String srcIP, List<NodeConnector> rt,
            boolean rL,  NodeConnector in, NodeConnector out, byte[] dstMAC,
            byte[] srcMAC,String dstPort, String srcPort) {

        this.rt = rt;
        this.rL = rL;

        this.srcIP = srcIP;
        this.dstIP = dstIP;

        this.dstMAC = dstMAC;
        this.srcMAC = srcMAC;

        this.in = in;
        this.out = out;
        
        this.dstPort = dstPort;
        this.srcPort = srcPort;

    }

    public void setRoute(List<NodeConnector> rt) {
        this.rt = rt;
    }

    public void setRateLimit(boolean rL) {
        this.rL = rL;
    }
    

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setDstIP(String dstIP) {
        this.dstIP = dstIP;
    }

    public void setSrcIP(String srcIP) {
        this.srcIP = srcIP;
    }

    public void setDstMAC(byte[] dstMAC) {
        this.dstMAC = dstMAC;
    }

    public void setSrcMAC(byte[] srcMAC) {
        this.srcMAC = srcMAC;
    }

    public void setInNode(NodeConnector in) {
        this.in = in;
    }

    public void setOutNode(NodeConnector out) {
        this.out = out;
    }

    public List<NodeConnector> getRoute() {
        return rt;
    }

    public boolean getRateLimit() {
        return rL;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getSrcIP() {
        return srcIP;
    }

    public String getDstIP() {
        return dstIP;
    }

    public byte[] getDstMAC() {
        return dstMAC;
    }

    public byte[] getSrcMAC() {
        return srcMAC;
    }

    public NodeConnector getInNode() {
        return in;
    }

    public NodeConnector getOutNode() {
        return out;
    }
    
    public String getSrcPort() {
        return srcPort;
    }

    public String getDstPort() {
        return dstPort;
    }
    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }
    public String getIPPort() {
        return IPPort;
    }
    public void setIPPort(String IPPort) {
        this.IPPort = IPPort;
    }
}