/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import java.util.List;
import java.util.Map;

import com.jeeplus.common.persistence.*;
import com.jeeplus.modules.lu.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.CrudService;
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

	@Transactional(readOnly = false)
	public void updateByDid(Devices devices){
		dao.updateByDid(devices);
	}

	@Transactional(readOnly = false)
	public Map findByDeviceid(Map map){
		return dao.findByDeviceid(map);
	}

	@Transactional(readOnly = false)
	public void deleteByCustomer(Customers customers){
		dao.deleteByCustomer(customers);
	}

	public Page<DevicesCustomers> findDeviceCustomerPage(Page<DevicesCustomers> page, DevicesCustomers devicesCustomers){
		devicesCustomers.setPage(page);
		page.setList(dao.getDeviceCustomer(devicesCustomers));
		for(DevicesCustomers devicesCustomersTemp : page.getList()){
			if(devicesCustomersTemp.getDevicesType() != null){
				devicesCustomersTemp.setDevicesType(DeviceTypeName.getByType(Integer.parseInt(devicesCustomersTemp.getDevicesType())).getDeviceTypeName());
			}
			//设置设备状态
			if(devicesCustomersTemp.getState() != null){
				devicesCustomersTemp.setState(DeviceStateName.getByState(Integer.parseInt(devicesCustomersTemp.getState())).getDeviceStateName());
			}
		}
		return page;
	}

	@Transactional(readOnly = false)
	public void deleteByMaster(Masters masters){
		dao.deleteByMaster(masters);
	}
}