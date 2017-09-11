/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao2;
import com.jeeplus.modules.lu.entity.ServiceRecords;

import java.util.Map;

/**
 * 服务记录DAO接口
 * @author khb
 * @version 2017-06-07
 */
//@MyBatisDao2
@MyBatisDao
public interface ServiceRecordsDao extends CrudDao<ServiceRecords> {

    void insertServiceRecords(ServiceRecords serviceRecords);

    void updateById(ServiceRecords serviceRecords);

    ServiceRecords findServiceRecordsByserverIDAndState(Map map);
	
}