<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人员管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(validateForm.form()){
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
			<td class="width-15 active"><label class="pull-right">姓名：</label></td>
			<td class="width-35">
				<form:input path="name" htmlEscape="false"    class="form-control "/>
			</td>
		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">所在公司：</label></td>
			<td class="width-35">
				<form:input path="company" htmlEscape="false"    class="form-control "/>
			</td>
			<td class="width-15 active"><label class="pull-right">邮箱：</label></td>
			<td class="width-35">
				<form:input path="email" htmlEscape="false"    class="form-control  email"/>
			</td>
		</tr>
		<tr>
			<td class="width-15 active"><label class="pull-right">电话：</label></td>
			<td class="width-35">
				<form:input path="phone" htmlEscape="false"    class="form-control  number"/>
			</td>
			<td class="width-15 active"><label class="pull-right">相片：</label></td>
			<td class="width-35">
				<form:hidden id="photo" path="photo" htmlEscape="false" maxlength="255" class="form-control"/>
				<sys:ckfinder input="photo" type="images" uploadPath="/test/my/worker" selectMultiple="true"/>
			</td>
		</tr>
		</tbody>
	</table>
</form:form>
</body>
</html>