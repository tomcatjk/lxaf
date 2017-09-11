package com.jeeplus.modules.lu.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by buwan on 2017/8/7.
 */
public enum  LoginTypeName {
    LOGIN_YES("启用", 1),
    LOGIN_NO("禁用", 0);

    private String loginTypeName;
    private int loginType;

    LoginTypeName(String loginTypeName, int loginType) {
        this.loginTypeName = loginTypeName;
        this.loginType = loginType;
    }

    public String getLoginTypeName() {
        return loginTypeName;
    }

    public void setLoginTypeName(String loginTypeName) {
        this.loginTypeName = loginTypeName;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public static LoginTypeName getByType(int loginType){
        for(LoginTypeName loginTypeNameTemp : LoginTypeName.values()){
            if(loginType == loginTypeNameTemp.getLoginType()){
                return loginTypeNameTemp;
            }
        }
        return null;
    }

    public static  LoginTypeName getByName(String loginTypeName){
        for(LoginTypeName loginTypeNameTemp : LoginTypeName.values()){
            if(loginTypeName.equals(loginTypeNameTemp.getLoginTypeName())){
                return loginTypeNameTemp;
            }
        }
        return null;
    }

    public static List getLoginTypeMapList(){
        List loginTypeMapList = new ArrayList();
        int i = 0;
        for(LoginTypeName loginTypeNameTemp : LoginTypeName.values()){
            Map mapTemp = new HashMap();
            mapTemp.put("loginType", loginTypeNameTemp.getLoginType());
            mapTemp.put("loginTypeName", loginTypeNameTemp.getLoginTypeName());
            loginTypeMapList.add(i, mapTemp);
            i++;
        }
        return loginTypeMapList;
    }
}
