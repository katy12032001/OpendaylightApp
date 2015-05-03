package org.opendaylight.controller.monitor;

import java.io.Serializable;

public class SwitchPortStatTuple implements Serializable {

    private String switchDPID;
    private String portNumber;
    private String receivePackets;
    private String transmitPackets;
    private String receiveBytes;
    private String transmitBytes;
    private String receiveDropped;
    private String transmitDropped;
    private String receiveErrors;
    private String transmitErrors;
    private String receiveFrameErrors;
    private String receiveOverrunErrors;
    private String receiveCRCErrors;
    private String collisions;

    public SwitchPortStatTuple(String switchDPID, String portNumber,
            String receivePackets, String transmitPackets, String receiveBytes,
            String transmitBytes, String receiveDropped,
            String transmitDropped, String receiveErrors,
            String transmitErrors, String receiveFrameErrors,
            String receiveOverrunErrors, String receiveCRCErrors,
            String collisions) {

        this.switchDPID = switchDPID;
        this.portNumber = portNumber;
        this.receivePackets = receivePackets;
        this.transmitPackets = transmitPackets;
        this.receiveBytes = receiveBytes;
        this.transmitBytes = transmitBytes;
        this.receiveDropped = receiveDropped;
        this.transmitDropped = transmitDropped;
        this.receiveErrors = receiveErrors;
        this.transmitErrors = transmitErrors;
        this.receiveFrameErrors = receiveFrameErrors;
        this.receiveOverrunErrors = receiveOverrunErrors;
        this.receiveCRCErrors = receiveCRCErrors;
        this.collisions = collisions;
    }

    public void setSwitchDPID(String switchDPID) {
        this.switchDPID = switchDPID;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public void setReceivePackets(String receivePackets) {
        this.receivePackets = receivePackets;
    }

    public void setTransmitPackets(String transmitPackets) {
        this.transmitPackets = transmitPackets;
    }

    public void setReceiveBytes(String receiveBytes) {
        this.receiveBytes = receiveBytes;
    }

    public void setTransmitBytes(String transmitBytes) {
        this.transmitBytes = transmitBytes;
    }

    public void setReceiveDropped(String receiveDropped) {
        this.receiveDropped = receiveDropped;
    }

    public void setTransmitDropped(String transmitDropped) {
        this.transmitDropped = transmitDropped;
    }

    public void setReceiveErrors(String receiveErrors) {
        this.receiveErrors = receiveErrors;
    }

    public void setTransmitErrors(String transmitErrors) {
        this.transmitErrors = transmitErrors;
    }

    public void setReceiveFrameErrors(String receiveFrameErrors) {
        this.receiveFrameErrors = receiveFrameErrors;
    }

    public void setReceiveOverrunErrors(String receiveOverrunErrors) {
        this.receiveOverrunErrors = receiveOverrunErrors;
    }

    public void setReceiveCRCErrors(String receiveCRCErrors) {
        this.receiveCRCErrors = receiveCRCErrors;
    }

    public void setCollisions(String collisions) {
        this.collisions = collisions;
    }

    public String getSwitchDPID() {
        return switchDPID;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public String getReceivePackets() {
        return receivePackets;
    }

    public String getTransmitPackets() {
        return transmitPackets;
    }

    public String getReceiveBytes() {
        return receiveBytes;
    }

    public String getTransmitBytes() {
        return transmitBytes;
    }

    public String getReceiveDropped() {
        return receiveDropped;
    }

    public String getTransmitDropped() {
        return transmitDropped;
    }

    public String getReceiveErrors() {
        return receiveErrors;
    }

    public String getTransmitErrors() {
        return transmitErrors;
    }

    public String getReceiveFrameErrors() {
        return receiveFrameErrors;
    }

    public String getReceiveOverrunErrors() {
        return receiveOverrunErrors;
    }

    public String getReceiveCRCErrors() {
        return receiveCRCErrors;
    }

    public String getCollisions() {
        return collisions;
    }

}