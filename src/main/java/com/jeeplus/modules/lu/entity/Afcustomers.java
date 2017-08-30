/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * AFCustomers Entity
 * @author Khb
 * @version 2017-06-16
 */
public class Afcustomers extends DataEntity<Afcustomers> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 编号
	private String name;		// 姓名
	private Integer age;		// 年龄
	private String gender;		// 性别
	private String phone;		// 电话
	private String address;		// 地址
	private String latitude;		// 坐标经度
	private String longitude;		// 坐标纬度
	private String imgurl;		// 头像
	
	public Afcustomers() {
		super();
	}

	public Afcustomers(String id){
		super(id);
	}

	@ExcelField(title="编号", align=2, sort=0)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@ExcelField(title="姓名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="年龄", align=2, sort=2)
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	@ExcelField(title="性别", align=2, sort=3)
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@ExcelField(title="电话", align=2, sort=4)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="地址", align=2, sort=5)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="坐标经度", align=2, sort=6)
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	@ExcelField(title="坐标纬度", align=2, sort=7)
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	@ExcelField(title="头像", align=2, sort=8)
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	
}