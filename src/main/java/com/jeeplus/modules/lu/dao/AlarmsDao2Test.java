package com.jeeplus.modules.lu.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by 63276 on 2017/6/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/spring-context.xml")
public class AlarmsDao2Test {
    @Autowired
    AlarmsDao2 dao2;
    @Autowired
    AlarmsDao dao;

    @Test
    public void init() {
        assertNotNull(dao2);
        assertNotNull(dao);
    }

    @Test
    public void findAlarmsAll() throws Exception {
//        System.out.println(dao.findAlarmsAll().size());
//        System.out.println(dao2.findAlarmsAll().size());

    }

    @Test
    public void findAlarmsDefencesAll() throws Exception {

    }

}