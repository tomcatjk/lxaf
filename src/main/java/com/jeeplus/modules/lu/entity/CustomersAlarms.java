package com.jeeplus.modules.lu.entity;

import com.jeeplus.common.persistence.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * 客户统计实体类
 * Created by Administrator on 2017/5/5.
 */
public class CustomersAlarms extends DataEntity<CustomersAlarms> {
//    private String id;
    //名称
    private String name;
    //客户类型
   private String customersType;
    //质检人
    private String qualityPerson;
    //安装人
    private String installPerson;
    //安装时间
    private Date installTime;
    //主机数
    private int masterNum;

    private int DEVICETYPE1;
    private int DEVICETYPE2;
    private int DEVICETYPE3;
    private int DEVICETYPE4;
    private int DEVICETYPE5;
    private int DEVICETYPE6;
    private int DEVICETYPE7;
    private int DEVICETYPE8;

    private String customerId;

    private String customerType;

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    private List deviceTypeNameList;

    public List getDeviceTypeNameList() {
        return deviceTypeNameList;
    }

    public void setDeviceTypeNameList(List deviceTypeNameList) {
        this.deviceTypeNameList = deviceTypeNameList;
    }

    public int getDEVICETYPE8() {
        return DEVICETYPE8;
    }

    public void setDEVICETYPE8(int DEVICETYPE8) {
        this.DEVICETYPE8 = DEVICETYPE8;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getDEVICETYPE1() {
        return DEVICETYPE1;
    }

    public void setDEVICETYPE1(int DEVICETYPE1) {
        this.DEVICETYPE1 = DEVICETYPE1;
    }

    public int getDEVICETYPE2() {
        return DEVICETYPE2;
    }

    public void setDEVICETYPE2(int DEVICETYPE2) {
        this.DEVICETYPE2 = DEVICETYPE2;
    }

    public int getDEVICETYPE3() {
        return DEVICETYPE3;
    }

    public void setDEVICETYPE3(int DEVICETYPE3) {
        this.DEVICETYPE3 = DEVICETYPE3;
    }

    public int getDEVICETYPE4() {
        return DEVICETYPE4;
    }

    public void setDEVICETYPE4(int DEVICETYPE4) {
        this.DEVICETYPE4 = DEVICETYPE4;
    }

    public int getDEVICETYPE5() {
        return DEVICETYPE5;
    }

    public void setDEVICETYPE5(int DEVICETYPE5) {
        this.DEVICETYPE5 = DEVICETYPE5;
    }

    public int getDEVICETYPE6() {
        return DEVICETYPE6;
    }

    public void setDEVICETYPE6(int DEVICETYPE6) {
        this.DEVICETYPE6 = DEVICETYPE6;
    }

    public int getDEVICETYPE7() {
        return DEVICETYPE7;
    }

    public void setDEVICETYPE7(int DEVICETYPE7) {
        this.DEVICETYPE7 = DEVICETYPE7;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualityPerson() {
        return qualityPerson;
    }

    public void setQualityPerson(String qualityPerson) {
        this.qualityPerson = qualityPerson;
    }

    public String getInstallPerson() {
        return installPerson;
    }

    public void setInstallPerson(String installPerson) {
        this.installPerson = installPerson;
    }

    public Date getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public int getMasterNum() {
        return masterNum;
    }

    public void setMasterNum(int masterNum) {
        this.masterNum = masterNum;
    }

    public String getCustomersType() {
        return customersType;
    }

    public void setCustomersType(String customersType) {
        this.customersType = customersType;
    }
}
