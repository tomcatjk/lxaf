/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.lu.entity.Customers;
import com.jeeplus.modules.lu.entity.Defences;
import com.jeeplus.modules.lu.entity.DefencesPart;

import java.util.List;
import java.util.Map;

/**
 * 防区表DAO接口
 * @author 陆华捷  
 * @version 2017-04-27
 */
@MyBatisDao
public interface DefencesDao extends CrudDao<Defences> {

    List getDefencesListByMasterId (String masterId);

    DefencesPart getDefencesbyDid (String did);

    void updatedefences(Map map);

    void deleteByCustomer(Customers customers);

}