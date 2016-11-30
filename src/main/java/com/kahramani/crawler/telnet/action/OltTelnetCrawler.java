package com.kahramani.crawler.telnet.action;

import com.kahramani.crawler.telnet.enums.PropertyPrefix;
import com.kahramani.crawler.telnet.exception.TelnetResponseTimeOutException;
import com.kahramani.crawler.telnet.model.NetworkElement;
import com.kahramani.crawler.telnet.model.Olt;
import com.kahramani.crawler.telnet.model.OltOntData;
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
class OltTelnetCrawler extends TelnetDriver implements TelnetCrawler {

    private static Logger logger = LoggerFactory.getLogger(OltTelnetCrawler.class);

    protected OltTelnetCrawler(PropertyPrefix propertyPrefix) {
        super(propertyPrefix);
    }

    /**
     * to establish a telnet connection to a device
     * @param ne olt which wanted to be connected
     * @return a boolean value which is the flag of operation is success or not
     */
    @Override
    public boolean establishTelnetConnection(NetworkElement ne) {
        try {
            return this.connect(ne.getIpAddress(), DEFAULT_TELNET_PORT);
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
     * to obtain data from the given olt via telnet
     * @param ne olt wanted to obtain data
     * @return a List of OltOntData which holds data obtained from the given olt
     */
    @Override
    public List<OltOntData> crawlOver(NetworkElement ne) {
        Assert.notNull(ne, "'ne' cannot be null");
        Assert.hasText(ne.getIpAddress(), "'ipAddress' cannot be null or empty");
        Assert.notNull(ne.getDeviceModel(), "'deviceModel' cannot be null");

        Olt olt = (Olt) ne;
        logger.info("Process started for address: " + this.address(olt));

        List<OltOntData> ontDataList;

        try {
            if (!this.establishTelnetConnection(olt))
                logger.error("Could not be connected or logged in to " + this.address(olt));
            else
                olt.setIsReachable(true);

            ontDataList = new ArrayList<>();
            OltOntData ontData = new OltOntData(olt);
            if (olt.isReachable()) {

                // TODO run telnet commands
                try {
                    StringBuilder response = this.runCommand("");
                } catch (IOException e) {
                    logger.error("Failed to run command", e);
                } catch (TelnetResponseTimeOutException e) {
                    logger.error("Failed to get response", e);
                }
                ontData.setSlot("");
                ontData.setPort("");
                ontData.setOntNo("");
                ontData.setSerialNumber("");
                // run telnet commands
            }
            ontDataList.add(ontData);
        } finally {
            this.terminateConnection();
        }
        logger.info("Process ended for address: " + this.address(olt));
        return ontDataList;
    }

    /**
     * to obtain data from the given olt list via telnet
     * @param neList olt list wanted to obtain data
     * @return a List of OltOntData which holds data obtained from the given olt list
     */
    @Override
    public List<OltOntData> crawlAllOver(List neList) {
        Assert.notEmpty(neList, "'neList' cannot be null or empty to run this operation");
        logger.info("Process started for list size with " + neList.size());

        List<OltOntData> cumulativePortDataList = new ArrayList<>();

        for(Object ne : neList) {
            if(ne != null) {
                List<OltOntData> ontDataList = crawlOver((NetworkElement) ne);

                if(!CollectionUtils.isEmpty(ontDataList))
                    cumulativePortDataList.addAll(ontDataList);
            }
        }

        logger.info("Process ended for list size with " + neList.size());
        return cumulativePortDataList;
    }

    /**
     * to get olt address which is currently operated -- Format IP:HostName
     * @param ne olt which is currently operated
     * @return a StringBuilder which is the olt address
     */
    @Override
    public StringBuilder address(NetworkElement ne) {
        char separator = '/';
        return new StringBuilder(ne.getIpAddress()).append(separator).append(ne.getHostName());
    }
}
