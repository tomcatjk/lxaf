/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.app.dao;

import com.jeeplus.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

/**
 * 报警记录表DAO接口
 * @author 陆华捷  
 * @version 2017-04-27
 */
@MyBatisDao
public interface AppDao {

   List selectAlarmrecord(Map map);

   List getDefences(Map map);

   int updateDefence(Map map);

   List getMasters(Map map);

   List getDevices(Map map);
}