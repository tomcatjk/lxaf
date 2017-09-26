package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.lu.entity.Customers;
import com.jeeplus.modules.lu.entity.Devices;
import com.jeeplus.modules.lu.entity.DevicesCustomers;
import com.jeeplus.modules.lu.entity.Masters;

import java.util.List;
import java.util.Map;

/**
 * 设备表DAO接口
 * @author 陆华捷  
 * @version 2017-05-05
 */
@MyBatisDao
public interface DevicesDao extends CrudDao<Devices> {

    List findPageByCustomerid(Devices devices);

    void updateByDid(Devices devices);

    List findByCidAndDevicetypeAndPage(Map map);

    Map findByDeviceid(Map map);

    void updatedevices(Map map);

    void deleteByCustomer(Customers customers);

    List getDeviceCustomer(DevicesCustomers devicesCustomers);

    void deleteByMaster(Masters masters);
}