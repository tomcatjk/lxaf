/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.lu.entity.Areas;
import com.jeeplus.modules.lu.service.AreasService;
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
import com.jeeplus.modules.lu.entity.RoleArea;
import com.jeeplus.modules.lu.service.RoleAreaService;

/**
 * 区域权限Controller
 * @author khb
 * @version 2017-08-30
 */
@Controller
@RequestMapping(value = "${adminPath}/lu/roleArea")
public class RoleAreaController extends BaseController {

	@Autowired
	private RoleAreaService roleAreaService;

	@Autowired
	private AreasService areasService;
	
	@ModelAttribute
	public RoleArea get(@RequestParam(required=false) String id) {
		RoleArea entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = roleAreaService.get(id);
		}
		if (entity == null){
			entity = new RoleArea();
		}
		return entity;
	}
	
	/**
	 * 区域权限列表页面
	 */
	@RequiresPermissions("lu:roleArea:list")
	@RequestMapping(value = {"list", ""})
	public String list(RoleArea roleArea, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RoleArea> page = roleAreaService.findPage(new Page<RoleArea>(request, response), roleArea); 
		model.addAttribute("page", page);
		return "modules/lu/roleAreaList";
	}

	/**
	 * 查看，增加，编辑区域权限表单页面
	 */
	@RequiresPermissions(value={"lu:roleArea:view","lu:roleArea:add","lu:roleArea:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(RoleArea roleArea, Model model) {
		model.addAttribute("roleArea", roleArea);
		return "modules/lu/roleAreaForm";
	}

	/**
	 * 保存区域权限
	 */
	@RequiresPermissions(value={"lu:roleArea:add","lu:roleArea:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(RoleArea roleArea, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, roleArea)){
			return form(roleArea, model);
		}
		if(!roleArea.getIsNewRecord()){//编辑表单保存
			RoleArea t = roleAreaService.get(roleArea.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(roleArea, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			roleAreaService.save(t);//保存
		}else{//新增表单保存
			roleAreaService.save(roleArea);//保存
		}
		addMessage(redirectAttributes, "保存区域权限成功");
		return "redirect:"+Global.getAdminPath()+"/lu/roleArea/?repage";
	}
	
	/**
	 * 删除区域权限
	 */
	@RequiresPermissions("lu:roleArea:del")
	@RequestMapping(value = "delete")
	public String delete(RoleArea roleArea, RedirectAttributes redirectAttributes) {
		roleAreaService.delete(roleArea);
		addMessage(redirectAttributes, "删除区域权限成功");
		return "redirect:"+Global.getAdminPath()+"/lu/roleArea/?repage";
	}
	
	/**
	 * 批量删除区域权限
	 */
	@RequiresPermissions("lu:roleArea:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			roleAreaService.delete(roleAreaService.get(id));
		}
		addMessage(redirectAttributes, "删除区域权限成功");
		return "redirect:"+Global.getAdminPath()+"/lu/roleArea/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("lu:roleArea:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(RoleArea roleArea, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "区域权限"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RoleArea> page = roleAreaService.findPage(new Page<RoleArea>(request, response, -1), roleArea);
    		new ExportExcel("区域权限", RoleArea.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出区域权限记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/roleArea/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("lu:roleArea:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RoleArea> list = ei.getDataList(RoleArea.class);
			for (RoleArea roleArea : list){
				try{
					roleAreaService.save(roleArea);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条区域权限记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条区域权限记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入区域权限失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/roleArea/?repage";
    }
	
	/**
	 * 下载导入区域权限数据模板
	 */
	@RequiresPermissions("lu:roleArea:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "区域权限数据导入模板.xlsx";
    		List<RoleArea> list = Lists.newArrayList(); 
    		new ExportExcel("区域权限数据", RoleArea.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/roleArea/?repage";
    }

	@RequestMapping(value = "areaList")
	public String areaList(RoleArea roleArea, Model model){
		//当前登录用户的区域列表
		Areas areas = new Areas();
		areas.setCid(UserUtils.getUser().getCustomerID());
		List areaList = areasService.findList(areas);
		model.addAttribute("areaList", areaList);

		//已选择的区域列表
		List areaIdList = new ArrayList();
		for(RoleArea roleAreaTemp : roleAreaService.findList(roleArea)){
			areaIdList.add(roleAreaTemp.getAreaId());
		}
		roleArea.setAreaIds(StringUtils.join(areaIdList, ","));
		model.addAttribute("roleArea", roleArea);

		return "modules/lu/roleAreaAuth";
	}

}