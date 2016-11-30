package com.kahramani.crawler.telnet.enums;

/**
 * Created by kahramani on 11/22/2016.
 */
public enum DeviceModel {

    /**
     * device, vendor, host_name_prefix, propertyPrefix
     */
    UNIDENTIFIED    ("UNIDENTIFIED", "UNIDENTIFIED", "UNIDENTIFIED"),
    SW_HUAWEI       ("SWITCH", "HUAWEI", "SW_HUA"),
    SW_CISCO        ("SWITCH", "CISCO", "SW_CIS"),
    OLT_HUAWEI      ("OLT", "HUAWEI", "OLT_HUA"),
    OLT_NOKIA       ("OLT", "NOKIA", "OLT_NOK");

    private final String device;
    private final String vendor;
    private final String prefix;

    DeviceModel(String device, String vendor, String prefix) {
        this.device = device;
        this.vendor = vendor;
        this.prefix = prefix;
    }

    public String getDevice() {
        return device;
    }

    public String getVendor() {
        return vendor;
    }

    public String getPrefix() {
        return prefix;
    }

    public String toString() {
        return new StringBuilder()
                .append("device: ").append(this.device)
                .append(", vendor: ").append(this.vendor)
                .append(", prefix: ").append(this.prefix).toString();
    }
}
