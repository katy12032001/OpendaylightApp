package org.opendaylight.controller.monitor;

import java.util.ArrayList;
import java.util.List;


import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.reader.NodeConnectorStatistics;
import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.opendaylight.controller.statisticsmanager.IStatisticsManager;
import org.opendaylight.controller.switchmanager.ISwitchManager;


public class StatisticHandling {

    List<SwitchPortStatTuple> switchPortStat = new ArrayList<>();

    public void getStatistics() {
        
        String containerName = "default";
        IStatisticsManager statsManager = (IStatisticsManager) ServiceHelper
                .getInstance(IStatisticsManager.class, containerName, this);

        ISwitchManager switchManager = (ISwitchManager) ServiceHelper
                .getInstance(ISwitchManager.class, containerName, this);
        
        for (Node node : switchManager.getNodes()) {

            for (NodeConnectorStatistics nodestatistics : statsManager
                    .getNodeConnectorStatistics(node)) {
                SwitchPortStatTuple switchPortStatTuple = new SwitchPortStatTuple(
                        null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null);

                switchPortStatTuple.setSwitchDPID(node.toString());

                switchPortStatTuple.setPortNumber(nodestatistics
                        .getNodeConnector().toString());
                switchPortStatTuple.setReceiveBytes(String
                        .valueOf(nodestatistics.getReceiveByteCount()));
                switchPortStatTuple.setReceiveCRCErrors(String
                        .valueOf(nodestatistics.getReceiveCRCErrorCount()));
                switchPortStatTuple.setReceiveDropped(String
                        .valueOf(nodestatistics.getReceiveDropCount()));
                switchPortStatTuple.setReceiveErrors(String
                        .valueOf(nodestatistics.getReceiveErrorCount()));
                switchPortStatTuple.setReceiveFrameErrors(String
                        .valueOf(nodestatistics.getReceiveFrameErrorCount()));
                switchPortStatTuple.setReceiveOverrunErrors(String
                        .valueOf(nodestatistics.getReceiveOverRunErrorCount()));
                switchPortStatTuple.setReceivePackets(String
                        .valueOf(nodestatistics.getReceivePacketCount()));
                switchPortStatTuple.setTransmitBytes(String
                        .valueOf(nodestatistics.getTransmitByteCount()));
                switchPortStatTuple.setTransmitDropped(String
                        .valueOf(nodestatistics.getTransmitDropCount()));
                switchPortStatTuple.setTransmitErrors(String
                        .valueOf(nodestatistics.getTransmitErrorCount()));
                switchPortStatTuple.setTransmitPackets(String
                        .valueOf(nodestatistics.getTransmitPacketCount()));
                switchPortStatTuple.setCollisions(String.valueOf(nodestatistics
                        .getCollisionCount()));

                
                switchPortStat.add(switchPortStatTuple);
            }
        }
    }

    public List<SwitchPortStatTuple> getSwitchPortStat() {
        return switchPortStat;
    }
}
