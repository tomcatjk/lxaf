package com.jeeplus.modules.lu.entity;

/**
 * Created by buwan on 2017/5/26.
 */
public class AlarmsInfo {
    /*
    SELECT
        a.AID AS aid,
        d.`name` AS aname,
        a.State AS state,
        a.AlarmTime AS alarmTime,
        c.`Name` AS cname,
        c.Address AS address,
        c.Point AS point
    FROM
        `alarms` a
    LEFT JOIN masters m ON m.MID = a.MasterID
    LEFT JOIN devices d ON d.did = a.DeviceId
    LEFT JOIN customers c ON c.CID = a.CustomerID
    WHERE
        a.State = '1';
     */
    private String aid;
    private String dname;
    private String state;
    private String alarmTime;
    private String cname;
    private String address;
    private String point;
    private String stateName;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
