package com.kahramani.crawler.telnet.service;

import com.kahramani.crawler.telnet.config.PropertyHelper;
import com.kahramani.crawler.telnet.util.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kahramani on 11/29/2016.
 */
abstract class TelnetServiceAbstract {

    private static Logger logger;

    @Autowired
    protected PropertyHelper propertyHelper;

    public TelnetServiceAbstract(Class c) {
        this.logger =  LoggerFactory.getLogger(c);
    }

    public void start() {
        logger.info("Service started.");
        Chronometer cr = new Chronometer();
        cr.start();
        runService();
        cr.stop();
        logger.info("Service ended. Run time : " + cr.getDuration() + ".");
        cr.clear();
    }

    protected abstract void runService();

    protected abstract boolean isRunning();

    protected abstract boolean setThreadExecutionManager();
}
