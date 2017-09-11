/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.lu.entity.RoleArea;

/**
 * 区域权限DAO接口
 * @author khb
 * @version 2017-08-30
 */
@MyBatisDao
public interface RoleAreaDao extends CrudDao<RoleArea> {

    void save2(RoleArea roleArea);
	
}