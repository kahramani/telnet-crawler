package com.kahramani.crawler.telnet.repository;

import com.kahramani.crawler.telnet.model.NetworkElement;
import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
public interface SourceRepository {
    <T extends NetworkElement> List<T> getList(String sqlQuery);
}
