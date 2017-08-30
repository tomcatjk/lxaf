/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 防区表Entity
 * @author 陆华捷  
 * @version 2017-04-27
 */
public class Defences extends DataEntity<Defences> {
	
	private static final long serialVersionUID = 1L;
	private String did;		// ID
	private String code;		// 防区编号
	private String name;		// 防区名称
	private Integer defencetype;		// 防区类型
	private Integer isline;		// 是否有线
	private Integer isside;		// 是否旁路
	private Integer state;		// 状态[布防=1，撤防=2]
	private String masterid;		// 所属主机
	private String mastercode;		// 主机编号
	private String customerid;		// 所属客户

	public Defences() {
		super();
	}

	public Defences(String id){
		super(id);
	}

	@ExcelField(title="ID", align=2, sort=0)
	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}
	
	@ExcelField(title="防区编号", align=2, sort=1)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="防区名称", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="防区类型不能为空")
	@ExcelField(title="防区类型", align=2, sort=3)
	public Integer getDefencetype() {
		return defencetype;
	}

	public void setDefencetype(Integer defencetype) {
		this.defencetype = defencetype;
	}
	
	@NotNull(message="是否有线不能为空")
	@ExcelField(title="是否有线", align=2, sort=4)
	public Integer getIsline() {
		return isline;
	}

	public void setIsline(Integer isline) {
		this.isline = isline;
	}
	
	@NotNull(message="是否旁路不能为空")
	@ExcelField(title="是否旁路", align=2, sort=5)
	public Integer getIsside() {
		return isside;
	}

	public void setIsside(Integer isside) {
		this.isside = isside;
	}
	
	@ExcelField(title="状态[布防=1，撤防=2]", align=2, sort=6)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	@ExcelField(title="所属主机", align=2, sort=7)
	public String getMasterid() {
		return masterid;
	}

	public void setMasterid(String masterid) {
		this.masterid = masterid;
	}
	
	@ExcelField(title="主机编号", align=2, sort=8)
	public String getMastercode() {
		return mastercode;
	}

	public void setMastercode(String mastercode) {
		this.mastercode = mastercode;
	}
	
	@ExcelField(title="所属客户", align=2, sort=9)
	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

}