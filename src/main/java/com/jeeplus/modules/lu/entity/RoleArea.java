/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 区域权限Entity
 * @author khb
 * @version 2017-08-30
 */
public class RoleArea extends DataEntity<RoleArea> {
	
	private static final long serialVersionUID = 1L;
	private String roleId;		// role_id
	private String areaId;		// area_id

	private String areaIds;

	public String getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}

	public RoleArea() {
		super();
	}

	public RoleArea(String id){
		super(id);
	}

	@ExcelField(title="role_id", align=2, sort=1)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@ExcelField(title="area_id", align=2, sort=2)
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
}