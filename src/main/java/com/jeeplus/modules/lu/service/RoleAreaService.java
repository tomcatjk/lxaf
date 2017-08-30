/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.entity.RoleArea;
import com.jeeplus.modules.lu.dao.RoleAreaDao;

/**
 * 区域权限Service
 * @author khb
 * @version 2017-08-30
 */
@Service
@Transactional(readOnly = true)
public class RoleAreaService extends CrudService<RoleAreaDao, RoleArea> {

	public RoleArea get(String id) {
		return super.get(id);
	}
	
	public List<RoleArea> findList(RoleArea roleArea) {
		return super.findList(roleArea);
	}
	
	public Page<RoleArea> findPage(Page<RoleArea> page, RoleArea roleArea) {
		return super.findPage(page, roleArea);
	}
	
	@Transactional(readOnly = false)
	public void save(RoleArea roleArea) {
		super.save(roleArea);
	}
	
	@Transactional(readOnly = false)
	public void delete(RoleArea roleArea) {
		super.delete(roleArea);
	}
	
	
	
	
}