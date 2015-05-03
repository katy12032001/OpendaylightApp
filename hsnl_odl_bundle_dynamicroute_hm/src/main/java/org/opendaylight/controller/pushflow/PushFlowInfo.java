package org.opendaylight.controller.pushflow;

import java.io.Serializable;

import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.core.NodeConnector;

public class PushFlowInfo implements Serializable {

    private int index; // 0:output 1:enqueue
    private int type; // 0:arp 1:ipv4
    private Node node;
    private NodeConnector outport;
    private String src_ip;
    private String dst_ip;
    private int protocal;
    private int src_port;
    private int dst_port;
    
    private String src_mac;
    private String dst_mac;
    private long queue;

    public PushFlowInfo(int index, int type, Node node, NodeConnector outport,
            String src_ip, String dst_ip, int src_port, int dst_port, int protocal,
            String src_mac, String dst_mac, long queue) {
        this.index = index;
        this.type = type;
        this.node = node;
        this.outport = outport;
        this.src_ip = src_ip;
        this.dst_ip = dst_ip;
        this.src_port = src_port;
        this.dst_port = dst_port;
        this.setProtocal(protocal);
        this.src_mac = src_mac;
        this.dst_mac = dst_mac;
        this.setQueue(queue);
    }

    public void set_index(int index) {
        this.index = index;
    }

    public int get_index() {
        return this.index;
    }

    public void set_type(int type) {
        this.type = type;
    }

    public int get_type() {
        return this.type;
    }

    public void set_node(Node node) {
        this.node = node;
    }

    public Node get_node() {
        return this.node;
    }

    public void set_outport(NodeConnector node) {
        this.outport = node;
    }

    public NodeConnector get_outport() {
        return this.outport;
    }

    public void set_src_ip(String src_ip) {
        this.src_ip = src_ip;
    }

    public String get_src_ip() {
        return this.src_ip;
    }

    public void set_dst_ip(String dst_ip) {
        this.dst_ip = dst_ip;
    }

    public String get_dst_ip() {
        return this.dst_ip;
    }

    public void set_src_port(int src_port) {
        this.src_port = src_port;
    }

    public int get_src_port() {
        return this.src_port;
    }

    public void set_dst_port(int dst_port) {
        this.dst_port = dst_port;
    }

    public int get_dst_port() {
        return this.dst_port;
    }

    public void set_src_mac(String src_mac) {
        this.src_mac = src_mac;
    }

    public String get_src_mac() {
        return this.src_mac;
    }

    public void set_dst_mac(String dst_mac) {
        this.dst_mac = dst_mac;
    }

    public String get_dst_mac() {
        return this.dst_mac;
    }

    public long getQueue() {
        return queue;
    }

    public void setQueue(long queue) {
        this.queue = queue;
    }

    public int getProtocal() {
        return protocal;
    }

    public void setProtocal(int protocal) {
        this.protocal = protocal;
    }
}
