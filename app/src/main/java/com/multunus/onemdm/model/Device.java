package com.multunus.onemdm.model;

/**
 * Created by yedhukrishnan on 15/10/15.
 */
public class Device {
    private String imeiNumber;
    private String model;
    private String uniqueId;

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
