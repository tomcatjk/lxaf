/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.jeeplus.modules.lu.entity.Recharges;
import com.jeeplus.modules.lu.service.RechargesService;

/**
 * 充值表Controller
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/lu/recharges")
public class RechargesController extends BaseController {

	@Autowired
	private RechargesService rechargesService;
	
	@ModelAttribute
	public Recharges get(@RequestParam(required=false) String id) {
		Recharges entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = rechargesService.get(id);
		}
		if (entity == null){
			entity = new Recharges();
		}
		return entity;
	}
	
	/**
	 * 记录充值信息列表页面
	 */
	@RequiresPermissions("lu:recharges:list")
	@RequestMapping(value = {"list", ""})
	public String list(Recharges recharges, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user= UserUtils.getUser();
		recharges.setUserid(user.getId());
		Page<Recharges> page = rechargesService.findPage(new Page<Recharges>(request, response), recharges); 
		model.addAttribute("page", page);
		return "modules/lu/rechargesList";
	}

	/**
	 * 查看，增加，编辑记录充值信息表单页面
	 */
	@RequiresPermissions(value={"lu:recharges:view","lu:recharges:add","lu:recharges:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Recharges recharges, Model model) {
		model.addAttribute("recharges", recharges);
		return "modules/lu/rechargesForm";
	}

	/**
	 * 保存记录充值信息
	 */
	@RequiresPermissions(value={"lu:recharges:add","lu:recharges:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Recharges recharges, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, recharges)){
			return form(recharges, model);
		}
		if(!recharges.getIsNewRecord()){//编辑表单保存
			Recharges t = rechargesService.get(recharges.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(recharges, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			rechargesService.save(t);//保存
		}else{//新增表单保存
			rechargesService.save(recharges);//保存
		}
		addMessage(redirectAttributes, "保存记录充值信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/recharges/?repage";
	}
	
	/**
	 * 删除记录充值信息
	 */
	@RequiresPermissions("lu:recharges:del")
	@RequestMapping(value = "delete")
	public String delete(Recharges recharges, RedirectAttributes redirectAttributes) {
		rechargesService.delete(recharges);
		addMessage(redirectAttributes, "删除记录充值信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/recharges/?repage";
	}
	
	/**
	 * 批量删除记录充值信息
	 */
	@RequiresPermissions("lu:recharges:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			rechargesService.delete(rechargesService.get(id));
		}
		addMessage(redirectAttributes, "删除记录充值信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/recharges/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("lu:recharges:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Recharges recharges, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			User user= UserUtils.getUser();
			recharges.setUserid(user.getId());
            String fileName = "记录充值信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Recharges> page = rechargesService.findPage(new Page<Recharges>(request, response, -1), recharges);
    		new ExportExcel("记录充值信息", Recharges.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出记录充值信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/recharges/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("lu:recharges:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Recharges> list = ei.getDataList(Recharges.class);
			for (Recharges recharges : list){
				try{
					rechargesService.save(recharges);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条记录充值信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条记录充值信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入记录充值信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/recharges/?repage";
    }
	
	/**
	 * 下载导入记录充值信息数据模板
	 */
	@RequiresPermissions("lu:recharges:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "记录充值信息数据导入模板.xlsx";
    		List<Recharges> list = Lists.newArrayList(); 
    		new ExportExcel("记录充值信息数据", Recharges.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/recharges/?repage";
    }

}