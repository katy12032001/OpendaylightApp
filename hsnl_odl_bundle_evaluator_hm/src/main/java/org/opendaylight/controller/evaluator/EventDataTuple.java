package org.opendaylight.controller.evaluator;

import org.opendaylight.controller.monitor.SwitchPortStatTuple;

public class EventDataTuple extends SwitchPortStatTuple {

    private String eventName;
    private String tarSwitchDPID;
    private String tarPortNumber;

    public EventDataTuple(String switchDPID, String portNumber,
            String receivePackets, String transmitPackets, String receiveBytes,
            String transmitBytes, String receiveDropped,
            String transmitDropped, String receiveErrors,
            String transmitErrors, String receiveFrameErrors,
            String receiveOverrunErrors, String receiveCRCErrors,
            String collisions) {

        super(switchDPID, portNumber, receivePackets, transmitPackets,
                receiveBytes, transmitBytes, receiveDropped, transmitDropped,
                receiveErrors, transmitErrors, receiveFrameErrors,
                receiveOverrunErrors, receiveCRCErrors, collisions);
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setTarSwitchDPID(String tarSwitchDPID) {
        this.tarSwitchDPID = tarSwitchDPID;
    }

    public void setTarPortNumber(String tarPortNumber) {
        this.tarPortNumber = tarPortNumber;
    }

    public String getEventName() {
        return eventName;
    }

    public String getTarSwitchDPID() {
        return tarSwitchDPID;
    }

    public String getTarPortNumber() {
        return tarPortNumber;
    }

}