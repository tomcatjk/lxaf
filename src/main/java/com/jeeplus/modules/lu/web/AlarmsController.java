/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.lu.web;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.lu.dao.AlarmsDao;
import com.jeeplus.modules.lu.entity.*;
import com.jeeplus.modules.lu.service.*;
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

/**
 * 报警记录表Controller
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
        if("0".equals(user.getCustomerID())){
			total = alarmsService.findAlarmsInfoAcd(paramMap).size();
			paramMap.put("currentPage", (currentPage-1) * pageSize);
            list = alarmsService.findAlarmsInfoAcd(paramMap);
        }else{
			paramMap.put("cid", user.getCustomerID());
			total = alarmsService.findAlarmsInfoAcd(paramMap).size();
			paramMap.put("currentPage", (currentPage-1) * pageSize);
			list = alarmsService.findAlarmsInfoAcd(paramMap);
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
		servicePersons.setState(1);
		servicePersonsService.updateById(servicePersons);
		//新增报警记录(sqlserver)
		User user = UserUtils.getUser();
		ServiceRecords serviceRecords = new ServiceRecords();
		serviceRecords.setCreatetime(new Date());
		serviceRecords.setCreator(user.getId());
		Devices devices = devicesService.findUniqueByProperty("defenceid",alarms.getDefenceid());//通过防区得到设备id
		serviceRecords.setDevicecode(devices == null ? "-1" : devices.getDid());//将设备id赋给设备编码
		serviceRecords.setServerid(Integer.parseInt(servicePersonsId));
		serviceRecords.setState(0);
		serviceRecordsService.addServiceRecords(serviceRecords);
		//向AFCustomers中添加记录
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
		return "ok";
	}

	/**
	 * 已处理选项卡-完成
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "completeAlarms")
	public String completeAlarms(String aid, String rated){
		//为报警记录添加记录时间以及更改报警状态（mysql)
		Alarms alarms = alarmsService.findUniqueByProperty("aid",aid);
		alarms.setRecordTime(new Date());
		alarms.setState(3);
		alarmsService.updateByAid(alarms);
		//更新服务人员状态(sqlserver)
		String servicePersonsId = alarms.getServicename();//存在servicename中的是人员id
		ServicePersons servicePersons = servicePersonsService.findUniqueByProperty("id", servicePersonsId);
		servicePersons.setState(0);
		servicePersonsService.updateById(servicePersons);
		//更新报警记录(sqlserver)
		//结果：没法取到sqlserver中的报警记录
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
	 * 记录报警信息列表页面
	 */
	@RequiresPermissions("lu:alarms:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/lu/alarmsList";
	}

    /**
     * 报警详单记录
     */
    @RequiresPermissions("lu:ararmsde:list")
    @RequestMapping(value = "alarmsdef")
    public String alarmsDefenceslist(AlarmsDefences alarmsDefences, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user=UserUtils.getUser();
    	alarmsDefences.setId(user.getId());
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
     * 记录报警类型的统计页面
     */

    @RequiresPermissions("lu:count:list")
    @RequestMapping(value = "count")
    public String CountList(Alarms alarms,AlarmsCount alarmsCount, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user=UserUtils.getUser();
		alarmsCount.setId(user.getId());
        if(alarms.getCustomerid()!=null&&alarms.getCustomerid()!=""){
            alarmsCount.setName(alarms.getCustomerid());
        }
        Page<AlarmsCount> page = alarmsService.findAlarmsCount(new Page<AlarmsCount>(request, response),alarmsCount);
        model.addAttribute("page",page);
        model.addAttribute("alarmscount",alarmsCount);

		List alarmTypeList = new ArrayList();
		int i = 0;
		for(AlarmTypeName alarmTypeNameTemp : AlarmTypeName.values()){
			alarmTypeList.add(i, alarmTypeNameTemp.getAlarmTypeName());
			i++;
		}
		model.addAttribute("alarmTypeList", alarmTypeList);
        return "modules/lu/alarmsCount";
    }

	/**
	 * 查看，增加，编辑记录报警信息表单页面
	 */
	@RequiresPermissions(value={"lu:alarms:view","lu:alarms:add","lu:alarms:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Alarms alarms, Model model) {
		model.addAttribute("alarms", alarms);
		return "modules/lu/alarmsForm";
	}

	/**
	 * 保存记录报警信息
	 */
	@RequiresPermissions(value={"lu:alarms:add","lu:alarms:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Alarms alarms, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, alarms)){
			return form(alarms, model);
		}
		if(!alarms.getIsNewRecord()){//编辑表单保存
			Alarms t = alarmsService.get(alarms.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(alarms, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			alarmsService.save(t);//保存
		}else{//新增表单保存
			alarmsService.save(alarms);//保存
		}
		addMessage(redirectAttributes, "保存记录报警信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsdef/?repage";
	}
	
	/**
	 * 删除记录报警信息
	 */
	@RequiresPermissions("lu:alarms:del")
	@RequestMapping(value = "delete")
	public String delete(Alarms alarms, RedirectAttributes redirectAttributes) {
		alarmsService.delete(alarms);
		addMessage(redirectAttributes, "删除记录报警信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsdef/?repage";
	}

	
	/**
	 * 批量删除记录报警信息
	 */
	@RequiresPermissions("lu:alarms:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			alarmsService.delete(alarmsService.get(id));
		}
		addMessage(redirectAttributes, "删除记录报警信息成功");
		return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsdef/?repage";
	}

    /**
     * 导出excel文件AlarmsDefences
     */
    @RequiresPermissions("lu:alarms:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "记录报警详单信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xls";
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
            cell.setCellValue("报警id");
            cell.setCellStyle(style);
            cell = row.createCell((short)1);
            cell.setCellValue("客户名");
            cell.setCellStyle(style);
            cell = row.createCell((short)2);
            cell.setCellValue("防区名");
            cell.setCellStyle(style);
            cell = row.createCell((short)3);
            cell.setCellValue("报警类型");
            cell.setCellStyle(style);
            cell = row.createCell((short)4);
            cell.setCellValue("处理结果");
            cell.setCellStyle(style);
            cell = row.createCell((short)5);
            cell.setCellValue("备注");
            cell.setCellStyle(style);
            sheet.addMergedRegion(new Region(0,(short)5,0,(short)6));
            cell = row.createCell((short)7);
            cell.setCellValue("报警时间");
            cell.setCellStyle(style);
            sheet.addMergedRegion(new Region(0,(short)7,0,(short)8));
            // 第五步，写入实体数据
			List<AlarmsDefences> list = alarmsService.findAlarmsDefencesAll();

            for(int i=0;i<list.size();i++){
                row =sheet.createRow((int)i+1);
                AlarmsDefences alarmsDefences = list.get(i);
                //创建单元格并赋值
                row.createCell((short)0).setCellValue(alarmsDefences.getId());
                row.createCell((short)1).setCellValue(alarmsDefences.getCustomersName());
                row.createCell((short)2).setCellValue(alarmsDefences.getDefencesName());
                row.createCell((short)3).setCellValue(alarmsDefences.getTypeName());
                row.createCell((short)4).setCellValue(alarmsDefences.getState());
                row.createCell((short)5).setCellValue(alarmsDefences.getRemark());
                cell = row.createCell((short)7);
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


			/*new ExportExcel("记录报警信息",AlarmsDefences.class).setDataList(list).write(response,fileName).dispose();*/
            /*Page<Alarms> page = alarmsService.findPage(new Page<Alarms>(request, response, -1), alarms);*/
    		/*new ExportExcel("记录报警信息", Alarms.class).setDataList(page.getList()).write(response, fileName).dispose();*/

            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出记录报警信息记录失败！失败信息："+e.getMessage());
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
            String fileName = "记录报警统计信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xls";
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet =wb.createSheet("报警信息统计表");
            HSSFRow row = sheet.createRow((int)0);
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            User user=UserUtils.getUser();
            AlarmsCount alarmsCountTemp = new AlarmsCount();
            alarmsCountTemp.setId(user.getId());

            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue("客户名");
            cell.setCellStyle(style);
            cell = row.createCell((short)1);
            cell.setCellValue("二十四小时紧急报警");
            cell.setCellStyle(style);
            cell = row.createCell((short)2);
            cell.setCellValue("个人救护");
            cell.setCellStyle(style);
            cell = row.createCell((short)3);
            cell.setCellValue("紧急");
            cell.setCellStyle(style);
            cell = row.createCell((short)4);
            cell.setCellValue("温感报警");
            cell.setCellStyle(style);
            cell = row.createCell((short)5);
            cell.setCellValue("防区报警1");
            cell.setCellStyle(style);
            cell = row.createCell((short)6);
            cell.setCellValue("防区报警2");
            cell.setCellStyle(style);
            cell = row.createCell((short)7);
            cell.setCellValue("防区报警3");
            cell.setCellStyle(style);
            cell = row.createCell((short)8);
            cell.setCellValue("防区报警4");
            cell.setCellStyle(style);
            cell = row.createCell((short)9);
            cell.setCellValue("防区报警5");
            cell.setCellStyle(style);
            cell = row.createCell((short)10);
            cell.setCellValue("报警恢复1");
            cell.setCellStyle(style);
            cell = row.createCell((short)11);
            cell.setCellValue("报警恢复2");
            cell.setCellStyle(style);
            cell = row.createCell((short)12);
            cell.setCellValue("报警恢复3");
            cell.setCellStyle(style);
            cell = row.createCell((short)13);
            cell.setCellValue("报警恢复4");
            cell.setCellStyle(style);
            cell = row.createCell((short)14);
            cell.setCellValue("报警恢复5");
            cell.setCellStyle(style);
            cell = row.createCell((short)15);
            cell.setCellValue("报警恢复6");
            cell.setCellStyle(style);
            cell = row.createCell((short)16);
            cell.setCellValue("报警恢复7");
            cell.setCellStyle(style);
            cell = row.createCell((short)17);
            cell.setCellValue("火警1");
            cell.setCellStyle(style);
            cell = row.createCell((short)18);
            cell.setCellValue("火警2");
            cell.setCellStyle(style);
            cell = row.createCell((short)19);
            cell.setCellValue("火警3");
            cell.setCellStyle(style);
            cell = row.createCell((short)20);
            cell.setCellValue("火警4");
            cell.setCellStyle(style);
            cell = row.createCell((short)21);
            cell.setCellValue("火警5");
            cell.setCellStyle(style);
            cell = row.createCell((short)22);
            cell.setCellValue("火警6");
            cell.setCellStyle(style);
            cell = row.createCell((short)23);
            cell.setCellValue("火警7");
            cell.setCellStyle(style);
            cell = row.createCell((short)24);
            cell.setCellValue("劫盗1");
            cell.setCellStyle(style);
            cell = row.createCell((short)25);
            cell.setCellValue("劫盗2");
            cell.setCellStyle(style);
            cell = row.createCell((short)26);
            cell.setCellValue("劫盗3");
            cell.setCellStyle(style);
            cell = row.createCell((short)27);
            cell.setCellValue("劫盗4");
            cell.setCellStyle(style);
            cell = row.createCell((short)28);
            cell.setCellValue("窃盗1");
            cell.setCellStyle(style);
            cell = row.createCell((short)29);
            cell.setCellValue("窃盗2");
            cell.setCellStyle(style);
            cell = row.createCell((short)30);
            cell.setCellValue("窃盗3");
            cell.setCellStyle(style);
            cell = row.createCell((short)31);
            cell.setCellValue("窃盗4");
            cell.setCellStyle(style);
            cell = row.createCell((short)32);
            cell.setCellValue("窃盗5");
            cell.setCellStyle(style);
            cell = row.createCell((short)33);
            cell.setCellValue("窃盗6");
            cell.setCellStyle(style);
            cell = row.createCell((short)34);
            cell.setCellValue("窃盗7");
            cell.setCellStyle(style);
            cell = row.createCell((short)35);
            cell.setCellValue("窃盗8");
            cell.setCellStyle(style);
            cell = row.createCell((short)36);
            cell.setCellValue("窃盗9");
            cell.setCellStyle(style);
            cell = row.createCell((short)37);
            cell.setCellValue("气体泄漏报警");
            cell.setCellStyle(style);
            cell = row.createCell((short)38);
            cell.setCellValue("水侵报警");
            cell.setCellStyle(style);
            cell = row.createCell((short)39);
            cell.setCellValue("故障事件1");
            cell.setCellStyle(style);
            cell = row.createCell((short)40);
            cell.setCellValue("交流电恢复");
            cell.setCellStyle(style);
            cell = row.createCell((short)41);
            cell.setCellValue("无交流");
            cell.setCellStyle(style);
            cell = row.createCell((short)42);
            cell.setCellValue("系统电池电压低");
            cell.setCellStyle(style);
            cell = row.createCell((short)43);
            cell.setCellValue("系统电池电压恢复");
            cell.setCellStyle(style);
            cell = row.createCell((short)44);
            cell.setCellValue("校验故障1");
            cell.setCellStyle(style);
            cell = row.createCell((short)45);
            cell.setCellValue("校验故障2");
            cell.setCellStyle(style);
            cell = row.createCell((short)46);
            cell.setCellValue("系统重新设定1");
            cell.setCellStyle(style);
            cell = row.createCell((short)47);
            cell.setCellValue("系统重新设定2");
            cell.setCellStyle(style);
            cell = row.createCell((short)48);
            cell.setCellValue("编程被改动");
            cell.setCellStyle(style);
            cell = row.createCell((short)49);
            cell.setCellValue("主机停机");
            cell.setCellStyle(style);
            cell = row.createCell((short)50);
            cell.setCellValue("故障事件2");
            cell.setCellStyle(style);
            cell = row.createCell((short)51);
            cell.setCellValue("警号故障1");
            cell.setCellStyle(style);
            cell = row.createCell((short)52);
            cell.setCellValue("警号故障2");
            cell.setCellStyle(style);
            cell = row.createCell((short)53);
            cell.setCellValue("警号故障3");
            cell.setCellStyle(style);
            cell = row.createCell((short)54);
            cell.setCellValue("总线开路");
            cell.setCellStyle(style);
            cell = row.createCell((short)55);
            cell.setCellValue("总线恢复");
            cell.setCellStyle(style);
            cell = row.createCell((short)56);
            cell.setCellValue("电话线故障1");
            cell.setCellStyle(style);
            cell = row.createCell((short)57);
            cell.setCellValue("电话线故障2");
            cell.setCellStyle(style);
            cell = row.createCell((short)58);
            cell.setCellValue("通讯失败");
            cell.setCellStyle(style);
            cell = row.createCell((short)59);
            cell.setCellValue("无线感应器电池低");
            cell.setCellStyle(style);
            cell = row.createCell((short)60);
            cell.setCellValue("无线感应器电池低恢复");
            cell.setCellStyle(style);
            cell = row.createCell((short)61);
            cell.setCellValue("无线监控失败");
            cell.setCellStyle(style);
            cell = row.createCell((short)62);
            cell.setCellValue("无线监控恢复");
            cell.setCellStyle(style);
            cell = row.createCell((short)63);
            cell.setCellValue("主机防拆报警");
            cell.setCellStyle(style);
            cell = row.createCell((short)64);
            cell.setCellValue("探测器防拆报警");
            cell.setCellStyle(style);

            List<AlarmsCount> list = alarmsDao.getAlarmsCount(alarmsCountTemp);

            for(int i=0;i<list.size();i++){
                row =sheet.createRow((int)i+1);
                AlarmsCount alarmsCount = list.get(i);
                //创建单元格并赋值
                row.createCell((short)0).setCellValue(alarmsCount.getName());
                row.createCell((short)1).setCellValue(alarmsCount.getWARNING1());
                row.createCell((short)2).setCellValue(alarmsCount.getWARNING2());
                row.createCell((short)3).setCellValue(alarmsCount.getWARNING3());
                row.createCell((short)4).setCellValue(alarmsCount.getWARNING4());
                row.createCell((short)5).setCellValue(alarmsCount.getWARNING5());
                row.createCell((short)6).setCellValue(alarmsCount.getWARNING6());
                row.createCell((short)7).setCellValue(alarmsCount.getWARNING7());
                row.createCell((short)8).setCellValue(alarmsCount.getWARNING8());
                row.createCell((short)9).setCellValue(alarmsCount.getWARNING9());
                row.createCell((short)10).setCellValue(alarmsCount.getWARNING10());
                row.createCell((short)11).setCellValue(alarmsCount.getWARNING11());
                row.createCell((short)12).setCellValue(alarmsCount.getWARNING12());
                row.createCell((short)13).setCellValue(alarmsCount.getWARNING13());
                row.createCell((short)14).setCellValue(alarmsCount.getWARNING14());
                row.createCell((short)15).setCellValue(alarmsCount.getWARNING15());
                row.createCell((short)16).setCellValue(alarmsCount.getWARNING16());
                row.createCell((short)17).setCellValue(alarmsCount.getWARNING17());
                row.createCell((short)18).setCellValue(alarmsCount.getWARNING18());
                row.createCell((short)19).setCellValue(alarmsCount.getWARNING19());
                row.createCell((short)20).setCellValue(alarmsCount.getWARNING20());
                row.createCell((short)21).setCellValue(alarmsCount.getWARNING21());
                row.createCell((short)22).setCellValue(alarmsCount.getWARNING22());
                row.createCell((short)23).setCellValue(alarmsCount.getWARNING23());
                row.createCell((short)24).setCellValue(alarmsCount.getWARNING24());
                row.createCell((short)25).setCellValue(alarmsCount.getWARNING25());
                row.createCell((short)26).setCellValue(alarmsCount.getWARNING26());
                row.createCell((short)27).setCellValue(alarmsCount.getWARNING27());
                row.createCell((short)28).setCellValue(alarmsCount.getWARNING28());
                row.createCell((short)29).setCellValue(alarmsCount.getWARNING29());
                row.createCell((short)30).setCellValue(alarmsCount.getWARNING30());
                row.createCell((short)31).setCellValue(alarmsCount.getWARNING31());
                row.createCell((short)32).setCellValue(alarmsCount.getWARNING32());
                row.createCell((short)33).setCellValue(alarmsCount.getWARNING33());
                row.createCell((short)34).setCellValue(alarmsCount.getWARNING34());
                row.createCell((short)35).setCellValue(alarmsCount.getWARNING35());
                row.createCell((short)36).setCellValue(alarmsCount.getWARNING36());
                row.createCell((short)37).setCellValue(alarmsCount.getWARNING37());
                row.createCell((short)38).setCellValue(alarmsCount.getWARNING38());
                row.createCell((short)39).setCellValue(alarmsCount.getWARNING39());
                row.createCell((short)40).setCellValue(alarmsCount.getWARNING40());
                row.createCell((short)41).setCellValue(alarmsCount.getWARNING41());
                row.createCell((short)42).setCellValue(alarmsCount.getWARNING42());
                row.createCell((short)43).setCellValue(alarmsCount.getWARNING43());
                row.createCell((short)44).setCellValue(alarmsCount.getWARNING44());
                row.createCell((short)45).setCellValue(alarmsCount.getWARNING45());
                row.createCell((short)46).setCellValue(alarmsCount.getWARNING46());
                row.createCell((short)47).setCellValue(alarmsCount.getWARNING47());
                row.createCell((short)48).setCellValue(alarmsCount.getWARNING48());
                row.createCell((short)49).setCellValue(alarmsCount.getWARNING49());
                row.createCell((short)50).setCellValue(alarmsCount.getWARNING50());
                row.createCell((short)51).setCellValue(alarmsCount.getWARNING51());
                row.createCell((short)52).setCellValue(alarmsCount.getWARNING52());
                row.createCell((short)53).setCellValue(alarmsCount.getWARNING53());
                row.createCell((short)54).setCellValue(alarmsCount.getWARNING54());
                row.createCell((short)55).setCellValue(alarmsCount.getWARNING55());
                row.createCell((short)56).setCellValue(alarmsCount.getWARNING56());
                row.createCell((short)57).setCellValue(alarmsCount.getWARNING57());
                row.createCell((short)58).setCellValue(alarmsCount.getWARNING58());
                row.createCell((short)59).setCellValue(alarmsCount.getWARNING59());
                row.createCell((short)60).setCellValue(alarmsCount.getWARNING60());
                row.createCell((short)61).setCellValue(alarmsCount.getWARNING61());
                row.createCell((short)62).setCellValue(alarmsCount.getWARNING62());
                row.createCell((short)63).setCellValue(alarmsCount.getWARNING63());
                row.createCell((short)64).setCellValue(alarmsCount.getWARNING64());

            }


            //选择保存路径
            response.addHeader("Content-Disposition","attachment;filename="+fileName);
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            wb.write(os);
            os.close();

            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出记录报警信息记录失败！失败信息："+e.getMessage());
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
				failureMsg.insert(0, "，失败 "+failureNum+" 条记录报警信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条记录报警信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入记录报警信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/lu/alarms/alarmsdef/?repage";
    }
	
	/**
	 * 下载导入记录报警信息数据模板
	 */
	@RequiresPermissions("lu:alarms:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "记录报警信息数据导入模板.xlsx";
    		List<Alarms> list = Lists.newArrayList(); 
    		new ExportExcel("记录报警信息数据", Alarms.class, 1).setDataList(list).write(response, fileName).dispose();
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