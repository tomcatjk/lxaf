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
public class ServicePersonsPart{

	private int id;
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

	public ServicePersonsPart(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public ServicePersonsPart() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Integer getSigncount() {
		return signcount;
	}

	public void setSigncount(Integer signcount) {
		this.signcount = signcount;
	}

	public Integer getServercount() {
		return servercount;
	}

	public void setServercount(Integer servercount) {
		this.servercount = servercount;
	}

	public String getRmark() {
		return rmark;
	}

	public void setRmark(String rmark) {
		this.rmark = rmark;
	}

	public String getDevicecode() {
		return devicecode;
	}

	public void setDevicecode(String devicecode) {
		this.devicecode = devicecode;
	}

	public Integer getLongitude() {
		return longitude;
	}

	public void setLongitude(Integer longitude) {
		this.longitude = longitude;
	}

	public Integer getLatitude() {
		return latitude;
	}

	public void setLatitude(Integer latitude) {
		this.latitude = latitude;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}