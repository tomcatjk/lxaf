/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.app.service;

import com.jeeplus.modules.app.dao.AppDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 报警记录表Service
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Service
@Transactional(readOnly = true)
public class AppService {
	@Autowired
    AppDao appDao;

	public List selectAlarmrecord(Map map){
		return appDao.selectAlarmrecord(map);
	}

	public List getdefence(String cid){
		return appDao.getdefence(cid);
	}

	public List getDefences(Map map){
		return appDao.getDefences(map);
	}

	@Transactional(readOnly = false)
	public int updateDefence(Map map){return appDao.updateDefence(map);}
}