/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jeeplus.common.persistence.TreeEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 区域表Entity
 * @author 陆华捷
 * @version 2017-04-26
 */
public class AreasCustomers extends TreeEntity<AreasCustomers> {

	private static final long serialVersionUID = 1L;
	private AreasCustomers parent;		// 父级编号
	private String parentIds;		// 所有父级编号
	private String name;		// 名称
	private Integer sort;		// 排序
//	private String cid;		// 客户编号
	private String customertype;		// 客户类型
	private String createName;  //创建人
	private List<Customers> customersList; //获取区域下的客户
	private String createDateStr;

	private String cid;		// 客户ID
	private String cname;		// 客户名称
	private String phone;   //客户电话
	private String areaid;		// 区域ID
	private String areaName;		//区域名
	private String point;		// 坐标
	private String address;		// 地址

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}


	public List<Customers> getCustomersList() {
		return customersList;
	}

	public void setCustomersList(List<Customers> customersList) {
		this.customersList = customersList;
	}

	public AreasCustomers() {
		super();
	}

	public AreasCustomers(String id){
		super(id);
	}

	@JsonBackReference
	@NotNull(message="父级编号不能为空")
	public AreasCustomers getParent() {
		return parent;
	}

	public void setParent(AreasCustomers parent) {
		this.parent = parent;
	}
	
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="排序不能为空")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
	
	public String getCustomertype() {
		return customertype;
	}

	public void setCustomertype(String customertype) {
		this.customertype = customertype;
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}

	public void setParentId(String parentId) {
		if(parent != null){
			parent.setId(parentId);
		}else{

		}
//		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}
}