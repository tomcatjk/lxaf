package com.jeeplus.modules.lu.entity;

/**
 * Created by buwan on 2017/6/19.
 */
public enum DefenceTypeName {
    WARNING1("禁止", 1),
    WARNING2("瞬时防区", 2),
    WARNING3("延时防区", 3),
    WARNING4("留守防区", 4),
    WARNING5("紧急防区", 5),
    WARNING6("火警", 6),
    WARNING7("水浸", 7),
    WARNING8("温感", 8),
    WARNING9("个人救助", 9),
    WARNING10("门禁", 10),
    WARNING11("门铃", 11);


    private String defenceTypeName;
    private int defenceType;

    DefenceTypeName() {
    }

    DefenceTypeName(String defenceTypeName, int defenceType) {
        this.defenceTypeName = defenceTypeName;
        this.defenceType = defenceType;
    }

    public String getDefenceTypeName() {
        return defenceTypeName;
    }

    public void setDefenceTypeName(String defenceTypeName) {
        this.defenceTypeName = defenceTypeName;
    }

    public int getDefenceType() {
        return defenceType;
    }

    public void setDefenceType(int defenceType) {
        this.defenceType = defenceType;
    }

    public static DefenceTypeName getByType(int defenceType){
        for(DefenceTypeName defenceTypeNameObject : DefenceTypeName.values()){
            if(defenceType == defenceTypeNameObject.getDefenceType()){
                return defenceTypeNameObject;
            }
        }
        return null;
    }

    public static DefenceTypeName getByName(String defenceTypeName){
        for(DefenceTypeName defenceTypeNameObject : DefenceTypeName.values()){
            if(defenceTypeName.equals(defenceTypeNameObject.getDefenceTypeName())){
                return defenceTypeNameObject;
            }
        }
        return null;
    }
}
