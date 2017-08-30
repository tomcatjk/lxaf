/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jeeplus.common.persistence.*;
import com.jeeplus.modules.lu.entity.AlarmTypeName;
import com.jeeplus.modules.lu.entity.DeviceTypeName;
import com.jeeplus.modules.lu.entity.DevicesCustomers;
import com.jeeplus.modules.lu.web.DevicesController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.entity.Devices;
import com.jeeplus.modules.lu.dao.DevicesDao;

/**
 * 设备表Service
 * @author 陆华捷  
 * @version 2017-05-05
 */
@Service
@Transactional(readOnly = true)
public class DevicesService extends CrudService<DevicesDao, Devices> {

	@Autowired
	DevicesDao devicesDao;

	public Devices get(String id) {
		return super.get(id);
	}
	
	public List<Devices> findList(Devices devices) {
		return super.findList(devices);
	}

	public Page<Devices> findPage(Page<Devices> page, Devices devices) {
		return super.findPage(page, devices);
	}

	@Transactional(readOnly = false)
	public Page findPageByCustomerid(Page page, Devices devices) {
		devices.setPage(page);
		List<Devices> list = devicesDao.findPageByCustomerid(devices);
		for(Devices devicesTemp : list){
			try{
				devicesTemp.setDevicetypeStr(DeviceTypeName.getByType(devicesTemp.getDevicetype()).getDeviceTypeName());
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		page.setList(list);
		return page;
	}

	@Transactional(readOnly = false)
	public void save(Devices devices) {
		super.save(devices);
	}

	@Transactional(readOnly = false)
	public void delete(Devices devices) {
		super.delete(devices);
	}

	/**
	 * 设备统计
	 */
	public List<DevicesCustomers> totalDevicesCustomers(){
		List<DevicesCustomers> list = devicesDao.totalDevicesCustomers();
		for(DevicesCustomers devicesCustomersTemp : list){
			if(devicesCustomersTemp.getDevicesType() != null){
				devicesCustomersTemp.setDevicesType(DeviceTypeName.getByType(Integer.parseInt(devicesCustomersTemp.getDevicesType())).getDeviceTypeName());
			}
		}
		return list;
	}

	/**
	 * 通过客户名查询客户id
	 */
	public List<String> findCustomersidByCustomerName(String name){
		List<String> customersid = devicesDao.findCustomersidByCustomerName(name);
		return customersid;
	}
	/**
	 * 查询所有客户类别
	 */
	public List<DevicesCustomers> findAllCustomersType(){
		List<DevicesCustomers> list = devicesDao.findAllCustomersType();

		return list;
	}

	@Transactional(readOnly = false)
	public void updateByDid(Devices devices){
		dao.updateByDid(devices);
	}

	@Transactional(readOnly = false)
	public List findByCidAndDevicetypeAndPage(Map map){
		return dao.findByCidAndDevicetypeAndPage(map);
	}

	@Transactional(readOnly = false)
	public Map findByDeviceid(Map map){
		return dao.findByDeviceid(map);
	}

	@Transactional(readOnly = false)
	public void updatedevices(Map map){devicesDao.updatedevices(map);}
}