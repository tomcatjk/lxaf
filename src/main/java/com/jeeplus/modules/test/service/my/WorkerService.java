/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.service.my;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.test.entity.my.Worker;
import com.jeeplus.modules.test.dao.my.WorkerDao;

/**
 * 人员管理Service
 * @author khb
 * @version 2017-04-14
 */
@Service
@Transactional(readOnly = true)
public class WorkerService extends CrudService<WorkerDao, Worker> {

	public Worker get(String id) {
		return super.get(id);
	}
	
	public List<Worker> findList(Worker worker) {
		return super.findList(worker);
	}
	
	public Page<Worker> findPage(Page<Worker> page, Worker worker) {
		return super.findPage(page, worker);
	}
	
	@Transactional(readOnly = false)
	public void save(Worker worker) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			worker.setCreateUser(user.getName());
			worker.setUpdateUser(user.getName());
		}
		worker.setUpdateDate(new Date());
		if(worker.getCreateDate()==null){
			worker.setCreateDate(worker.getUpdateDate());
		}
//		worker.updateDate = new Date();
//		worker.createDate = worker.updateDate;

		if(worker.getCode()==null||worker.getCode().equals("")){
			worker.setCode(IdGen.uuid());
		}
		super.save(worker);
	}
	
	@Transactional(readOnly = false)
	public void delete(Worker worker) {
		super.delete(worker);
	}

}