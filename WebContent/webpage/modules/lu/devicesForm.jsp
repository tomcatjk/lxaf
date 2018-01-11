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
			var devices="${devices}";
			console.info(devices);
			var begintime="${devices.begintime}";
			var endtime="${devices.endtime}";
			//document.getElementById("sel")[2].selected=true;
			$("#beginTimeSelect").val(begintime);
			$("#endTimeSelect").val(endtime);
			var url = "${ctx}/lu/masters/findMastersListByCid?cid=${devices.customerid}";
			$.post(url,function (mastersList) {
				var options = "";
				$.each(mastersList,function (index,masters) {
					if("${devices.masterid}" == masters.mid) {
						options += "<option value='" + masters.mid + "' selected>" + masters.name + "</option>"
					}else {
						options += "<option value=" + masters.mid + ">" + masters.name + "</option>"
					}
				});
				$("#masterSelect").html(options);
				var mid = $("#masterSelect").val();
				masterSelectChange(mid);
			});
		});

		$(document).ready(function(){
			$("#masterSelect").change(function(){
				var mid = $(this).val();
				masterSelectChange(mid);
			});

			$("#defenceSelect").change(function(){
				var did = $(this).val();
				defenceSelectChange(did);
			});
		});

		var showFlag = 0;
		var masterSelectChange = function (mid) {
			var url = "${ctx}/lu/defences/findDefencesListByMasterId?masterId=" + mid;
			$.post(url,function (defencesListJson) {
				var options = "";
				if(showFlag == 0 && ${devices.code != null}){
					options += "<option value='${devices.defenceid}' selected>${devices.code}</option>";
					showFlag++;
				}
				$.each(defencesListJson,function (index,defences) {
					options += "<option value='"+defences.did+"'>"+defences.code+"</option>"
				});
				$("#defenceSelect").html(options);
				var did = $("#defenceSelect").val();
				var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
				$.post(url,function (defences) {
					var input = "<input type='text' name='defenceName' value='"+defences.name+"' class='form-control'>";
					$("#defenceName").html(input);
				});
			});
		}

		var defenceSelectChange = function (did) {
			var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
			$.post(url,function (defences) {
				var input = "<input type='text' name='defenceName' value='"+defences.name+"' class='form-control'>";
				$("#defenceName").html(input);
			});
		}

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
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>设备名称：</label></td>
					<td class="width-35">
						<form:input path="name" id="name" htmlEscape="false" class="form-control requiredClass"/>
					</td>
					<td class="width-15 active"><label class="pull-right">设备类型：</label></td>
					<td class="width-35">
						<script type="text/javascript">
							function optionChange(){
								var hk=$("#devicetypeselect").val();
								//alert(hk);
								if(hk==8){
									$('#test').show();
								}
								else {
									$('#test').hide();
								}
							}

						</script>
						<select name="devicetype" id="devicetypeselect" style="width: 255px; height: 33px;" onchange="optionChange()">
							<c:forEach items="${deviceTypeNameMap}" var="deviceTypeName">
								<c:choose>
									<c:when test="${devices.devicetype == deviceTypeName.key}">
										<option value="${deviceTypeName.key}" selected>${deviceTypeName.value}</option>
									</c:when>
									<c:otherwise>
										<option value="${deviceTypeName.key}">${deviceTypeName.value}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属主机：</label></td>
					<td class="width-35">
							<select name="masterid" id="masterSelect" style='width: 255px; height: 33px;' class="form-control requiredClass"></select>
					</td>
					<td class="width-15 active"><label class="pull-right">防区名称：</label></td>
					<td class="width-35">
						<div id="defenceName"></div>
					</td>
					<input type="hidden" name="customerid" value="${devices.customerid}">
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">防区编号：</label></td>
					<td class="width-35">
						<select name="defenceid" id="defenceSelect" style='width: 255px; height: 33px;'></select>
					</td>
					<td class="width-15 active"><label class="pull-right">防区类型：</label></td>
					<td class="width-35">
						<select name="defenceType" id="defenceTypeSelect" style='width: 255px; height: 33px;'>
							<c:forEach items="${defenceTypeMapList}" var="defenceTypeMap">
								<option value="${defenceTypeMap.defenceType}"
										<c:if test="${defenceTypeTemp == defenceTypeMap.defenceType}">selected</c:if>
								>
									${defenceTypeMap.defenceTypeName}
								</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<%--<script type="text/javascript">
					$(function () {
						var a=${devices.begintime};
						alert(a);
						$("#beginTimeSelect").val(a);
					})
				</script>--%>
		   		<tr id="test" class="layui-hide">
					<td class="width-15 active"><label class="pull-right">睡眠起始时间：</label></td>
					<td class="width-35">
						<select name="begintime" id="beginTimeSelect" style='width: 255px; height: 33px;'>
							<option value='' disabled selected style='display:none;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请选择睡眠时间起始时间</option>
							<%--<option value ="01" <c:if test="${'01' eq devices.begintime}">selected</c:if> >01</option>--%>
							<option value ="01:00">01:00</option>
							<option value ="02:00">02:00</option>
							<option value ="03:00">03:00</option>
							<option value ="04:00">04:00</option>
							<option value ="05:00">05:00</option>
							<option value ="06:00">06:00</option>
							<option value ="07:00">07:00</option>
							<option value ="08:00">08:00</option>
							<option value ="09:00">09:00</option>
							<option value ="10:00">10:00</option>
							<option value ="11:00">11:00</option>
							<option value ="12:00">12:00</option>
							<option value ="13:00">13:00</option>
							<option value ="14:00">14:00</option>
							<option value ="15:00">15:00</option>
							<option value ="16:00">16:00</option>
							<option value ="17:00">17:00</option>
							<option value ="18:00">18:00</option>
							<option value ="19:00">19:00</option>
							<option value ="20:00">20:00</option>
							<option value ="21:00">21:00</option>
							<option value ="22:00">22:00</option>
							<option value ="23:00">23:00</option>
							<option value ="00:00">00:00</option>
						</select>
					</td>
					<td class="width-15 active"><label class="pull-right">睡眠结束时间：</label></td>
					<td class="width-35">
						<select name="endtime" id="endTimeSelect" style='width: 255px; height: 33px;' >
							<option value='' disabled selected style='display:none;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请选择睡眠时间结束时间</option>
							<option value ="01:00">01:00</option>
							<option value ="02:00">02:00</option>
							<option value ="03:00">03:00</option>
							<option value ="04:00">04:00</option>
							<option value ="05:00">05:00</option>
							<option value ="06:00">06:00</option>
							<option value ="07:00">07:00</option>
							<option value ="08:00">08:00</option>
							<option value ="09:00">09:00</option>
							<option value ="10:00">10:00</option>
							<option value ="11:00">11:00</option>
							<option value ="12:00">12:00</option>
							<option value ="13:00">13:00</option>
							<option value ="14:00">14:00</option>
							<option value ="15:00">15:00</option>
							<option value ="16:00">16:00</option>
							<option value ="17:00">17:00</option>
							<option value ="18:00">18:00</option>
							<option value ="19:00">19:00</option>
							<option value ="20:00">20:00</option>
							<option value ="21:00">21:00</option>
							<option value ="22:00">22:00</option>
							<option value ="23:00">23:00</option>
							<option value ="00:00">00:00</option>
						</select>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>

	<%--devicesForm表单验证--%>
	<script>
		$(function () {
			if($('#devicetypeselect').val()==8){
				$('#test').addClass('layui-show');
			}
			else {
				//$('#test').addClass('layui-hide');
				//$('#test').css('display','none');
				$('#test').hide();
			}
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

				//验证用户名
				if( $(this).is('#masterSelect') ){
					if( this.value=="" ){
						var errorMsg = '请先创建主机.';
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