package com.jeeplus.modules.app.controller;

import com.jeeplus.modules.app.entity.DefencesDevice;
import com.jeeplus.modules.app.service.AppService;
import com.jeeplus.modules.lu.entity.*;
import com.jeeplus.modules.lu.service.CustomersService;
import com.jeeplus.modules.lu.service.DefencesService;
import com.jeeplus.modules.lu.service.DevicesService;
import com.jeeplus.modules.lu.service.MastersService;
import com.jeeplus.modules.lu.web.DevicesController;
import com.jeeplus.modules.lu.web.MastersController;
import com.jeeplus.modules.sys.dao.UserDao;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.XMLType;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
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

    @Autowired
    private DevicesController devicesController;

    @Autowired
    private MastersController mastersController;

    @Autowired
    private SystemService systemService;

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
                map.put("tel", user.getPhone() != null ? user.getPhone() : ""); //user.phone为null
                if(customers != null) {
                    map.put("address", customers.getAddress()); //user没有地址
                }else{
                    map.put("address", "无"); //user没有地址
                }
                map.put("cid",user.getCustomerID());
                data.put("result", "success");
                data.put("data",map);
            }else{
                data.put("result", "fail");
                data.put("data","用户名或密码错误");
            }
        }else{
            data.put("result", "fail");
            data.put("data","用户名或密码错误");
        }
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
        }else {
            data.put("result", "fail");
            data.put("data","旧密码错误");
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
            if(customers != null) {
                user.setName(username);
                user.setPhone(tel);
                try {
                    customers.setAddress(address);
                    customersService.editByCid(customers);
                    userDao.update(user);
                } catch (Exception e) {
                    data.put("result", "fail");
                    data.put("data", "更新失败");
                }
                data.put("result", "success");
                data.put("data", "");
            }
        }else{
            data.put("result", "fail");
            data.put("data","更新失败");
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
                             @RequestParam(value="page",required = false)Integer page) {
        if(page == 0 || page == null){
            page = 1;
        }
        Map paramMap =  new HashedMap();
        paramMap.put("customerid", cid);
        paramMap.put("devicetype", devicetype);
        paramMap.put("pageBegin", (page - 1) * 10);
        paramMap.put("pageEnd", page * 10);
        String jsonList = "";
        if(devicetype == -1){
            jsonList = getMasters(paramMap);
        }else{
            jsonList = getDevices(paramMap);
        }
        return jsonList;
    }

    public String getMasters(Map map){
        JSONObject data = new JSONObject();
        List<Map> masterList = appService.getMasters(map);
        for(int i = 0; i < masterList.size(); i++){
            masterList.get(i).put("devicetype", -1);
            masterList.get(i).put("statename", DeviceStateName.getByState(Integer.parseInt(masterList.get(i).get("statename").toString())).getDeviceStateName());
        }
        if(masterList.size() < 10){
            data.put("more", false);
        }else{
            data.put("more", true);
        }
        data.put("list", masterList);
        return data.toString();
    }

    public String getDevices(Map map){
        JSONObject data = new JSONObject();
        List<Map> deviceList = appService.getDevices(map);
        if(deviceList.size() < 10){
            data.put("more", false);
        }else{
            data.put("more", true);
        }
        data.put("list", deviceList);
        return data.toString();
    }

    /**
     * 查看设备明细
     * @param userid
     * @param cid
     * @param devicetype
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "loaddevice")
    public String loaddevice(@RequestParam(value="userid")String userid,
                            @RequestParam(value="cid")String cid,
                            @RequestParam(value="deviceid")String did,
                            @RequestParam(value="devicetype")Integer devicetype) {
        JSONObject data = new JSONObject();
        Map paramMap =  new HashedMap();
        paramMap.put("did", did);
        if(devicetype == -1){
            data = getMaster(paramMap);
        }else{
            data = getDevice(paramMap);
        }

        return data.toString();
    }

    public JSONObject getMaster(Map map){
        JSONObject data = new JSONObject();
        Masters masterTemp = mastersService.findUniqueByProperty("mid", map.get("did"));
        if(masterTemp != null) {
            Map objectMap = new HashMap();
            objectMap.put("deviceid", masterTemp.getMid());
            objectMap.put("devicename", masterTemp.getName());
            objectMap.put("devicetype", "-1");
            objectMap.put("devicemode", masterTemp.getMaintype());
            objectMap.put("defencecode", masterTemp.getCode());
            objectMap.put("createtime", new SimpleDateFormat("yyyy-MM-dd").format(masterTemp.getCreatetime()));
            objectMap.put("state", masterTemp.getState());
            objectMap.put("statename", "1".equals(masterTemp.getIsOnline()) ? "上线" : "离线");
            objectMap.put("version", masterTemp.getVersion());
            objectMap.put("sim", masterTemp.getSim());
            System.out.println("loadDevice-masterid:" + objectMap.get("deviceid"));
            data.put("result", "success");
            data.put("data", objectMap);
        }else{
            data.put("result", "fail");
            data.put("data", "主机信息不存在");
        }
        return data;
    }

    public JSONObject getDevice(Map map){
        JSONObject data = new JSONObject();
        Map objectMap = devicesService.findByDeviceid(map);
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
            objectMap.put("createtime", objectMap.get("createtime").toString());
            int defencetypeTemp = Integer.parseInt(objectMap.get("defencetype").toString());
            int devicetypenameTemp = Integer.parseInt(objectMap.get("devicetype").toString());
            int stateTemp = Integer.parseInt(objectMap.get("state").toString());
            objectMap.put("devicetypename", DeviceTypeName.getByType(devicetypenameTemp).getDeviceTypeName());
            objectMap.put("defencetypename", DefenceTypeName.getByType(defencetypeTemp).getDefenceTypeName());
            objectMap.put("statename", DeviceStateName.getByState(stateTemp).getDeviceStateName());
            System.out.println("loadDevice-deviceId:" + objectMap.get("deviceid"));
            data.put("result", "success");
            data.put("data", objectMap);
        }else{
            data.put("result", "fail");
            data.put("data", "设备信息不存在");
        }
        return data;
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
                            @RequestParam(value="devicetype")String devicetype,
                            @RequestParam(value="deviceid")String deviceid) {
        JSONObject data = new JSONObject();
        if("-1".equals(devicetype)){
            Masters master = new Masters();
            master.setMid(deviceid);
            if(mastersService.findUniqueByProperty("mid", master.getMid()) !=null){
                mastersController.delMaster(master);
                data.put("result", "success");
                data.put("data", "");
            }else {
                data.put("result", "fail");
                data.put("data", "");
            }
        }else{
            Devices paramDevices = new Devices();
            paramDevices.setDid(deviceid);
            if(devicesService.findUniqueByProperty("d.did", deviceid) !=null){
                devicesController.delDevice(paramDevices);
                data.put("result", "success");
                data.put("data", "");
            }else {
                data.put("result", "fail");
                data.put("data", "");
            }
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
    public String setdefence(@RequestParam(value="userid")String userid,
                            @RequestParam(value="cid")String cid,
                            @RequestParam(value="tag")int tag) {
        JSONObject data = new JSONObject();
        try {
            List<MastersPart> mastersPartList = mastersService.findMastersListByCid(cid);
            String action = "";
            String masterCode = "";
            if(tag == 1){
                action = "SetGarrison";
            }else if(tag == 2){
                action = "SetDisarm";
            }else{
                action = "SetSolved";
            }
            StringBuffer msg = new StringBuffer();
            StringBuffer msgFalse = new StringBuffer();
            for(MastersPart mastersPartTemp : mastersPartList){
                masterCode = mastersPartTemp.getCode();
                if("true".equals(setFun(action, masterCode))){
                    Masters masterTemp = mastersService.findUniqueByProperty("code", masterCode);
                    if("0".equals(masterTemp.getDisarmState())){
                        masterTemp.setDisarmState("1");
                    }else{
                        masterTemp.setDisarmState("0");
                    }
                    mastersService.updateState(masterTemp);
                    msg.append(mastersPartTemp.getName());
                    msg.append(",");
                }else{
                    msgFalse.append(mastersPartTemp.getName());
                    msgFalse.append(",");
                }
            }
            if("SetGarrison".equals(action)){
                if(msg.length()>1) {
                    msg.append("布防成功");
                }
                if(msgFalse.length()>1){
                    msgFalse.append("布防失败");
                }
            }else{
                if(msg.length()>1) {
                    msg.append("撤防成功");
                }
                if(msgFalse.length()>1){
                    msgFalse.append("撤防失败");
                }
            }
            data.put("result", "success");
            data.put("data", msg.toString() + "  " + msgFalse.toString());
        }catch (Exception e){
            e.printStackTrace();
            data.put("result", "fail");
            data.put("data", "主机暂未连接，布防/撤防命令已保存，联机后下发");
        }
        return data.toString();
    }

    public String setFun(String action, String masterCode) throws RemoteException, ServiceException {
        String url = "http://localhost:1806/Service?wsdl";
        String namespace = "http://tempuri.org/";
        String methodName = action;
        String soapActionURI = "http://tempuri.org/IService/" + action;

        Service service = new Service();
        Call call;
        call = (Call) service.createCall();

        call.setTargetEndpointAddress(url);
        call.setUseSOAPAction(true);
        call.setSOAPActionURI(soapActionURI);
        call.setOperationName(new QName(namespace, methodName));

        call.addParameter(new QName(namespace, "DeviceCode"), XMLType.XSD_STRING, ParameterMode.IN);
        call.setReturnType(XMLType.XSD_STRING);

        String[] str = new String[1];
        str[0] = masterCode;
        Object obj = call.invoke(str);
        return (String)obj;
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
        JSONArray jsonArray=new JSONArray();
        for(AlarmTypeName alarmTypeNameObject : AlarmTypeName.values()){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",alarmTypeNameObject.getAlarmTypeName());
            jsonObject.put("value",alarmTypeNameObject.getAlarmType());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    /**
     *
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
                              @RequestParam(value = "startdate" , required =  false ) String startdate,
                              @RequestParam(value = "enddate" , required =  false) String enddate,
                              @RequestParam(value = "page") int page,
                              @RequestParam(value = "alarmtype" , required = false) Integer alarmtype){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map map=new HashMap();
        map.put("cid",cid);
        map.put("alarmtype",alarmtype);
        map.put("pageBegin", (page - 1) * 10);
        map.put("pageEnd", page * 10);
        if(startdate == null || startdate==""){
            map.put("startdate",null);
        }else{
            map.put("startdate",startdate);
        }
        System.out.println("map"+map);
        if (enddate == null || enddate ==""){
            map.put("enddate",null);
        }else {
            map.put("enddate",enddate);
        }
        List<AlarmsDefences> list=appService.selectAlarmrecord(map);
        JSONObject data = new JSONObject();
        JSONArray jsonArray=new JSONArray();
        for (AlarmsDefences alarmsDefences:list){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("defencename",alarmsDefences.getDefencesName());
            jsonObject.put("alarmtypename", AlarmTypeName.getByType(Integer.parseInt(alarmsDefences.getTypeName())).getAlarmTypeName());
            jsonObject.put("alarmtime",sdf.format(alarmsDefences.getDate()));
            jsonObject.put("statename", AlarmStateName.getByState(Integer.parseInt(alarmsDefences.getState())).getAlarmStateName());
            jsonArray.add(jsonObject);
        }
        if(list.size() < 10){
            data.put("more", false);
        }else{
            data.put("more", true);
        }
        data.put("list", jsonArray.toString());
        return data.toString();
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
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name", "主机");
        jsonObject.put("value", -1);
        jsonArray.add(0, jsonObject);
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
        Map map=new HashMap();
        map.put("cid",cid);
        map.put("masterid",masterid);
        List<DefencesDevice> list=appService.getDefences(map);
        for (DefencesDevice defencesDevice:list){
            int defencetype=defencesDevice.getDefencetype();
            if(defencetype != -1){
                defencesDevice.setDefencetypename(DefenceTypeName.getByType(defencetype).getDefenceTypeName());
            }
            int devicetype=defencesDevice.getDevicetype();
            if(devicetype != -1){
                defencesDevice.setDevicetypename(DeviceTypeName.getByType(devicetype).getDeviceTypeName());
            }
            int state=defencesDevice.getState();
            if(state==1){
                defencesDevice.setStatename("布防");
            }else {
                defencesDevice.setStatename("撤防");
            }
        }
        return JSONArray.fromObject(list).toString();
    }
    /**
     * 新增保存防区信息接口
     */
    @ResponseBody
    @RequestMapping("updateDefence")
    public String updateDef(@RequestParam("defenceid") String defenceid,
                            @RequestParam("defencename") String defencename,
                            @RequestParam("defencetypename") String defencetypename){
        int defencetype= DefenceTypeName.getByName(defencetypename).getDefenceType();
        Map map=new HashMap();
        map.put("defencename",defencename);
        map.put("defencetype",defencetype);
        map.put("defenceid",defenceid);
        int row=appService.updateDefence(map);
        JSONObject jsonObject=new JSONObject();
        if (row == 1) {
            jsonObject.put("result","success");
            jsonObject.put("data","更新成功");
        }else {
            jsonObject.put("result","fail");
            jsonObject.put("data","更新失败");
        }
        return jsonObject.toString();
    }
    /**
     * 新增修改设备
     * @param userid
     * @param cid
     * @param devicetype
     * @param devicename
     * @param version
     * @param state
     * @param defencename
     * @param defencetype
     * @param devicemode
     * @param masterid
     * @param sim
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updatemaster")
    public String updatemaster(@RequestParam(value = "userid",required = false)String userid,
                               @RequestParam(value = "cid",required = false) String cid,
                               @RequestParam(value = "deviceid",required = false) String deviceid,
                               @RequestParam(value = "devicetype",required = false) Integer devicetype,
                               @RequestParam(value = "devicename",required = false) String devicename,
                               @RequestParam(value = "version",required = false) String version,//----------
                               @RequestParam(value = "state",required = false) Integer state,
                               @RequestParam(value = "defencecode",required = false) String defencecode,
                               @RequestParam(value = "defencename",required = false) String defencename,//---------
                               @RequestParam(value = "defencetype",required = false) Integer defencetype,//-------
                               @RequestParam(value = "devicemode",required = false) String devicemode,
                               @RequestParam(value = "masterid",required = false) String masterid,
                               @RequestParam(value = "sim",required = false) String sim){//---------
        JSONObject jsonObject=new JSONObject();
        if(devicetype == -1){
            Masters master = new Masters();
            master.setCreateId(userid);
            master.setCustomerid(cid);
            master.setName(devicename);
            master.setVersion(version);
            master.setState(String.valueOf(state));
            master.setCode(defencecode);
            master.setMaintype(devicemode);
            master.setMid(masterid);
            master.setSim(sim);
            System.out.println("updatemaster-masterId:" + master.getMid());
            Masters masterTemp = mastersService.findUniqueByProperty("code", master.getCode());
            if(masterTemp != null && !masterTemp.getMid().equals(master.getMid())){
                jsonObject.put("result","fail");
                jsonObject.put("data","主机编号已存在");
                return jsonObject.toString();
            }
            masterTemp = mastersService.findUniqueByProperty("sim", master.getSim());
            if(masterTemp != null && !masterTemp.getMid().equals(master.getMid())){
                jsonObject.put("result","fail");
                jsonObject.put("data","sim卡号已存在");
                return jsonObject.toString();
            }
            try {
                mastersController.saveMaster(master);
                jsonObject.put("result","success");
                jsonObject.put("data","");
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.put("result","fail");
                jsonObject.put("data","更新失败");
            }
        }else{
            User userTemp = systemService.getUser(userid);
            Devices devices = new Devices();
            devices.setCustomerid(cid);
            devices.setDid(deviceid);
            devices.setDevicetype(devicetype);
            devices.setName(devicename);
            devices.setState(String.valueOf(state));
            Defences defenceTemp = new Defences();
            defenceTemp.setCode(defencecode);
            defenceTemp.setMasterid(masterid);
            devices.setDefenceid(defencesService.findDefence(defenceTemp).getDid());
            devices.setDevicemodel(devicemode);
            devices.setMasterid(masterid);
            devices.setCreateid(userid);
            devices.setDefenceName(defencename);
            devices.setDefenceType(defencetype.toString());
            System.out.println("updatemaster-deviceId:" + devices.getDid());
            try {
//                if (devices.getDid() == null){
//                    jsonObject.put("result","fail");
//                    jsonObject.put("data","更新失败");
//                    return jsonObject.toString();
//                }
                devicesController.savedevice(devices, userTemp);
                jsonObject.put("result","success");
                jsonObject.put("data","");
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.put("result","fail");
                jsonObject.put("data","更新失败");
            }
        }

        return jsonObject.toString();
    }

}
