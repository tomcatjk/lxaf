package com.jeeplus.modules.lu.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.lu.entity.Devices;
import com.jeeplus.modules.lu.entity.DevicesCustomers;

import java.util.List;
import java.util.Map;

/**
 * 设备表DAO接口
 * @author 陆华捷  
 * @version 2017-05-05
 */
@MyBatisDao
public interface DevicesDao extends CrudDao<Devices> {

    /**
     * 根据客户名查询客户id
     */
    List<String> findCustomersidByCustomerName(String name);

    /**
     * 查询所有客户类别
     */
    List<DevicesCustomers> findAllCustomersType();

    List findPageByCustomerid(Devices devices);

    void updateByDid(Devices devices);

    List findByCidAndDevicetypeAndPage(Map map);

    Map findByDeviceid(Map map);

    void updatedevices(Map map);
}