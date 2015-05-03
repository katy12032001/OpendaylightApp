package org.opendaylight.controller.flowcheck;

import java.io.Serializable;

public class FlowCheckInfo implements Serializable{
	private String TarNode;
	private String SrcIP;
	private String DstIP;
	private String SrcMAC;
	private String DstMAC;
	private String SrcPort;
	private String DstPort;
	
	public FlowCheckInfo(String TarNode, String SrcIP, String DstIP, String SrcMAC, String DstMAC, String SrcPort, String DstPort) {
        this.TarNode = TarNode;
        this.SrcIP = SrcIP;
        this.DstIP = DstIP;
        this.SrcMAC = SrcMAC;
        this.DstMAC = DstMAC;
        this.SrcPort = SrcPort;
        this.DstPort = DstPort;
    }
	
	public void SetNode(String TarNode){
		this.TarNode = TarNode;
	}
	
	public void SetSrcIP(String SrcIP){
		this.SrcIP = SrcIP;
	}
	
	public void SetDstIP(String DstIP){
		this.DstIP = DstIP;
	}
	
	public void SetSrcMAC(String SrcMAC){
		this.SrcMAC = SrcMAC;
	}
	
	public void SetDstMAC(String DstMAC){
		this.DstMAC = DstMAC;
	}
	
	public void SetSrcPort(String SrcPort){
		this.SrcPort = SrcPort;
	}
	
	public void SetDstPort(String DstPort){
		this.DstPort = DstPort;
	}
	
	public String GetNode(){
		return this.TarNode;
	}
	
	public String GetSrcIP(){
		return this.SrcIP;
	}
	
	public String GetDstIP(){
		return this.DstIP;
	}
	
	public String GetSrcPort(){
		return this.SrcPort;
	}
	
	public String GetDstPort(){
		return this.DstPort;
	}
	
	public String GetSrcMAC(){
		return this.SrcMAC;
	}
	
	public String GetDstMAC(){
		return this.DstMAC;
	}
}
