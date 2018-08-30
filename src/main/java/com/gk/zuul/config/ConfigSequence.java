package com.gk.zuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConfigSequence {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${zuulversion}")
    private String version;

    private static volatile Integer sequence;

    public synchronized Integer getSequence() {
        if(sequence==null){
           sequence= jdbcTemplate.queryForObject("SELECT nextval('"+version+"') ",Integer.class);
        }
        return sequence;
    }
}
