package org.opendaylight.controller.pushmeter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.opendaylight.controller.pushmeter.internal.PushMeter;
import org.opendaylight.controller.sal.reader.NodeConnectorStatistics;
import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.opendaylight.controller.switchmanager.ISwitchManager;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.meters.Meter;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.meters.MeterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.meters.MeterKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.service.rev130918.AddMeterInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.BandId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.MeterBandType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.MeterFlags;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.MeterId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.band.type.band.type.DropBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.meter.MeterBandHeadersBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.meter.meter.band.headers.MeterBandHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.meter.meter.band.headers.MeterBandHeaderBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.meter.meter.band.headers.meter.band.header.MeterBandTypesBuilder;

public class PushMeterServiceLocalRequest {

	public PushMeterServiceLocalRequest(Socket client) {
		// TODO Auto-generated constructor stub
		try {System.out.println("for1");
			ObjectInputStream inFromServer = new ObjectInputStream(
					client.getInputStream());
			
			PushMeterInfo meterinfo_get = null;
			meterinfo_get = (PushMeterInfo) inFromServer.readObject();
			ISwitchManager switchManager = (ISwitchManager) ServiceHelper
	                .getInstance(ISwitchManager.class, "default", this);
			System.out.println("for2");
	        for (org.opendaylight.controller.sal.core.Node nn : switchManager.getNodes()) {
	        	System.out.println("for");
	        	System.out.println(nn);
	            NodeRef o_node = org.opendaylight.controller.sal.compatibility.NodeMapping.toNodeRef(nn);
	            pushMeter(o_node, meterinfo_get.getId(), meterinfo_get.getRate());
	            System.out.println(o_node);
	        }
			
			
			client.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void pushMeter(NodeRef node, long meter_id, long rate){
		AddMeterInputBuilder meterBuilder = new AddMeterInputBuilder();

        MeterKey key = new MeterKey(new MeterId(meter_id));
        MeterBuilder meter = new MeterBuilder();
        meter.setContainerName("abcd");
        meter.setKey(key);
        meter.setMeterId(new MeterId(9L));
        meter.setMeterName("meter1");
        meter.setFlags(new MeterFlags(false, true, false, false));
        MeterBandHeadersBuilder bandHeaders = new MeterBandHeadersBuilder();
        List<MeterBandHeader> bandHdr = new ArrayList<MeterBandHeader>();
        MeterBandHeaderBuilder bandHeader = new MeterBandHeaderBuilder();
        
        bandHeader.setBandBurstSize((long) 444);
        DropBuilder dropRemark = new DropBuilder();
        dropRemark.setDropRate(rate);
        dropRemark.setDropBurstSize((long)1024);
        
        bandHeader.setBandType(dropRemark.build());
        MeterBandTypesBuilder bandTypes = new MeterBandTypesBuilder();
        MeterBandType bandType = new MeterBandType(true, false, false);
        bandTypes.setFlags(bandType);
        bandHeader.setMeterBandTypes(bandTypes.build());
        bandHeader.setBandId(new BandId(0L));
        bandHdr.add(bandHeader.build());
        bandHeaders.setMeterBandHeader(bandHdr);
        meter.setMeterBandHeaders(bandHeaders.build());
        
        Meter testMeter = meter.build();
        
        meterBuilder.setContainerName(testMeter.getContainerName());
        meterBuilder.setFlags(testMeter.getFlags());
        meterBuilder.setMeterBandHeaders(testMeter.getMeterBandHeaders());
        meterBuilder.setNode(node);
        meterBuilder.setMeterId(testMeter.getMeterId());
        meterBuilder.setMeterName(testMeter.getMeterName());
        System.out.println("for2");
        PushMeter.flowService.addMeter(meterBuilder.build());
	} 
	
	private NodeRef match_node(String dpid){
		NodeRef node =null;
		ISwitchManager switchManager = (ISwitchManager) ServiceHelper
                .getInstance(ISwitchManager.class, "default", this);
        
        for (org.opendaylight.controller.sal.core.Node nn : switchManager.getNodes()) {
        	if(nn.getNodeIDString().equalsIgnoreCase(dpid)){
        		node = org.opendaylight.controller.sal.compatibility.NodeMapping.toNodeRef(nn);
        		break;
        	}
        }
		return node;
		
	}

}
