/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.entity.ServiceRecords;
import com.jeeplus.modules.lu.dao.ServiceRecordsDao;

/**
 * 服务记录Service
 * @author khb
 * @version 2017-06-07
 */
@Service
@Transactional(readOnly = true)
public class ServiceRecordsService extends CrudService<ServiceRecordsDao, ServiceRecords> {

	public ServiceRecords get(String id) {
		return super.get(id);
	}
	
	public List<ServiceRecords> findList(ServiceRecords serviceRecords) {
		return super.findList(serviceRecords);
	}
	
	public Page<ServiceRecords> findPage(Page<ServiceRecords> page, ServiceRecords serviceRecords) {
		return super.findPage(page, serviceRecords);
	}
	
	@Transactional(readOnly = false)
	public void save(ServiceRecords serviceRecords) {
		super.save(serviceRecords);
	}
	
	@Transactional(readOnly = false)
	public void delete(ServiceRecords serviceRecords) {
		super.delete(serviceRecords);
	}

	@Transactional(readOnly = false)
	public void addServiceRecords(ServiceRecords serviceRecords){
		dao.insertServiceRecords(serviceRecords);
	}

	@Transactional(readOnly = false)
	public void updateById(ServiceRecords serviceRecords){
		dao.updateById(serviceRecords);
	}

	@Transactional(readOnly = false)
	public ServiceRecords getServiceRecordsByserverIDAndState(Map map){
		return dao.findServiceRecordsByserverIDAndState(map);
	}
	
}