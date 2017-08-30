package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.lu.entity.Alarms;
import com.jeeplus.modules.lu.entity.AlarmsCount;
import com.jeeplus.modules.lu.entity.AlarmsDefences;

import java.util.List;
import java.util.Map;

/**
 * 报警记录表DAO接口
 * @author 陆华捷  
 * @version 2017-04-27
 */
@MyBatisDao
public interface AlarmsDao extends CrudDao<Alarms> {

    /**
     * 报警信息统计
     * @param
     * @return
     */
    List<AlarmsCount> findAlarmsAll( );

    /**
     * 查询报警信息详单
     */
    List<AlarmsDefences> findAlarmsDefencesAll();

    List getAlarmsInfoAcd(Map map);

    void updateByAid(Alarms alarms);

    List getAlarmsCount(AlarmsCount alarmsCount);
}