package com.jeeplus.modules.lu.entity;

/**
 * Created by 63276 on 2017/6/7.
 */
public class UserTemp {
    private String id;
    private String name;

    public UserTemp(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
