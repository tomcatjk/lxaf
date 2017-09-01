package com.jeeplus.modules.lu.web;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
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
	public String listCustomersAlarms(Customers customers,CustomersAlarms customersAlarms, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user=UserUtils.getUser();
		customersAlarms.setId(user.getId());
		Page<CustomersAlarms> page = customersService.findCustomersAlarms(new Page<CustomersAlarms>(request, response), customersAlarms);
		String customerType = request.getParameter("customersTypeStr");
		customers.setCustomerTypeStr(customerType);
		if((customers.getName()!=null||customers.getName()!="")||customerType!=null){
			if(customers.getName()!=null||customers.getName()!=""){
				customersAlarms.setName(customers.getName());
				model.addAttribute("customers",customers);
			}
			if(customerType!=null&&StringUtils.isNotBlank(customerType)){
				CustomerTypeName customerTypeName = CustomerTypeName.getByName(customerType);
				customers.setCustomertype(customerTypeName.getCustomerType());
				customersAlarms.setCustomertype(customers.getCustomertype());
				model.addAttribute("customerType",customerType);
			}else {

			}
			page = customersService.findCustomersAlarms(new Page<CustomersAlarms>(request, response), customersAlarms);
		}else {

		}
		List customersTypeList = new ArrayList();
		int i = 0;
		for(CustomerTypeName customerTypeName : CustomerTypeName.values()){
			customersTypeList.add(i, customerTypeName.getCustomerTypeName());
			i++;
		}
		customersTypeList.add(0, "");
		model.addAttribute("customersTypeList", customersTypeList);
		model.addAttribute("customerTypeTemp",customers.getCustomerTypeStr());
		model.addAttribute("page",page);

		List deviceTypeNameList = new ArrayList();
		int j = 0;
		for(DeviceTypeName deviceTypeNameTemp : DeviceTypeName.values()){
			deviceTypeNameList.add(j, deviceTypeNameTemp.getDeviceTypeName());
			j++;
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
	public String formAdd(Customers customers, Model model,HttpServletRequest request) {
		/*根据点击不同的页面加载不同的客户类别*/
		if(customers.getCustomertype()!=null){
			CustomerTypeName customerTypeName = CustomerTypeName.getByType(customers.getCustomertype());
			customers.setCustomerTypeStr(customerTypeName.getCustomerTypeName());
		}
		/*获取登录的用户*/
		User user =  UserUtils.getUser();
        /*查区域管理员登录区域和普通用户登录区域*/
		List<Areas> listAreas = new ArrayList<Areas>();
		if("0".equals(user.getCustomerID())){
			User user1 = new User();
			listAreas = customersService.findAllAreas(user1.getCustomerID());
		}else{
			listAreas = customersService.findAllAreas(user.getCustomerID());
		}
        /*获取一个areaid*/
        String areaid = customersService.findOneAreasID(user.getCustomerID());

		model.addAttribute("customertype",customers.getCustomertype());
		model.addAttribute("customerTypeStr",customers.getCustomerTypeStr());
        model.addAttribute("areaid",areaid);
		model.addAttribute("listAreas",listAreas);
		return "modules/lu/customersFormAdd";
	}
	/**
	 * 客户iframe跳转
	 */
	@RequiresPermissions("lu:customers:iframe")
	@RequestMapping(value = "formiframe")
	public String formIframe(Customers customers,String masterFlag, Model model) {
		model.addAttribute("customertype",customers.getCustomertype());
		model.addAttribute("cid",customers.getCid());
		model.addAttribute("masterFlag",masterFlag == null ? "0" : masterFlag);
		return "modules/lu/iframeForm";
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
//			return "redirect:"+Global.getAdminPath()+"/lu/customers/list?repage&customertype=" + customers.getCustomertype();
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
//				return "redirect:"+Global.getAdminPath()+"/lu/customers/list?repage&customertype=" + customers.getCustomertype();
				return "error";
			}
		}
//		return "redirect:"+Global.getAdminPath()+"/lu/customers/formiframe/?repage&cid="+customers.getCid() + "&masterFlag=1";
		return customers.getCid();
	}
	
	/**
	 * 删除客户信息
	 */
	@RequiresPermissions("lu:customers:del")
	@RequestMapping(value = "delete")
	public String delete(Customers customers, RedirectAttributes redirectAttributes) {
		customersService.delete(customers);
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
			cell = row.createCell((short)7);
			cell.setCellValue("红外设备");
			cell.setCellStyle(style);
			sheet.addMergedRegion(new Region(0,(short)7,0,(short)8));
			cell = row.createCell((short)9);
			cell.setCellValue("烟感设备");
			cell.setCellStyle(style);
			sheet.addMergedRegion(new Region(0,(short)9,0,(short)10));
			cell = row.createCell((short)11);
			cell.setCellValue("门磁设备");
			cell.setCellStyle(style);
			sheet.addMergedRegion(new Region(0,(short)11,0,(short)12));
			cell = row.createCell((short)13);
			cell.setCellValue("遥控器");
			cell.setCellStyle(style);
			sheet.addMergedRegion(new Region(0,(short)13,0,(short)14));
			cell = row.createCell((short)15);
			cell.setCellValue("紧急按钮");
			cell.setCellStyle(style);
			sheet.addMergedRegion(new Region(0,(short)15,0,(short)16));
			cell = row.createCell((short)17);
			cell.setCellValue("地涝设备");
			cell.setCellStyle(style);
			sheet.addMergedRegion(new Region(0,(short)17,0,(short)18));
			cell = row.createCell((short)19);
			cell.setCellValue("天然气设备");
			cell.setCellStyle(style);
			sheet.addMergedRegion(new Region(0,(short)19,0,(short)20));
			cell = row.createCell((short)21);

			CustomersAlarms customersAlarmsParamer = new CustomersAlarms();
			customersAlarmsParamer.setId(UserUtils.getUser().getId());
			List<CustomersAlarms> list = customersDao.getCustomersAlarms(customersAlarmsParamer);
			for(CustomersAlarms customersAlarmsTemp : list){
				customersAlarmsTemp.setCustomersType(CustomerTypeName.getByType(customersAlarmsTemp.getCustomertype()).getCustomerTypeName());
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
				row.createCell((short)7).setCellValue(customersAlarms.getDEVICETYPE1());
				row.createCell((short)9).setCellValue(customersAlarms.getDEVICETYPE2());
				row.createCell((short)11).setCellValue(customersAlarms.getDEVICETYPE3());
				row.createCell((short)13).setCellValue(customersAlarms.getDEVICETYPE4());
				row.createCell((short)15).setCellValue(customersAlarms.getDEVICETYPE5());
				row.createCell((short)17).setCellValue(customersAlarms.getDEVICETYPE6());
				row.createCell((short)19).setCellValue(customersAlarms.getDEVICETYPE7());
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