package com.kahramani.crawler.telnet.util;

import com.kahramani.crawler.telnet.enums.DeviceModel;
import org.springframework.util.Assert;

import java.util.Locale;

/**
 * Created by kahramani on 11/29/2016.
 */
public class TelnetUtils {

    /**
     * to detect model of the device
     * @param hostName hostName of device
     * @return a DeviceModel enum
     */
    // TODO update here for your purposes
    public static DeviceModel getDeviceModelByHostName(String hostName) {
        Assert.hasText(hostName, "'hostName' cannot be null or empty");

        if(hostName.toUpperCase(Locale.US).startsWith(DeviceModel.SW_HUAWEI.getPrefix()))
            return DeviceModel.SW_HUAWEI;
        else if(hostName.toUpperCase(Locale.US).startsWith(DeviceModel.SW_CISCO.getPrefix()))
            return DeviceModel.SW_CISCO;
        else if(hostName.toUpperCase(Locale.US).startsWith(DeviceModel.OLT_HUAWEI.getPrefix()))
            return DeviceModel.OLT_HUAWEI;
        else if(hostName.toUpperCase(Locale.US).startsWith(DeviceModel.OLT_NOKIA.getPrefix()))
            return DeviceModel.OLT_NOKIA;

        return DeviceModel.UNIDENTIFIED;
    }
}
