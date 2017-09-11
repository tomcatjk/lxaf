/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.lu.entity.AlarmsDefences;

import java.util.List;

/**
 * 报警记录表DAO接口
 * @author 陆华捷  
 * @version 2017-04-27
 */
@MyBatisDao
public interface AlarmsDefencesDao extends CrudDao<AlarmsDefences> {
    List<AlarmsDefences> findAlarmsDefencesByTime(AlarmsDefences alarmsDefences);

}