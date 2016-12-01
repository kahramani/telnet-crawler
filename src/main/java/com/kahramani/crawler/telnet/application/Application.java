package com.kahramani.crawler.telnet.application;

import com.kahramani.crawler.telnet.service.OltTelnetService;
import com.kahramani.crawler.telnet.service.SwitchTelnetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by kahramani on 11/15/2016.
 */
@ComponentScan("com.kahramani.crawler.*")
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        // run switch telnet service
        SwitchTelnetService switchTelnetService = context.getBean(SwitchTelnetService.class);
        switchTelnetService.start();

        // run olt telnet service
        OltTelnetService oltTelnetService = context.getBean(OltTelnetService.class);
        oltTelnetService.start();

        // destroy all singleton beans
        ((AnnotationConfigApplicationContext) context).destroy();
    }
}
