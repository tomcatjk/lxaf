package com.jeeplus.modules.lu.entity;

/**
 * Created by buwan on 2017/6/20.
 */
public enum AlarmStateName {
    STATE1("待处理", 1),
    STATE2("已处理", 2),
    STATE3("已解除", 3);

    private String alarmStateName;
    private int alarmState;

    AlarmStateName(String alarmStateName, int alarmState) {
        this.alarmStateName = alarmStateName;
        this.alarmState = alarmState;
    }

    public String getAlarmStateName() {
        return alarmStateName;
    }

    public void setAlarmStateName(String alarmStateName) {
        this.alarmStateName = alarmStateName;
    }

    public int getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(int alarmState) {
        this.alarmState = alarmState;
    }

    public static AlarmStateName getByState(int alarmState){
        for(AlarmStateName alarmStateNameObject : AlarmStateName.values()){
            if(alarmState == alarmStateNameObject.getAlarmState()){
                return alarmStateNameObject;
            }
        }
        return null;
    }

    public static AlarmStateName getByName(String alarmStateName){
        for(AlarmStateName alarmStateNameObject : AlarmStateName.values()){
            if(alarmStateName.equals(alarmStateNameObject.getAlarmStateName())){
                return alarmStateNameObject;
            }
        }
        return null;
    }
}
