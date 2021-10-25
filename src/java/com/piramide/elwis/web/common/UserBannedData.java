package com.piramide.elwis.web.common;

import java.io.Serializable;

/**
 * @author Mauren Carrasco
 * @version 4.2.2
 */
public class UserBannedData implements Serializable {

    private String type;
    private String bannedByIP;

    public UserBannedData(String type, String bannedByIP) {
        this.type = type;
        this.bannedByIP = bannedByIP;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBannedByIP() {
        return bannedByIP;
    }

    public void setBannedByIP(String bannedByIP) {
        this.bannedByIP = bannedByIP;
    }

}
