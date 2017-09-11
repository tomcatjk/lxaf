/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao2;
import com.jeeplus.modules.lu.entity.ServicePersons;

import java.util.List;

/**
 * 服务人员DAO接口
 * @author khb
 * @version 2017-06-07
 */
//@MyBatisDao2
@MyBatisDao
public interface ServicePersonsDao extends CrudDao<ServicePersons> {

    List findServicePersonsByState(int state);

    void updateById(ServicePersons servicePersons);
	
}