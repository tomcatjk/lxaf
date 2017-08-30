/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.jeeplus.modules.lu.entity.Afcustomers;
import com.jeeplus.modules.lu.service.AfcustomersService;

/**
 * AFCustomers Controller
 * @author Khb
 * @version 2017-06-16
 */
@Controller
@RequestMapping(value = "${adminPath}/lu/afcustomers")
public class AfcustomersController extends BaseController {

	@Autowired
	private AfcustomersService afcustomersService;
	
	@ModelAttribute
	public Afcustomers get(@RequestParam(required=false) String id) {
		Afcustomers entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = afcustomersService.get(id);
		}
		if (entity == null){
			entity = new Afcustomers();
		}
		return entity;
	}
	
	/**
	 * AFCustomers 列表页面
	 */
	@RequiresPermissions("lu:afcustomers:list")
	@RequestMapping(value = {"list", ""})
	public String list(Afcustomers afcustomers, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Afcustomers> page = afcustomersService.findPage(new Page<Afcustomers>(request, response), afcustomers); 
		model.addAttribute("page", page);
		return "modules/lu/afcustomersList";
	}

	/**
	 * 查看，增加，编辑AFCustomers 表单页面
	 */
	@RequiresPermissions(value={"lu:afcustomers:view","lu:afcustomers:add","lu:afcustomers:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Afcustomers afcustomers, Model model) {
		model.addAttribute("afcustomers", afcustomers);
		return "modules/lu/afcustomersForm";
	}

	/**
	 * 保存AFCustomers 
	 */
	@RequiresPermissions(value={"lu:afcustomers:add","lu:afcustomers:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Afcustomers afcustomers, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, afcustomers)){
			return form(afcustomers, model);
		}
		if(!afcustomers.getIsNewRecord()){//编辑表单保存
			Afcustomers t = afcustomersService.get(afcustomers.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(afcustomers, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			afcustomersService.save(t);//保存
		}else{//新增表单保存
			afcustomersService.save(afcustomers);//保存
		}
		addMessage(redirectAttributes, "保存AFCustomers 成功");
		return "redirect:"+Global.getAdminPath()+"/lu/afcustomers/?repage";
	}
	
	/**
	 * 删除AFCustomers 
	 */
	@RequiresPermissions("lu:afcustomers:del")
	@RequestMapping(value = "delete")
	public String delete(Afcustomers afcustomers, RedirectAttributes redirectAttributes) {
		afcustomersService.delete(afcustomers);
		addMessage(redirectAttributes, "删除AFCustomers 成功");
		return "redirect:"+Global.getAdminPath()+"/lu/afcustomers/?repage";
	}
	
	/**
	 * 批量删除AFCustomers 
	 */
	@RequiresPermissions("lu:afcustomers:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			afcustomersService.delete(afcustomersService.get(id));
		}
		addMessage(redirectAttributes, "删除AFCustomers 成功");
		return "redirect:"+Global.getAdminPath()+"/lu/afcustomers/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("lu:afcustomers:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Afcustomers afcustomers, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "AFCustomers "+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Afcustomers> page = afcustomersService.findPage(new Page<Afcustomers>(request, response, -1), afcustomers);
    		new ExportExcel("AFCustomers ", Afcustomers.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出AFCustomers 记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/afcustomers/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("lu:afcustomers:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Afcustomers> list = ei.getDataList(Afcustomers.class);
			for (Afcustomers afcustomers : list){
				try{
					afcustomersService.save(afcustomers);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条AFCustomers 记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条AFCustomers 记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入AFCustomers 失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/afcustomers/?repage";
    }
	
	/**
	 * 下载导入AFCustomers 数据模板
	 */
	@RequiresPermissions("lu:afcustomers:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "AFCustomers 数据导入模板.xlsx";
    		List<Afcustomers> list = Lists.newArrayList(); 
    		new ExportExcel("AFCustomers 数据", Afcustomers.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/afcustomers/?repage";
    }
	
	
	

}