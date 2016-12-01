package com.kahramani.crawler.telnet;

import com.kahramani.crawler.telnet.model.NetworkElement;

import java.util.List;

/**
 * Created by kahramani on 11/29/2016.
 */
public interface TelnetCrawler <T extends NetworkElement> {
    boolean establishTelnetConnection(T ne, int targetPort);
    boolean terminateConnection();
    List<?> crawlOver(T ne);
    List<?> crawlAllOver(List<T> tList);
    StringBuilder address(T t);
}
