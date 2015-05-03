package org.opendaylight.controller.evaluator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.opendaylight.controller.monitor.SwitchPortStatTuple;

public class EvaluatorCalc {

    String inputFile = "EventList.conf";
    InputStream fis;
    BufferedReader br = null;
    String line = null;

    List<EventDataTuple> eventData = new ArrayList<>();

    public EvaluatorCalc() {

    }

    public void setEventFileInput() {

        // Input raw list
        try {
            fis = new FileInputStream(inputFile);
            br = new BufferedReader(new InputStreamReader(fis,
                    Charset.forName("UTF-8")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setEventFileProcess(List<SwitchPortStatTuple> switchPortStat) {

        // Process
        try {

            // For each switch
            while ((line = br.readLine()) != null) {

                /*
                 * 0: eventName, 1: switchDPID, 2: portNumber, 3: tarSwitchDPID,
                 * 4: tarPortNumber, 5: receivePackets, 6: transmitPackets, 7:
                 * receiveBytes, 8: transmitBytes, 9: receiveDropped, 10:
                 * transmitDropped, 11: receiveErrors, 12: transmitErrors, 13:
                 * receiveFrameErrors, 14: receiveOverrunErrors, 15:
                 * receiveCRCErrors, 16: collisions
                 */
                String[] listEntry = line.split(", ");

                int flagMatch = 0;

                EventDataTuple eventDataTuple = null;
                // If the statistics matches the threshold
                for (int i = 0; i < switchPortStat.size(); i++) {
                	/*
                	System.out.println("size: "+ switchPortStat.size() +"/ i: "+i);
                    System.out.println(switchPortStat.get(i).getSwitchDPID()
                            + ":" + listEntry[1]);
                    System.out.println(switchPortStat.get(i).getPortNumber()
                            + ":" + listEntry[2]);
                    System.out.println(switchPortStat.get(i)
                            .getReceivePackets() + ":" + listEntry[5]);
                    System.out.println(switchPortStat.get(i)
                            .getTransmitPackets() + ":" + listEntry[6]);
                    System.out.println(switchPortStat.get(i).getReceiveBytes()
                            + ":" + listEntry[7]);
                    System.out.println(switchPortStat.get(i).getTransmitBytes()
                            + ":" + listEntry[8]);
                    System.out.println(switchPortStat.get(i)
                            .getReceiveDropped() + ":" + listEntry[9]);
                    System.out.println(switchPortStat.get(i)
                            .getTransmitDropped() + ":" + listEntry[10]);
                    */
                    if ((switchPortStat.get(i).getSwitchDPID()
                            .equalsIgnoreCase(listEntry[1]) && switchPortStat
                            .get(i).getPortNumber()
                            .equalsIgnoreCase(listEntry[2]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getReceivePackets()) > Integer
                                    .valueOf(listEntry[5]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getTransmitPackets()) > Integer
                                    .valueOf(listEntry[6]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getReceiveBytes()) > Integer
                                    .valueOf(listEntry[7]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getTransmitBytes()) > Integer
                                    .valueOf(listEntry[8]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getReceiveDropped()) > Integer
                                    .valueOf(listEntry[9]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getTransmitDropped()) > Integer
                                    .valueOf(listEntry[10]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getReceiveErrors()) > Integer
                                    .valueOf(listEntry[11]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getTransmitErrors()) > Integer
                                    .valueOf(listEntry[12]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getReceiveFrameErrors()) > Integer
                                    .valueOf(listEntry[13]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getReceiveOverrunErrors()) > Integer
                                    .valueOf(listEntry[14]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getReceiveCRCErrors()) > Integer
                                    .valueOf(listEntry[15]))
                            && (Integer.valueOf(switchPortStat.get(i)
                                    .getCollisions()) > Integer
                                    .valueOf(listEntry[16]))) {
                        System.out.println("upper");
                        eventDataTuple = new EventDataTuple(switchPortStat.get(
                                i).getSwitchDPID(), switchPortStat.get(i)
                                .getPortNumber(), switchPortStat.get(i)
                                .getReceivePackets(), switchPortStat.get(i)
                                .getTransmitPackets(), switchPortStat.get(i)
                                .getReceiveBytes(), switchPortStat.get(i)
                                .getTransmitBytes(), switchPortStat.get(i)
                                .getReceiveDropped(), switchPortStat.get(i)
                                .getTransmitDropped(), switchPortStat.get(i)
                                .getReceiveErrors(), switchPortStat.get(i)
                                .getTransmitErrors(), switchPortStat.get(i)
                                .getReceiveFrameErrors(), switchPortStat.get(i)
                                .getReceiveOverrunErrors(), switchPortStat.get(
                                i).getReceiveCRCErrors(), switchPortStat.get(i)
                                .getCollisions());
                        flagMatch = 1;
                        break;
                    }
                }

                // If over the threshold
                if (flagMatch == 1) {

                    int flagTargetRule = 0;
                    // Check current rules
                    System.out.println( "eventdatasize"+eventData.size());
                    for (int i = 0; i < eventData.size(); i++) {
                        System.out.println("event check");
                        System.out.println(eventData.get(i).getSwitchDPID()+" : "+listEntry[3]);
                        System.out.println(eventData.get(i).getPortNumber()+" : "+listEntry[4]);
                        // Target rule exists
                        if (eventData.get(i).getSwitchDPID()
                                .equalsIgnoreCase(listEntry[3])
                                && eventData.get(i).getPortNumber()
                                        .equalsIgnoreCase(listEntry[4])) {
                            flagTargetRule = 1;
                            System.out.println("exist");
                            break;
                        }
                    }

                    // Target rule does not exist
                    if (flagTargetRule == 0) {
                        System.out.println( "1eventdatasize"+eventData.size());
                        System.out.println("not exist");
                        eventDataTuple.setEventName(listEntry[0]);
                        eventDataTuple.setTarSwitchDPID(listEntry[3]);
                        eventDataTuple.setTarPortNumber(listEntry[4]);
                        eventData.add(eventDataTuple);
                        System.out.println( "2eventdatasize"+eventData.size());
                    }
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Clean up
        try {
            br.close();
            br = null;
            fis = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<EventDataTuple> getEventData() {
        return eventData;
    }

}