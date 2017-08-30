/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.service;

import java.util.List;
import java.util.Map;

import com.jeeplus.modules.lu.entity.DefencesPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.lu.entity.Defences;
import com.jeeplus.modules.lu.dao.DefencesDao;

/**
 * 防区表Service
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Service
@Transactional(readOnly = true)
public class DefencesService extends CrudService<DefencesDao, Defences> {

	@Autowired
	DefencesDao defencesDao;

	public Defences get(String id) {
		return super.get(id);
	}
	
	public List<Defences> findList(Defences defences) {
		return super.findList(defences);
	}
	
	public Page<Defences> findPage(Page<Defences> page, Defences defences) {
		return super.findPage(page, defences);
	}
	
	@Transactional(readOnly = false)
	public void save(Defences defences) {
		super.save(defences);
	}
	
	@Transactional(readOnly = false)
	public void delete(Defences defences) {
		super.delete(defences);
	}

	@Transactional(readOnly = false)
	public void saveOfNoCheck(Defences defences) {
		dao.insert(defences);
	}

	@Transactional(readOnly = false)
	public void updateOfNoCheck(Defences defences) {
		dao.update(defences);
	}

	@Transactional(readOnly = false)
	public List findDefencesListByMasterId(String masterId) {
		return dao.getDefencesListByMasterId(masterId);
	}

	@Transactional(readOnly = false)
	public DefencesPart findDefencesbyDid(String did) {
		return dao.getDefencesbyDid(did);
	}

	@Transactional(readOnly = false)
	public void update(Map map){defencesDao.updatedefences(map);};
	
}