/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 服务人员Entity
 * @author khb
 * @version 2017-06-07
 */
public class ServicePersons extends DataEntity<ServicePersons> {
	
	private static final long serialVersionUID = 1L;
	private String loginname;		// loginname
	private String pwd;		// pwd
	private String name;		// name
	private String tel;		// tel
	private String score;		// score
	private Integer signcount;		// signcount
	private Integer servercount;		// servercount
	private String rmark;		// rmark
	private String devicecode;		// devicecode
	private Integer longitude;		// longitude
	private Integer latitude;		// latitude
	private Integer state;		// state
	
	public ServicePersons() {
		super();
	}

	public ServicePersons(String id){
		super(id);
	}

	@ExcelField(title="loginname", align=2, sort=1)
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	
	@ExcelField(title="pwd", align=2, sort=2)
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	@ExcelField(title="name", align=2, sort=3)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="tel", align=2, sort=4)
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	@ExcelField(title="score", align=2, sort=5)
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	@ExcelField(title="signcount", align=2, sort=6)
	public Integer getSigncount() {
		return signcount;
	}

	public void setSigncount(Integer signcount) {
		this.signcount = signcount;
	}
	
	@ExcelField(title="servercount", align=2, sort=7)
	public Integer getServercount() {
		return servercount;
	}

	public void setServercount(Integer servercount) {
		this.servercount = servercount;
	}
	
	@ExcelField(title="rmark", align=2, sort=8)
	public String getRmark() {
		return rmark;
	}

	public void setRmark(String rmark) {
		this.rmark = rmark;
	}
	
	@ExcelField(title="devicecode", align=2, sort=9)
	public String getDevicecode() {
		return devicecode;
	}

	public void setDevicecode(String devicecode) {
		this.devicecode = devicecode;
	}
	
	@ExcelField(title="longitude", align=2, sort=10)
	public Integer getLongitude() {
		return longitude;
	}

	public void setLongitude(Integer longitude) {
		this.longitude = longitude;
	}
	
	@ExcelField(title="latitude", align=2, sort=11)
	public Integer getLatitude() {
		return latitude;
	}

	public void setLatitude(Integer latitude) {
		this.latitude = latitude;
	}
	
	@ExcelField(title="state", align=2, sort=12)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
}