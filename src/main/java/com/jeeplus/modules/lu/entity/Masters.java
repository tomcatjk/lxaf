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
 * 主机表Entity
 * @author 陆华捷  
 * @version 2017-04-27
 */
public class Masters extends DataEntity<Masters> {
	
	private static final long serialVersionUID = 1L;
	private String mid;		// ID
	private String code;		// 编号
	private String name;		// 名称
	private String sim;		// SIM卡号
	private String customerid;		// 所属客户
	private String maintype;		// 主机型号
	private String version;		// 版本
	private String state;		// 状态
	private String stateStr; //状态
	private Date createtime;		// 创建时间

	private String isOnline;

	private String createId;

	private String disarmState;

	public String getDisarmState() {
		return disarmState;
	}

	public void setDisarmState(String disarmState) {
		this.disarmState = disarmState;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public Masters() {
		super();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Masters(String id){
		super(id);
	}

	@ExcelField(title="ID", align=2, sort=0)
	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
	
	@ExcelField(title="编号", align=2, sort=1)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="名称", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="SIM卡号", align=2, sort=3)
	public String getSim() {
		return sim;
	}

	public void setSim(String sim) {
		this.sim = sim;
	}
	
	@ExcelField(title="所属客户", align=2, sort=4)
	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	@ExcelField(title="主机型号", align=2, sort=5)
	public String getMaintype() {
		return maintype;
	}

	public void setMaintype(String maintype) {
		this.maintype = maintype;
	}
	
	@ExcelField(title="版本", align=2, sort=6)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
//	@ExcelField(title="状态", align=2, sort=7)
//	public Integer getState() {
//		return state;
//	}
//
//	public void setState(Integer state) {
//		this.state = state;
//	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="创建时间不能为空")
	@ExcelField(title="创建时间", align=2, sort=8)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getStateStr() {
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
}