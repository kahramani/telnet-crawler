package com.kahramani.crawler.telnet.service;

import com.kahramani.crawler.telnet.SwitchTelnetTaskRunnable;
import com.kahramani.crawler.telnet.config.ThreadExecutionManager;
import com.kahramani.crawler.telnet.enums.PropertyPrefix;
import com.kahramani.crawler.telnet.model.Switch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kahramani on 11/29/2016.
 */
@Service("switchTelnetService")
public class SwitchTelnetService extends TelnetServiceAbstract {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TelnetServiceTaskGenerator taskGenerator;
    private ThreadExecutionManager threadExecutionManager;

    public SwitchTelnetService() {
        super(SwitchTelnetService.class);
    }

    @Override
    protected void runService() {
        // get switch list to crawl over
        List<Switch> switchList = repositoryService.getSwitchList();

        // create runnables from the list for thread execution
        List<SwitchTelnetTaskRunnable> switchTelnetTaskRunnables =
                taskGenerator.generate(PropertyPrefix.SW_PREFIX, switchList, SwitchTelnetTaskRunnable.class);

        // start executing threads
        if(this.setThreadExecutionManager())
            this.threadExecutionManager.submitRunnables(switchTelnetTaskRunnables);
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
        String threadNamePrefix = "SwitchTelnetThread_";
        int timeOut = this.propertyHelper.getInt(PropertyPrefix.SW_PREFIX.getPrefix() +
                ".crawler.timeout", Integer.MAX_VALUE);
        this.threadExecutionManager =
                applicationContext.getBean(ThreadExecutionManager.class, timeOut, threadNamePrefix);

        return true;
    }
}
