package com.kahramani.crawler.telnet.repository;

import com.kahramani.crawler.telnet.model.Switch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by kahramani on 11/22/2016.
 */
@Repository
public class SwitchSourceRepository implements SourceRepository {

    @Autowired
    @Qualifier("switchSourceJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * to get switch list from the source repository
     * @param sqlQuery sql query wanted to run
     * @return a List of Switch
     */
    @Override
    public List<Switch> getList(String sqlQuery) {
        return jdbcTemplate.query(sqlQuery, new RowMapper<Switch>() {
            @Override
            public Switch mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Switch(resultSet.getString("SWITCH_IP"), resultSet.getString("SWITCH_NAME"));
            }
        });
    }
}
