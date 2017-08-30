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
 * 报警记录表Entity
 * @author 陆华捷  
 * @version 2017-04-27
 */
public class Alarms extends DataEntity<Alarms> {
	
	private static final long serialVersionUID = 1L;
	private String aid;		// ID
	private String customerid;		// 客户ID
	private String masterid;		// 主机ID
	private String defenceid;		// 防区ID
	private Integer middefence;		// 分区ID
	private Integer alarmtype;		// 报警类型
	private Date alarmtime;		// 报警时间
	private Integer state;		// 状态
	private String servicename;		// 服务人员
	private Date servicetime;		// 服务时间
	private String result;		// 结果
	private String remark;		// 备注
	private Date finishtime;		// 确认时间
	private Customers customers; //顾客表
	private Date recordTime;
	private String disarmState;

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public String getDisarmState() {
		return disarmState;
	}

	public void setDisarmState(String disarmState) {
		this.disarmState = disarmState;
	}

	public Customers getCustomers() {
		return customers;
	}

	public void setCustomers(Customers customers) {
		this.customers = customers;
	}

	public Alarms() {
		super();
	}

	public Alarms(String id){
		super(id);
	}

	@ExcelField(title="ID", align=2, sort=0)
	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}
	
	@ExcelField(title="客户ID", align=2, sort=1)
	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	@ExcelField(title="主机ID", align=2, sort=2)
	public String getMasterid() {
		return masterid;
	}

	public void setMasterid(String masterid) {
		this.masterid = masterid;
	}
	
	@ExcelField(title="防区ID", align=2, sort=3)
	public String getDefenceid() {
		return defenceid;
	}

	public void setDefenceid(String defenceid) {
		this.defenceid = defenceid;
	}
	
	@NotNull(message="分区ID不能为空")
	@ExcelField(title="分区ID", align=2, sort=4)
	public Integer getMiddefence() {
		return middefence;
	}

	public void setMiddefence(Integer middefence) {
		this.middefence = middefence;
	}
	
	@NotNull(message="报警类型不能为空")
	@ExcelField(title="报警类型", align=2, sort=5)
	public Integer getAlarmtype() {
		return alarmtype;
	}

	public void setAlarmtype(Integer alarmtype) {
		this.alarmtype = alarmtype;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="报警时间不能为空")
	@ExcelField(title="报警时间", align=2, sort=6)
	public Date getAlarmtime() {
		return alarmtime;
	}

	public void setAlarmtime(Date alarmtime) {
		this.alarmtime = alarmtime;
	}
	
	@NotNull(message="状态不能为空")
	@ExcelField(title="状态", align=2, sort=7)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	@ExcelField(title="服务人员", align=2, sort=8)
	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="服务时间", align=2, sort=9)
	public Date getServicetime() {
		return servicetime;
	}

	public void setServicetime(Date servicetime) {
		this.servicetime = servicetime;
	}
	
	@ExcelField(title="结果", align=2, sort=10)
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@ExcelField(title="备注", align=2, sort=11)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="确认时间", align=2, sort=12)
	public Date getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Date finishtime) {
		this.finishtime = finishtime;
	}
	
}