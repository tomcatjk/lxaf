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
import com.jeeplus.modules.lu.entity.ServiceRecords;
import com.jeeplus.modules.lu.service.ServiceRecordsService;

/**
 * 服务记录Controller
 * @author khb
 * @version 2017-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/lu/serviceRecords")
public class ServiceRecordsController extends BaseController {

	@Autowired
	private ServiceRecordsService serviceRecordsService;
	
	@ModelAttribute
	public ServiceRecords get(@RequestParam(required=false) String id) {
		ServiceRecords entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = serviceRecordsService.get(id);
		}
		if (entity == null){
			entity = new ServiceRecords();
		}
		return entity;
	}
	
	/**
	 * 服务记录列表页面
	 */
	@RequiresPermissions("lu:serviceRecords:list")
	@RequestMapping(value = {"list", ""})
	public String list(ServiceRecords serviceRecords, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ServiceRecords> page = serviceRecordsService.findPage(new Page<ServiceRecords>(request, response), serviceRecords); 
		model.addAttribute("page", page);
		return "modules/lu/serviceRecordsList";
	}

	/**
	 * 查看，增加，编辑服务记录表单页面
	 */
	@RequiresPermissions(value={"lu:serviceRecords:view","lu:serviceRecords:add","lu:serviceRecords:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ServiceRecords serviceRecords, Model model) {
		model.addAttribute("serviceRecords", serviceRecords);
		return "modules/lu/serviceRecordsForm";
	}

	/**
	 * 保存服务记录
	 */
	@RequiresPermissions(value={"lu:serviceRecords:add","lu:serviceRecords:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ServiceRecords serviceRecords, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, serviceRecords)){
			return form(serviceRecords, model);
		}
		if(!serviceRecords.getIsNewRecord()){//编辑表单保存
			ServiceRecords t = serviceRecordsService.get(serviceRecords.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(serviceRecords, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			serviceRecordsService.save(t);//保存
		}else{//新增表单保存
			serviceRecordsService.save(serviceRecords);//保存
		}
		addMessage(redirectAttributes, "保存服务记录成功");
		return "redirect:"+Global.getAdminPath()+"/lu/serviceRecords/?repage";
	}
	
	/**
	 * 删除服务记录
	 */
	@RequiresPermissions("lu:serviceRecords:del")
	@RequestMapping(value = "delete")
	public String delete(ServiceRecords serviceRecords, RedirectAttributes redirectAttributes) {
		serviceRecordsService.delete(serviceRecords);
		addMessage(redirectAttributes, "删除服务记录成功");
		return "redirect:"+Global.getAdminPath()+"/lu/serviceRecords/?repage";
	}
	
	/**
	 * 批量删除服务记录
	 */
	@RequiresPermissions("lu:serviceRecords:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			serviceRecordsService.delete(serviceRecordsService.get(id));
		}
		addMessage(redirectAttributes, "删除服务记录成功");
		return "redirect:"+Global.getAdminPath()+"/lu/serviceRecords/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("lu:serviceRecords:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(ServiceRecords serviceRecords, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "服务记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ServiceRecords> page = serviceRecordsService.findPage(new Page<ServiceRecords>(request, response, -1), serviceRecords);
    		new ExportExcel("服务记录", ServiceRecords.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出服务记录记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/serviceRecords/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("lu:serviceRecords:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ServiceRecords> list = ei.getDataList(ServiceRecords.class);
			for (ServiceRecords serviceRecords : list){
				try{
					serviceRecordsService.save(serviceRecords);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条服务记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条服务记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入服务记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/serviceRecords/?repage";
    }
	
	/**
	 * 下载导入服务记录数据模板
	 */
	@RequiresPermissions("lu:serviceRecords:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "服务记录数据导入模板.xlsx";
    		List<ServiceRecords> list = Lists.newArrayList(); 
    		new ExportExcel("服务记录数据", ServiceRecords.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/serviceRecords/?repage";
    }
}