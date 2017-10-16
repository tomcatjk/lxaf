package com.jeeplus.modules.lu.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by buwan on 2017/6/20.
 */
public enum DeviceStateName {
    STARTUSING("启用", 1), BLOCKUP("停用", 0);

    private String deviceStateName;
    private int deviceState;

    DeviceStateName() {
    }

    DeviceStateName(String deviceStateName, int deviceState) {
        this.deviceStateName = deviceStateName;
        this.deviceState = deviceState;
    }

    public String getDeviceStateName() {
        return deviceStateName;
    }

    public void setDeviceStateName(String deviceStateName) {
        this.deviceStateName = deviceStateName;
    }

    public int getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }

    public static DeviceStateName getByState(int deviceState){
        for(DeviceStateName deviceStateNameObject : DeviceStateName.values()){
            if(deviceState == deviceStateNameObject.getDeviceState()){
                return deviceStateNameObject;
            }
        }
        return null;
    }

    public static DeviceStateName getByName(String deviceStateName){
        for(DeviceStateName deviceStateNameObject : DeviceStateName.values()){
            if(deviceStateName.equals(deviceStateNameObject.getDeviceStateName())){
                return deviceStateNameObject;
            }
        }
        return null;
    }

    public static List getDeviceStateMapList(){
        List deviceStateNameMapList = new ArrayList();
        int i = 0;
        for(DeviceStateName deviceStateNameTemp : DeviceStateName.values()){
            Map mapTemp = new HashMap();
            mapTemp.put("deviceState", deviceStateNameTemp.getDeviceState());
            mapTemp.put("deviceStateName", deviceStateNameTemp.getDeviceStateName());
            deviceStateNameMapList.add(i, mapTemp);
            i++;
        }
        return deviceStateNameMapList;
    }
}
