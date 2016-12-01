package com.kahramani.crawler.telnet.service;

import com.kahramani.crawler.telnet.OltTelnetTaskRunnable;
import com.kahramani.crawler.telnet.config.ThreadExecutionManager;
import com.kahramani.crawler.telnet.enums.PropertyPrefix;
import com.kahramani.crawler.telnet.model.Olt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kahramani on 11/29/2016.
 */
@Service("oltTelnetService")
public class OltTelnetService extends TelnetServiceAbstract {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TelnetServiceTaskGenerator taskGenerator;
    private ThreadExecutionManager threadExecutionManager;

    public OltTelnetService() {
        super(OltTelnetService.class);
    }

    @Override
    protected void runService() {
        // get switch list to crawl over
        List<Olt> oltList = repositoryService.getOltList();

        // create runnables from the list for thread execution
        List<OltTelnetTaskRunnable> oltTelnetTaskRunnables =
                taskGenerator.generate(PropertyPrefix.OLT_PREFIX, oltList, OltTelnetTaskRunnable.class);

        // start executing threads
        if(this.setThreadExecutionManager())
            this.threadExecutionManager.submitRunnables(oltTelnetTaskRunnables);
    }

    @Override
    protected boolean isRunning() {
        if(this.threadExecutionManager == null || this.threadExecutionManager.getExecutor() == null)
            return false;
        if(this.threadExecutionManager.getExecutor().getActiveCount() == 0)
            return false;

        return true;
    }

    @Override
    protected boolean setThreadExecutionManager() {
        String threadNamePrefix = "OltTelnetThread_";
        int timeOut = this.propertyHelper.getInt(PropertyPrefix.OLT_PREFIX.getPrefix() +
                ".crawler.timeout", Integer.MAX_VALUE);
        this.threadExecutionManager =
                applicationContext.getBean(ThreadExecutionManager.class, timeOut, threadNamePrefix);

        return true;
    }
}
