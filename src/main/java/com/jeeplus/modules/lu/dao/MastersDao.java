/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.dao;

import com.github.abel533.echarts.style.LineStyle;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.lu.entity.Masters;

import java.util.List;
import java.util.Map;

/**
 * 主机表DAO接口
 * @author 陆华捷  
 * @version 2017-04-27
 */
@MyBatisDao
public interface MastersDao extends CrudDao<Masters> {
    /**
     * 修改页面的内容
     */
    Masters findOneMaters(String mid);

    List getMastersListByCid(String cid);

    List findPageByCustomerid(Masters masters);

    void updateByMid(Masters masters);

    void updatemasters(Map map);
}