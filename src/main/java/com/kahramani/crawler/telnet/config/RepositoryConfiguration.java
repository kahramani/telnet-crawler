package com.kahramani.crawler.telnet.config;

import com.kahramani.crawler.telnet.enums.PropertyPrefix;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Created by kahramani on 11/22/2016.
 */
@Configuration
public class RepositoryConfiguration {

    private static final int DEFAULT_INITIAL_CONNECTION_SIZE = 2;
    private static final int DEFAULT_MAX_ACTIVE_CONNECTION_SIZE = 10;
    private static final int DEFAULT_MAX_IDLE_CONNECTION_SIZE = 1;

    @Autowired
    private PropertyHelper propertyHelper;

    @Primary
    @Bean("applicationJdbcTemplate")
    public JdbcTemplate applicationJdbcTemplate() {
        String prefix = PropertyPrefix.APPLICATION_DB_PREFIX.getPrefix();
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(this.propertyHelper.getString(prefix + ".url"));
        ds.setDriverClassName(this.propertyHelper.getString(prefix + ".driverClass"));
        ds.setUsername(this.propertyHelper.getString(prefix + ".username"));
        ds.setPassword(this.propertyHelper.getString(prefix + ".password"));
        ds.setInitialSize(this.propertyHelper.getInt(prefix + ".initial.connection.size",
                DEFAULT_INITIAL_CONNECTION_SIZE));
        ds.setMaxTotal(this.propertyHelper.getInt(prefix + ".max.active.connection.size",
                DEFAULT_MAX_ACTIVE_CONNECTION_SIZE));
        ds.setMaxIdle(this.propertyHelper.getInt(prefix + ".max.idle.connection.size",
                DEFAULT_MAX_IDLE_CONNECTION_SIZE));

        return new JdbcTemplate(ds);
    }

    @Bean("switchSourceJdbcTemplate")
    public JdbcTemplate switchSourceJdbcTemplate() {
        String prefix = PropertyPrefix.SW_SOURCE_DB_PREFIX.getPrefix();
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(this.propertyHelper.getString(prefix + ".url"));
        ds.setDriverClassName(this.propertyHelper.getString(prefix + ".driverClass"));
        ds.setUsername(this.propertyHelper.getString(prefix + ".username"));
        ds.setPassword(this.propertyHelper.getString(prefix + ".password"));

        return new JdbcTemplate(ds);
    }

    @Bean("oltSourceJdbcTemplate")
    public JdbcTemplate oltSourceJdbcTemplate() {
        String prefix = PropertyPrefix.OLT_SOURCE_DB_PREFIX.getPrefix();
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(this.propertyHelper.getString(prefix + ".url"));
        ds.setDriverClassName(this.propertyHelper.getString(prefix + ".driverClass"));
        ds.setUsername(this.propertyHelper.getString(prefix + ".username"));
        ds.setPassword(this.propertyHelper.getString(prefix + ".password"));

        return new JdbcTemplate(ds);
    }

    @Bean("switchSelectQuery")
    public StringBuilder switchSelectQuery() {
        String prefix = PropertyPrefix.SW_SOURCE_DB_PREFIX.getPrefix();
        return this.propertyHelper.getSqlQueryFromFile(prefix + ".select.query.file", true, "UTF-8");
    }

    @Bean("oltSelectQuery")
    public StringBuilder oltSelectQuery() {
        String prefix = PropertyPrefix.OLT_SOURCE_DB_PREFIX.getPrefix();
        return this.propertyHelper.getSqlQueryFromFile(prefix + ".select.query.file", true, "UTF-8");
    }

    @Bean("switchInsertQuery")
    public StringBuilder switchInsertQuery() {
        String prefix = PropertyPrefix.APPLICATION_DB_PREFIX.getPrefix();
        prefix += ".sw";
        return this.propertyHelper.getSqlQueryFromFile(prefix + ".insert.query.file", true, "UTF-8");
    }

    @Bean("oltInsertQuery")
    public StringBuilder oltInsertQuery() {
        String prefix = PropertyPrefix.APPLICATION_DB_PREFIX.getPrefix();
        prefix += ".olt";
        return this.propertyHelper.getSqlQueryFromFile(prefix + ".insert.query.file", true, "UTF-8");
    }
}
