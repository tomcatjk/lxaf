<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>记录主机信息管理</title>
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
			var url = "${ctx}/lu/masters/save";
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

		$(document).ready(function() {
			validateForm = $("#reg-form").validate({
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
		<form id="reg-form" modelAttribute="masters" action="${ctx}/lu/masters/save" method="post" class="form-horizontal">
			<input type="hidden" value="${masters.mid}" id="mid" name="mid">
			<input type="hidden" value="${masters.state}" id="state" name="state">
			<input type="hidden"  name="customerid" id="customerid"    value="${masters.customerid}"/>
			<sys:message content="${message}"/>
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				<tbody>
					<tr>
						<input type="hidden" name="masterFlag" value="${masterFlag}">
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>编号：</label></td>
						<td class="width-35">
							<input type="text" name="code" id="code" class="form-control requiredClass" value="${masters.code}"/>
						</td>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>主机名称：</label></td>
						<td class="width-35">
							<input type="text" name="name" id="name" class="form-control requiredClass" value="${masters.name}"/>
						</td>
					</tr>
					<tr>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>SIM卡号：</label></td>
						<td class="width-35">
							<input type="text" name="sim" id="sim" class="form-control " value="${masters.sim}"></td>
						</td>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
						<td class="width-35">
							<select class="form-control requiredClass" name="state" id="state">
									<c:forEach items="${mastersStateNameMap}" var="mastersStateName">
										<option value="${mastersStateName.key}">${mastersStateName.value}</option>
									</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td class="width-15 active"><label class="pull-right">主机型号：</label></td>
						<td class="width-35">
							<input type="text"  name="maintype" id="maintype"   class="form-control" value="${masters.maintype}"/>
						</td>
						<td class="width-15 active"><label class="pull-right">版本：</label></td>
						<td class="width-35">
							<input type="text"  name="version" id="version"   class="form-control" value="${masters.version}"/>
						</td>
					</tr>
				</tbody>
			</table>
	</form>

	<%--masterForm表单验证--%>
	<script>
		$(function () {
			//文本框失去焦点后
			$('#reg-form :input').blur(function(){
				var $parent = $(this).parent();
				$parent.find(".formtips").remove();
				//验证编号
				if( $(this).is('#code') ){
					if( this.value==""){
						var errorMsg = '请输入编号.';
						$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
					}else if(this.value != "${masters.code}"){
						$.get("${ctx}/lu/masters/masterExist?code=" + this.value, function (data) {
							if(data == "exist"){
								var errorMsg = '主机编号已存在.';
								$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
							}
						});
					}
				}

				//验证主机名称
				if( $(this).is('#name') ){
					if( this.value=="" ){
						var errorMsg = '请输入主机名称.';
						$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
					}
				}

				//验证sim卡号
				if( $(this).is('#sim') ){
					if( this.value==""){
						var errorMsg = '请输入sim卡号.';
						$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
					}else if(this.value != "${masters.sim}"){
						$.get("${ctx}/lu/masters/simExist?sim=" + this.value, function (data) {
							if(data == "exist"){
								var errorMsg = 'sim卡号已存在.';
								$parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
							}
						});
					}
				}

				//验证主机状态
				if( $(this).is('#state') ){
					if( this.value==""){
						var errorMsg = '请选择主机状态.';
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