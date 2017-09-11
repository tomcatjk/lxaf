/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.TreeDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.lu.entity.Areas;
import com.jeeplus.modules.sys.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 区域表DAO接口
 * @author 陆华捷
 * @version 2017-04-26
 */
@MyBatisDao
public interface AreasDao extends TreeDao<Areas> {

	Areas getAreasById(String id);

	void deleteAreas(User user);

	List getAreasCustomers(Map map);

	List getAreasToAreasCustomers(User user);

	List getAreasToRoleArea(Areas areas);

}