package com.piramide.elwis.dto.contactmanager;

import java.io.Serializable;

/**
 * Telecom DTO fro address telecomunication number data transport
 *
 * @author Ernesto
 * @version $Id: TelecomDTO.java 10239 2012-06-15 18:08:26Z miguel $
 */
public class TelecomDTO implements Serializable {
    private String telecomId;
    private String description;
    private String data;
    private String version;
    private boolean predetermined;
    private Integer addressId;
    private Integer contactPersonId;


    /**
     * This constructor is used when a telecom is added from interface without saving, only managed in the interface
     *
     * @param data        the data
     * @param description the description
     */
    public TelecomDTO(String data, String description, boolean predetermined) {
        this.description = description;
        this.data = data;
        this.telecomId = "";
        this.predetermined = predetermined;

    }

    public TelecomDTO(String telecomId, String data, String description, boolean predetermined) {
        this.description = description;
        this.data = data;
        this.telecomId = telecomId;
        this.predetermined = predetermined;
    }


    public TelecomDTO() {
        this.predetermined = false;
    }

    public String getTelecomId() {
        return telecomId;
    }

    public void setTelecomId(String telecomId) {
        this.telecomId = telecomId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean getPredetermined() {
        return predetermined;
    }

    public void setPredetermined(boolean predetermined) {
        this.predetermined = predetermined;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(Integer contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public String toString() {
        return "telecomId=" + telecomId + ", description=" + description + ", data=" + data + ", predetermined=" + predetermined;
    }
}
