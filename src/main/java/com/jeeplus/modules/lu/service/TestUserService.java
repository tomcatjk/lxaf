/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;

import com.jeeplus.modules.lu.dao.TestUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "insurance", rollbackFor = Exception.class)
public class TestUserService{

	@Autowired
	TestUserDao testUserDao;

	public int findCount(){
		return testUserDao.getCount();
	}

}