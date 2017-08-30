/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.web;

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
import com.jeeplus.modules.test.entity.Ectechtest;
import com.jeeplus.modules.test.service.EctechtestService;

/**
 * 测试生成代码Controller
 * @author 赵林华
 * @version 2017-03-13
 */
@Controller
@RequestMapping(value = "${adminPath}/test/ectechtest")
public class EctechtestController extends BaseController {

	@Autowired
	private EctechtestService ectechtestService;
	
	@ModelAttribute
	public Ectechtest get(@RequestParam(required=false) String id) {
		Ectechtest entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ectechtestService.get(id);
		}
		if (entity == null){
			entity = new Ectechtest();
		}
		return entity;
	}
	
	/**
	 * 测试列表页面
	 */
	@RequiresPermissions("test:ectechtest:list")
	@RequestMapping(value = {"list", ""})
	public String list(Ectechtest ectechtest, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Ectechtest> page = ectechtestService.findPage(new Page<Ectechtest>(request, response), ectechtest); 
		model.addAttribute("page", page);
		return "modules/test/ectechtestList";
	}

	/**
	 * 查看，增加，编辑测试表单页面
	 */
	@RequiresPermissions(value={"test:ectechtest:view","test:ectechtest:add","test:ectechtest:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Ectechtest ectechtest, Model model) {
		model.addAttribute("ectechtest", ectechtest);
		return "modules/test/ectechtestForm";
	}

	/**
	 * 保存测试
	 */
	@RequiresPermissions(value={"test:ectechtest:add","test:ectechtest:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Ectechtest ectechtest, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, ectechtest)){
			return form(ectechtest, model);
		}
		if(!ectechtest.getIsNewRecord()){//编辑表单保存
			Ectechtest t = ectechtestService.get(ectechtest.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(ectechtest, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			ectechtestService.save(t);//保存
		}else{//新增表单保存
			ectechtestService.save(ectechtest);//保存
		}
		addMessage(redirectAttributes, "保存测试成功");
		return "redirect:"+Global.getAdminPath()+"/test/ectechtest/?repage";
	}
	
	/**
	 * 删除测试
	 */
	@RequiresPermissions("test:ectechtest:del")
	@RequestMapping(value = "delete")
	public String delete(Ectechtest ectechtest, RedirectAttributes redirectAttributes) {
		ectechtestService.delete(ectechtest);
		addMessage(redirectAttributes, "删除测试成功");
		return "redirect:"+Global.getAdminPath()+"/test/ectechtest/?repage";
	}
	
	/**
	 * 批量删除测试
	 */
	@RequiresPermissions("test:ectechtest:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			ectechtestService.delete(ectechtestService.get(id));
		}
		addMessage(redirectAttributes, "删除测试成功");
		return "redirect:"+Global.getAdminPath()+"/test/ectechtest/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:ectechtest:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Ectechtest ectechtest, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "测试"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Ectechtest> page = ectechtestService.findPage(new Page<Ectechtest>(request, response, -1), ectechtest);
    		new ExportExcel("测试", Ectechtest.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出测试记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/ectechtest/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:ectechtest:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Ectechtest> list = ei.getDataList(Ectechtest.class);
			for (Ectechtest ectechtest : list){
				try{
					ectechtestService.save(ectechtest);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条测试记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条测试记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入测试失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/ectechtest/?repage";
    }
	
	/**
	 * 下载导入测试数据模板
	 */
	@RequiresPermissions("test:ectechtest:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "测试数据导入模板.xlsx";
    		List<Ectechtest> list = Lists.newArrayList(); 
    		new ExportExcel("测试数据", Ectechtest.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/ectechtest/?repage";
    }
	
	
	

}