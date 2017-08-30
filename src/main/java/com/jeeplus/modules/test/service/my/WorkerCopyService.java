/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.service.my;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.test.entity.my.WorkerCopy;
import com.jeeplus.modules.test.dao.my.WorkerCopyDao;

/**
 * 人员管理2Service
 * @author khb
 * @version 2017-04-17
 */
@Service
@Transactional(readOnly = true)
public class WorkerCopyService extends CrudService<WorkerCopyDao, WorkerCopy> {

	public WorkerCopy get(String id) {
		return super.get(id);
	}
	
	public List<WorkerCopy> findList(WorkerCopy workerCopy) {
		return super.findList(workerCopy);
	}
	
	public Page<WorkerCopy> findPage(Page<WorkerCopy> page, WorkerCopy workerCopy) {
		return super.findPage(page, workerCopy);
	}
	
	@Transactional(readOnly = false)
	public void save(WorkerCopy workerCopy) {
		super.save(workerCopy);
	}
	
	@Transactional(readOnly = false)
	public void delete(WorkerCopy workerCopy) {
		super.delete(workerCopy);
	}
	
	
	
	
}