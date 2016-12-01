package com.kahramani.crawler.telnet.service;

import com.kahramani.crawler.telnet.model.Olt;
import com.kahramani.crawler.telnet.model.OltOntData;
import com.kahramani.crawler.telnet.model.Switch;
import com.kahramani.crawler.telnet.model.SwitchPortData;
import com.kahramani.crawler.telnet.repository.ApplicationRepository;
import com.kahramani.crawler.telnet.repository.OltSourceRepository;
import com.kahramani.crawler.telnet.repository.SwitchSourceRepository;
import com.kahramani.crawler.telnet.util.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by kahramani on 11/29/2016.
 */
@Service("repositoryService")
public class RepositoryService {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryService.class);

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private SwitchSourceRepository switchSourceRepository;
    @Autowired
    private OltSourceRepository oltSourceRepository;
    @Autowired
    @Qualifier("switchSelectQuery")
    private StringBuilder switchSelectQuery;
    @Autowired
    @Qualifier("oltSelectQuery")
    private StringBuilder oltSelectQuery;
    @Autowired
    @Qualifier("switchInsertQuery")
    private StringBuilder switchInsertQuery;
    @Autowired
    @Qualifier("oltInsertQuery")
    private StringBuilder oltInsertQuery;

    /**
     * to get switch list from the source repository
     * @return a List of Switch
     */
    public List<Switch> getSwitchList() {
        Assert.notNull(switchSelectQuery, "'telnet.sw.db.select.sql' must be added to resources");
        Assert.hasText(switchSelectQuery.toString(), "'telnet.sw.db.select.sql' must not be empty");

        Chronometer cr = new Chronometer();
        cr.start();
        List<Switch> switchList = this.switchSourceRepository.getList(switchSelectQuery.toString());
        cr.stop();
        logger.info("Switch list retrieved successfully. Duration: " + cr.getDuration() + ".");

        if(switchList == null)
            return null;

        logger.info("Switch list size: " + switchList.size() + ".");
        cr.clear();

        return switchList;
    }

    /**
     * to get olt list from the source repository
     * @return a List of Olt
     */
    public List<Olt> getOltList() {
        Assert.notNull(oltSelectQuery, "'telnet.olt.db.select.sql' must be added to resources");
        Assert.hasText(oltSelectQuery.toString(), "'telnet.olt.db.select.sql' must not be empty");

        Chronometer cr = new Chronometer();
        cr.start();
        List<Olt> oltList = this.oltSourceRepository.getList(oltSelectQuery.toString());
        cr.stop();
        logger.info("Olt list retrieved successfully. Duration: " + cr.getDuration() + ".");

        if(oltList == null)
            return null;

        logger.info("Olt list size: " + oltList.size() + ".");
        cr.clear();

        return oltList;
    }

    /**
     * to insert switch port data list to application db via batching
     * @param portDataList list to insert
     * @return an int array with length of inserted count
     */
    public int[] insertSwitchPortDataList(final List<SwitchPortData> portDataList) {
        Assert.notNull(switchInsertQuery, "'telnet.sw.db.insert.sql' must be added to resources");
        Assert.hasText(switchInsertQuery.toString(), "'telnet.sw.db.insert.sql' must not be empty");

        Chronometer cr = new Chronometer();
        cr.start();
        int[] insertionCount = this.applicationRepository
                .insertSwitchPortDataList(switchInsertQuery.toString(), portDataList);
        cr.stop();
        logger.info("Switch port data list inserted successfully. Duration: " + cr.getDuration() + ".");

        if(insertionCount == null)
            return null;

        logger.info("Switch port data insertion count: " + insertionCount.length + ".");
        cr.clear();

        return insertionCount;
    }

    /**
     * to insert olt ont data list to application db via batching
     * @param ontDataList list to insert
     * @return an int array with length of inserted count
     */
    public int[] insertOltOntDataList(final List<OltOntData> ontDataList) {
        Assert.notNull(oltInsertQuery, "'telnet.olt.db.insert.sql' must be added to resources");
        Assert.hasText(oltInsertQuery.toString(), "'telnet.olt.db.insert.sql' must not be empty");

        Chronometer cr = new Chronometer();
        cr.start();
        int[] insertionCount = this.applicationRepository
                .insertOltOntDataList(oltInsertQuery.toString(), ontDataList);
        cr.stop();
        logger.info("Olt ont data list inserted successfully. Duration: " + cr.getDuration() + ".");

        if(insertionCount == null)
            return null;

        logger.info("Olt ont data insertion count: " + insertionCount.length + ".");
        cr.clear();

        return insertionCount;
    }
}
