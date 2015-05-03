package org.opendaylight.controller.test.internal;

public class MatchInfoParse {
	private String RawData;
	private String Solution;
	
	public MatchInfoParse(String raw){
		this.RawData = raw;
	}
	
	public void TransformToDstIP(){
		
		/*
		 * * Ipv4Match [_ipv4Destination=Ipv4Prefix [_value=203.117.124.83/32], _ipv4Source=Ipv4Prefix [_value=192.168.0.110/32], augmentation=[]]
		 */
		//System.out.println(this.RawData);
		String[] tokens = this.RawData.split("\\[");
		System.out.println(tokens[2]);
		String[] tokens2 = tokens[2].split("\\]");
		System.out.println(tokens2[0]);
		String[] tokens3 = tokens2[0].split("=");
		String[] tokens4 = tokens3[1].split("/");
		String solution = tokens4[0];
		System.out.println(solution);
		
		this.Solution = solution;
	}
	public void TransformToSrcIP(){
		
		/*
		 * Ipv4Match [_ipv4Destination=Ipv4Prefix [_value=203.117.124.83/32], _ipv4Source=Ipv4Prefix [_value=192.168.0.110/32], augmentation=[]]
		 */
		
		String[] tokens = this.RawData.split("\\[");
		String[] tokens2 = tokens[3].split("\\]");
		String[] tokens3 = tokens2[0].split("=");
		String[] tokens4 = tokens3[1].split("/");
		String solution = tokens4[0];
		System.out.println(solution);
		
		this.Solution = solution;
	}
	
	public void TransformToDstMAC(){
		
		/*
		 * EthernetDestination [_address=MacAddress [_value=1C:7E:E5:94:00:AE], augmentation=[]]
		 */
		String[] tokens = this.RawData.split("value=");
		String[] tokens2 = tokens[1].split("\\]");
		String solution = tokens2[0];
		
		this.Solution = solution;
	}
	
	public void TransformToSrcMAC(){
		
		/*
		 * EthernetSource [_address=MacAddress [_value=60:EB:69:56:E4:50], augmentation=[]]
		 * 
		 */
		
		String[] tokens = this.RawData.split("value=");
		String[] tokens2 = tokens[1].split("\\]");
		String solution = tokens2[0];
		
		this.Solution = solution;
	}
	
	public void TransformToDstPort(){
		
		/*
		 * TcpMatch [_tcpDestinationPort=PortNumber [_value=62877], _tcpSourcePort=PortNumber [_value=80], augmentation=[]]
		 * 
		 */
		
		String[] tokens = this.RawData.split("\\[");
		String[] tokens2 = tokens[2].split("\\]");
		String[] tokens3 = tokens2[0].split("=");
		String solution = tokens3[1];
		
		this.Solution = solution;
	}
	public void TransformToSrcPort(){
		
		/*
		 * TcpMatch [_tcpDestinationPort=PortNumber [_value=62877], _tcpSourcePort=PortNumber [_value=80], augmentation=[]]
		 * 
		 */
		
		String[] tokens = this.RawData.split("\\[");
		String[] tokens2 = tokens[3].split("\\]");
		String[] tokens3 = tokens2[0].split("=");
		String solution = tokens3[1];
		
		this.Solution = solution;
	}
	public String GetSolution(){
		return this.Solution;
	}
}
