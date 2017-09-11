/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 客户表Entity
 * @author 陆华捷
 * @version 2017-04-27
 */
public class Customers extends DataEntity<Customers> {
	
	private static final long serialVersionUID = 1L;
	private String cid;		// 客户ID
	private String name;		// 客户名称
	private Integer customertype;		// 客户类别
	private String customerTypeStr; //客户类型
	private String areaid;		// 区域ID
	private String areaName;		//区域名
	private String point;		// 坐标
	private String address;		// 地址
	private String contacts;		// 联系人
	private String phone;		// 联系电话
	private String qualityperson;		// 质检人
	private String installperson;		// 安装人
	private String preparer;		// 填表人
	private Date installtime;		// 安装时间
	private Date duetime;		// 到期时间
	private String remark;		// 备注
	private Date createtime;		// 创建时间
	private String createid;  //创建者id

	private String startTime;
	private String endTime;

	private String parentCid;

	public String getParentCid() {
		return parentCid;
	}

	public void setParentCid(String parentCid) {
		this.parentCid = parentCid;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Customers() {
		super();
	}

	public Customers(String id){
		super(id);
	}

	@ExcelField(title="客户ID", align=2, sort=0)
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
	
	@ExcelField(title="客户名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="客户类别不能为空")
	@ExcelField(title="客户类别", align=2, sort=2)
	public Integer getCustomertype() {
		return customertype;
	}

	public void setCustomertype(Integer customertype) {
		this.customertype = customertype;
	}
	
	@ExcelField(title="区域ID", align=2, sort=3)
	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}
	
	@ExcelField(title="坐标", align=2, sort=4)
	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}
	
	@ExcelField(title="地址", align=2, sort=5)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="联系人", align=2, sort=6)
	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	
	@ExcelField(title="联系电话", align=2, sort=7)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="质检人", align=2, sort=8)
	public String getQualityperson() {
		return qualityperson;
	}

	public void setQualityperson(String qualityperson) {
		this.qualityperson = qualityperson;
	}
	
	@ExcelField(title="安装人", align=2, sort=9)
	public String getInstallperson() {
		return installperson;
	}

	public void setInstallperson(String installperson) {
		this.installperson = installperson;
	}
	
	@ExcelField(title="填表人", align=2, sort=10)
	public String getPreparer() {
		return preparer;
	}

	public void setPreparer(String preparer) {
		this.preparer = preparer;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="安装时间", align=2, sort=11)
	public Date getInstalltime() {
		return installtime;
	}

	public void setInstalltime(Date installtime) {
		this.installtime = installtime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="到期时间", align=2, sort=12)
	public Date getDuetime() {
		return duetime;
	}

	public void setDuetime(Date duetime) {
		this.duetime = duetime;
	}
	
	@ExcelField(title="备注", align=2, sort=13)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="创建时间不能为空")
	@ExcelField(title="创建时间", align=2, sort=14)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getCustomerTypeStr() {
		return customerTypeStr;
	}

	public void setCustomerTypeStr(String customerTypeStr) {
		this.customerTypeStr = customerTypeStr;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCreateid() {
		return createid;
	}

	public void setCreateid(String createid) {
		this.createid = createid;
	}
}