package org.opendaylight.controller.monitor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SwitchPortStatCalc {

    int i, j;

    List<SwitchPortStatTuple> switchPortStatBase = new ArrayList<>();
    List<SwitchPortStatTuple> switchPortStatUpdate = new ArrayList<>();
    List<SwitchPortStatTuple> switchPortStatResult = new ArrayList<>();

    public SwitchPortStatCalc() {

    }

    /*
     * Port Statistics: DPID, receivePackets, transmitPackets, receiveBytes,
     * transmitBytes, receiveDropped, transmitDropped, receiveErrors,
     * transmitErrors, receiveFrameErrors, receiveOverrunErrors,
     * receiveCRCErrors, collisions, portNumber
     */

    public void setSwitchPortStatBase(List<SwitchPortStatTuple> switchPortStat) {
        switchPortStatBase = new ArrayList<SwitchPortStatTuple>(switchPortStat);
    }

    public void setSwitchPortStatUpdate(List<SwitchPortStatTuple> switchPortStat) {
        switchPortStatUpdate = new ArrayList<SwitchPortStatTuple>(
                switchPortStat);
    }

    public void setSwitchPortStatProcess() {

        /*
         * Calculation Policy RecevingRate: receiveBytes/TimeUnit transmitRate:
         * transmitBytes/TimeUnit
         */

        // Calculation based on the proposed policy
        for (i = 0; i < switchPortStatUpdate.size(); i++) {

            BigInteger rPacketsDiff = ((new BigInteger(switchPortStatUpdate
                    .get(i).getReceivePackets())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getReceivePackets())))
                    .divide(BigInteger.valueOf(5));
            BigInteger tPacketsDiff = ((new BigInteger(switchPortStatUpdate
                    .get(i).getTransmitPackets())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getTransmitPackets())))
                    .divide(BigInteger.valueOf(5));
            BigInteger rBytesDiff = ((new BigInteger(switchPortStatUpdate
                    .get(i).getReceiveBytes())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getReceiveBytes())))
                    .divide(BigInteger.valueOf(5));
            BigInteger tBytesDiff = ((new BigInteger(switchPortStatUpdate
                    .get(i).getTransmitBytes())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getTransmitBytes())))
                    .divide(BigInteger.valueOf(5));
            BigInteger rDroppedDiff = ((new BigInteger(switchPortStatUpdate
                    .get(i).getReceiveDropped())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getReceiveDropped())))
                    .divide(BigInteger.valueOf(5));
            BigInteger tDroppedDiff = ((new BigInteger(switchPortStatUpdate
                    .get(i).getTransmitDropped())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getTransmitDropped())))
                    .divide(BigInteger.valueOf(5));
            BigInteger rErrorsDiff = ((new BigInteger(switchPortStatUpdate.get(
                    i).getReceiveErrors())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getReceiveErrors())))
                    .divide(BigInteger.valueOf(5));
            BigInteger tErrorsDiff = ((new BigInteger(switchPortStatUpdate.get(
                    i).getTransmitErrors())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getTransmitErrors())))
                    .divide(BigInteger.valueOf(5));
            BigInteger rFrameErrorsDiff = ((new BigInteger(switchPortStatUpdate
                    .get(i).getReceiveFrameErrors())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getReceiveFrameErrors())))
                    .divide(BigInteger.valueOf(5));
            BigInteger rOverrunErrorsDiff = ((new BigInteger(
                    switchPortStatUpdate.get(i).getReceiveOverrunErrors()))
                    .subtract(new BigInteger(switchPortStatBase.get(i)
                            .getReceiveOverrunErrors()))).divide(BigInteger
                    .valueOf(5));
            BigInteger rCRCErrorsDiff = ((new BigInteger(switchPortStatUpdate
                    .get(i).getReceiveCRCErrors())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getReceiveCRCErrors())))
                    .divide(BigInteger.valueOf(5));
            BigInteger collisionsDiff = ((new BigInteger(switchPortStatUpdate
                    .get(i).getCollisions())).subtract(new BigInteger(
                    switchPortStatBase.get(i).getCollisions())))
                    .divide(BigInteger.valueOf(5));

            SwitchPortStatTuple switchPortStatTuple = new SwitchPortStatTuple(
                    switchPortStatUpdate.get(i).getSwitchDPID(),
                    switchPortStatUpdate.get(i).getPortNumber(),
                    rPacketsDiff.toString(), tPacketsDiff.toString(),
                    rBytesDiff.toString(), tBytesDiff.toString(),
                    rDroppedDiff.toString(), tDroppedDiff.toString(),
                    rErrorsDiff.toString(), tErrorsDiff.toString(),
                    rFrameErrorsDiff.toString(), rOverrunErrorsDiff.toString(),
                    rCRCErrorsDiff.toString(), collisionsDiff.toString());

            switchPortStatResult.add(switchPortStatTuple);
        }

    }

    public String getOutputByteDiff(int numOfPorts) {
        /*
         * for(int i=0;i<numOfPorts;i++){
         * if(arrStatResult[i][1].equalsIgnoreCase("1")){ return
         * arrStatResult[i][2]; } }
         */
        return null;
    }

    public List<SwitchPortStatTuple> getSwitchPortStatBase() {
        return switchPortStatBase;
    }

    public List<SwitchPortStatTuple> getSwitchPortStatUpdate() {
        return switchPortStatUpdate;
    }

    public List<SwitchPortStatTuple> getSwitchPortStatResult() {
        return switchPortStatResult;
    }

    public void getStatResultFile() {

        // Output the result to the file "switchPortDiff.stat"
        try {

            PrintWriter switchPortDiffWriter = new PrintWriter(
                    "switchPortDiff.stat", "UTF-8");
            for (i = 0; i < switchPortStatResult.size(); i++) {

                switchPortDiffWriter.print(switchPortStatResult.get(i)
                        .getSwitchDPID() + " ");
                switchPortDiffWriter.print(switchPortStatResult.get(i)
                        .getPortNumber() + " ");
                switchPortDiffWriter.print((new BigInteger(switchPortStatResult
                        .get(i).getReceiveBytes())).add(
                        new BigInteger(switchPortStatResult.get(i)
                                .getTransmitBytes())).toString());
                switchPortDiffWriter.print("\n");
            }
            switchPortDiffWriter.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printSwitchPortStatBase() {

        System.out.println("SwitchPortStatBase - ");
        for (i = 0; i < switchPortStatBase.size(); i++) {

            System.out.print(switchPortStatBase.get(i).getSwitchDPID() + " "
                    + switchPortStatBase.get(i).getReceivePackets() + " "
                    + switchPortStatBase.get(i).getTransmitPackets() + " "
                    + switchPortStatBase.get(i).getReceiveBytes() + " "
                    + switchPortStatBase.get(i).getTransmitBytes() + " "
                    + switchPortStatBase.get(i).getReceiveDropped() + " "
                    + switchPortStatBase.get(i).getTransmitDropped() + " "
                    + switchPortStatBase.get(i).getReceiveErrors() + " "
                    + switchPortStatBase.get(i).getTransmitErrors() + " "
                    + switchPortStatBase.get(i).getReceiveFrameErrors() + " "
                    + switchPortStatBase.get(i).getReceiveOverrunErrors() + " "
                    + switchPortStatBase.get(i).getReceiveCRCErrors() + " "
                    + switchPortStatBase.get(i).getCollisions() + " "
                    + switchPortStatBase.get(i).getPortNumber());

            System.out.println();
        }
        System.out.println();
    }

    public void printSwitchPortStatUpdate() {

        System.out.println("SwitchPortStatUpdate - ");
        for (i = 0; i < switchPortStatUpdate.size(); i++) {

            System.out.print(switchPortStatUpdate.get(i).getSwitchDPID() + " "
                    + switchPortStatUpdate.get(i).getReceivePackets() + " "
                    + switchPortStatUpdate.get(i).getTransmitPackets() + " "
                    + switchPortStatUpdate.get(i).getReceiveBytes() + " "
                    + switchPortStatUpdate.get(i).getTransmitBytes() + " "
                    + switchPortStatUpdate.get(i).getReceiveDropped() + " "
                    + switchPortStatUpdate.get(i).getTransmitDropped() + " "
                    + switchPortStatUpdate.get(i).getReceiveErrors() + " "
                    + switchPortStatUpdate.get(i).getTransmitErrors() + " "
                    + switchPortStatUpdate.get(i).getReceiveFrameErrors() + " "
                    + switchPortStatUpdate.get(i).getReceiveOverrunErrors()
                    + " " + switchPortStatUpdate.get(i).getReceiveCRCErrors()
                    + " " + switchPortStatUpdate.get(i).getCollisions() + " "
                    + switchPortStatUpdate.get(i).getPortNumber());

            System.out.println();
        }
        System.out.println();
    }

    public void printSwitchPortStatResult() {

        System.out.println("SwitchPortStatResult - ");
        for (i = 0; i < switchPortStatResult.size(); i++) {

            System.out.print(switchPortStatResult.get(i).getSwitchDPID() + " "
                    + switchPortStatResult.get(i).getReceivePackets() + " "
                    + switchPortStatResult.get(i).getTransmitPackets() + " "
                    + switchPortStatResult.get(i).getReceiveBytes() + " "
                    + switchPortStatResult.get(i).getTransmitBytes() + " "
                    + switchPortStatResult.get(i).getReceiveDropped() + " "
                    + switchPortStatResult.get(i).getTransmitDropped() + " "
                    + switchPortStatResult.get(i).getReceiveErrors() + " "
                    + switchPortStatResult.get(i).getTransmitErrors() + " "
                    + switchPortStatResult.get(i).getReceiveFrameErrors() + " "
                    + switchPortStatResult.get(i).getReceiveOverrunErrors()
                    + " " + switchPortStatResult.get(i).getReceiveCRCErrors()
                    + " " + switchPortStatResult.get(i).getCollisions() + " "
                    + switchPortStatResult.get(i).getPortNumber());

            System.out.println();
        }
        System.out.println();

    }

}
