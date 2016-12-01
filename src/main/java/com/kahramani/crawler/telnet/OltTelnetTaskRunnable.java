package com.kahramani.crawler.telnet;

import com.kahramani.crawler.telnet.config.PropertyHelper;
import com.kahramani.crawler.telnet.enums.PropertyPrefix;
import com.kahramani.crawler.telnet.model.NetworkElement;
import com.kahramani.crawler.telnet.model.Olt;
import com.kahramani.crawler.telnet.model.OltOntData;
import com.kahramani.crawler.telnet.service.RepositoryService;
import com.kahramani.crawler.telnet.util.Chronometer;
import com.kahramani.crawler.telnet.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by kahramani on 11/29/2016.
 */
public class OltTelnetTaskRunnable implements TelnetTaskRunnable {

    private static final Logger logger = LoggerFactory.getLogger(OltTelnetTaskRunnable.class);
    private static final int DEFAULT_OLT_MAX_COUNT_TO_INSERT = 20;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private PropertyHelper propertyHelper;
    @Autowired
    private RepositoryService repositoryService;

    private List<Olt> oltList;

    @Override
    public <T extends NetworkElement> void setList(List<T> neList) {
        this.oltList = (List<Olt>) neList;
    }

    @Override
    public void run() {
        Assert.notEmpty(this.oltList, "'oltList' cannot be null or empty");

        String maxOltCountToInsertKey = PropertyPrefix.OLT_PREFIX + ".max.insert.count.to.db";
        int maxOltCountToInsert = propertyHelper
                .getInt(maxOltCountToInsertKey, DEFAULT_OLT_MAX_COUNT_TO_INSERT);

        Assert.isTrue(maxOltCountToInsert > 0, "'" + maxOltCountToInsertKey + "' cannot be 0 or lower");

        Chronometer cr = new Chronometer();
        cr.start();

        logger.info("Thread is started. List size: " + this.oltList.size());

        List<List<?>> splitList = ListUtils.splitListByPartitionSize(this.oltList, maxOltCountToInsert);

        Assert.notEmpty(splitList, "'splitList' could not be created");

        OltTelnetCrawler crawler = applicationContext.getBean(OltTelnetCrawler.class);
        for(List<?> partition : splitList) {
            List<OltOntData> ontDataList = crawler.crawlAllOver(partition);

            if(!CollectionUtils.isEmpty(ontDataList))
                repositoryService.insertOltOntDataList(ontDataList);
        }

        cr.stop();

        logger.info("Thread is completed. Run Time: " + cr.getDuration());
        cr.clear();
    }
}
