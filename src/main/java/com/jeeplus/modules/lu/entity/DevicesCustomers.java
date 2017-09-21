package com.jeeplus.modules.lu.entity;

import com.jeeplus.common.persistence.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */
public class DevicesCustomers extends DataEntity<DevicesCustomers> {

    private String id;
    //设备型号
    private String devicemodel;
    //设备名字
    private String devicesName;
    //设备类型
    private String devicesType;
    //所属客户名
    private String customersName;
    //状态(1:启用0:未启用)
    private String state;
    //安装时间
    private Date installTime;
    //到期时间
    private  Date dueTime;
    //安装人
    private String installPerson;
    //质检人
    private String qualityPerson;

    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevicemodel() {
        return devicemodel;
    }

    public void setDevicemodel(String devicemodel) {
        this.devicemodel = devicemodel;
    }

    public String getDevicesName() {
        return devicesName;
    }

    public void setDevicesName(String devicesName) {
        this.devicesName = devicesName;
    }

    public String getDevicesType() {
        return devicesType;
    }

    public void setDevicesType(String devicesType) {
        this.devicesType = devicesType;
    }

    public String getCustomersName() {
        return customersName;
    }

    public void setCustomersName(String customersName) {
        this.customersName = customersName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }

    public String getInstallPerson() {
        return installPerson;
    }

    public void setInstallPerson(String installPerson) {
        this.installPerson = installPerson;
    }

    public String getQualityPerson() {
        return qualityPerson;
    }

    public void setQualityPerson(String qualityPerson) {
        this.qualityPerson = qualityPerson;
    }
}
