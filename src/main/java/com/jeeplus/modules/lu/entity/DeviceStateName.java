package com.jeeplus.modules.lu.entity;

/**
 * Created by buwan on 2017/6/20.
 */
public enum DeviceStateName {
    STARTUSING("启用", 1), BLOCKUP("停用", 2);

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
}
