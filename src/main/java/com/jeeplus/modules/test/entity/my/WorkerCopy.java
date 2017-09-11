/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.entity.my;

import org.hibernate.validator.constraints.Email;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 人员管理2Entity
 * @author khb
 * @version 2017-04-17
 */
public class WorkerCopy extends DataEntity<WorkerCopy> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String code;		// 人员编号
	private String company;		// 所在公司
	private String email;		// 邮箱
	private String phone;		// 电话
	private String photo;		// 相片
	private String createUser;		// create_user
	private String deleteFlag;		// delete_flag
	private String updateUser;		// update_user
	private String deleteUser;		// delete_user
	private String password;		// password
	
	public WorkerCopy() {
		super();
	}

	public WorkerCopy(String id){
		super(id);
	}

	@ExcelField(title="姓名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="人员编号", align=2, sort=2)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="所在公司", align=2, sort=3)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	@Email(message="邮箱必须为合法邮箱")
	@ExcelField(title="邮箱", align=2, sort=4)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@ExcelField(title="电话", align=2, sort=5)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="相片", align=2, sort=6)
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@ExcelField(title="create_user", align=2, sort=8)
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@ExcelField(title="delete_flag", align=2, sort=9)
	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	@ExcelField(title="update_user", align=2, sort=11)
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	@ExcelField(title="delete_user", align=2, sort=12)
	public String getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	
	@ExcelField(title="password", align=2, sort=13)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}