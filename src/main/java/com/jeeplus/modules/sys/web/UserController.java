package com.jeeplus.modules.sys.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.*;
import com.jeeplus.modules.lu.entity.Customers;
import com.jeeplus.modules.lu.entity.LoginTypeName;
import com.jeeplus.modules.lu.service.AreasService;
import com.jeeplus.modules.lu.service.CustomersService;
import com.jeeplus.modules.sys.entity.User;
import org.apache.poi.hssf.usermodel.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.beanvalidator.BeanValidators;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sys.dao.UserDao;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.SystemConfig;
import com.jeeplus.modules.sys.service.SystemConfigService;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 用户Controller
 * @author jeeplus
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomersService customersService;

    @Autowired
    private AreasService areasService;

    @ModelAttribute
    public User get(@RequestParam(required=false) String id) {
        if (StringUtils.isNotBlank(id)){
            return systemService.getUser(id);
        }else{
            return new User();
        }
    }

    @RequiresPermissions("sys:user:index")
    @RequestMapping(value = {"index"})
    public String index(User user, Model model) {
        return "modules/sys/userIndex";
    }

    @RequiresPermissions("sys:user:index")
    @RequestMapping(value = {"list", ""})
    public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
        user.setCustomerID(UserUtils.getUser().getCustomerID());
        user.setId(UserUtils.getUser().getId());
        Page<User> page = systemService.findUserPage(new Page<User>(request, response), user);
        model.addAttribute("loginTypeMapList",LoginTypeName.getLoginTypeMapList());
        model.addAttribute("page", page);
        model.addAttribute("user", user);
        return "modules/sys/userList";
    }

    @RequiresPermissions(value={"sys:user:view","sys:user:add","sys:user:edit"},logical=Logical.OR)
    @RequestMapping(value = "form")
    public String form(User user, Model model) {
        //获取用户信息
        if(user.getId() != null) {
            user = systemService.getUser(user.getId());
            user.setRoleid(systemService.findRoleId(user));
        }
        model.addAttribute("user", user);
        //当前登录用户的角色列表
        List<Role> roleList = systemService.findRoles(UserUtils.getUser());
        model.addAttribute("roleList",roleList);

        //当前登录用户下的客户列表
        Customers customersParameter = new Customers();
        customersParameter.setParentCid(UserUtils.getUser().getCustomerID());
        List customerList = customersService.findList(customersParameter);
        model.addAttribute("customerList", customerList);
        model.addAttribute("loginTypeMapList",LoginTypeName.getLoginTypeMapList());
        return "modules/sys/userForm";
    }

    @RequiresPermissions(value={"sys:user:add","sys:user:edit"},logical=Logical.OR)
    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(User userTemp, HttpServletRequest request) {
        String idTemp = request.getParameter("id");
        User user = new User();
        if(idTemp != null && !"123".equals(idTemp)){
            user = systemService.getUser(idTemp);
        }
        user.setName(request.getParameter("name"));
        user.setLoginName(request.getParameter("loginName"));
        user.setRoleid(request.getParameter("roleid"));
        user.setLoginFlag(request.getParameter("loginFlag"));
        user.setPassword(request.getParameter("password"));
        user.setCustomerID(request.getParameter("customerID"));
        user.setRemarks(request.getParameter("remarks"));

        //所属客户为空时
        if("00".equals(user.getCustomerID())){
            user.setCustomerID(UserUtils.getUser().getCustomerID());
        }
        // 密码MD5加密
        if(user.getPassword().length() < 20) {
            user.setPassword(SystemService.entryptPassword(user.getPassword()));
        }
        systemService.saveUser2(user);
        // 清除当前用户缓存
        if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
            UserUtils.clearCache();
        }
        return "ok";
    }

    @RequiresPermissions("sys:user:del")
    @RequestMapping(value = "delete")
    public String delete(User user, RedirectAttributes redirectAttributes) {
        if(Global.isDemoMode()){
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        if (UserUtils.getUser().getId().equals(user.getId())){
            addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
        }else if (User.isAdmin(user.getId())){
            addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
        }else{
//            systemService.deleteRole(user);
//            areasService.deleteAreas(user);
            systemService.deleteUser(user);
            addMessage(redirectAttributes, "删除用户成功");
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 批量删除用户
     */
    @RequiresPermissions("sys:user:del")
    @RequestMapping(value = "deleteAll")
    public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] =ids.split(",");
        for(String id : idArray){
            User user = systemService.getUser(id);
            if(Global.isDemoMode()){
                addMessage(redirectAttributes, "演示模式，不允许操作！");
                return "redirect:" + adminPath + "/sys/user/list?repage";
            }
            if (UserUtils.getUser().getId().equals(user.getId())){
                addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
            }else if (User.isAdmin(user.getId())){
                addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
            }else{
//                systemService.deleteRole(user);
//                areasService.deleteAreas(user);
                systemService.deleteUser(user);
                addMessage(redirectAttributes, "删除用户成功");
            }
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 导出用户数据
     * @param user
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "用户数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xls";
			/*Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
			new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();*/

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet =wb.createSheet("用户管理表");
            HSSFRow row = sheet.createRow((int)0);
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue("姓名");
            cell.setCellStyle(style);
            cell = row.createCell((short)1);
            cell.setCellValue("用户名");
            cell.setCellStyle(style);
            cell = row.createCell((short)2);
            cell.setCellValue("角色");
            cell.setCellStyle(style);
            cell = row.createCell((short)3);
            cell.setCellValue("状态");
            cell.setCellStyle(style);
            cell = row.createCell((short)4);
            cell.setCellValue("所属客户");
            cell.setCellStyle(style);
            cell = row.createCell((short)5);
            cell.setCellValue("备注");
            cell.setCellStyle(style);


            List<User> list = userDao.totoalUser();

            for(int i=0;i<list.size();i++){
                row =sheet.createRow((int)i+1);
                User user1 = list.get(i);
                //创建单元格并赋值
                row.createCell((short)0).setCellValue(user1.getName());
                row.createCell((short)1).setCellValue(user1.getLoginName());
                row.createCell((short)2).setCellValue(user1.getUserType());
                row.createCell((short)3).setCellValue(user1.getLoginFlag());
                row.createCell((short)4).setCellValue(user1.getCustomerID());
                row.createCell((short)5).setCellValue(user1.getRemarks());

            }


            //选择保存路径
            response.addHeader("Content-Disposition","attachment;filename="+fileName);
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            wb.write(os);
            os.close();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 导入用户数据
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        if(Global.isDemoMode()){
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<User> list = ei.getDataList(User.class);
            for (User user : list){
                try{
                    if ("true".equals(checkLoginName("", user.getLoginName()))){
                        user.setPassword(SystemService.entryptPassword("123456"));
                        BeanValidators.validateWithException(validator, user);
                        systemService.saveUser(user);
                        successNum++;
                    }else{
                        failureMsg.append("<br/>登录名 "+user.getLoginName()+" 已存在; ");
                        failureNum++;
                    }
                }catch(ConstraintViolationException ex){
                    failureMsg.append("<br/>登录名 "+user.getLoginName()+" 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList){
                        failureMsg.append(message+"; ");
                        failureNum++;
                    }
                }catch (Exception ex) {
                    failureMsg.append("<br/>登录名 "+user.getLoginName()+" 导入失败："+ex.getMessage());
                }
            }
            if (failureNum>0){
                failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 下载导入用户数据模板
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "用户数据导入模板.xlsx";
            List<User> list = Lists.newArrayList(); list.add(UserUtils.getUser());
            new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 验证登录名是否有效
     * @param oldLoginName
     * @param loginName
     * @return
     */
    @ResponseBody
    @RequiresPermissions(value={"sys:user:add","sys:user:edit"},logical=Logical.OR)
    @RequestMapping(value = "checkLoginName")
    public String checkLoginName(String oldLoginName, String loginName) {
        if (loginName !=null && loginName.equals(oldLoginName)) {
            return "true";
        } else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 用户信息显示
     * @param user
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "info")
    public String info(HttpServletResponse response, Model model) {
        User currentUser = UserUtils.getUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("Global", new Global());
        return "modules/sys/userInfo";
    }

    /**
     * 用户信息显示编辑保存
     * @param user
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "infoEdit")
    public String infoEdit(User user, boolean __ajax, HttpServletResponse response, Model model) {
        User currentUser = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getName())){
            if(Global.isDemoMode()){
                model.addAttribute("message", "演示模式，不允许操作！");
                return "modules/sys/userInfo";
            }
            if(user.getName() !=null )
                currentUser.setName(user.getName());
            if(user.getEmail() !=null )
                currentUser.setEmail(user.getEmail());
            if(user.getPhone() !=null )
                currentUser.setPhone(user.getPhone());
            if(user.getMobile() !=null )
                currentUser.setMobile(user.getMobile());
            if(user.getRemarks() !=null )
                currentUser.setRemarks(user.getRemarks());
//			if(user.getPhoto() !=null )
//				currentUser.setPhoto(user.getPhoto());
            systemService.updateUserInfo(currentUser);
            if(__ajax){//手机访问
                AjaxJson j = new AjaxJson();
                j.setSuccess(true);
                j.setMsg("修改个人资料成功!");
                return renderString(response, j);
            }
            model.addAttribute("user", currentUser);
            model.addAttribute("Global", new Global());
            model.addAttribute("message", "保存用户信息成功");
            return "modules/sys/userInfo";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("Global", new Global());
        return "modules/sys/userInfoEdit";
    }


    /**
     * 用户头像显示编辑保存
     * @param user
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "imageEdit")
    public String imageEdit(User user, boolean __ajax, HttpServletResponse response, Model model) {
        User currentUser = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getName())){
            if(Global.isDemoMode()){
                model.addAttribute("message", "演示模式，不允许操作！");
                return "modules/sys/userInfo";
            }
            if(user.getPhoto() !=null )
                currentUser.setPhoto(user.getPhoto());
            systemService.updateUserInfo(currentUser);
            if(__ajax){//手机访问
                AjaxJson j = new AjaxJson();
                j.setSuccess(true);
                j.setMsg("修改个人头像成功!");
                return renderString(response, j);
            }
            model.addAttribute("message", "保存用户信息成功");
            return "modules/sys/userInfo";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("Global", new Global());
        return "modules/sys/userImageEdit";
    }
    /**
     * 用户头像显示编辑保存
     * @param user
     * @param model
     * @return
     * @throws IOException
     * @throws IllegalStateException
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "imageUpload")
    public String imageUpload( HttpServletRequest request, HttpServletResponse response,MultipartFile file) throws IllegalStateException, IOException {
        User currentUser = UserUtils.getUser();

        // 判断文件是否为空
        if (!file.isEmpty()) {
            // 文件保存路径
            String realPath = Global.USERFILES_BASE_URL
                    + UserUtils.getPrincipal() + "/images/" ;
            // 转存文件
            FileUtils.createDirectory(Global.getUserfilesBaseDir()+realPath);
            file.transferTo(new File( Global.getUserfilesBaseDir() +realPath +  file.getOriginalFilename()));
            currentUser.setPhoto(request.getContextPath()+realPath + file.getOriginalFilename());
            systemService.updateUserInfo(currentUser);
        }

        return "modules/sys/userImageEdit";
    }

    /**
     * 返回用户信息
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "infoData")
    public AjaxJson infoData() {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setErrorCode("-1");
        j.setMsg("获取个人信息成功!");
        j.put("data", UserUtils.getUser());
        return j;
    }

    /**
     * 修改个人用户密码
     * @param oldPassword
     * @param newPassword
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "modifyPwd")
    public String modifyPwd(String oldPassword, String newPassword, Model model) {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
            if(Global.isDemoMode()){
                model.addAttribute("message", "演示模式，不允许操作！");
                return "modules/sys/userInfo";
            }
            if (SystemService.validatePassword(oldPassword, user.getPassword())){
                systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
                model.addAttribute("message", "修改密码成功");
            }else{
                model.addAttribute("message", "修改密码失败，旧密码错误");
            }
            return "modules/sys/userInfo";
        }
        model.addAttribute("user", user);
        return "modules/sys/userModifyPwd";
    }

    /**
     * 保存签名
     */
    @ResponseBody
    @RequestMapping(value = "saveSign")
    public AjaxJson saveSign(User user, boolean __ajax, HttpServletResponse response, Model model) throws Exception{
        AjaxJson j = new AjaxJson();
        User currentUser = UserUtils.getUser();
        currentUser.setSign(user.getSign());
        systemService.updateUserInfo(currentUser);
        j.setMsg("设置签名成功");
        return j;
    }
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required=false) String officeId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<User> list = systemService.findUserByOfficeId(officeId);
        for (int i=0; i<list.size(); i++){
            User e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", "u_"+e.getId());
            map.put("pId", officeId);
            map.put("name", StringUtils.replace(e.getName(), " ", ""));
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * web端ajax验证用户名是否可用
     * @param loginName
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "validateLoginName")
    public boolean validateLoginName(String loginName, HttpServletResponse response) {

        User user =  userDao.findUniqueByProperty("login_name", loginName);
        if(user == null){
            return true;
        }else{
            return false;
        }

    }

    /**
     * web端ajax验证手机号是否可以注册（数据库中不存在）
     */
    @ResponseBody
    @RequestMapping(value = "validateMobile")
    public boolean validateMobile(String mobile, HttpServletResponse response, Model model) {
        User user =  userDao.findUniqueByProperty("mobile", mobile);
        if(user == null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * web端ajax验证手机号是否已经注册（数据库中已存在）
     */
    @ResponseBody
    @RequestMapping(value = "validateMobileExist")
    public boolean validateMobileExist(String mobile, HttpServletResponse response, Model model) {
        User user =  userDao.findUniqueByProperty("mobile", mobile);
        if(user != null){
            return true;
        }else{
            return false;
        }
    }

    @ResponseBody
    @RequestMapping(value = "resetPassword")
    public AjaxJson resetPassword(String mobile, HttpServletResponse response, Model model) {
        SystemConfig config = systemConfigService.get("1");//获取短信配置的用户名和密码
        AjaxJson j = new AjaxJson();
        if(userDao.findUniqueByProperty("mobile", mobile) == null){
            j.setSuccess(false);
            j.setMsg("手机号不存在!");
            j.setErrorCode("1");
            return j;
        }
        User user =  userDao.findUniqueByProperty("mobile", mobile);
        String newPassword = String.valueOf((int) (Math.random() * 900000 + 100000));
        try {
            String result = UserUtils.sendPass(config.getSmsName(), config.getSmsPassword(), mobile, newPassword);
            if (!result.equals("100")) {
                j.setSuccess(false);
                j.setErrorCode("2");
                j.setMsg("短信发送失败，密码重置失败，错误代码："+result+"，请联系管理员。");
            }else{
                j.setSuccess(true);
                j.setErrorCode("-1");
                j.setMsg("短信发送成功，密码重置成功!");
                systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
            }
        } catch (IOException e) {
            j.setSuccess(false);
            j.setErrorCode("3");
            j.setMsg("因未知原因导致短信发送失败，请联系管理员。");
        }
        return j;
    }

    @RequestMapping(value = "userExist")
    @ResponseBody
    public String verifyUser(User user){
        User userTemp = systemService.findUserByLoginName(user);
        if(userTemp != null){
            return "exist";
        }else{
            return "notExist";
        }
    }

//	@InitBinder
//	public void initBinder(WebDataBinder b) {
//		b.registerCustomEditor(List.class, "roleList", new PropertyEditorSupport(){
//			@Autowired
//			private SystemService systemService;
//			@Override
//			public void setAsText(String text) throws IllegalArgumentException {
//				String[] ids = StringUtils.split(text, ",");
//				List<Role> roles = new ArrayList<Role>();
//				for (String id : ids) {
//					Role role = systemService.getRole(Long.valueOf(id));
//					roles.add(role);
//				}
//				setValue(roles);
//			}
//			@Override
//			public String getAsText() {
//				return Collections3.extractToString((List) getValue(), "id", ",");
//			}
//		});
//	}
}
