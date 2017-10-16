/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.lu.entity.Defences;
import com.jeeplus.modules.lu.entity.DeviceStateName;
import com.jeeplus.modules.lu.service.AlarmsService;
import com.jeeplus.modules.lu.service.DefencesService;
import com.jeeplus.modules.lu.service.DevicesService;
import net.sf.json.JSONArray;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.lu.entity.Masters;
import com.jeeplus.modules.lu.service.MastersService;

/**
 * 主机表Controller
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/lu/masters")
public class MastersController extends BaseController {

	@Autowired
	private MastersService mastersService;

	@Autowired
	private DefencesService defencesService;

	@Autowired
	private DevicesService devicesService;

	@Autowired
	private AlarmsService alarmsService;

	@ModelAttribute
	public Masters get(@RequestParam(required=false) String id) {
		Masters entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mastersService.get(id);
		}
		if (entity == null){
			entity = new Masters();
		}
		return entity;
	}

	@ResponseBody
	@RequestMapping(value = "findMastersListByCid")
	public JSONArray findMastersListByCid(String cid){
		return JSONArray.fromObject(mastersService.findMastersListByCid(cid));
	}

	/**
	 * 主机信息列表页面
	 */
	@RequiresPermissions("lu:masters:list")
	@RequestMapping(value = {"list", ""})
	public String list(Masters masters, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Masters> page = mastersService.findPageByCustomerid(new Page<Masters>(request, response), masters);
		model.addAttribute("customerid",masters.getCustomerid());
		model.addAttribute("page", page);
		return "modules/lu/mastersList";
	}

	/**
	 * 查看，增加，编辑主机信息表单页面
	 */
	@RequiresPermissions(value={"lu:masters:view","lu:masters:add","lu:masters:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Masters masters,String masterFlag, Model model) {
		if(masters.getMid()!=null){
			masters = mastersService.findUniqueByProperty("mid", masters.getMid());
		}
		List deviceStateMapList = DeviceStateName.getDeviceStateMapList();
		model.addAttribute("deviceStateMapList", deviceStateMapList);
		model.addAttribute("masters", masters);
		model.addAttribute("masterFlag",masterFlag); //注意第126行参数有变化
		return "modules/lu/mastersForm";
	}

	@RequestMapping(value = "mastersStateNameMap")
	@ResponseBody
	public JSONArray getMastersStateNameMap(){
		List list = new ArrayList();
		int i = 0;
		for(DeviceStateName deviceStateName : DeviceStateName.values()){
			Map mastersStateNameMap = new HashMap();
			mastersStateNameMap.put("state", String.valueOf(deviceStateName.getDeviceState()));
			mastersStateNameMap.put("name", deviceStateName.getDeviceStateName());
			list.add(i, mastersStateNameMap);
			i++;
		}
		return JSONArray.fromObject(list);
	}

	/**
	 * 保存主机信息
	 */
	@RequiresPermissions(value={"lu:masters:add","lu:masters:edit"},logical=Logical.OR)
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(Masters masters) throws Exception{
		return saveMaster(masters);
	}

	public String saveMaster(Masters masters) throws Exception {
		if(masters.getCustomerid() != null && masters.getCustomerid().contains(",")){
			masters.setCustomerid(masters.getCustomerid().split(",")[1]);
		}
		if(masters.getMid() != null && masters.getMid().length() != 0){
			Masters mastersTemp = mastersService.findUniqueByProperty("mid", masters.getMid());
			mastersTemp.setCode(masters.getCode());
			mastersTemp.setName(masters.getName());
			mastersTemp.setSim(masters.getSim());
			mastersTemp.setState(masters.getState());
			mastersTemp.setMaintype(masters.getMaintype());
			mastersTemp.setVersion(masters.getVersion());
			mastersService.updateByMid(mastersTemp);
			return masters.getCustomerid();
		}else{
			if(masters.getState().contains(",")) {
				masters.setState(masters.getState().split(",")[1]);
			}else {
				masters.setState(masters.getState());
			}
			masters.setId(masters.getMid());
			if(!masters.getIsNewRecord()){//编辑表单保存
				Masters t = mastersService.get(masters.getId());//从数据库取出的值
				MyBeanUtils.copyBeanNotNull2Bean(masters, t);//将编辑表单中的非NULL值覆盖数据库中的值
				mastersService.save(t);//保存
			}else{//新增表单保存
				masters.setMid(UUID.randomUUID().toString());
				masters.setIsOnline("0");
				masters.setDisarmState("0");
				mastersService.save(masters);//保存
				Defences defences = new Defences();
				defences.setCustomerid(masters.getCustomerid());
				defences.setMasterid(masters.getMid());
				defences.setState(1);
				for(int i = 0; i < 32; i++){
					defences.setDid(UUID.randomUUID().toString());
					if(i<9){
						String temp = "0" + (i+1);
						defences.setCode(temp);
						defences.setName(temp);
					}else{
						defences.setCode(String.valueOf(i+1));
						defences.setName(String.valueOf(i+1));
					}
					defencesService.saveOfNoCheck(defences);
				}
			}
		}
		return masters.getCustomerid();
	}
	
	/**
	 * 删除主机信息
	 */
	@RequiresPermissions("lu:masters:del")
	@RequestMapping(value = "delete")
	public String delete(Masters masters, RedirectAttributes redirectAttributes) {
		delMaster(masters);
		addMessage(redirectAttributes, "删除主机信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/masters/?repage&customerid=" + masters.getCustomerid();
	}

	public void delMaster(Masters master){
		mastersService.delete(master);
		devicesService.deleteByMaster(master);
		alarmsService.deleteByMasterId(master);
	}
	
	/**
	 * 批量删除主机信息
	 */
	@RequiresPermissions("lu:masters:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		Masters masterTemp = new Masters();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			masterTemp = mastersService.findUniqueByProperty("mid", id);
			delMaster(masterTemp);
		}
		addMessage(redirectAttributes, "删除主机信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/masters/?repage&customerid=" + masterTemp.getCustomerid();
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("lu:masters:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Masters masters, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "主机信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Masters> page = mastersService.findPage(new Page<Masters>(request, response, -1), masters);
    		new ExportExcel("主机信息", Masters.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出主机信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/masters/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("lu:masters:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Masters> list = ei.getDataList(Masters.class);
			for (Masters masters : list){
				try{
					mastersService.save(masters);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条主机信息。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条主机信息"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入主机信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/masters/?repage";
    }
	
	/**
	 * 下载导入主机信息数据模板
	 */
	@RequiresPermissions("lu:masters:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "主机信息数据导入模板.xlsx";
    		List<Masters> list = Lists.newArrayList(); 
    		new ExportExcel("主机信息数据", Masters.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/masters/?repage";
    }

	@RequestMapping(value = "masterExist")
	@ResponseBody
	public String masterExist(Masters masters){
		Masters mastersTemp = mastersService.findUniqueByProperty("code", masters.getCode());
		if(mastersTemp != null){
			return "exist";
		}else{
			return "notExist";
		}
	}

	@RequestMapping(value = "simExist")
	@ResponseBody
	public String simExist(Masters masters){
		Masters mastersTemp = mastersService.findUniqueByProperty("sim", masters.getSim());
		if(mastersTemp != null){
			return "exist";
		}else{
			return "notExist";
		}
	}
}