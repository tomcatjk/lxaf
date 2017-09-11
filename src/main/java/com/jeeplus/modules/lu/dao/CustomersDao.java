package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.lu.entity.Areas;
import com.jeeplus.modules.lu.entity.Customers;
import com.jeeplus.modules.lu.entity.CustomersAlarms;

import java.util.List;

/**
 * 客户表DAO接口
 * @author 陆华捷
 * @version 2017-04-27
 */
@MyBatisDao
public interface CustomersDao extends CrudDao<Customers> {

    Customers getCustomersByCid(String cid);

    void updateByCid(Customers customers);

    List getCustomers(Customers customers);

    List getCustomersAlarms(CustomersAlarms customersAlarms);
}