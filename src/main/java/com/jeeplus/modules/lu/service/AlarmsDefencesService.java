/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.dao.AlarmsDefencesDao;
import com.jeeplus.modules.lu.entity.AlarmStateName;
import com.jeeplus.modules.lu.entity.AlarmTypeName;
import com.jeeplus.modules.lu.entity.Alarms;
import com.jeeplus.modules.lu.entity.AlarmsDefences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报警记录表Service
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Service
@Transactional(readOnly = true)
public class AlarmsDefencesService extends CrudService<AlarmsDefencesDao, AlarmsDefences> {
	@Autowired
	AlarmsDefencesDao alarmsDefencesDao;

	public Page<AlarmsDefences> find(Page<AlarmsDefences> page, AlarmsDefences alarmsDefences){
		return super.findPage(page,alarmsDefences);
	}

	public Page<AlarmsDefences> find1(Page<AlarmsDefences> page, AlarmsDefences alarmsDefences){
		alarmsDefences.setPage(page);
		page.setList(alarmsDefencesDao.findAlarmsDefencesByTime(alarmsDefences));
		List<AlarmsDefences> list=page.getList();
		for(AlarmsDefences alarmsDefencesTemp : list){
			alarmsDefencesTemp.setTypeName(AlarmTypeName.getByType(Integer.parseInt(alarmsDefencesTemp.getTypeName())).getAlarmTypeName());
			alarmsDefencesTemp.setState(AlarmStateName.getByState(Integer.parseInt(alarmsDefencesTemp.getState())).getAlarmStateName());
		}
		return page;
	}
}