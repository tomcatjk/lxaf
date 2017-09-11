package com.jeeplus.modules.lu.entity;



import com.jeeplus.common.persistence.DataEntity;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/4.
 */
public class AlarmsDefences extends DataEntity<AlarmsDefences> {
   /* //报警表id
    private int aid;*/
    //客户名
    private String customersName;
    //防区名
    private String defencesName;
    //报警类型
    private String typeName;
    //报警时间
    private Date date;
    //处理结果
    private String state;
    //备注
    private String remark;

    private String startTime;
    private String endTime;

    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

   /* public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }*/

    public String getCustomersName() {
        return customersName;
    }

    public void setCustomersName(String customersName) {
        this.customersName = customersName;
    }

    public String getDefencesName() {
        return defencesName;
    }

    public void setDefencesName(String defencesName) {
        this.defencesName = defencesName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
