<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人员管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        var phonesign = new Boolean();
        var namesign = new Boolean();
        var emainsign = new Boolean();
        var compnaysign = new Boolean();

        $(function () {
            $(".phoneClass").blur(function () {
                var phone = $(".phoneClass").val();
                var reg=/^(\+\d{2,3}\-)?\d{11}$/;
                if(!reg.test(phone)){
                    $(".phone-errorInfo").show();
                    $(".phone-errorInfo").html("<font color='red'>请输入正确的手机号码</font>")
                    phonesign = false;
                }else {
                    var url="${ctx}/test/my/worker/verifyPhone?phone="+phone;
                    $.post(url,function (data) {
                        if(data=='success'){
                            $(".phone-errorInfo").show();
                            $(".phone-errorInfo").html("<font color='red'>此手机号已经存在</font>");
                            if(${isEdit=='isEdit'}){
                                phonesign = true;
                            }else {
                                phonesign = false;
                            }
                        }else{
                            $(".phone-errorInfo").hide();
                            phonesign = true;
                        }
                    });
                }
            });

            $(".form-control-name").blur(function () {
                var name = $(".form-control-name").val();
                var reg = /^[\u4E00-\u9FA5]{1,6}$/;
                if(!reg.test(name)){
                    $(".name-errorInfo").show();
                    $(".name-errorInfo").html("<font color = 'red'>请输入正确的姓名</font>");
                    namesign = false;
                }else {
                    $(".name-errorInfo").hide();
                    namesign = true;
                }
            });

            $(".form-control-email").blur(function () {
                var email = $(".form-control-email").val();
                var reg = /\w+[@]{1}\w+[.]\w+/;
                if(reg.test(email) || email==null || email == ''){
                    $(".email-errorInfo").hide();
                    emainsign = true;
                }else {
                    $(".email-errorInfo").show();
                    $(".email-errorInfo").html("<font color = 'red'>请输入正确的邮箱地址</font>");
                    emainsign = false;
                }
            });

            $(".form-control-company").blur(function () {
                var company = $(".form-control-company").val();
                var reg = /[\u4e00-\u9fa5a-zA-Z]{1,20}/;
                if(!reg.test(company)){
                    $(".company-errorInfo").show();
                    $(".company-errorInfo").html("<font color = 'red'>请输入正确的公司名</font>");
                    compnaysign = false;
                }else {
                    $(".company-errorInfo").hide();
                    compnaysign = true;
                }
            });
        });

		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            $(".phoneClass").focus();
            $(".phoneClass").blur();
            $(".form-control-name").focus();
            $(".form-control-name").blur();
            $(".form-control-email").focus();
            $(".form-control-email").blur();
            $(".form-control-company").focus();
            $(".form-control-company").blur();
//            alert("phonesign:"+phonesign);
//            var sign = new Boolean();
            var sign = phonesign & namesign & emainsign & compnaysign;
//            alert("sign:"+sign);
//            alert(validateForm.form()&sign);
		  if(validateForm.form()&sign){
			  $("#inputForm").submit();
			  return true;
		  }


		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="worker" action="${ctx}/test/my/worker/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
            <tbody>
            <tr>
                <td class="width-15 active">	<label class="pull-right"><font color="red">*</font>相片：</label></td>
                <td class="width-35"><form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
                    <sys:ckfinder input="nameImage" type="images" uploadPath="/test/my/worker" selectMultiple="false" maxWidth="100" maxHeight="100"/></td>
                <td class="active"><label class="pull-right"><label class="pull-right"><font color="red">*</font>归属公司:</label></label></td>
                <td class="width-35">
                    <form:input path="company" htmlEscape="false"    class="form-control-company"/><br /><span class="company-errorInfo"></span>
                </td>
            </tr>

            <tr>
                <td class="active"><label class="pull-right"><font color="red">*</font>编号:</label></td>
                <td class="width-35">
                    <form:input path="code" htmlEscape="false" maxlength="50" class="form-control " disabled="true"/>
                </td>
            </tr>

            <tr>
                <td class="active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
                <td class="width-35">
                    <form:input path="name" htmlEscape="false" maxlength="50" class="form-control-name"/><br><span class="name-errorInfo"></span>
                </td>
                <%--<td class="active"><label class="pull-right"><font color="red">*</font>登录手机:</label></td>--%>
                <%--<td>--%>
                    <%--<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control"/></td>--%>
            </tr>

            <tr>
                <td class="active"><label class="pull-right">邮箱:</label></td>
                <td class="width-35">
                    <form:input path="email" htmlEscape="false" maxlength="100" class="form-control-email"/><br /><span class="email-errorInfo"></span>
                </td>
                <%--<td><form:input path="email" htmlEscape="false" maxlength="100" class="form-control email"/></td>--%>
                <td class="active"><label class="pull-right">电话:</label></td>
                <td class="width-35">
                    <%--<form:input path="phone" htmlEscape="false"  maxlength="100" class="form-control required"/>--%>
                    <input name="phone" type="text" value="${worker.phone}" class="phoneClass"><br /><span class="phone-errorInfo"></span>
                </td>
            </tr>

            <%--<tr>--%>
                <%--&lt;%&ndash;<td></td>&ndash;%&gt;--%>
                <%--&lt;%&ndash;<td><input id="phone2" name="phone2" type="text" value="" maxlength="50" minlength="3" class="form-control ${empty worker.id?'required':''}"/>&ndash;%&gt;--%>
                    <%--&lt;%&ndash;<c:if test="${not empty worker.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if></td>&ndash;%&gt;--%>
                    <%--<td class="active"><label class="pull-right"><font color="red">*</font>登录名:</label></td>--%>
                <%--<td><input id="phone3" name="phone3" type="hidden" value="${worker.phone}">--%>
                    <%--<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control required"/></td>--%>
            <%--</tr>--%>
            

            <tr>
                <td class="active"><label class="pull-right"><c:if test="${empty worker.id}"><font color="red">*</font></c:if>密码:</label></td>
                <%--<td class="width-35">--%>
                    <%--<form:input path="password" htmlEscape="false"  maxlength="100" class="form-control"/>--%>
                <%--</td>--%>
                <td><input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="form-control ${empty worker.id?'required':''}"/>
                    <c:if test="${not empty worker.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if></td>
                <td class="active"><label class="pull-right"><c:if test="${empty worker.id}"><font color="red">*</font></c:if>确认密码:</label></td>
                <td><input id="confirmNewPassword" name="password" type="password"  class="form-control ${empty worker.id?'required':''}" value="" maxlength="50" minlength="3" equalTo="#newPassword"/></td>
            </tr>

            <%--<tr>--%>
                <%--<td class="active"><label class="pull-right">手机:</label></td>--%>
                <%--<td><form:input path="mobile" htmlEscape="false" maxlength="100" class="form-control"/></td>--%>
                <%--<td class="active"><label class="pull-right">是否允许登录:</label></td>--%>
                <%--<td><form:select path="loginFlag"  class="form-control">--%>
                    <%--<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
                <%--</form:select></td>--%>
            <%--</tr>--%>

            <%--<tr>--%>
                <%--<td class="active"><label class="pull-right">用户类型:</label></td>--%>
                <%--<td><form:select path="userType"  class="form-control">--%>
                    <%--<form:option value="" label="请选择"/>--%>
                    <%--<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
                <%--</form:select></td>--%>
                <%--<td class="active"><label class="pull-right"><font color="red">*</font>用户角色:</label></td>--%>
                <%--<td>--%>
                    <%--<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" cssClass="i-checks required"/>--%>
                    <%--<label id="roleIdList-error" class="error" for="roleIdList"></label>--%>
                <%--</td>--%>
            <%--</tr>--%>

            <%--<tr>--%>
                <%--<td class="active"><label class="pull-right">备注:</label></td>--%>
                <%--<td><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/></td>--%>
                <%--<td class="active"><label class="pull-right"></label></td>--%>
                <%--<td></td>--%>
            <%--</tr>--%>

            <%--<c:if test="${not empty user.id}">--%>
                <%--<tr>--%>
                    <%--<td class=""><label class="pull-right">创建时间:</label></td>--%>
                    <%--<td><span class="lbl"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></span></td>--%>
                    <%--<td class=""><label class="pull-right">最后登陆:</label></td>--%>
                    <%--<td><span class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></span></td>--%>
                <%--</tr>--%>
            <%--</c:if>--%>
            </tbody>

		</table>
	</form:form>

<script>


</script>
</body>
</html>