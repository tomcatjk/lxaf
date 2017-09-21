package com.jeeplus.modules.lu.web;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.lu.dao.CustomersDao;
import com.jeeplus.modules.lu.entity.*;
import com.jeeplus.modules.lu.service.*;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;

/**
 * 客户表Controller
 * @author 陆华捷
 * @version 2017-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/lu/customers")
public class CustomersController extends BaseController {

	@Autowired
	private CustomersService customersService;

	@Autowired
	private AreasService areasService;

	@Autowired
	private CustomersDao customersDao;

	@Autowired
	private MastersService mastersService;

	@Autowired
	private DevicesService devicesService;

	@Autowired
	private DefencesService defencesService;
	
	@ModelAttribute
	public Customers get(@RequestParam(required=false) String id) {
		Customers entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customersService.get(id);
		}
		if (entity == null){
			entity = new Customers();
		}
		return entity;
	}

	/**
	 * 客户列表
	 * @param customers
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("lu:customers:list")
	@RequestMapping(value = {"list", ""})
	public String list(Customers customers, HttpServletRequest request, HttpServletResponse response, Model model) {
		customers.setCid(UserUtils.getUser().getCustomerID());
		customers.setCreateid(UserUtils.getUser().getId());
		Page<Customers> page = customersService.findCustomers(new Page<Customers>(request, response), customers);

		model.addAttribute("page", page);
        model.addAttribute("customertype",customers.getCustomertype());
		model.addAttribute("customers", customers);
        return "modules/lu/customersList";
	}

	/**
	 * 客户统计
	 */
	@RequiresPermissions("lu:customersalarms:list")
	@RequestMapping(value = "listcustomer")
	public String listCustomersAlarms(CustomersAlarms customersAlarms, HttpServletRequest request, HttpServletResponse response, Model model) {
		customersAlarms.setCustomerId(UserUtils.getUser().getCustomerID());
		customersAlarms.setDeviceTypeNameList(new ArrayList(Arrays.asList(DeviceTypeName.values())));
		Page<CustomersAlarms> page = customersService.findCustomersAlarms(new Page<CustomersAlarms>(request, response), customersAlarms);
		model.addAttribute("page",page);
		model.addAttribute("customersAlarms",customersAlarms);

		List customersTypeNameMapList = CustomerTypeName.getCustomerTypeNameMapList();
		Map customerTypeNameMap = new HashMap();
		customerTypeNameMap.put("customerType", "");
		customerTypeNameMap.put("customerTypeName", "全部客户");
		customersTypeNameMapList.add(0, customerTypeNameMap);
		model.addAttribute("customersTypeNameMapList", customersTypeNameMapList);
		model.addAttribute("currentType", customersAlarms.getCustomerType());

		List deviceTypeNameList = new ArrayList();
		int i = 0;
		for(DeviceTypeName deviceTypeNameTemp : DeviceTypeName.values()){
			deviceTypeNameList.add(i, deviceTypeNameTemp.getDeviceTypeName());
			i++;
		}
		model.addAttribute("deviceTypeNameList", deviceTypeNameList);
		return "modules/lu/customersAlarms";
	}

	/**
	 * 查看，编辑客户信息表单页面
	 */
	@RequiresPermissions(value={"lu:customers:view","lu:customers:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Customers customers, Model model) {
		//通过cid获取customer对象
		customers = customersService.findCustomersByCid(customers.getCid());
        Areas areas = areasService.findAreasById(customers.getAreaid());
        if(areas != null) {
            customers.setAreaName(areas.getName());
        }
		model.addAttribute("customers",customers);
		return "modules/lu/customersForm";
	}

	/**
	 * 添加客户信息表单页面
	 */
	@RequiresPermissions(value="lu:customers:add")
	@RequestMapping(value = "formadd")
	public String formAdd(Customers customers, Model model) {
		customers.setCustomerTypeStr(CustomerTypeName.getByType(customers.getCustomertype()).getCustomerTypeName());
		model.addAttribute("customertype",customers.getCustomertype());
		model.addAttribute("customerTypeStr",customers.getCustomerTypeStr());
		return "modules/lu/customersFormAdd";
	}

	/**
	 * 保存客户信息
	 */
	@RequiresPermissions(value={"lu:customers:add","lu:customers:edit"},logical=Logical.OR)
	@ResponseBody
	@RequestMapping(value = "save")
	public String save( Customers customers, String areaName, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) throws Exception{
		Customers customersTemp = customersService.findCustomersByCid(customers.getCid());
		if(customersTemp != null){
			if (customers.getName() != null && customers.getName() != "") {
				customersTemp.setName(customers.getName());
			}
			if (customers.getAreaid() != null && customers.getAreaid().length() != 0) {
				customersTemp.setAreaid(customers.getAreaid());
			}
			if (customers.getCustomertype() != null) {
				customersTemp.setCustomertype(customers.getCustomertype());
			}
			if (customers.getAddress() != null && customers.getAddress() != "") {
				customersTemp.setAddress(customers.getAddress());
			}
			if (customers.getPoint() != null && customers.getPoint() != "") {
				customersTemp.setPoint(customers.getPoint());
			}
			if (customers.getQualityperson() != null && customers.getQualityperson() != "") {
				customersTemp.setQualityperson(customers.getQualityperson());
			}
			if (customers.getContacts() != null && customers.getContacts() != "") {
				customersTemp.setContacts(customers.getContacts());
			}
			if (customers.getPhone() != null && customers.getPhone() != "") {
				customersTemp.setPhone(customers.getPhone());
			}
			if (customers.getInstallperson() != null && customers.getInstallperson() != "") {
				customersTemp.setInstallperson(customers.getInstallperson());
			}
			if (customers.getPreparer() != null && customers.getPreparer() != "") {
				customersTemp.setPreparer(customers.getPreparer());
			}
			if (customers.getRemark() != null && customers.getRemark() != "") {
				customersTemp.setRemark(customers.getRemark());
			}
			customersService.editByCid(customersTemp);
			addMessage(redirectAttributes, "更新客户成功");
			model.addAttribute("customers", customersTemp);
			return "ok";
        }else{
			if (customers.getCustomerTypeStr() != null) {
				CustomerTypeName customerTypeName = CustomerTypeName.getByName(customers.getCustomerTypeStr());
				customers.setCustomertype(customerTypeName.getCustomerType());
			}
			try {
				String[] areaIds = customers.getAreaid().split(",");
				customers.setAreaid(areaIds[areaIds.length - 1]);
				customers.setCreateid(UserUtils.getUser().getId());
				customers.setCid(UUID.randomUUID().toString());
				customers.setParentCid(UserUtils.getUser().getCustomerID());
				customersService.save(customers);//保存
				addMessage(redirectAttributes, "新增客户成功");
				model.addAttribute("customers", customers);
			}catch (Exception e){
				return "error";
			}
		}
		return customers.getCid();
	}
	
	/**
	 * 删除客户信息
	 */
	@RequiresPermissions("lu:customers:del")
	@RequestMapping(value = "delete")
	public String delete(Customers customers, RedirectAttributes redirectAttributes) {
		customersService.delete(customers);
		mastersService.deleteByCustomer(customers);
		devicesService.deleteByCustomer(customers);
		defencesService.deleteByCustomer(customers);
		addMessage(redirectAttributes, "删除客户信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/customers/list?repage&customertype=" + customers.getCustomertype();
	}
	
	/**
	 * 批量删除客户信息
	 */
	@RequiresPermissions("lu:customers:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
        Customers customers = customersService.findCustomersByCid(idArray[0]);
		for(String id : idArray){
            customers.setCid(id);
			customersService.delete(customers);
			mastersService.deleteByCustomer(customers);
			devicesService.deleteByCustomer(customers);
			defencesService.deleteByCustomer(customers);
		}
		addMessage(redirectAttributes, "删除客户信息成功");
        System.out.println(Global.getAdminPath());
        return "redirect:"+Global.getAdminPath()+"/lu/customers/list?repage&customertype=" + customers.getCustomertype();
	}
	
	/**
	 * 导出客户统计excel文件
	 */
	@RequiresPermissions("lu:customers:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Customers customers, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            /*String fileName = "客户信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Customers> page = customersService.findPage(new Page<Customers>(request, response, -1), customers);
    		new ExportExcel("客户信息", Customers.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;*/

			String fileName = "客户统计信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xls";
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet =wb.createSheet("客户信息统计表");
			HSSFRow row = sheet.createRow((int)0);
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			HSSFCell cell = row.createCell((short) 0);
			cell.setCellValue("名称");
			cell.setCellStyle(style);
			cell = row.createCell((short)1);
			cell.setCellValue("客户类型");
			cell.setCellStyle(style);
			cell = row.createCell((short)2);
			cell.setCellValue("质检人");
			cell.setCellStyle(style);
			cell = row.createCell((short)3);
			cell.setCellValue("安装人");
			cell.setCellStyle(style);
			cell = row.createCell((short)4);
			cell.setCellValue("安装时间");
			cell.setCellStyle(style);
			sheet.addMergedRegion(new Region(0,(short)4,0,(short)5));

			cell = row.createCell((short)6);
			cell.setCellValue("主机数");
			cell.setCellStyle(style);

			for(DeviceTypeName deviceTypeNameTemp : DeviceTypeName.values()){
				cell = row.createCell((short)(6 + deviceTypeNameTemp.getDeviceType()));
				cell.setCellValue(deviceTypeNameTemp.getDeviceTypeName());
				cell.setCellStyle(style);
			}

			CustomersAlarms customersAlarmsParamer = new CustomersAlarms();
			customersAlarmsParamer.setCustomerId(UserUtils.getUser().getCustomerID());
			customersAlarmsParamer.setDeviceTypeNameList(new ArrayList(Arrays.asList(DeviceTypeName.values())));
			List<CustomersAlarms> list = customersDao.getCustomersAlarms(customersAlarmsParamer);
			for(CustomersAlarms customersAlarmsTemp : list){
				customersAlarmsTemp.setCustomersType(CustomerTypeName.getByType(Integer.parseInt(customersAlarmsTemp.getCustomerType())).getCustomerTypeName());
			}

			for(int i=0;i<list.size();i++){
				row =sheet.createRow((int)i+1);
				CustomersAlarms customersAlarms = list.get(i);
				//创建单元格并赋值
				row.createCell((short)0).setCellValue(customersAlarms.getName());
				row.createCell((short)1).setCellValue(customersAlarms.getCustomersType());
				row.createCell((short)2).setCellValue(customersAlarms.getQualityPerson());
				row.createCell((short)3).setCellValue(customersAlarms.getInstallPerson());
				cell = row.createCell((short)4);
				cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(customersAlarms.getInstallTime()));
				row.createCell((short)6).setCellValue(customersAlarms.getMasterNum());

				Class customersAlarmClass = CustomersAlarms.class;
				for(DeviceTypeName deviceTypeNameTemp : DeviceTypeName.values()){
					Method method = customersAlarmClass.getMethod("getDEVICETYPE" + deviceTypeNameTemp.getDeviceType());
					row.createCell((short)(6 + deviceTypeNameTemp.getDeviceType())).setCellValue((int)method.invoke(customersAlarms));
				}
			}

			//选择保存路径
			response.addHeader("Content-Disposition","attachment;filename="+fileName);
			OutputStream os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			wb.write(os);
			os.close();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出客户信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/customers/listcustomer/?repage";
    }
    /**
     * 导出客户统计excel文件
     */
    @RequiresPermissions("lu:customers:export")
    @RequestMapping(value = "enterprisecustomers", method=RequestMethod.POST)
    public String exportEnterpriseCustomers(Customers customers, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "企业客户统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xls";
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet =wb.createSheet("企业客户统计表");
            HSSFRow row = sheet.createRow((int)0);
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue("客户名称");
            cell.setCellStyle(style);
            cell = row.createCell((short)1);
            cell.setCellValue("客户类型");
            cell.setCellStyle(style);
            cell = row.createCell((short)2);
            cell.setCellValue("区域名");
            cell.setCellStyle(style);
            cell = row.createCell((short)3);
            cell.setCellValue("坐标");
            cell.setCellStyle(style);
            sheet.addMergedRegion(new Region(0,(short)3,0,(short)4));
            cell = row.createCell((short)5);
            cell.setCellValue("地址");
            cell.setCellStyle(style);
            sheet.addMergedRegion(new Region(0,(short)5,0,(short)6));
            cell = row.createCell((short)7);
            cell.setCellValue("联系人");
            cell.setCellStyle(style);
            cell = row.createCell((short)8);
            cell.setCellValue("联系电话");
            cell.setCellStyle(style);
            cell = row.createCell((short)9);
            cell.setCellValue("质检人");
            cell.setCellStyle(style);
            cell = row.createCell((short)10);
            cell.setCellValue("安装人");
            cell.setCellStyle(style);
            cell = row.createCell((short)11);
            cell.setCellValue("填表人");
            cell.setCellStyle(style);
            cell = row.createCell((short)12);
            cell.setCellValue("安装时间");
            cell.setCellStyle(style);
            sheet.addMergedRegion(new Region(0,(short)12,0,(short)13));
            cell = row.createCell((short)14);
            cell.setCellValue("到期时间");
            cell.setCellStyle(style);
            sheet.addMergedRegion(new Region(0,(short)14,0,(short)15));
            cell = row.createCell((short)16);
            cell.setCellValue("创建时间");
            cell.setCellStyle(style);
            sheet.addMergedRegion(new Region(0,(short)16,0,(short)17));
            cell = row.createCell((short)18);
            cell.setCellValue("备注");
            cell.setCellStyle(style);

			customers.setCid(UserUtils.getUser().getCustomerID());
			customers.setCreateid(UserUtils.getUser().getId());
            List<Customers> list = customersDao.getCustomers(customers);
			for(Customers customersTemp : list){
				customersTemp.setCustomerTypeStr(CustomerTypeName.getByType(customers.getCustomertype()).getCustomerTypeName());
			}

            for(int i=0;i<list.size();i++){
                row =sheet.createRow((int)i+1);
                Customers customers1 = list.get(i);
                //创建单元格并赋值
                row.createCell((short)0).setCellValue(customers1.getName());
                row.createCell((short)1).setCellValue(customers1.getCustomerTypeStr());
                row.createCell((short)2).setCellValue(customers1.getAreaName());
                row.createCell((short)3).setCellValue(customers1.getPoint());
                row.createCell((short)5).setCellValue(customers1.getAddress());
                row.createCell((short)7).setCellValue(customers1.getContacts());
                row.createCell((short)8).setCellValue(customers1.getPhone());
                row.createCell((short)9).setCellValue(customers1.getQualityperson());
                row.createCell((short)10).setCellValue(customers1.getInstallperson());
                row.createCell((short)11).setCellValue(customers1.getPreparer());
                cell = row.createCell((short)12);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(customers1.getInstalltime()));
                cell = row.createCell((short)14);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(customers1.getDuetime()));
                cell = row.createCell((short)16);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(customers1.getCreatetime()));
                row.createCell((short)18).setCellValue(customers1.getRemark());
            }


            //选择保存路径
            response.addHeader("Content-Disposition","attachment;filename="+fileName);
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            wb.write(os);
            os.close();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出客户信息失败！失败信息："+e.getMessage());
        }
        if(customers.getCustomertype()==3){
            return "redirect:"+Global.getAdminPath()+"/lu/customers/listGovernment/?repage";
        }else if(customers.getCustomertype()==2){
            return "redirect:"+Global.getAdminPath()+"/lu/customers/listPerson/?repage";
        }else{
            return "redirect:"+Global.getAdminPath()+"/lu/customers/list/?repage";
        }
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("lu:customers:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Customers> list = ei.getDataList(Customers.class);
			for (Customers customers : list){
				try{
					customersService.save(customers);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条客户信息。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条客户信息"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入客户信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/customers/customersAlarms/?repage";
    }
	
	/**
	 * 下载导入客户信息数据模板
	 */
	@RequiresPermissions("lu:customers:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "客户信息数据导入模板.xlsx";
    		List<Customers> list = Lists.newArrayList(); 
    		new ExportExcel("客户信息数据", Customers.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/customers/customersAlarms/?repage";
    }
}