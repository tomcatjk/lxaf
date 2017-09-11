/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.entity.ServicePersons;
import com.jeeplus.modules.lu.dao.ServicePersonsDao;

/**
 * 服务人员Service
 * @author khb
 * @version 2017-06-07
 */
@Service
@Transactional(readOnly = true)
public class ServicePersonsService extends CrudService<ServicePersonsDao, ServicePersons> {

	public ServicePersons get(String id) {
		return super.get(id);
	}
	
	public List<ServicePersons> findList(ServicePersons servicePersons) {
		return super.findList(servicePersons);
	}
	
	public Page<ServicePersons> findPage(Page<ServicePersons> page, ServicePersons servicePersons) {
		return super.findPage(page, servicePersons);
	}
	
	@Transactional(readOnly = false)
	public void save(ServicePersons servicePersons) {
		super.save(servicePersons);
	}
	
	@Transactional(readOnly = false)
	public void delete(ServicePersons servicePersons) {
		super.delete(servicePersons);
	}

	@Transactional(readOnly = false)
	public List getServicePersonsByState(int state){
		return dao.findServicePersonsByState(state);
	}

	@Transactional(readOnly = false)
	public void updateById(ServicePersons servicePersons){
		dao.updateById(servicePersons);
	}
}