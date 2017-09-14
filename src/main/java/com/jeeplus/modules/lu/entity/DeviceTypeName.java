package com.jeeplus.modules.lu.entity;

/**
 * Created by buwan on 2017/6/20.
 */
public enum DeviceTypeName {
    DEVICETYPE1("红外设备", 1),
    DEVICETYPE2("烟感设备", 2),
    DEVICETYPE3("门磁设备", 3),
    DEVICETYPE4("遥控器", 4),
    DEVICETYPE5("紧急按钮", 5),
    DEVICETYPE6("地涝设备", 6),
    DEVICETYPE7("天然气设备", 7),
    DEVICETYPE8("红外扩展设备", 8);


    private String deviceTypeName;
    private int deviceType;

    DeviceTypeName() {
    }

    DeviceTypeName(String deviceTypeName, int deviceType) {
        this.deviceTypeName = deviceTypeName;
        this.deviceType = deviceType;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public static DeviceTypeName getByType(int deviceType){
        for(DeviceTypeName deviceTypeNameObject : DeviceTypeName.values()){
            if(deviceType == deviceTypeNameObject.getDeviceType()){
                return deviceTypeNameObject;
            }
        }
        return null;
    }

    public static DeviceTypeName getByName(String deviceTypeName){
        for(DeviceTypeName deviceTypeNameObject : DeviceTypeName.values()){
            if(deviceTypeName.equals(deviceTypeNameObject.getDeviceTypeName())){
                return deviceTypeNameObject;
            }
        }
        return null;
    }
}
