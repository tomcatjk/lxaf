/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.entity.Recharges;
import com.jeeplus.modules.lu.dao.RechargesDao;

/**
 * 充值表Service
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Service
@Transactional(readOnly = true)
public class RechargesService extends CrudService<RechargesDao, Recharges> {

	public Recharges get(String id) {
		return super.get(id);
	}
	
	public List<Recharges> findList(Recharges recharges) {
		return super.findList(recharges);
	}
	
	public Page<Recharges> findPage(Page<Recharges> page, Recharges recharges) {
		return super.findPage(page, recharges);
	}
	
	@Transactional(readOnly = false)
	public void save(Recharges recharges) {
		super.save(recharges);
	}
	
	@Transactional(readOnly = false)
	public void delete(Recharges recharges) {
		super.delete(recharges);
	}

}