/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.entity.Afcustomers;
import com.jeeplus.modules.lu.dao.AfcustomersDao;

/**
 * AFCustomers Service
 * @author Khb
 * @version 2017-06-16
 */
@Service
@Transactional(readOnly = true)
public class AfcustomersService extends CrudService<AfcustomersDao, Afcustomers> {

	public Afcustomers get(String id) {
		return super.get(id);
	}
	
	public List<Afcustomers> findList(Afcustomers afcustomers) {
		return super.findList(afcustomers);
	}
	
	public Page<Afcustomers> findPage(Page<Afcustomers> page, Afcustomers afcustomers) {
		return super.findPage(page, afcustomers);
	}
	
	@Transactional(readOnly = false)
	public void save(Afcustomers afcustomers) {
		super.save(afcustomers);
	}
	
	@Transactional(readOnly = false)
	public void delete(Afcustomers afcustomers) {
		super.delete(afcustomers);
	}
	
	
	
	
}