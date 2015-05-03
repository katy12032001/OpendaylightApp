package org.opendaylight.controller.pushmeter;

import java.io.Serializable;

public class PushMeterInfo implements Serializable{
	private long id;
	private int type;
	private long rate;
	//private String dpid;
	
	public PushMeterInfo(long id, int type, long rate) {
        this.id = id;
        this.type = type;
        this.rate = rate;
        //this.dpid = dpid;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getRate() {
		return rate;
	}

	public void setRate(long rate) {
		this.rate = rate;
	}
	/*
	public String getDpid() {
		return dpid;
	}

	public void setDpid(String dpid) {
		this.dpid = dpid;
	}*/
}
