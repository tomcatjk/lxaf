/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 服务记录Entity
 * @author khb
 * @version 2017-06-07
 */
public class ServiceRecords extends DataEntity<ServiceRecords> {
	
	private static final long serialVersionUID = 1L;
	private Integer serverid;		// serverid
	private String devicecode;		// devicecode
	private Integer deviceid;		// deviceid
	private String creator;		// creator
	private Date createtime;		// createtime
	private Integer state;		// state
	private String remark;		// remark
	private String rated;		// rated
	private Integer star;		// star
	
	public ServiceRecords() {
		super();
	}

	public ServiceRecords(String id){
		super(id);
	}

	@ExcelField(title="serverid", align=2, sort=1)
	public Integer getServerid() {
		return serverid;
	}

	public void setServerid(Integer serverid) {
		this.serverid = serverid;
	}
	
	@ExcelField(title="devicecode", align=2, sort=2)
	public String getDevicecode() {
		return devicecode;
	}

	public void setDevicecode(String devicecode) {
		this.devicecode = devicecode;
	}
	
	@ExcelField(title="deviceid", align=2, sort=3)
	public Integer getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(Integer deviceid) {
		this.deviceid = deviceid;
	}
	
	@ExcelField(title="creator", align=2, sort=4)
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="createtime", align=2, sort=5)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@ExcelField(title="state", align=2, sort=6)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	@ExcelField(title="remark", align=2, sort=7)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ExcelField(title="rated", align=2, sort=8)
	public String getRated() {
		return rated;
	}

	public void setRated(String rated) {
		this.rated = rated;
	}
	
	@ExcelField(title="star", align=2, sort=9)
	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}
	
}