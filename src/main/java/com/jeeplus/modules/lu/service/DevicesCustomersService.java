/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.dao.DevicesCustomersDao;
import com.jeeplus.modules.lu.entity.AlarmTypeName;
import com.jeeplus.modules.lu.entity.DeviceStateName;
import com.jeeplus.modules.lu.entity.DeviceTypeName;
import com.jeeplus.modules.lu.entity.DevicesCustomers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报警记录表Service
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Service
@Transactional(readOnly = true)
public class DevicesCustomersService extends CrudService<DevicesCustomersDao, DevicesCustomers> {
	@Autowired
    DevicesCustomersDao devicesCustomersDao;
	public Page<DevicesCustomers> find(Page<DevicesCustomers> page, DevicesCustomers devicesCustomers){
		page=super.findPage(page,devicesCustomers);
		List<DevicesCustomers> list=page.getList();
		for(DevicesCustomers devicesCustomersTemp : list){
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
	public Page<DevicesCustomers> find1(Page<DevicesCustomers> page, DevicesCustomers devicesCustomers){
		devicesCustomers.setPage(page);
		page.setList(devicesCustomersDao.totalDevicesCustomersBySelect(devicesCustomers));
		List<DevicesCustomers> list =page.getList();
		for(DevicesCustomers devicesCustomersTemp : list){
			//设置类型
			if(devicesCustomersTemp.getDevicesType()==null){
				devicesCustomersTemp.setDevicesType("");
			}else {
				devicesCustomersTemp.setDevicesType(DeviceTypeName.getByType(Integer.parseInt(devicesCustomersTemp.getDevicesType())).getDeviceTypeName());
			}
			//设置设备状态
			if(devicesCustomersTemp.getState()==null){
				devicesCustomersTemp.setState("");
			}else {
				devicesCustomersTemp.setState(DeviceStateName.getByState(Integer.parseInt(devicesCustomersTemp.getState())).getDeviceStateName());
			}
		}
		return page;
	}
}