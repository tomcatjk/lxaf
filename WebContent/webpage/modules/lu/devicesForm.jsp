<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>新增设备</title>
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
			var url = "${ctx}/lu/devices/save";
			var obj = $("#inputForm").serialize();
			$.ajaxSetup({
				async : false
			});

			$("#inputForm :input.requiredClass").trigger('blur');
			var numError = $('#inputForm .onErrorDevices').length;

			if(numError){
				return false;
			}
			$.ajax({
				type: "post",
				url: url,
				data: obj,
				success: function (data) {
					if (data != null && data != '') {

					} else {
						isTrue = false;
					}
				},
				error: function () {
					isTrue = false;
				}
			});
			return isTrue;
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

	<script>
		$(document).ready(function () {
			var url = "${ctx}/lu/masters/findMastersListByCid?cid=${devices.customerid}";
			$.post(url,function (mastersList) {
				var options = "";
				$.each(mastersList,function (index,masters) {
					options += "<option value="+masters.mid+">"+masters.name+"</option>"
				})
				$("#masterSelect").html(options);
				var mid = $("#masterSelect").val();
				var url = "${ctx}/lu/defences/findDefencesListByMasterId?masterId=" + mid;
				$.post(url,function (defencesListJson) {
					var options = "";
					$.each(defencesListJson,function (index,defences) {
						options += "<option value="+defences.did+">"+defences.code+"</option>"
					})
					$("#defenceSelect").html(options);
					var did = $("#defenceSelect").val();
					var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
					$.post(url,function (defences) {
						var input = "<input type='text' name='defencesName' value='"+defences.name+"' class='form-control'>";
						$("#defencesName").html(input);
					})
				})
			})

		})

		$(document).ready(function(){
			$("#masterSelect").change(function(){
				var mid = $(this).val();
				var url = "${ctx}/lu/defences/findDefencesListByMasterId?masterId=" + mid;
				$.post(url,function (defencesListJson) {
					var options = "";
					$.each(defencesListJson,function (index,defences) {
						options += "<option value="+defences.did+">"+defences.code+"</option>"
					})
					$("#defenceSelect").html(options);
					var did = $("#defenceSelect").val();
					var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
					$.post(url,function (defences) {
						var input = "<input type='text' name='defencesName' value='"+defences.name+"' class='form-control'>";
						$("#defencesName").html(input);
					})
				})
			});
		});

		$(document).ready(function(){
			$("#defenceSelect").change(function(){
				var did = $(this).val();
				var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
				$.post(url,function (defences) {
					var input = "<input type='text' name='defencesName' value='"+defences.name+"' class='form-control'>";
					$("#defencesName").html(input);
				})
			});
		});

	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="devices" action="${ctx}/lu/devices/save" method="post" class="form-horizontal">
			<input type="hidden" name="did" value="${devices.did}">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">设备名称：</label></td>
					<td class="width-35">
						<form:input path="name" id="name" htmlEscape="false" class="form-control requiredClass"/>
					</td>
					<td class="width-15 active"><label class="pull-right">设备类型：</label></td>
					<td class="width-35">
						<select name="devicetype" style="width: 250px; height: 30px;">
							<c:forEach items="${deviceTypeNameMap}" var="deviceTypeName">
								<option value="${deviceTypeName.key}">${deviceTypeName.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属主机：</label></td>
					<td class="width-35">
							<select name="masterid" id="masterSelect" style='width: 250px; height: 30px;'></select>
					</td>
					<td></td>
					<td><input type="hidden" name="customerid" value="${devices.customerid}"></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">防区编号：</label></td>
					<td class="width-35">
						<select name="defenceid" id="defenceSelect" style='width: 250px; height: 30px;'></select>
					</td>
					<td class="width-15 active"><label class="pull-right">防区名称：</label></td>
					<td class="width-35">
						<div id="defencesName"></div>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>

	<%--devicesForm表单验证--%>
	<script>
		$(function () {
			//文本框失去焦点后
			$('#inputForm :input').blur(function(){
				var $parent = $(this).parent();
				$parent.find(".formtips").remove();
				//验证设备名称
				if( $(this).is('#name') ){
					if( this.value==""){
						var errorMsg = '请输入设备名称.';
						$parent.append('<span class="formtips onErrorDevices">'+errorMsg+'</span>');
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