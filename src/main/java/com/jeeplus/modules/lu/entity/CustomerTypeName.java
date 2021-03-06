package com.jeeplus.modules.lu.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by buwan on 2017/7/25.
 */
public enum CustomerTypeName {
    TYPE1("企业客户", 1),
    TYPE2("个体客户", 2),
    TYPE3("政府客户", 3);

    private String customerTypeName;
    private int customerType;

    CustomerTypeName(String customerTypeName, int customerType) {
        this.customerTypeName = customerTypeName;
        this.customerType = customerType;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public static CustomerTypeName getByType(int customerType){
        for(CustomerTypeName customerTypeNameObject : CustomerTypeName.values()){
            if(customerType == customerTypeNameObject.getCustomerType()){
                return customerTypeNameObject;
            }
        }
        return null;
    }

    public static CustomerTypeName getByName(String customerTypeName){
        for(CustomerTypeName customerTypeNameObject : CustomerTypeName.values()){
            if(customerTypeName.equals(customerTypeNameObject.getCustomerTypeName())){
                return customerTypeNameObject;
            }
        }
        return null;
    }

    public static List getCustomerTypeNameMapList(){
        List customerTypeNameMapList = new ArrayList();
        int i = 0;
        for(CustomerTypeName customerTypeNameTemp : CustomerTypeName.values()){
            Map mapTemp = new HashMap();
            mapTemp.put("customerType", customerTypeNameTemp.getCustomerType());
            mapTemp.put("customerTypeName", customerTypeNameTemp.getCustomerTypeName());
            customerTypeNameMapList.add(i, mapTemp);
            i++;
        }
        return customerTypeNameMapList;
    }

}
