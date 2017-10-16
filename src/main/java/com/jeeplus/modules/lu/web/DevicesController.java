/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.web;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.lu.dao.DevicesDao;
import com.jeeplus.modules.lu.entity.*;
import com.jeeplus.modules.lu.service.AlarmsService;
import com.jeeplus.modules.lu.service.DefencesService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
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
import com.jeeplus.modules.lu.service.DevicesService;

/**
 * 设备表Controller
 * @author 陆华捷  
 * @version 2017-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/lu/devices")
public class DevicesController extends BaseController {

	@Autowired
	private DevicesService devicesService;

	@Autowired
	private DefencesService defencesService;

	@Autowired
	private DevicesDao devicesDao;

	@Autowired
	private AlarmsService alarmsService;

	@ModelAttribute
	public Devices get(@RequestParam(required=false) String id) {
		Devices entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = devicesService.get(id);
		}
		if (entity == null){
			entity = new Devices();
		}
		return entity;
	}
	
	/**
	 * 设备信息列表页面
	 */
	@RequiresPermissions("lu:devices:list")
	@RequestMapping(value = {"list", ""})
	public String list(Devices devices, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Devices> page = devicesService.findPageByCustomerid(new Page<Devices>(request, response), devices);
		model.addAttribute("page", page);
		model.addAttribute("customerid", devices.getCustomerid());
		return "modules/lu/devicesList";
	}

	/**
	 * 设备统计页面
	 */
	@RequiresPermissions("lu:devicescustomer:list")
	@RequestMapping(value = "customerdevices")
	public String customerList(DevicesCustomers devicesCustomers, HttpServletRequest request, HttpServletResponse response, Model model) {
		devicesCustomers.setCustomerId(UserUtils.getUser().getCustomerID());
		Page<DevicesCustomers> page = devicesService.findDeviceCustomerPage(new Page<DevicesCustomers>(request, response), devicesCustomers);
		model.addAttribute("page",page);

		List deviceTypeNameMapList = DeviceTypeName.getDeviceTypeMapList();
		Map mapTemp = new HashMap();
		mapTemp.put("deviceType", "");
		mapTemp.put("deviceTypeName", "全部");
		deviceTypeNameMapList.add(0, mapTemp);
		model.addAttribute("deviceTypeNameMapList",deviceTypeNameMapList);
		model.addAttribute("currentType",devicesCustomers.getDevicesType());
		return "modules/lu/devicesCustomers";
	}

	/**
	 * 查看，增加，编辑设备信息表单页面
	 */
	@RequiresPermissions(value={"lu:devices:view","lu:devices:add","lu:devices:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Devices devices, Model model) {
		Devices devicesTemp = devicesService.findUniqueByProperty("d.did", devices.getDid());
		if(devicesTemp != null){
			Defences defence = defencesService.findUniqueByProperty("did", devicesTemp.getDefenceid());
			model.addAttribute("defenceTypeTemp", defence.getDefencetype());
			model.addAttribute("devices", devicesTemp);
		}else{

		}
		Map deviceTypeNameMap = new HashMap();
		for(DeviceTypeName deviceTypeNameObject : DeviceTypeName.values()){
			deviceTypeNameMap.put(String.valueOf(deviceTypeNameObject.getDeviceType()), deviceTypeNameObject.getDeviceTypeName());
		}
		model.addAttribute("deviceTypeNameMap", deviceTypeNameMap);
		model.addAttribute("defenceTypeMapList", DefenceTypeName.getDefenceTypeMapList());
		return "modules/lu/devicesForm";
	}

	@RequestMapping(value = "deviceTypeNameMap")
	@ResponseBody
	public JSONArray getMastersStateNameMap(){
		List list = new ArrayList();
		int i = 0;
		for(DeviceTypeName deviceTypeNameObject : DeviceTypeName.values()){
			Map deviceTypeNameMap = new HashMap();
			deviceTypeNameMap.put("type", String.valueOf(deviceTypeNameObject.getDeviceType()));
			deviceTypeNameMap.put("name", deviceTypeNameObject.getDeviceTypeName());
			list.add(i, deviceTypeNameMap);
			i++;
		}
		return JSONArray.fromObject(list);
	}

	/**
	 * 保存设备信息
	 */
	@RequiresPermissions(value={"lu:devices:add","lu:devices:edit"},logical=Logical.OR)
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(Devices devices,Model model, RedirectAttributes redirectAttributes) throws Exception{
		String msg = savedevice(devices, new User());
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/lu/devices/?repage&customerid=" + devices.getCustomerid();
	}

	public String savedevice(Devices devices,User user) throws Exception {
		String msg = "";
		if(devices.getDid() != null && devices.getDid().length() != 0){
			Devices devicesTemp = devicesService.findUniqueByProperty("d.did", devices.getDid());
			devicesTemp.setName(devices.getName());
			devicesTemp.setDevicetype(devices.getDevicetype());
			devicesTemp.setMasterid(devices.getMasterid());
			devicesTemp.setDefenceid(devices.getDefenceid());
			devicesService.updateByDid(devicesTemp);
			Defences defencesTemp = defencesService.findUniqueByProperty("did",devices.getDefenceid());
			defencesTemp.setName(devices.getDefenceName());
			defencesTemp.setDefencetype(Integer.valueOf(devices.getDefenceType()));
			defencesService.updateOfNoCheck(defencesTemp);
			msg = "更新设备信息成功";
		}else{
			if(user.getId() == null) {
				user = UserUtils.getUser();
			}
			Defences defences = defencesService.findUniqueByProperty("did",devices.getDefenceid());
			defences.setName(devices.getDefenceName());
			if(devices.getDevicetype() != null) {
				defences.setDefencetype(devices.getDevicetype());
			}
			defencesService.updateOfNoCheck(defences);
			if(!devices.getIsNewRecord()){//编辑表单保存
				Devices t = devicesService.get(devices.getId());//从数据库取出的值
				MyBeanUtils.copyBeanNotNull2Bean(devices, t);//将编辑表单中的非NULL值覆盖数据库中的值
				devicesService.save(t);//保存
			}else{//新增表单保存
				devices.setCreatetime(new Date());
				devices.setDid(UUID.randomUUID().toString());
				if(devices.getCreateid() == null) {
					devices.setCreateid(user.getId());
				}
				devicesService.save(devices);//保存
			}
			msg = "保存设备信息成功";
		}
		return msg;
	}

	/**
	 * 删除设备信息
	 */
	@RequiresPermissions("lu:devices:del")
	@RequestMapping(value = "delete")
	public String delete(Devices devices, RedirectAttributes redirectAttributes) {
		delDevice(devices);
		addMessage(redirectAttributes, "删除设备信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/devices/?repage&customerid=" + devices.getCustomerid();
	}

	public void delDevice(Devices device){
		devicesService.delete(device);
		alarmsService.deleteByDefenceId(device);
	}

	/**
	 * 批量删除设备信息
	 */
	@RequiresPermissions("lu:devices:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		Devices devicesTemp = new Devices();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			devicesTemp = devicesService.findUniqueByProperty("d.did", id);
			delDevice(devicesTemp);
		}
		addMessage(redirectAttributes, "删除设备信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/devices/?repage&customerid=" + devicesTemp.getCustomerid();
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("lu:devices:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Devices devices, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
          /* String fileName = "设备信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Devices> page = devicesService.findPage(new Page<Devices>(request, response, -1), devices);
    		new ExportExcel("设备信息", Devices.class).setDataList(page.getList()).write(response, fileName).dispose();*/


			String fileName = "报警统计信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xls";
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet =wb.createSheet("报警信息统计表");
			HSSFRow row = sheet.createRow((int)0);
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

//			HSSFCell cell = row.createCell((short) 0);
			/*cell.setCellValue("设备型号");
			cell.setCellStyle(style);*/
			HSSFCell cell = row.createCell((short)0);
			cell.setCellValue("设备名字");
			cell.setCellStyle(style);
			cell = row.createCell((short)1);
			cell.setCellValue("设备类型");
			cell.setCellStyle(style);
			cell = row.createCell((short)2);
			cell.setCellValue("所属客户名");
			cell.setCellStyle(style);
			cell = row.createCell((short)3);
			cell.setCellValue("状态");
			cell.setCellStyle(style);
			cell = row.createCell((short)4);
			cell.setCellValue("安装时间");
			cell.setCellStyle(style);
			sheet.addMergedRegion(new Region(0,(short)4,0,(short)5));
			cell = row.createCell((short)6);
			cell.setCellValue("到期时间");
			cell.setCellStyle(style);
			sheet.addMergedRegion(new Region(0,(short)6,0,(short)7));
			cell = row.createCell((short)8);
			cell.setCellValue("安装人");
			cell.setCellStyle(style);
			cell = row.createCell((short)9);
			cell.setCellValue("质检人");
			cell.setCellStyle(style);

			DevicesCustomers devicesCustomersParameter = new DevicesCustomers();
			devicesCustomersParameter.setCustomerId(UserUtils.getUser().getCustomerID());
			List<DevicesCustomers> list = devicesDao.getDeviceCustomer(devicesCustomersParameter);
			for(DevicesCustomers devicesCustomersTemp : list){
				if(devicesCustomersTemp.getDevicesType() != null){
					devicesCustomersTemp.setDevicesType(DeviceTypeName.getByType(Integer.parseInt(devicesCustomersTemp.getDevicesType())).getDeviceTypeName());
				}
				//设置设备状态
				if(devicesCustomersTemp.getState() != null){
					devicesCustomersTemp.setState(DeviceStateName.getByState(Integer.parseInt(devicesCustomersTemp.getState())).getDeviceStateName());
				}
			}

			for(int i=0;i<list.size();i++){
				row =sheet.createRow((int)i+1);
				DevicesCustomers devicesCustomers = list.get(i);
				//创建单元格并赋值
//				row.createCell((short)0).setCellValue(devicesCustomers.getDevicemodel());
				row.createCell((short)0).setCellValue(devicesCustomers.getDevicesName());
				row.createCell((short)1).setCellValue(devicesCustomers.getDevicesType());
				row.createCell((short)2).setCellValue(devicesCustomers.getCustomersName());
				row.createCell((short)3).setCellValue(devicesCustomers.getState());
				cell = row.createCell((short)4);
				cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(devicesCustomers.getInstallTime()));
				cell = row.createCell((short)6);
				cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(devicesCustomers.getDueTime()));
				row.createCell((short)8).setCellValue(devicesCustomers.getInstallPerson());
				row.createCell((short)9).setCellValue(devicesCustomers.getQualityPerson());
			}


			//选择保存路径
			response.addHeader("Content-Disposition","attachment;filename="+fileName);
			OutputStream os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			wb.write(os);
			os.close();

    		return null;
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导出设备信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/devices/devicesCustomers/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("lu:devices:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Devices> list = ei.getDataList(Devices.class);
			for (Devices devices : list){
				try{
					devicesService.save(devices);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条设备信息。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条设备信息"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入设备信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/devices/customerdevices/?repage";
    }
	
	/**
	 * 下载导入设备信息数据模板
	 */
	@RequiresPermissions("lu:devices:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "设备信息数据导入模板.xlsx";
    		List<Devices> list = Lists.newArrayList(); 
    		new ExportExcel("设备信息数据", Devices.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/devices/customerdevices/?repage";
    }
}