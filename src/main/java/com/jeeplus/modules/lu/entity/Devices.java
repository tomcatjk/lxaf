/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 设备表Entity
 * @author 陆华捷  
 * @version 2017-05-05
 */
public class Devices extends DataEntity<Devices> {
	
	private static final long serialVersionUID = 1L;
	private String did;		// ID
	private String name;		// 名称
	private Integer devicetype;		// 设备类型
	private String devicetypeStr;   //设备类型
	private String devicemodel;		// 设备型号
	private String defenceid;		// 防区ID
	private String masterid;		// 所属主机
	private String customerid;		// 所属客户
	private Date createtime;		// 创建时间
	private String state;		// 状态(1:启用，2:禁用)
	private String createid;  //创建人

	private String code;

	private String defenceName;
	private String defenceType;

	public String getDefenceName() {
		return defenceName;
	}

	public void setDefenceName(String defenceName) {
		this.defenceName = defenceName;
	}

	public String getDefenceType() {
		return defenceType;
	}

	public void setDefenceType(String defenceType) {
		this.defenceType = defenceType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Devices() {
		super();
	}

	public Devices(String id){
		super(id);
	}

	@ExcelField(title="ID", align=2, sort=0)
	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}
	
	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="设备类型", align=2, sort=2)
	public Integer getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(Integer devicetype) {
		this.devicetype = devicetype;
	}
	
	@ExcelField(title="设备型号", align=2, sort=3)
	public String getDevicemodel() {
		return devicemodel;
	}

	public void setDevicemodel(String devicemodel) {
		this.devicemodel = devicemodel;
	}
	
	@ExcelField(title="防区ID", align=2, sort=4)
	public String getDefenceid() {
		return defenceid;
	}

	public void setDefenceid(String defenceid) {
		this.defenceid = defenceid;
	}
	
	@ExcelField(title="所属主机", align=2, sort=5)
	public String getMasterid() {
		return masterid;
	}

	public void setMasterid(String masterid) {
		this.masterid = masterid;
	}
	
	@ExcelField(title="所属客户", align=2, sort=6)
	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="创建时间不能为空")
	@ExcelField(title="创建时间", align=2, sort=7)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@ExcelField(title="状态(1:启用，2:禁用)", align=2, sort=8)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDevicetypeStr() {
		return devicetypeStr;
	}

	public void setDevicetypeStr(String devicetypeStr) {
		this.devicetypeStr = devicetypeStr;
	}

	public String getCreateid() {
		return createid;
	}

	public void setCreateid(String createid) {
		this.createid = createid;
	}
}