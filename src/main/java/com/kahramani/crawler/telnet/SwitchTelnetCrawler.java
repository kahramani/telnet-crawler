package com.kahramani.crawler.telnet;

import com.kahramani.crawler.telnet.exception.TelnetResponseTimeOutException;
import com.kahramani.crawler.telnet.model.NetworkElement;
import com.kahramani.crawler.telnet.model.Switch;
import com.kahramani.crawler.telnet.model.SwitchPortData;
import com.kahramani.crawler.telnet.util.TelnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kahramani on 11/29/2016.
 */
@Component
@Scope("prototype")
class SwitchTelnetCrawler extends TelnetDriver implements TelnetCrawler {

    private static Logger logger = LoggerFactory.getLogger(SwitchTelnetCrawler.class);

    protected SwitchTelnetCrawler() {
        super();
    }

    /**
     * to establish a telnet connection to a device
     * @param ne switch which wanted to be connected
     * @param targetPort switch port which wanted to be connected
     * @return a boolean value which is the flag of operation is success or not
     */
    @Override
    public boolean establishTelnetConnection(NetworkElement ne, int targetPort) {
        try {
            return this.connect(ne.getIpAddress(), targetPort);
        } catch (IOException e) {
            logger.error("Failed to establish telnet connection to " + this.address(ne));
        }
        return false;
    }

    @Override
    public boolean terminateConnection() {
        try {
            this.disconnect();
        } catch (IOException e) {
            logger.error("Failed to terminate telnet connection");
            return false;
        }
        return true;
    }

    /**
     * to obtain data from the given switch via telnet
     * @param ne switch wanted to obtain data
     * @return a List of SwitchPortData which holds data obtained from the given switch
     */
    @Override
    public List<SwitchPortData> crawlOver(NetworkElement ne) {
        Assert.notNull(ne, "'ne' cannot be null");
        Assert.hasText(ne.getIpAddress(), "'ipAddress' cannot be null or empty");
        Assert.notNull(ne.getDeviceModel(), "'deviceModel' cannot be null");

        Switch sw = (Switch) ne;
        logger.info("Process started for address: " + this.address(sw));

        List<SwitchPortData> portDataList;

        try {
            this.setConfiguration(TelnetUtils.getPropertyPrefixByDeviceModel(sw.getDeviceModel()));
            if (!this.establishTelnetConnection(sw, DEFAULT_TELNET_PORT))
                logger.error("Could not be connected or logged in to " + this.address(sw));
            else
                sw.setIsReachable(true);

            portDataList = new ArrayList<>();
            SwitchPortData portData = new SwitchPortData(sw);
            if (sw.isReachable()) {

                // TODO run telnet commands
                try {
                    StringBuilder response = this.runCommand("");
                } catch (IOException e) {
                    logger.error("Failed to run command", e);
                } catch (TelnetResponseTimeOutException e) {
                    logger.error("Failed to get response", e);
                }
                portData.setVlan("");
                portData.setPort("");
                portData.setMacAddress("");
                // run telnet commands
            }
            portDataList.add(portData);
        } finally {
            this.terminateConnection();
        }
        logger.info("Process ended for address: " + this.address(sw));
        return portDataList;
    }

    /**
     * to obtain data from the given switch list via telnet
     * @param neList switch list wanted to obtain data
     * @return a List of SwitchPortData which holds data obtained from the given switch list
     */
    @Override
    public List<SwitchPortData> crawlAllOver(List neList) {
        Assert.notEmpty(neList, "'neList' cannot be null or empty to run this operation");
        logger.info("Process started for list size with " + neList.size());

        List<SwitchPortData> cumulativePortDataList = new ArrayList<>();

        for(Object ne : neList) {
            if(ne != null) {
                List<SwitchPortData> portDataList = crawlOver((NetworkElement) ne);

                if(!CollectionUtils.isEmpty(portDataList))
                    cumulativePortDataList.addAll(portDataList);
            }
        }

        logger.info("Process ended for list size with " + neList.size());
        return cumulativePortDataList;
    }

    /**
     * to get switch address which is currently operated -- Format IP:HostName
     * @param ne switch which is currently operated
     * @return a StringBuilder which is the switch address
     */
    @Override
    public StringBuilder address(NetworkElement ne) {
        char separator = '/';
        return new StringBuilder(ne.getIpAddress()).append(separator).append(ne.getHostName());
    }
}
