package com.kahramani.crawler.telnet.enums;

/**
 * Created by kahramani on 11/22/2016.
 */
public enum PropertyPrefix {

    /**
     * prefix, deviceModel
     */
    SW_PREFIX("telnet.sw", DeviceModel.UNIDENTIFIED),
    SW_HUAWEI_PREFIX("telnet.sw.huawei", DeviceModel.SW_HUAWEI),
    SW_CISCO_PREFIX("telnet.sw.cisco", DeviceModel.SW_CISCO),
    OLT_PREFIX("telnet.olt", DeviceModel.UNIDENTIFIED),
    OLT_HUAWEI_PREFIX("telnet.olt.huawei", DeviceModel.OLT_HUAWEI),
    OLT_NOKIA_PREFIX("telnet.olt.nokia", DeviceModel.OLT_NOKIA),
    SW_SOURCE_DB_PREFIX("db.sw.source", DeviceModel.UNIDENTIFIED),
    OLT_SOURCE_DB_PREFIX("db.olt.source", DeviceModel.UNIDENTIFIED),
    APPLICATION_DB_PREFIX("db.application", DeviceModel.UNIDENTIFIED);

    private String prefix;
    private DeviceModel deviceModel;

    PropertyPrefix(String prefix, DeviceModel deviceModel) {
        this.prefix = prefix;
        this.deviceModel = deviceModel;
    }

    public String getPrefix() {return this.prefix; }

    public DeviceModel getDeviceModel() {return this.deviceModel; }

    public String toString() {
        return new StringBuilder()
                .append("prefix: ").append(this.prefix)
                .append(", deviceModel: ").append(this.deviceModel).toString();
    }
}
