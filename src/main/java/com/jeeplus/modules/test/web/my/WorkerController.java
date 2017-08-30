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
import com.jeeplus.modules.test.entity.my.Worker;
import com.jeeplus.modules.test.service.my.WorkerService;

/**
 * 人员管理Controller
 * @author khb
 * @version 2017-04-14
 */
@Controller
@RequestMapping(value = "${adminPath}/test/my/worker")
public class WorkerController extends BaseController {

	@Autowired
	private WorkerService workerService;
	
	@ModelAttribute
	public Worker get(@RequestParam(required=false) String id) {
		Worker entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workerService.get(id);
		}
		if (entity == null){
			entity = new Worker();
		}
		return entity;
	}
	
	/**
	 * 人员管理列表页面
	 */
	@RequiresPermissions("test:my:worker:list")
	@RequestMapping(value = {"list", ""})
	public String list(Worker worker, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Worker> page = workerService.findPage(new Page<Worker>(request, response), worker); 
		model.addAttribute("page", page);
		return "modules/test/my/workerList";
	}

	/**
	 * 查看，增加，编辑人员管理表单页面
	 */
	@RequiresPermissions(value={"test:my:worker:view","test:my:worker:add","test:my:worker:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Worker worker, Model model) {
		model.addAttribute("worker", worker);
		return "modules/test/my/workerForm";
	}

    @RequiresPermissions(value={"test:my:worker:view","test:my:worker:add","test:my:worker:edit"},logical=Logical.OR)
    @RequestMapping(value = "form2")
    public String form2(Worker worker, Model model) {
        model.addAttribute("worker", worker);
        model.addAttribute("isEdit", "isEdit");
        return "modules/test/my/workerForm";
    }

	/**
	 * 保存人员管理
	 */
	@RequiresPermissions(value={"test:my:worker:add","test:my:worker:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Worker worker, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, worker)){
			return form(worker, model);
		}
		if(!worker.getIsNewRecord()){//编辑表单保存
			Worker t = workerService.get(worker.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(worker, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			workerService.save(t);//保存
		}else{//新增表单保存
			workerService.save(worker);//保存
		}
		addMessage(redirectAttributes, "保存人员管理成功");
		return "redirect:"+Global.getAdminPath()+"/test/my/worker/?repage";
	}

	/**
	 * 验证手机号是否唯一
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "verifyPhone")
	@ResponseBody
	public String verifyPhone(String phone){
		Worker worker = workerService.findUniqueByProperty("phone",phone);
		if(worker != null){
			return "success";
		}
		return "false";
	}
	
	/**
	 * 删除人员管理
	 */
	@RequiresPermissions("test:my:worker:del")
	@RequestMapping(value = "delete")
	public String delete(Worker worker, RedirectAttributes redirectAttributes) {
		workerService.delete(worker);
		addMessage(redirectAttributes, "删除人员管理成功");
		return "redirect:"+Global.getAdminPath()+"/test/my/worker/?repage";
	}
	
	/**
	 * 批量删除人员管理
	 */
	@RequiresPermissions("test:my:worker:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			workerService.delete(workerService.get(id));
		}
		addMessage(redirectAttributes, "删除人员管理成功");
		return "redirect:"+Global.getAdminPath()+"/test/my/worker/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:my:worker:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Worker worker, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "人员管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Worker> page = workerService.findPage(new Page<Worker>(request, response, -1), worker);
    		new ExportExcel("人员管理", Worker.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出人员管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/my/worker/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:my:worker:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Worker> list = ei.getDataList(Worker.class);
			for (Worker worker : list){
				try{
					workerService.save(worker);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条人员管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条人员管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入人员管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/my/worker/?repage";
    }
	
	/**
	 * 下载导入人员管理数据模板
	 */
	@RequiresPermissions("test:my:worker:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "人员管理数据导入模板.xlsx";
    		List<Worker> list = Lists.newArrayList(); 
    		new ExportExcel("人员管理数据", Worker.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/my/worker/?repage";
    }
	
	
	

}