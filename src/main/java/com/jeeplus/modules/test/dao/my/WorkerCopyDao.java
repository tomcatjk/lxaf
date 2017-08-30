/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.dao.my;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.test.entity.my.WorkerCopy;

/**
 * 人员管理2DAO接口
 * @author khb
 * @version 2017-04-17
 */
@MyBatisDao
public interface WorkerCopyDao extends CrudDao<WorkerCopy> {

	
}