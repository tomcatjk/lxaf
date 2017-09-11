package com.jeeplus.modules.lu.service;

import java.util.List;

import com.jeeplus.modules.lu.entity.Areas;
import com.jeeplus.modules.lu.entity.CustomerTypeName;
import com.jeeplus.modules.lu.entity.CustomersAlarms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.entity.Customers;
import com.jeeplus.modules.lu.dao.CustomersDao;

/**
 * 客户表Service
 * @author 陆华捷
 * @version 2017-04-27
 */
@Service
@Transactional(readOnly = true)
public class CustomersService extends CrudService<CustomersDao, Customers> {
	@Autowired
	CustomersDao customersDao;

	public Customers get(String id) {
		return super.get(id);
	}
	
	public List<Customers> findList(Customers customers) {
		return super.findList(customers);
	}
	
	public Page<Customers> findPage(Page<Customers> page, Customers customers) {
		return super.findPage(page, customers);
	}

	@Transactional(readOnly = false)
	public void save(Customers customers) {
		super.save(customers);
	}
	
	@Transactional(readOnly = false)
	public void delete(Customers customers) {
		super.delete(customers);
	}

	public Customers findCustomersByCid(String cid){
		return customersDao.getCustomersByCid(cid);
	}

	@Transactional(readOnly = false)
	public void editByCid(Customers customers){
		dao.updateByCid(customers);
	}

	@Transactional(readOnly = true)
	public Page<Customers> findCustomers(Page<Customers> page, Customers customers) {
		customers.setPage(page);
		page.setList(dao.getCustomers(customers));
		return page;
	}

	@Transactional(readOnly = true)
	public Page<CustomersAlarms> findCustomersAlarms(Page<CustomersAlarms> page, CustomersAlarms CustomersAlarms){
		CustomersAlarms.setPage(page);
		page.setList(dao.getCustomersAlarms(CustomersAlarms));
		for(CustomersAlarms customersAlarmsTemp : page.getList()){
			customersAlarmsTemp.setCustomersType(CustomerTypeName.getByType(customersAlarmsTemp.getCustomertype()).getCustomerTypeName());
		}
		return page;
	}
}