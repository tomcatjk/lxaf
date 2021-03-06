/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.web;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.lu.dao.AlarmsDao;
import com.jeeplus.modules.lu.dao.AlarmsDefencesDao;
import com.jeeplus.modules.lu.entity.*;
import com.jeeplus.modules.lu.service.*;
import com.jeeplus.modules.lu.utils.JiguangPush;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
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

/**
 * 报警表Controller
 * @author 陆华捷  
 * @version 2017-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/lu/alarms")
public class AlarmsController extends BaseController {

	@Autowired
	private AlarmsService alarmsService;

	@Autowired
	private ServicePersonsService servicePersonsService;

	@Autowired
	private ServiceRecordsService serviceRecordsService;

	@Autowired
	private DevicesService devicesService;

	@Autowired
	private AfcustomersService afcustomersService;

	@Autowired
	private CustomersService customersService;

    @Autowired
    private AlarmsDefencesService alarmsDefencesService;

    @Autowired
    private AlarmsDao alarmsDao;

    @Autowired
    private AlarmsDefencesDao alarmsDefencesDao;

    @Autowired
    private SystemService systemService;

    @Autowired
    private RoleAreaService roleAreaService;

    @Autowired
    private AreasService areasService;
	
	@ModelAttribute
	public Alarms get(@RequestParam(required=false) String id) {
		Alarms entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = alarmsService.get(id);
		}
		if (entity == null){
			entity = new Alarms();
		}
		return entity;
	}

	@ResponseBody
	@RequestMapping(value = "findAlarmsInfoAcd")
	public JSONArray findAlarmsInfoAcd(Model model, HttpServletRequest request){
        //根据登录的用户获取相应的报警信息
        User user = UserUtils.getUser();
        List list = new ArrayList();
		Map paramMap = new HashMap();
		String state = request.getParameter("state");
		int currentPage = Integer.parseInt(request.getParameter("currentPage"));
		int pageSize = 5;
		paramMap.put("state", state);
		paramMap.put("pageSize", pageSize);
		paramMap.put("currentPage", null);
        int total = 0;

        user.setRoleid(systemService.getUserObject(UserUtils.getUser()).getRoleid());
        paramMap.put("cid",user.getCustomerID());
        RoleArea roleAreaParameter = new RoleArea();
        roleAreaParameter.setRoleId(user.getRoleid());
        List<RoleArea> roleAreaList = new ArrayList<RoleArea>();
        Role userRole = systemService.findRole(user);
        if(userRole != null) {
            if (user.getCustomerID().equals(userRole.getCustomerid())) {
                roleAreaList = roleAreaService.findList(roleAreaParameter);
            } else {
                user.setRoleid("");
                Areas areasParameter = new Areas();
                areasParameter.setCid(user.getCustomerID());
                roleAreaList = areasService.findAreasToRoleArea(areasParameter);
            }
            paramMap.put("roleAreaList", roleAreaList);
            total += alarmsService.findAlarmsInfoAcd(paramMap).size();
            paramMap.put("currentPage", (currentPage-1) * pageSize);
			List<AlarmsInfoAcd> l = alarmsService.findAlarmsInfoAcd(paramMap);
            list.addAll(l);
        }
        Map resultMap = new HashMap();
		resultMap.put("currentPage", currentPage);
		resultMap.put("endPage", total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		resultMap.put("alarmsInfoAcdList", list);
		return JSONArray.fromObject(resultMap);
	}

	@ResponseBody
	@RequestMapping(value = "findServicePersons")
	public JSONArray findServicePersons(Model model){
		List ServicePersonsPartList = servicePersonsService.getServicePersonsByState(0);
		return JSONArray.fromObject(ServicePersonsPartList);
	}

	/**
	 * 指派服务人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addServicePersonForAlarms")
	public String addServicePersonForAlarms(String aid,String servicePersonsId){
		ServicePersons servicePersons = servicePersonsService.findUniqueByProperty("id", servicePersonsId);
		//为报警设备指派服务人员（mysql)
		Alarms alarms = alarmsService.findUniqueByProperty("aid",aid);
		alarms.setServicename(servicePersonsId);//将人员id存在人员姓名中
		alarms.setState(2);
		alarmsService.updateByAid(alarms);
		//更新服务人员的状态(sqlserver)
//		servicePersons.setState(1);
		servicePersonsService.updateById(servicePersons);
		//新增报警(sqlserver)
		User user = UserUtils.getUser();
		ServiceRecords serviceRecords = new ServiceRecords();
		serviceRecords.setCreatetime(new Date());
		serviceRecords.setCreator(user.getId());
		Devices devices = devicesService.findUniqueByProperty("defenceid",alarms.getDefenceid());//通过防区得到设备id
		serviceRecords.setDevicecode(devices == null ? "-1" : devices.getDid());//将设备id赋给设备编码
		serviceRecords.setServerid(Integer.parseInt(servicePersonsId));
		serviceRecords.setState(0);
		serviceRecordsService.addServiceRecords(serviceRecords);
		//向AFCustomers中添加
		String code = "AF" + alarms.getCustomerid();
		Afcustomers afcustomers = afcustomersService.findUniqueByProperty("Code", code);
		if(afcustomers == null){
			Customers customers = customersService.findUniqueByProperty("CID", alarms.getCustomerid());
			Afcustomers afcustomersTemp = new Afcustomers();
			afcustomersTemp.setCode(code);
			afcustomersTemp.setName(customers.getName());
			afcustomersTemp.setAge(0);
			afcustomersTemp.setGender("");
			afcustomersTemp.setPhone(customers.getPhone());
			afcustomersTemp.setAddress(customers.getAddress());
			afcustomersTemp.setLatitude(customers.getPoint().split(",")[0]);
			afcustomersTemp.setLongitude(customers.getPoint().split(",")[1]);
			afcustomersTemp.setImgurl("");
			afcustomersService.save(afcustomersTemp);
		}
		//极光推送
//		JiguangPush.jiguangPush(servicePersonsId);
		return "ok";
	}

	/**
	 * 已处理选项卡-完成
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "completeAlarms")
	public String completeAlarms(String aid, String rated){
		//为报警添加时间以及更改报警状态（mysql)
		Alarms alarms = alarmsService.findUniqueByProperty("aid",aid);
		alarms.setRecordTime(new Date());
		alarms.setState(3);
		alarmsService.updateByAid(alarms);
		//更新服务人员状态(sqlserver)
		String servicePersonsId = alarms.getServicename();//存在servicename中的是人员id
		ServicePersons servicePersons = servicePersonsService.findUniqueByProperty("id", servicePersonsId);
//		servicePersons.setState(0);
		servicePersonsService.updateById(servicePersons);
		//更新报警(sqlserver)
		//结果：没法取到sqlserver中的报警
		//用假数据测试
		String serverId = alarms.getServicename();
		Map map = new HashMap();
		map.put("serverID", serverId);
		map.put("state", 0);
		ServiceRecords serviceRecords = serviceRecordsService.getServiceRecordsByserverIDAndState(map);
		serviceRecords.setState(1);//更改状态
		serviceRecords.setRated(rated);//添加评论
		serviceRecordsService.updateById(serviceRecords);

		return "ok";
	}
	
	/**
	 * 报警信息列表页面
	 */
	@RequiresPermissions("lu:alarms:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/lu/alarmsList";
	}

    /**
     * 报警详单
     */
    @RequiresPermissions("lu:ararmsde:list")
    @RequestMapping(value = "alarmsdef")
    public String alarmsDefenceslist(AlarmsDefences alarmsDefences, HttpServletRequest request, HttpServletResponse response, Model model) {
        alarmsDefences.setCustomerId(UserUtils.getUser().getCustomerID());
    	Page<AlarmsDefences> page= alarmsDefencesService.find1(new Page<AlarmsDefences>(request, response),alarmsDefences);
        String startDate = request.getParameter("startTime");
        String endDate = request.getParameter("endTime");
        try{
            if(startDate!=null||endDate!=null){
                if(startDate!=null){
                    alarmsDefences.setStartTime(startDate);
                }
                if(endDate!=null){
                    alarmsDefences.setEndTime(endDate);
                }
                page= alarmsDefencesService.find1(new Page<AlarmsDefences>(request, response),alarmsDefences);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("alarmsDefences",alarmsDefences);
        model.addAttribute("page",page);
        return "modules/lu/alarmsDefences";
    }

    /**
     * 报警类型的统计页面
     */

    @RequiresPermissions("lu:count:list")
    @RequestMapping(value = "count")
    public String CountList(AlarmsCount alarmsCount, HttpServletRequest request, HttpServletResponse response, Model model) {
        alarmsCount.setCustomerid(UserUtils.getUser().getCustomerID());
        alarmsCount.setAlarmTypeNameList(new ArrayList(Arrays.asList(AlarmTypeName.values())));
		//sun 添加数据到报警总数
		AlarmsCount devidedAlarmsCount = alarmsService.getDevidedAlarmsCount(alarmsCount);
		model.addAttribute("devidedAlarmsCount",devidedAlarmsCount);

        Page<AlarmsCount> page = alarmsService.findAlarmsCount(new Page<AlarmsCount>(request, response),alarmsCount);

		model.addAttribute("page",page);
        model.addAttribute("alarmscount",alarmsCount);



		List alarmTypeList = new ArrayList();
		int i = 0;
		for(AlarmTypeName alarmTypeNameTemp : AlarmTypeName.values()){
			alarmTypeList.add(i, alarmTypeNameTemp.getAlarmTypeName());
			i++;
		}

		List customersTypeNameMapList = CustomerTypeName.getCustomerTypeNameMapList();
		Map customerTypeNameMap = new HashMap();
		customerTypeNameMap.put("customerType", "");
		customerTypeNameMap.put("customerTypeName", "全部客户");
		customersTypeNameMapList.add(0, customerTypeNameMap);
		model.addAttribute("customersTypeNameMapList", customersTypeNameMapList);
		model.addAttribute("currentType", alarmsCount.getCustomerType());

		model.addAttribute("alarmTypeList", alarmTypeList);
        return "modules/lu/alarmsCount";
    }

	/**
	 * 查看，增加，编辑报警信息表单页面
	 */
	@RequiresPermissions(value={"lu:alarms:view","lu:alarms:add","lu:alarms:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Alarms alarms, Model model) {
		model.addAttribute("alarms", alarms);
		return "modules/lu/alarmsForm";
	}

	/**
	 * 保存报警信息
	 */
	@RequiresPermissions(value={"lu:alarms:add","lu:alarms:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Alarms alarms, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, alarms)){
			return form(alarms, model);
		}
		if(!alarms.getIsNewRecord()){//编辑表单保存
			Alarms t = alarmsService.get(alarms.getId());//从数据库取出的值
			MyBeanUtils.copyBeanNotNull2Bean(alarms, t);//将编辑表单中的非NULL值覆盖数据库中的值
			alarmsService.save(t);//保存
		}else{//新增表单保存
			alarmsService.save(alarms);//保存
		}
		addMessage(redirectAttributes, "保存报警信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsdef/?repage";
	}
	
	/**
	 * 删除报警信息
	 */
	@RequiresPermissions("lu:alarms:del")
	@RequestMapping(value = "delete")
	public String delete(Alarms alarms, RedirectAttributes redirectAttributes) {
		alarmsService.delete(alarms);
		addMessage(redirectAttributes, "删除报警信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsdef/?repage";
	}

	
	/**
	 * 批量删除报警信息
	 */
	@RequiresPermissions("lu:alarms:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			alarmsService.delete(alarmsService.get(id));
		}
		addMessage(redirectAttributes, "删除报警信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsdef/?repage";
	}

    /**
     * 导出excel文件AlarmsDefences
     */
    @RequiresPermissions("lu:alarms:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "报警详单信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xls";
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet =wb.createSheet("报警信息详单表");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int)0);
            //第四步，创建单元格，并设置值表头、设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//创建一个居中格式

            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue("客户名");
            cell.setCellStyle(style);
            cell = row.createCell((short)1);
            cell.setCellValue("防区名");
            cell.setCellStyle(style);
            cell = row.createCell((short)2);
            cell.setCellValue("报警类型");
            cell.setCellStyle(style);
            cell = row.createCell((short)3);
            cell.setCellValue("处理结果");
            cell.setCellStyle(style);
            cell = row.createCell((short)4);
            cell.setCellValue("备注");
            cell.setCellStyle(style);
            sheet.addMergedRegion(new Region(0,(short)4,0,(short)5));
            cell = row.createCell((short)6);
            cell.setCellValue("报警时间");
            cell.setCellStyle(style);
            sheet.addMergedRegion(new Region(0,(short)6,0,(short)7));
            // 第五步，写入实体数据

            AlarmsDefences alarmsDefencesParameter = new AlarmsDefences();
            alarmsDefencesParameter.setCustomerId(UserUtils.getUser().getCustomerID());
			List<AlarmsDefences> list = alarmsDefencesDao.findAlarmsDefencesByTime(alarmsDefencesParameter);
            for(AlarmsDefences alarmsDefencesTemp : list){
                alarmsDefencesTemp.setTypeName(AlarmTypeName.getByType(Integer.parseInt(alarmsDefencesTemp.getTypeName())).getAlarmTypeName());
                alarmsDefencesTemp.setState(AlarmStateName.getByState(Integer.parseInt(alarmsDefencesTemp.getState())).getAlarmStateName());
            }

            for(int i=0;i<list.size();i++){
                row =sheet.createRow((int)i+1);
                AlarmsDefences alarmsDefences = list.get(i);
                //创建单元格并赋值
                row.createCell((short)0).setCellValue(alarmsDefences.getCustomersName());
                row.createCell((short)1).setCellValue(alarmsDefences.getDefencesName());
                row.createCell((short)2).setCellValue(alarmsDefences.getTypeName());
                row.createCell((short)3).setCellValue(alarmsDefences.getState());
                row.createCell((short)4).setCellValue(alarmsDefences.getRemark());
                cell = row.createCell((short)6);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(alarmsDefences.getDate()));
            }
            //第六步，将文件存在指定位置
           /* FileOutputStream fout = new FileOutputStream("F:/test.xlsx");
            wb.write(fout);
            fout.close();*/


            //选择保存路径
            response.addHeader("Content-Disposition","attachment;filename="+fileName);
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            wb.write(os);
            os.close();


			/*new ExportExcel("报警信息",AlarmsDefences.class).setDataList(list).write(response,fileName).dispose();*/
            /*Page<Alarms> page = alarmsService.findPage(new Page<Alarms>(request, response, -1), alarms);*/
    		/*new ExportExcel("报警信息", Alarms.class).setDataList(page.getList()).write(response, fileName).dispose();*/

            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出报警信息失败！失败信息："+e.getMessage());
        }
        return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsdef/?repage";
    }
    /**
     * 导出excel文件AlarmsCount数据
     */
    @RequiresPermissions("lu:alarms:export")
    @RequestMapping(value = "exportcount", method=RequestMethod.POST)
    public String exportFileCount(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "报警统计信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xls";
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet =wb.createSheet("报警信息统计表");
            HSSFRow row = sheet.createRow((int)0);
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue("客户名");
            cell.setCellStyle(style);
            int count1 = 0;
            for(AlarmTypeName alarmTypeNameTemp : AlarmTypeName.values()){
                count1++;
                cell = row.createCell((short)count1);
                cell.setCellValue(alarmTypeNameTemp.getAlarmTypeName());
                cell.setCellStyle(style);
            }

            AlarmsCount alarmsCountTemp = new AlarmsCount();
            alarmsCountTemp.setCustomerid(UserUtils.getUser().getCustomerID());
            alarmsCountTemp.setAlarmTypeNameList(new ArrayList(Arrays.asList(AlarmTypeName.values())));
            List<AlarmsCount> list = alarmsDao.getAlarmsCount(alarmsCountTemp);

            for(int i=0;i<list.size();i++){
                row =sheet.createRow((int)i+1);
                AlarmsCount alarmsCount = list.get(i);
                //创建单元格并赋值
                row.createCell((short)0).setCellValue(alarmsCount.getName());
                Class alarmsCountClass = AlarmsCount.class;
                int count2 = 0;
                for(AlarmTypeName alarmTypeNameTemp : AlarmTypeName.values()){
                    count2++;
                    Method method = alarmsCountClass.getMethod("getWARNING" + count2);
                    row.createCell((short)count2).setCellValue((int)method.invoke(alarmsCount));
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
            addMessage(redirectAttributes, "导出报警信息失败！失败信息："+e.getMessage());
        }
        return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsCount/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("lu:alarms:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Alarms> list = ei.getDataList(Alarms.class);
			for (Alarms alarms : list){
				try{
					alarmsService.save(alarms);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条报警信息。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条报警信息"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入报警信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsdef/?repage";
    }
	
	/**
	 * 下载导入报警信息数据模板
	 */
	@RequiresPermissions("lu:alarms:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "报警信息数据导入模板.xlsx";
    		List<Alarms> list = Lists.newArrayList(); 
    		new ExportExcel("报警信息数据", Alarms.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsdef/?repage";
    }

	@RequestMapping(value = "layuiTest")
	public String layuiTest(){
		return "modules/lu/test/hello";
	}

}