package com.kahramani.crawler.telnet.enums;

/**
 * Created by kahramani on 11/22/2016.
 */
public enum PropertyPrefix {

    /**
     * prefix
     */
    SW_PREFIX("telnet.sw"),
    SW_HUAWEI_PREFIX("telnet.sw.huawei"),
    SW_CISCO_PREFIX("telnet.sw.cisco"),
    OLT_PREFIX("telnet.olt"),
    OLT_HUAWEI_PREFIX("telnet.olt.huawei"),
    OLT_NOKIA_PREFIX("telnet.olt.nokia"),
    SW_SOURCE_DB_PREFIX("db.sw.source"),
    OLT_SOURCE_DB_PREFIX("db.olt.source"),
    APPLICATION_DB_PREFIX("db.application");

    private String prefix;

    PropertyPrefix(String prefix) { this.prefix = prefix; }

    public String get() {return this.prefix; }

    public String toString() {
        return this.prefix;
    }
}
