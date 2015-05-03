package org.opendaylight.controller.transfer_fivetuple.service.api;

public interface FlowclassificationServiceAPI{
	boolean Transfer_fivetuple(String SRC_IP, String DST_IP, String SRC_PORT, String DST_PORT, String APP, String IPPort, String apppercent);
}
