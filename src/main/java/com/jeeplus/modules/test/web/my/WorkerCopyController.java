/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.web.my;

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
import com.jeeplus.modules.test.entity.my.WorkerCopy;
import com.jeeplus.modules.test.service.my.WorkerCopyService;

/**
 * 人员管理2Controller
 * @author khb
 * @version 2017-04-17
 */
@Controller
@RequestMapping(value = "${adminPath}/test/my/workerCopy")
public class WorkerCopyController extends BaseController {

	@Autowired
	private WorkerCopyService workerCopyService;
	
	@ModelAttribute
	public WorkerCopy get(@RequestParam(required=false) String id) {
		WorkerCopy entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workerCopyService.get(id);
		}
		if (entity == null){
			entity = new WorkerCopy();
		}
		return entity;
	}
	
	/**
	 * 人员管理2列表页面
	 */
	@RequiresPermissions("test:my:workerCopy:list")
	@RequestMapping(value = {"list", ""})
	public String list(WorkerCopy workerCopy, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WorkerCopy> page = workerCopyService.findPage(new Page<WorkerCopy>(request, response), workerCopy); 
		model.addAttribute("page", page);
		return "modules/test/my/workerCopyList";
	}

	/**
	 * 查看，增加，编辑人员管理2表单页面
	 */
	@RequiresPermissions(value={"test:my:workerCopy:view","test:my:workerCopy:add","test:my:workerCopy:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WorkerCopy workerCopy, Model model) {
		model.addAttribute("workerCopy", workerCopy);
		return "modules/test/my/workerCopyForm";
	}

	/**
	 * 保存人员管理2
	 */
	@RequiresPermissions(value={"test:my:workerCopy:add","test:my:workerCopy:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WorkerCopy workerCopy, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, workerCopy)){
			return form(workerCopy, model);
		}
		if(!workerCopy.getIsNewRecord()){//编辑表单保存
			WorkerCopy t = workerCopyService.get(workerCopy.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(workerCopy, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			workerCopyService.save(t);//保存
		}else{//新增表单保存
			workerCopyService.save(workerCopy);//保存
		}
		addMessage(redirectAttributes, "保存人员管理2成功");
		return "redirect:"+Global.getAdminPath()+"/test/my/workerCopy/?repage";
	}
	
	/**
	 * 删除人员管理2
	 */
	@RequiresPermissions("test:my:workerCopy:del")
	@RequestMapping(value = "delete")
	public String delete(WorkerCopy workerCopy, RedirectAttributes redirectAttributes) {
		workerCopyService.delete(workerCopy);
		addMessage(redirectAttributes, "删除人员管理2成功");
		return "redirect:"+Global.getAdminPath()+"/test/my/workerCopy/?repage";
	}
	
	/**
	 * 批量删除人员管理2
	 */
	@RequiresPermissions("test:my:workerCopy:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			workerCopyService.delete(workerCopyService.get(id));
		}
		addMessage(redirectAttributes, "删除人员管理2成功");
		return "redirect:"+Global.getAdminPath()+"/test/my/workerCopy/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:my:workerCopy:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(WorkerCopy workerCopy, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "人员管理2"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WorkerCopy> page = workerCopyService.findPage(new Page<WorkerCopy>(request, response, -1), workerCopy);
    		new ExportExcel("人员管理2", WorkerCopy.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出人员管理2记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/my/workerCopy/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:my:workerCopy:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkerCopy> list = ei.getDataList(WorkerCopy.class);
			for (WorkerCopy workerCopy : list){
				try{
					workerCopyService.save(workerCopy);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条人员管理2记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条人员管理2记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入人员管理2失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/my/workerCopy/?repage";
    }
	
	/**
	 * 下载导入人员管理2数据模板
	 */
	@RequiresPermissions("test:my:workerCopy:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "人员管理2数据导入模板.xlsx";
    		List<WorkerCopy> list = Lists.newArrayList(); 
    		new ExportExcel("人员管理2数据", WorkerCopy.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/my/workerCopy/?repage";
    }
	
	
	

}