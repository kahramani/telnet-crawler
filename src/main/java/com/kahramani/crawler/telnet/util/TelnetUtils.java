package com.kahramani.crawler.telnet.util;

import com.kahramani.crawler.telnet.enums.DeviceModel;
import com.kahramani.crawler.telnet.enums.PropertyPrefix;
import org.springframework.util.Assert;

import java.util.EnumSet;
import java.util.Iterator;
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

    /**
     * to get property prefix for the given device model
     * @param d device model whose property prefix wanted to be obtained
     * @return a PropertyPrefix
     */
    public static PropertyPrefix getPropertyPrefixByDeviceModel(DeviceModel d) {
        Assert.notNull(d, "'deviceModel' cannot be null");
        EnumSet<PropertyPrefix> ppSet = EnumSet.allOf(PropertyPrefix.class);
        Iterator<PropertyPrefix> it = ppSet.iterator();
        while(it.hasNext()) {
            PropertyPrefix p = it.next();
            if(d == p.getDeviceModel()) {
                return p;
            }
        }
        throw new IllegalArgumentException("'deviceModel' could not be found in property prefixes");
    }
}
