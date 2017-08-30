package com.jeeplus.modules.lu.service;

import java.util.*;

import com.jeeplus.modules.lu.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.dao.AlarmsDao;

/**
 * 报警记录表Service
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Service
@Transactional(readOnly = true)
public class AlarmsService extends CrudService<AlarmsDao, Alarms> {

	@Autowired
	AlarmsDao alarmsDao;

	public Alarms get(String id) {
		return super.get(id);
	}
	
	public List<Alarms> findList(Alarms alarms) {
		return super.findList(alarms);
	}
	
	public Page<Alarms> findPage(Page<Alarms> page, Alarms alarms) {
		return super.findPage(page, alarms);
	}
	
	@Transactional(readOnly = false)
	public void save(Alarms alarms) {
		super.save(alarms);
	}
	
	@Transactional(readOnly = false)
	public void delete(Alarms alarms) {
		super.delete(alarms);
	}

    /**
     * 查询报警信息详单
     */
    public List<AlarmsDefences> findAlarmsDefencesAll(){
        List<AlarmsDefences> list = alarmsDao.findAlarmsDefencesAll();
		for(AlarmsDefences alarmsDefencesTemp : list){
			if(alarmsDefencesTemp.getTypeName() != null){
				alarmsDefencesTemp.setTypeName(AlarmTypeName.getByType(Integer.parseInt(alarmsDefencesTemp.getTypeName())).getAlarmTypeName());
				alarmsDefencesTemp.setState(AlarmStateName.getByState(Integer.parseInt(alarmsDefencesTemp.getState())).getAlarmStateName());
			}
		}
        return list;
    }

	@Transactional(readOnly = false)
	public List findAlarmsInfoAcd(Map map) {
		List<AlarmsInfoAcd> AlarmsInfoAcdTemp = alarmsDao.getAlarmsInfoAcd(map);
		for (int i = 0; i < AlarmsInfoAcdTemp.size(); i++) {
			try {
				AlarmsInfoAcdTemp.get(i).setAlarmTypeName(AlarmTypeName.getByType(Integer.parseInt(AlarmsInfoAcdTemp.get(i).getAlarmType())).getAlarmTypeName());
			}catch (Exception e){
				AlarmsInfoAcdTemp.get(i).setAlarmTypeName("无");
			}
		}
		return AlarmsInfoAcdTemp;
	}

	@Transactional(readOnly = false)
	public void updateByAid(Alarms alarms){
		dao.updateByAid(alarms);
	}

    /**
     * 报警统计
     * @param page
     * @param alarmsCount
     * @return
     */
    public Page<AlarmsCount> find(Page<AlarmsCount> page, AlarmsCount alarmsCount) {
        alarmsCount.setPage(page);
        page.setList(alarmsDao.findAlarmsAll());
        return page;
    }

	public Page<AlarmsCount> findAlarmsCount(Page<AlarmsCount> page, AlarmsCount alarmsCount){
		alarmsCount.setPage(page);
		page.setList(dao.getAlarmsCount(alarmsCount));
		return page;
	}
}