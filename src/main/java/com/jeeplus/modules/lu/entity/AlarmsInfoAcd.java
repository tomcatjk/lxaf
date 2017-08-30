package com.jeeplus.modules.lu.entity;

/**
 * Created by BUWAN on 2017/5/26.
 */
public class AlarmsInfoAcd {
//    列表显示，客户名称，联系电话，报警防区，报警类型，报警时间，操作列（显示处理）
//    SELECT
//    a.AID AS aid,
//    c.`Name` AS cname,
//    c.Phone AS phone,
//    d.`Name` AS dname,
//    a.AlarmType AS alarmType,
//    a.AlarmTime AS alarmTime,
//    c.Point AS point,
//    a.State AS state
//            FROM
//	`alarms` a
//    LEFT JOIN customers c ON c.CID = a.CustomerID
//    LEFT JOIN defences d ON d.DID = a.DefenceID
    private String aid;
    private String cname;
    private String phone;
    private String dname;
    private String alarmType;
    private String alarmTime;
    private String point;
    private String state;
    private String recordTime;
    private String disarmState;
    private String serviceName;
    private String alarmTypeName;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getDisarmState() {
        return disarmState;
    }

    public void setDisarmState(String disarmState) {
        this.disarmState = disarmState;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
