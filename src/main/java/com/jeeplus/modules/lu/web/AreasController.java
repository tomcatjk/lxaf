/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.web;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.lu.entity.Areas;
import com.jeeplus.modules.lu.service.AreasService;

/**
 * 区域表Controller
 * @author 陆华捷
 * @version 2017-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/lu/areas")
public class AreasController extends BaseController {

	@Autowired
	private AreasService areasService;
	
	@ModelAttribute
	public Areas get(@RequestParam(required=false) String id) {
		Areas entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = areasService.get(id);
		}
		if (entity == null){
			entity = new Areas();
		}
		return entity;
	}
	
	/**
	 * 区域列表页面
	 */
	/**
	 * 区域列表页面
	 */
	@RequiresPermissions("lu:areas:list")
	@RequestMapping(value = {"list", ""})
	public String list(Areas areas, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		areas.setCid(user.getCustomerID());
		Page<Areas> page = areasService.findPage(new Page<Areas>(request,response), areas);
		for(Areas areasTemp : page.getList()){
			/*将创建时间从yyyy-MM-dd HH:mm:ss转为 yyyy-MM-dd*/
			areasTemp.setCreateDateStr(DateUtils.formatDate(areasTemp.getCreateDate(),"yyyy-MM-dd"));
		}
		model.addAttribute("page", page);
		return "modules/lu/areasList";
	}

	@RequiresPermissions("lu:areas:list")
	@RequestMapping(value = "maptest")
	public String maptest() {
		return "modules/lu/maptest";
	}

	/**
	 * 查看，增加，编辑区域表单页面
	 */
	@RequiresPermissions(value={"lu:areas:view","lu:areas:add","lu:areas:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Areas areas, Model model) {
		if (areas.getParent()!=null && StringUtils.isNotBlank(areas.getParent().getId())){
			areas.setParent(areasService.get(areas.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(areas.getId())){
				Areas areasChild = new Areas();
				areasChild.setParent(new Areas(areas.getParent().getId()));
				List<Areas> list = areasService.findList(areas);
				if (list.size() > 0){
					areas.setSort(list.get(list.size()-1).getSort());
					if (areas.getSort() != null){
						areas.setSort(areas.getSort() + 30);
					}
				}
			}
		}
		if (areas.getSort() == null){
			areas.setSort(30);
		}
		model.addAttribute("areas", areas);
		return "modules/lu/areasForm";
	}

	/**
	 * 保存区域
	 */
	@RequiresPermissions(value={"lu:areas:add","lu:areas:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Areas areas, Model model, RedirectAttributes redirectAttributes) throws Exception{
		User user = UserUtils.getUser();
		areas.setCustomertype("1");
		areas.setCid(user.getCustomerID());
		if (!beanValidator(model, areas)){
			return form(areas, model);
		}
		if(!areas.getIsNewRecord()){//编辑表单保存
			Areas t = areasService.get(areas.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(areas, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			areasService.save(t);//保存
		}else{//新增表单保存
			areasService.save(areas);//保存
		}
		addMessage(redirectAttributes, "保存区域成功");
		return "redirect:"+Global.getAdminPath()+"/lu/areas/?repage";
	}
	
	/**
	 * 删除区域
	 */
	@RequiresPermissions("lu:areas:del")
	@RequestMapping(value = "delete")
	public String delete(Areas areas, RedirectAttributes redirectAttributes) {
		areasService.delete(areas);
		addMessage(redirectAttributes, "删除区域成功");
		return "redirect:"+Global.getAdminPath()+"/lu/areas/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Areas areas = new Areas();
		areas.setCid(UserUtils.getUser().getCustomerID());
		List<Areas> list = areasService.findList(areas);
		for (int i=0; i<list.size(); i++){
			Areas e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}