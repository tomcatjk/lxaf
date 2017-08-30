/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.app.entity;

/**
 * 防区表Entity
 * @author 陆华捷  
 * @version 2017-04-27
 */
public class DefencesDevice {

	private String defenceid;
	private String defencecode;
	private String defencename;
	private int defencetype;
	private String defencetypename;
	private int devicetype;
	private String devicetypename;
	private String masterid;
	private int state;
	private String statename;


	public String getDefenceid() {
		return defenceid;
	}

	public void setDefenceid(String defenceid) {
		this.defenceid = defenceid;
	}

	public String getDefencecode() {
		return defencecode;
	}

	public void setDefencecode(String defencecode) {
		this.defencecode = defencecode;
	}

	public String getDefencename() {
		return defencename;
	}

	public void setDefencename(String defencename) {
		this.defencename = defencename;
	}

	public int getDefencetype() {
		return defencetype;
	}

	public void setDefencetype(int defencetype) {
		this.defencetype = defencetype;
	}

	public String getDefencetypename() {
		return defencetypename;
	}

	public void setDefencetypename(String defencetypename) {
		this.defencetypename = defencetypename;
	}

	public int getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(int devicetype) {
		this.devicetype = devicetype;
	}

	public String getDevicetypename() {
		return devicetypename;
	}

	public void setDevicetypename(String devicetypename) {
		this.devicetypename = devicetypename;
	}

	public String getMasterid() {
		return masterid;
	}

	public void setMasterid(String masterid) {
		this.masterid = masterid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStatename() {
		return statename;
	}

	public void setStatename(String statename) {
		this.statename = statename;
	}

	@Override
	public String toString() {
		return "DefencesDevice{" +
				"defenceid='" + defenceid + '\'' +
				", defencecode='" + defencecode + '\'' +
				", defencename='" + defencename + '\'' +
				", defencetype=" + defencetype +
				", defencetypename='" + defencetypename + '\'' +
				", devicetype=" + devicetype +
				", devicetypename='" + devicetypename + '\'' +
				", masterid='" + masterid + '\'' +
				", state=" + state +
				", statename='" + statename + '\'' +
				'}';
	}
}