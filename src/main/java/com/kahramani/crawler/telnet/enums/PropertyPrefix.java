package com.kahramani.crawler.snmp.enums;

/**
 * Created by kahramani on 11/22/2016.
 */
public enum PropertyPrefix {

    /**
     * prefix
     */
    SW_PREFIX("telnet.sw"),
    OLT_PREFIX("telnet.olt"),
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
