package com.jeeplus.modules.app.controller;

import com.jeeplus.modules.app.entity.DefencesDevice;
import com.jeeplus.modules.app.service.AppService;
import com.jeeplus.modules.lu.entity.*;
import com.jeeplus.modules.lu.service.CustomersService;
import com.jeeplus.modules.lu.service.DefencesService;
import com.jeeplus.modules.lu.service.DevicesService;
import com.jeeplus.modules.lu.service.MastersService;
import com.jeeplus.modules.sys.dao.UserDao;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.cxf.binding.corba.wsdl.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BUWAN on 2017/6/19.
 */

@Controller
@RequestMapping(value = "${adminPath}/app")
public class AppController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomersService customersService;

    @Autowired
    private DevicesService devicesService;

    @RequestMapping(value = "test")
    public String test(){
        return "/modules/lu/test/hello2";
    }

    /**
     * 登录
     * @param loginName
     * @param pwd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "login")
    public String login(@RequestParam(value="loginName")String loginName,
                        @RequestParam(value="pwd")String pwd) {
        User user = userDao.findUniqueByProperty("login_name", loginName);
        JSONObject data = new JSONObject();
        if(user != null){
            if(SystemService.validatePassword(pwd, user.getPassword())){
                Customers customers = customersService.findUniqueByProperty("cid", user.getCustomerID());
                Map map = new HashedMap();
                map.put("userid", user.getId());
                map.put("username", user.getName());
                map.put("tel", user.getPhone()); //user.phone为null
                if(customers != null) {
                    map.put("address", customers.getAddress()); //user没有地址
                }else{
                    map.put("address", "无"); //user没有地址
                }
                map.put("cid",user.getCustomerID());
                data.put("result", "success");
                data.put("data",map);
                return data.toString();
            }
        }
        data.put("result", "fail");
        data.put("data","用户名或密码错误");

        return data.toString();
    }

    /**
     * 修改密码
     * @param userid
     * @param oldpwd
     * @param newpwd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "changepwd")
    public String changepwd(@RequestParam(value="userid")String userid,
                            @RequestParam(value="oldpwd")String oldpwd,
                            @RequestParam(value="newpwd")String newpwd) {
        User user = userDao.findUniqueByProperty("id", userid);
        JSONObject data = new JSONObject();
        if(user != null){
            if(SystemService.validatePassword(oldpwd, user.getPassword())){
                user.setPassword(SystemService.entryptPassword(newpwd));
                userDao.update(user);
                data.put("result", "success");
                data.put("data", "");
            }else{
                data.put("result", "fail");
                data.put("data","旧密码错误");
            }
        }
        return data.toString();
    }


    /**
     * 更新个人中心
     * @param userid
     * @param username
     * @param tel
     * @param address
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateuser")
    public String updateuser(@RequestParam(value="userid")String userid,
                            @RequestParam(value="username")String username,
                            @RequestParam(value="tel")String tel,
                            @RequestParam(value="address")String address) {
        User user = userDao.findUniqueByProperty("id", userid);
        JSONObject data = new JSONObject();
        if(user != null){
            Customers customers = customersService.findUniqueByProperty("cid", user.getCustomerID());
            user.setName(username);
            user.setPhone(tel);
            customers.setAddress(address);
            customersService.editByCid(customers);
            userDao.update(user);
            try {
                userDao.update(user);
            }catch (Exception e){
                data.put("result", "fail");
                data.put("data","更新失败");
            }
            data.put("result", "success");
            data.put("data", "");
        }
        return data.toString();
    }

    /**
     * 设备列表
     * @param userid
     * @param cid
     * @param devicetype
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getdevice")
    public String getdevice(@RequestParam(value="userid")String userid,
                             @RequestParam(value="cid")String cid,
                             @RequestParam(value="devicetype")int devicetype,
                             @RequestParam(value="page")int page) {
        JSONObject data = new JSONObject();
        Map paramMap =  new HashedMap();
        paramMap.put("userid", userid);
        paramMap.put("customerid", cid);
        paramMap.put("devicetype", devicetype);
        paramMap.put("pageBegin", (page - 1) * 10);
        paramMap.put("pageEnd", page * 10);
        List<Map> deviceList = devicesService.findByCidAndDevicetypeAndPage(paramMap);
        int state = 0;
        for(int i = 0; i < deviceList.size(); i++){
            if(deviceList.get(i).get("devicemode") == null){
                deviceList.get(i).put("devicemode", "");
            }
            deviceList.get(i).put("createtime", deviceList.get(i).get("createtime").toString().substring(0,10));
            state = Integer.parseInt(deviceList.get(i).get("state").toString());
            deviceList.get(i).put("statename", DeviceStateName.getByState(state).getDeviceStateName());
        }
        data.put("more", true);
        data.put("list", deviceList);
        return data.toString();
    }

    /**
     * 查看设备明细
     * @param userid
     * @param cid
     * @param deviceid
     * @param devicetype
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "loaddevice")
    public String loaddevice(@RequestParam(value="userid")String userid,
                            @RequestParam(value="cid")String cid,
                            @RequestParam(value="deviceid")String deviceid,
                            @RequestParam(value="devicetype")int devicetype) {
        JSONObject data = new JSONObject();
        Map paramMap =  new HashedMap();
        paramMap.put("did", deviceid);
        paramMap.put("devicetype", devicetype);
        Map objectMap = devicesService.findByDeviceid(paramMap);
        if(objectMap != null){
            if(objectMap.get("devicemode") == null){
                objectMap.put("devicemode", "");
            }
            if(objectMap.get("state") == null){
                objectMap.put("state", 1);
            }
            if(objectMap.get("defencetype") == null){
                objectMap.put("defencetype", 1);
            }
            objectMap.put("createtime", objectMap.get("createtime").toString().substring(0,10));
            int defencetypeTemp = Integer.parseInt(objectMap.get("defencetype").toString());
            int devicetypenameTemp = Integer.parseInt(objectMap.get("devicetype").toString());
            int stateTemp = Integer.parseInt(objectMap.get("state").toString());
            objectMap.put("devicetypename", DeviceTypeName.getByType(devicetypenameTemp).getDeviceTypeName());
            objectMap.put("defencetypename", DefenceTypeName.getByType(defencetypeTemp).getDefenceTypeName());
            objectMap.put("statename", DeviceStateName.getByState(stateTemp).getDeviceStateName());
            data.put("result", "success");
            data.put("data", objectMap);
        }else{
            data.put("result", "fail");
            data.put("data", "主机信息不存在");
        }
        return data.toString();
    }

    /**
     * 删除设备
     * @param userid
     * @param cid
     * @param deviceid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "deldevice")
    public String deldevice(@RequestParam(value="userid")String userid,
                            @RequestParam(value="cid")String cid,
                            @RequestParam(value="deviceid")String deviceid) {
        JSONObject data = new JSONObject();
        Devices paramDevices = new Devices();
        paramDevices.setDid(deviceid);
        if(devicesService.findUniqueByProperty("did", deviceid) !=null){
            devicesService.delete(paramDevices);
            data.put("result", "success");
            data.put("data", "");
        }else {
            data.put("result", "fail");
            data.put("data", "");
        }
        return data.toString();
    }

    /**
     * 布撤防
     * @param userid
     * @param cid
     * @param tag
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "setdefence")
    public String deldevice(@RequestParam(value="userid")String userid,
                            @RequestParam(value="cid")String cid,
                            @RequestParam(value="tag")int tag) {
        JSONObject data = new JSONObject();
        Devices paramDevices = new Devices();
        try {
            List<Masters> mastersList = mastersService.findMastersListByCid(cid);
            Masters masters = mastersList.get(0);
            String action = "";
            String deviceCode = masters.getCode();
            if(tag == 1){
                action = "SetGarrison";
            }else if(tag == 2){
                action = "SetDisarm";
            }else{
                action = "SetGarrison";
            }

            String strURL = "http://localhost/api/fenceapi.ashx?action=" + action + "&DeviceCode=" + deviceCode;
            URL url = new URL(strURL);
            HttpURLConnection httpConn = (HttpURLConnection)
                    url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            httpConn.disconnect();

            data.put("result", "success");
            data.put("data", "");
        }catch (Exception e){
            data.put("result", "fail");
            data.put("data", "主机暂未连接，布防命令已保存，联机后下发");
        }
        return data.toString();
    }

    @Autowired
    AppService appService;
    @Autowired
    MastersService mastersService;
    @Autowired
    DefencesService defencesService;

    /**
     * 报警类型
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "alarmtype")
    public String alarmtype(){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name","水浸");
        jsonObject.put("value",1);
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("name","火警");
        jsonObject1.put("value",2);
        JSONObject jsonObject2=new JSONObject();
        jsonObject2.put("name","sos");
        jsonObject2.put("value",3);
        JSONArray jsonArray=new JSONArray();
        jsonArray.add(jsonObject);
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
        System.out.println(jsonArray);
        return jsonArray.toString();
    }

    /**
     * 报警记录
     * @param userid
     * @param cid
     * @param startdate
     * @param enddate
     * @param page
     * @param alarmtype
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "alarmrecord")
    public String alarmrecord(@RequestParam(value = "userid") String userid,
                              @RequestParam(value = "cid") String cid,
                              @RequestParam(value = "startdate") Date startdate,
                              @RequestParam(value = "enddate") Date enddate,
                              @RequestParam(value = "page") int page,
                              @RequestParam(value = "alarmtype",required = false) int alarmtype){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Map map=new HashMap();
        map.put("cid",cid);
        map.put("startdate",sdf.format(startdate));
        map.put("enddate",sdf.format(enddate));
        System.out.println("map"+map);
        List list=appService.selectAlarmrecord(map);
        System.out.println("list"+list+"json"+JSONArray.fromObject(list).toString());
        return JSONArray.fromObject(list).toString();
    }

    /**
     * 防区类型
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "defencttype")
    public String defencttype(){
        JSONArray jsonArray=new JSONArray();
        for(DefenceTypeName defenceTypeNameObject : DefenceTypeName.values()){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",defenceTypeNameObject.getDefenceTypeName());
            jsonObject.put("value",defenceTypeNameObject.getDefenceType());
            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray);
        return jsonArray.toString();
    }

    /**
     * 设备类别
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "devicetype")
    public String devicetype(){
        JSONArray jsonArray=new JSONArray();
        for(DeviceTypeName deviceTypeNameObject : DeviceTypeName.values()){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",deviceTypeNameObject.getDeviceTypeName());
            jsonObject.put("value",deviceTypeNameObject.getDeviceType());
            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray);
        return jsonArray.toString();
    }

    /**
     *防区列表
     * @param userid
     * @param cid
     * @param masterid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getdefence")
    public String getdefence(@RequestParam(value = "userid")String userid,
                             @RequestParam(value = "cid")String cid,
                             @RequestParam(value = "masterid",required = false)String masterid){
        System.out.println("userid"+userid+"cid"+cid+"masterid"+masterid);
        List<DefencesDevice> list=appService.getdefence(cid);
        System.out.println("list"+list);
        for (DefencesDevice defencesDevice:list){
            int defencetype=defencesDevice.getDefencetype();
            if(defencetype!=0){
                defencesDevice.setDefencetypename(DefenceTypeName.getByType(defencetype).getDefenceTypeName());
            }
            int devicetype=defencesDevice.getDevicetype();
            if(devicetype!=0){
                defencesDevice.setDevicetypename(DeviceTypeName.getByType(devicetype).getDeviceTypeName());
            }
            int state=defencesDevice.getState();
            if(state==1){
                defencesDevice.setStatename("开启");
            }else if (state==2){
                defencesDevice.setStatename("禁用");
            }
        }
        System.out.println(JSONArray.fromObject(list).toString());
        return JSONArray.fromObject(list).toString();
    }

    /**
     * 新增修改设备
     * @param userid
     * @param cid
     * @param devicerid
     * @param devicetype
     * @param devicename
     * @param version
     * @param state
     * @param defencencode
     * @param defencename
     * @param defencetype
     * @param devicemode
     * @param masterid
     * @param sim
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updatemaster")
    public String updatemaster(@RequestParam(value = "userid")String userid,
                               @RequestParam(value = "cid") String cid,
                               @RequestParam(value = "devicerid") String devicerid,
                               @RequestParam(value = "devicetype") Integer devicetype,
                               @RequestParam(value = "devicename") String devicename,
                               @RequestParam(value = "version") String version,
                               @RequestParam(value = "state") Integer state,
                               @RequestParam(value = "defencencode") String defencencode,
                               @RequestParam(value = "defencename") String defencename,
                               @RequestParam(value = "defencetype") Integer defencetype,
                               @RequestParam(value = "devicemode") String devicemode,
                               @RequestParam(value = "masterid") String masterid,
                               @RequestParam(value = "sim") String sim){
        if(userid==null){
            userid="";
        }
        if(cid==null){
            cid="";
        }if(devicerid==null){
            devicerid="";
        }if(devicetype==null){
            devicetype=0;
        }if(devicename==null){
            devicename="";
        }if(version==null){
            version="";
        }if(state==null){
            state=0;
        }if(defencencode==null){
            defencencode="";
        }if(defencename==null){
            defencename="";
        }if(defencetype==null){
            defencetype=0;
        }if(devicemode==null){
            devicemode="";
        }if(masterid==null){
            masterid="";
        }if(sim==null){
            sim="";
        }
        try {

            Map map=new HashMap();
            map.put("mid",masterid);
            map.put("customerid",cid);
            map.put("sim",sim);
            map.put("version",version);
            map.put("state",state);
            mastersService.updatemasters(map);
            Map map1=new HashMap();
            map1.put("mid",masterid);
            map1.put("customerid",cid);
            map1.put("devicerid",devicerid);
            map1.put("devicename",devicename);
            map1.put("devicetype",devicetype);
            map1.put("devicemode",devicemode);
            map1.put("state",state);
            map1.put("userid",userid);
            devicesService.updatedevices(map1);
            Map map2=new HashMap();
            map2.put("mid",masterid);
            map2.put("customerid",cid);
            map2.put("defencencode",defencencode);
            map2.put("defencename",defencename);
            map2.put("defencetype",defencetype);
            map2.put("State",state);
            defencesService.update(map2);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("result","success");
            jsonObject.put("data","");
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("result","fail");
            jsonObject.put("data","更新失败");
            return jsonObject.toString();
        }
    }


}
