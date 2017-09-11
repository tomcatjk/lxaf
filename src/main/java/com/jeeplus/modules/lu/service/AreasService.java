/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import java.util.List;
import java.util.Map;

import com.jeeplus.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.lu.entity.Areas;
import com.jeeplus.modules.lu.dao.AreasDao;

/**
 * 区域表Service
 * @author 陆华捷
 * @version 2017-04-26
 */
@Service
@Transactional(readOnly = true)
public class AreasService extends TreeService<AreasDao, Areas> {

    @Autowired
    AreasDao areasDao;
	public Areas get(String id) {
		return super.get(id);
	}
	
	public List<Areas> findList(Areas areas) {
		if (StringUtils.isNotBlank(areas.getParentIds())){
			areas.setParentIds(","+areas.getParentIds()+",");
		}
		return super.findList(areas);
	}

	@Transactional(readOnly = false)
	public Areas findAreasById(String id) {
		return areasDao.getAreasById(id);
	}

	@Transactional(readOnly = false)
	public void save(Areas areas) {
		super.save(areas);
	}

	@Transactional(readOnly = false)
	public void update(Areas areas) {
		areasDao.update(areas);
	}
	
	@Transactional(readOnly = false)
	public void delete(Areas areas) {
		super.delete(areas);
	}

	@Transactional(readOnly = false)
	public void deleteAreas(User user){
		areasDao.deleteAreas(user);
	}

	public List findAreasCustomers(Map map){
		return areasDao.getAreasCustomers(map);
	}

	public List findAreasToAreasCustomers(User user){
		return areasDao.getAreasToAreasCustomers(user);
	}

	public List findAreasToRoleArea(Areas areas){
		return areasDao.getAreasToRoleArea(areas);
	}

}