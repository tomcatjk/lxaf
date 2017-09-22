<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<style>
		.formtips{
			color: red;
		}
	</style>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			var isTrue = true;
			var url = "${ctx}/sys/user/save";
			var obj = $("#reg-form").serialize();
			$.ajaxSetup({
				async : false
			});

			$("#reg-form :input.requiredClass").trigger('blur');
			var numError = $('#reg-form .onErrorMasters').length;

			if(numError){
				return false;
			}

			$.ajax({
				type : "post",
				url : url,
				data : obj,
				success : function(data) {
					if(data != null && data != ''){

					}else {
						isTrue = false;
					}
				},
				error : function () {
					isTrue = false;
				}
			});
			return isTrue;
		}
	</script>
</head>
<body class="hideScroll">
	<form:form id="reg-form" action="" method="post" class="form-horizontal">
		<input type="hidden" value="${user.id == null ? '123' : user.id}" id="id" name="id" />
		<input type="hidden" value="${oldLoginName}"  id="oldLoginName" name="oldLoginName" />
		<input type="hidden" value="${createBy}"  id="createBy" name="createBy" />
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
				  <td class="width-35">
					  <input type="text" name="name" id="name" value="${user.name}" class="form-control requiredClass"/>
				  </td>
		         <td class="active"><label class="pull-right"><font color="red">*</font>用户名:</label></td>
				  <td class="width-35">
					  <input type="text" name="loginName" id="loginName" value="${user.loginName}" class="form-control requiredClass"/>
				  </td>
		      </tr>

		      <tr>
				  <td class="active"><label class="pull-right"><font color="red">*</font>用户角色:</label></td>
				  <td>
					  <select  class="form-control requiredClass" name="roleid" id="roleid">
						  <c:forEach items="${roleList}" var="userrole">
							  <c:choose>
								  <c:when test="${user != null && user.roleid == userrole.id}">
									  <option value="${userrole.id}" selected="selected">
											  ${userrole.name}
									  </option>
								  </c:when>
								  <c:otherwise>
									  <option value="${userrole.id}">
											  ${userrole.name}
									  </option>
								  </c:otherwise>
							  </c:choose>

						  </c:forEach>
					  </select>
				  </td>
		         <td class="active"><label class="pull-right"><font color="red">*</font>状态:</label></td>
				 <td>
					 <select  class="form-control" name="loginFlag" id="loginFlag">
						 <c:forEach items="${loginTypeMapList}" var="loginTypeMap">
							<option value="${loginTypeMap.loginType}" <c:if test="${user != null && user.loginFlag == loginTypeMap.loginType}">selected</c:if> >
								${loginTypeMap.loginTypeName}
							</option>
						 </c:forEach>
					 </select>
				 </td>
		      </tr>
			  <tr>
				  <td class="active"><label class="pull-right"><font color="red">*</font>密码:</label></td>
				  <td><input name="password" type="password" id="password" value="${user.password}" class="form-control requiredClass"></td>
				  <td class="active"><label class="pull-right"><font color="red">*</font>确认密码:</label></td>
				  <td><input name="psw2" type="password" id="psw2" value="${user.password}" class="form-control requiredClass"></td>
			  </tr>
		       <tr>
		           <td class="active"><label class="pull-right">所属客户:</label></td>
				   <td>
						  <select  class="form-control" name="customerID" id="customerID">
							  <option value="00">请选择</option>
							  <c:forEach items="${customerList}" var="customer">
								  <c:choose>
									  <c:when test="${user != null && user.customerID == customer.cid}">
										  <option value="${customer.cid}" selected="selected">
												  ${customer.name}
										  </option>
									  </c:when>
									  <c:otherwise>
										  <option value="${customer.cid}">
												  ${customer.name}
										  </option>
									  </c:otherwise>
								  </c:choose>
							  </c:forEach>
						  </select>
				    </td>
		      </tr>
			  <tr>
				  <td class="active"><label class="pull-right">备注:</label></td>
				  <td class="width-35" colspan="6" >
                    <textarea rows="3" name="remarks" id="remarks" value="" class="form-control "
							  data-easyform="null;">${user.remarks}</textarea>
				  </td>
			  </tr>
			</tbody>
		</table>
		<input type="hidden" name="menuIds" id="menuIds">
	</form:form>

	<%--userForm表单验证--%>
	<script>
		$(function () {
			//文本框失去焦点后
			$('#reg-form :input').blur(function(){
				var $parent = $(this).parent();
				$parent.find(".formtips").remove();
				//验证姓名
				if( $(this).is('#name') ){
					if( this.value==""){
						var errorMsg = '请输入姓名.';
						$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
					}
				}

				//验证用户名
				if( $(this).is('#loginName') ){
					if( this.value=="" ){
						var errorMsg = '请输入用户名.';
						$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
					}else if(this.value != "${user.loginName}"){
						$.get("${ctx}/sys/user/userExist?loginName=" + this.value, function (data) {
							if(data == "exist"){
								var errorMsg = '用户名已存在.';
								$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
							}
						});
					}
				}

				//验证用户名
				if( $(this).is('#roleid') ){
					if( this.value=="" ){
						var errorMsg = '请先创建用户角色.';
						$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
					}
				}

				//验证密码
				if( $(this).is('#password') ){
					if( this.value==""){
						var errorMsg = '请输入密码.';
						$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
					}
				}

				//验证密码
				if( $(this).is('#psw2') ){
					if( this.value==""){
						var errorMsg = '请再次输入密码.';
						$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
					}else if( this.value != $("#password").val()){
						var errorMsg = '两次密码输入不同.';
						$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
					}
				}

			}).keyup(function(){
				$(this).triggerHandler("blur");
			}).focus(function(){
				$(this).triggerHandler("blur");
			});//end blur
		})
	</script>
</body>
</html>