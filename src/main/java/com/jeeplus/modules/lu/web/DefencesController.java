/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.lu.entity.DefencesPart;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import com.jeeplus.modules.lu.entity.Defences;
import com.jeeplus.modules.lu.service.DefencesService;

/**
 * 防区表Controller
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/lu/defences")
public class DefencesController extends BaseController {

	@Autowired
	private DefencesService defencesService;

	@ModelAttribute
	public Defences get(@RequestParam(required=false) String id) {
		Defences entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = defencesService.get(id);
		}
		if (entity == null){
			entity = new Defences();
		}
		return entity;
	}

	@ResponseBody
	@RequestMapping(value = "findDefencesbyDid")
	public JSONObject findDefencesbyDid(String did){
		return JSONObject.fromObject(defencesService.findDefencesbyDid(did));
	}

	@ResponseBody
	@RequestMapping(value = "findDefencesListByMasterId")
	public JSONArray findDefencesListByMasterId(String masterId, Model model){
		List list = defencesService.findDefencesListByMasterId(masterId);
		return JSONArray.fromObject(list);
	}

	/**
	 * 记录防区信息列表页面
	 */
	@RequiresPermissions("lu:defences:list")
	@RequestMapping(value = {"list", ""})
	public String list(Defences defences, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		return "modules/lu/defencesList";
	}

	/**
	 * 查看，增加，编辑记录防区信息表单页面
	 */
	@RequiresPermissions(value={"lu:defences:view","lu:defences:add","lu:defences:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Defences defences, Model model) {
		model.addAttribute("defences", defences);
		return "modules/lu/defencesForm";
	}

	/**
	 * 保存记录防区信息
	 */
	@RequiresPermissions(value={"lu:defences:add","lu:defences:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Defences defences, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, defences)){
			return form(defences, model);
		}
		if(!defences.getIsNewRecord()){//编辑表单保存
			Defences t = defencesService.get(defences.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(defences, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			defencesService.save(t);//保存
		}else{//新增表单保存
			defencesService.save(defences);//保存
		}
		addMessage(redirectAttributes, "保存记录防区信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/defences/?repage";
	}
	
	/**
	 * 删除记录防区信息
	 */
	@RequiresPermissions("lu:defences:del")
	@RequestMapping(value = "delete")
	public String delete(Defences defences, RedirectAttributes redirectAttributes) {
		defencesService.delete(defences);
		addMessage(redirectAttributes, "删除记录防区信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/defences/?repage";
	}
	
	/**
	 * 批量删除记录防区信息
	 */
	@RequiresPermissions("lu:defences:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			defencesService.delete(defencesService.get(id));
		}
		addMessage(redirectAttributes, "删除记录防区信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/defences/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("lu:defences:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Defences defences, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "记录防区信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Defences> page = defencesService.findPage(new Page<Defences>(request, response, -1), defences);
    		new ExportExcel("记录防区信息", Defences.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出记录防区信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/defences/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("lu:defences:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Defences> list = ei.getDataList(Defences.class);
			for (Defences defences : list){
				try{
					defencesService.save(defences);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条记录防区信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条记录防区信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入记录防区信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/defences/?repage";
    }
	
	/**
	 * 下载导入记录防区信息数据模板
	 */
	@RequiresPermissions("lu:defences:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "记录防区信息数据导入模板.xlsx";
    		List<Defences> list = Lists.newArrayList(); 
    		new ExportExcel("记录防区信息数据", Defences.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/defences/?repage";
    }
	
	
	

}