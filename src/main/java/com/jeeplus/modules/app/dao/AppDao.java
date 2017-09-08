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

   public List selectAlarmrecord(Map map);

   public List getdefence(String cid);

   public List getDefences(Map map);

   public int updateDefence(Map map);
}