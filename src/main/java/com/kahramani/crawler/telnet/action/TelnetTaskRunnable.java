package com.kahramani.crawler.telnet.action;

import com.kahramani.crawler.telnet.model.NetworkElement;

import java.util.List;

/**
 * Created by kahramani on 11/29/2016.
 */
public interface TelnetTaskRunnable extends Runnable {
    <T extends NetworkElement> void setList(List<T> neList);
}
