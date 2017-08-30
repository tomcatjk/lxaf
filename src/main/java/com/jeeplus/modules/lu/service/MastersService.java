/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import java.util.List;
import java.util.Map;

import com.jeeplus.modules.lu.entity.DeviceStateName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.entity.Masters;
import com.jeeplus.modules.lu.dao.MastersDao;

/**
 * 主机表Service
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Service
@Transactional(readOnly = true)
public class MastersService extends CrudService<MastersDao, Masters> {

	@Autowired
	MastersDao mastersDao;

	public Masters get(String id) {
		return super.get(id);
	}
	
	public List<Masters> findList(Masters masters) {
		return super.findList(masters);
	}
	
	public Page<Masters> findPage(Page<Masters> page, Masters masters) {
		return super.findPage(page, masters);
	}
	
	@Transactional(readOnly = false)
	public void save(Masters masters) {
		super.save(masters);
	}
	
	@Transactional(readOnly = false)
	public void delete(Masters masters) {
		super.delete(masters);
	}

	@Transactional(readOnly = false)
	public Page findPageByCustomerid(Page page, Masters masters){
		masters.setPage(page);
		List<Masters> pageListTemp = dao.findPageByCustomerid(masters);
		for(int i = 0; i < pageListTemp.size(); i++){
			pageListTemp.get(i).setStateStr(DeviceStateName.getByState(Integer.parseInt(pageListTemp.get(i).getState())).getDeviceStateName());
		}
		page.setList(pageListTemp);
		return page;
	}

	public List findMastersListByCid(String cid){
		return mastersDao.getMastersListByCid(cid);
	}

	@Transactional(readOnly = false)
	public void updateByMid(Masters masters){
		dao.updateByMid(masters);
	}

	@Transactional(readOnly = false)
	public void updatemasters(Map map){mastersDao.updatemasters(map);}

}