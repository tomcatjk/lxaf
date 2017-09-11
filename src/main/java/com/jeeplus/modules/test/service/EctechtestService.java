/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.test.entity.Ectechtest;
import com.jeeplus.modules.test.dao.EctechtestDao;

/**
 * 测试生成代码Service
 * @author 赵林华
 * @version 2017-03-13
 */
@Service
@Transactional(readOnly = true)
public class EctechtestService extends CrudService<EctechtestDao, Ectechtest> {

	public Ectechtest get(String id) {
		return super.get(id);
	}
	
	public List<Ectechtest> findList(Ectechtest ectechtest) {
		return super.findList(ectechtest);
	}
	
	public Page<Ectechtest> findPage(Page<Ectechtest> page, Ectechtest ectechtest) {
		return super.findPage(page, ectechtest);
	}
	
	@Transactional(readOnly = false)
	public void save(Ectechtest ectechtest) {
		super.save(ectechtest);
	}
	
	@Transactional(readOnly = false)
	public void delete(Ectechtest ectechtest) {
		super.delete(ectechtest);
	}
	
	
	
	
}