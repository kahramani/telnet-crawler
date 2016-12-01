package com.kahramani.crawler.telnet.service;

import com.kahramani.crawler.telnet.TelnetTaskRunnable;
import com.kahramani.crawler.telnet.config.PropertyHelper;
import com.kahramani.crawler.telnet.enums.PropertyPrefix;
import com.kahramani.crawler.telnet.model.NetworkElement;
import com.kahramani.crawler.telnet.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kahramani on 11/29/2016.
 */
@Component
class TelnetServiceTaskGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TelnetServiceTaskGenerator.class);
    private static final int DEFAULT_MAX_ACTIVE_THREAD_COUNT = 3;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PropertyHelper propertyHelper;

    /**
     * to generate runnables for telnet tasks
     * @param propertyPrefix property file prefix for configuration
     * @param neList child object list of NetworkElement
     * @param taskClass which contain the method overrides run
     * @param <T> Type which is a child of TelnetTaskRunnable interface
     * @return a List of T
     */
    <T extends TelnetTaskRunnable> List<T> generate(PropertyPrefix propertyPrefix,
                                                  List<? extends NetworkElement> neList,
                                                  Class<T> taskClass) {
        Assert.notNull(propertyPrefix, "'propertyPrefix' cannot be null or empty");
        Assert.notEmpty(neList, "'neList' cannot be null or empty");

        String prefix = propertyPrefix.getPrefix();
        int maxActiveThreadCount = this.propertyHelper.getInt(prefix + ".thread.max.active.count",
                DEFAULT_MAX_ACTIVE_THREAD_COUNT);

        if(maxActiveThreadCount <= 0) {
            String m = prefix + ".thread.max.active.count cannot be lower than 0 to run this operation.";
            throw new IllegalArgumentException(m);
        }

        int partitionCount = maxActiveThreadCount;
        if(neList.size() < partitionCount) {
            partitionCount = neList.size();
        }
        logger.info("Thread Count: " + maxActiveThreadCount);

        // to split into lists for desired max active thread count
        List<List<?>> splitList = ListUtils.splitListByPartitionCount(neList, partitionCount);

        Assert.notEmpty(splitList, "'splitList' could not be created");

        logger.info("List with size of " + neList.size() + " splitted into " + partitionCount +
                " small partition for each thread.");

        List<T> taskList = new ArrayList<>();
        for (List<?> partition : splitList) {
            T runnable = applicationContext.getBean(taskClass);
            runnable.setList((List<NetworkElement>) partition);
            taskList.add(runnable);
        }

        return taskList;
    }
}
