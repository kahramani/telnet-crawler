package com.kahramani.crawler.telnet.action;

import com.kahramani.crawler.telnet.config.PropertyHelper;
import com.kahramani.crawler.telnet.enums.PropertyPrefix;
import com.kahramani.crawler.telnet.model.NetworkElement;
import com.kahramani.crawler.telnet.model.Switch;
import com.kahramani.crawler.telnet.service.RepositoryService;
import com.kahramani.crawler.telnet.util.Chronometer;
import com.kahramani.crawler.telnet.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by kahramani on 11/29/2016.
 */
public class SwitchTelnetTaskRunnable implements TelnetTaskRunnable {

    private static final Logger logger = LoggerFactory.getLogger(SwitchTelnetTaskRunnable.class);
    private static final int DEFAULT_SWITCH_MAX_COUNT_TO_INSERT = 20;

    @Autowired
    private PropertyHelper propertyHelper;
    @Autowired
    private RepositoryService repositoryService;

    private List<Switch> switchList;

    @Override
    public <T extends NetworkElement> void setList(List<T> neList) {
        this.switchList = (List<Switch>) neList;
    }

    @Override
    public void run() {
        Assert.notEmpty(this.switchList, "'switchList' cannot be null or empty");

        String maxSwitchCountToInsertKey = PropertyPrefix.SW_PREFIX + ".max.insert.count.to.db";
        int maxSwitchCountToInsert = propertyHelper
                .getInt(maxSwitchCountToInsertKey, DEFAULT_SWITCH_MAX_COUNT_TO_INSERT);

        Assert.isTrue(maxSwitchCountToInsert > 0, "'" + maxSwitchCountToInsert + "' cannot be 0 or lower");

        Chronometer cr = new Chronometer();
        cr.start();

        logger.info("Thread is started. List size: " + this.switchList.size());

        List<List<?>> splitList = ListUtils.splitListByPartitionSize(this.switchList, maxSwitchCountToInsert);

        Assert.notEmpty(splitList, "'splitList' could not be created");

        for(List<?> partition : splitList) {
            // TODO crawler stuff
        }

        cr.stop();

        logger.info("Thread is completed. Run Time: " + cr.getDuration());
        cr.clear();

    }
}
