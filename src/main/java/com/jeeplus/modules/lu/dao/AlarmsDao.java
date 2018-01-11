package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.lu.entity.*;

import java.util.List;
import java.util.Map;

/**
 * 报警记录表DAO接口
 * @author 陆华捷  
 * @version 2017-04-27
 */
@MyBatisDao
public interface AlarmsDao extends CrudDao<Alarms> {

    List getAlarmsInfoAcd(Map map);

    void updateByAid(Alarms alarms);

    List getAlarmsCount(AlarmsCount alarmsCount);

    void deleteByDefenceId(Devices devices);

    void deleteByCustomerId(Customers customers);

    void deleteByMasterId(Masters masters);

    AlarmsCount getDevidedAlarmsCount(AlarmsCount alarmsCount);
}