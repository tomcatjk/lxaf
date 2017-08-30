/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 测试生成代码Entity
 * @author 赵林华
 * @version 2017-03-13
 */
public class Ectechtest extends DataEntity<Ectechtest> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// name
	private String sex;		// sex
	
	public Ectechtest() {
		super();
	}
	
	public Ectechtest(String id){
		super(id);
	}
	
	@ExcelField(title="name", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="sex", align=2, sort=2)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
}