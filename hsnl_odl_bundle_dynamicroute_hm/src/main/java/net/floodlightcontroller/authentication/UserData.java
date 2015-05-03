package net.floodlightcontroller.authentication;

import java.io.Serializable;

public class UserData implements Serializable {

    private String ipAddress;
    private String macAddress;
    private String accessRight;

    public UserData(String ipAddress, String macAddress, String accessRight) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.accessRight = accessRight;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getAccessRight() {
        return accessRight;
    }

}
