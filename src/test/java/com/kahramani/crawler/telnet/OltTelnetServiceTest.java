package com.kahramani.crawler.telnet;

import com.kahramani.crawler.telnet.application.Application;
import com.kahramani.crawler.telnet.config.PropertyHelper;
import com.kahramani.crawler.telnet.service.OltTelnetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by kahramani on 11/29/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class OltTelnetServiceTest {

    @Autowired
    private PropertyHelper propertyHelper;
    @Autowired
    private OltTelnetService oltTelnetService;

    @Test
    public void crawlAllOltsOver() {
        this.oltTelnetService.start();
    }
}
