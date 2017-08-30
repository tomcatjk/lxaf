/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 充值表Entity
 * @author 陆华捷  
 * @version 2017-04-27
 */
public class Recharges extends DataEntity<Recharges> {
	
	private static final long serialVersionUID = 1L;
//	private String rid;		// ID
	private String customerid;		// 客户ID
	private String customerName;      //客户姓名
	private String userid;           //用户id
	private String userName;  		//用户姓名
	private String price;		//充值价格
	private String remark;		//备注
	private String receiver;		// 受理人
	private Integer state;		// 状态[正在处理=1，到账=2]
	private Date createtime;		// 创建时间
    private String recordPerson; //记录人
	
	public Recharges() {
		super();
	}

	public Recharges(String id){
		super(id);
	}


	public String getRid() {
		return super.id;
	}

	public void setRid(String rid) {
		this.id = rid;
	}
	

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	@ExcelField(title="受理人", align=2, sort=5)
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	@ExcelField(title="状态[正在处理=1，到账=2]", align=2, sort=3)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="创建时间不能为空")
	@ExcelField(title="创建时间", align=2, sort=6)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@ExcelField(title="充值金额", align=2, sort=2)
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@ExcelField(title="备注", align=2, sort=4)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@ExcelField(title="客户", align=2, sort=0)
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@ExcelField(title="用户", align=2, sort=1)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

    public String getRecordPerson() {
        return recordPerson;
    }

    public void setRecordPerson(String recordPerson) {
        this.recordPerson = recordPerson;
    }
}