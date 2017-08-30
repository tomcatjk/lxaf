/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao2;
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
@MyBatisDao2
public interface AlarmsDao2 extends CrudDao<Alarms> {

}