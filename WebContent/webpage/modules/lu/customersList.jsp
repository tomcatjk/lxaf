<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>记录客户信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/jedate/jedate.js"></script>
	<script src="${ctxStatic}/common/jeeplus.js?101=1" type="text/javascript"></script>
	<%--<script src="${ctxStatic}/lianxun/layui/src/layui.js" charset="utf-8"></script>--%>
	<%--<script src="${ctxStatic}/layer-v3.0.3/layer/layer.js?1=1" charset="utf-8"></script>--%>
	<%--<link rel="stylesheet" href="${ctxStatic}/lianxun/layui/src/css/layui.css" media="all">--%>
	<script type="text/javascript">
		function submitFrom() {
			document.getElementById('searchForm').submit();
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>客户列表 </h5>
			</div>
			<div class="ibox-content">
				<!--查询条件-->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="customers" action="${ctx}/lu/customers/" method="post" class="form-inline">
							<input type="hidden" value="${customertype}" id="customertype" name="customertype">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
							<div class="form-group">
								<span>客户名称：</span>
								<input name="name" value="${customers.name}" maxlength="50"  class=" form-control input-sm">
							 </div>
							<div class="form-group">
								<span>到期时间：</span>
								<input type="datetime" name="startTime" value="${customers.startTime}" id="startTime" placeholder="请选择" class="laydate-icon form-control layer-date input-sm" />
							</div>
							<div class="form-group">
								<span>—</span>
								<input type="datetime" name="endTime" value="${customers.endTime}" id="endTime" placeholder="请选择" class="laydate-icon form-control layer-date input-sm" />
							</div>
						</form:form>
						<br/>
					</div>
				</div>

				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="lu:customers:add">
								<table:addRow2 url="${ctx}/lu/customers/formadd?customertype=${customertype}" title="新增客户"></table:addRow2><!-- 增加按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="lu:customers:del">
								<table:delRowCustomer url="${ctx}/lu/customers/deleteAll" id="contentTable"></table:delRowCustomer><!-- 删除按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="lu:customers:import">
								<table:importExcel url="${ctx}/lu/customers/import"></table:importExcel><!-- 导入按钮 -->
							</shiro:hasPermission>
							<shiro:hasPermission name="lu:customers:export">
								<table:exportExcel url="${ctx}/lu/customers/enterprisecustomers?customertype=${customertype}"></table:exportExcel><!-- 导出按钮 -->
							</shiro:hasPermission>
						   <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>

							</div>
						<div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
					</div>
				</div>
				<!-- 表格 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th  > <input type="checkbox" class="i-checks"></th>
							<th  >客户名称</th>
							<th  >所属区域</th>
							<th  >联系人</th>
							<th  >联系电话</th>
							<th  >地址</th>
							<th  >到期时间</th>
							<th  >创建人</th>
							<th  >创建时间</th>
							<th  >备注</th>
							<th  >操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="customers">
							<tr>
								<td> <input type="checkbox" id="${customers.cid}" class="i-checks"></td>
								<td>
									${customers.name}
								</td>
								<td>
									${customers.areaName}
								</td>
								<td>
									${customers.contacts}
								</td>
								<td>
									${customers.phone}
								</td>
								<td>
									${customers.address}
								</td>
								<td>
									<fmt:formatDate value="${customers.duetime}" pattern="yyyy-MM-dd "/>
								</td>
								<td>
									${customers.createid}
								</td>
								<td>
									<fmt:formatDate value="${customers.createtime}" pattern="yyyy-MM-dd "/> <%--HH:mm:ss--%>
								</td>
								<td>
										${customers.remark}
								</td>
								<td>
									<a href="#" onclick="openDialogView('查看主机信息', '${ctx}/lu/masters/list?customerid=${customers.cid}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>主机</a>
									<a href="#" onclick="openDialogView('查看设备信息', '${ctx}/lu/devices/list?customerid=${customers.cid}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>设备</a>
									<shiro:hasPermission name="lu:customers:edit">
										<a href="#" onclick="openDialogEdit('编辑客户信息', '${ctx}/lu/customers/form?cid=${customers.cid}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="lu:customers:del">
										<a href="${ctx}/lu/customers/delete?cid=${customers.cid}&customertype=${customertype}" onclick="return confirmx('确认要删除该客户信息吗？<br>同时会删除该客户下的所有主机和设备', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<!-- 分页代码 -->
				<table:page page="${page}"></table:page>
				<br/>
				<br/>
			</div>
		</div>
	</div>

	<%--初始化时间插件--%>
	<script type="text/javascript">
		$(document).ready(function(){
			/*获取当前系统时间并转化为指定格式*/
			var date = new Date();
			var mon = date.getMonth() + 1;
			var day = date.getDate();
			var hour = date.getHours();
			var minutes = date.getMinutes();
			var seconds = date.getSeconds();
			var nowDay = date.getFullYear() + "-" + (mon<10?"0"+mon:mon) + "-" +(day<10?"0"+day:day) + " " +
					(hour<10?"0"+hour:hour) + ":" + (minutes<10?"0"+minutes:minutes) + ":" + (seconds<10?"0"+seconds:seconds);
			/*选择查询开始时间*/
			jeDate({
				dateCell:"#startTime",
				format:"YYYY-MM-DD hh:mm:ss",
				isinitVal:false, /*初始值*/
				isTime:true, //isClear:false,
				minDate:"2014-09-19 00:00:00", //最小时间
				okfun:function(val){}
			})

			/*选择查询结束时间*/
			jeDate({
				dateCell:"#endTime",
				format:"YYYY-MM-DD hh:mm:ss",
				isinitVal:false, /*初始值*/
				isTime:true, //isClear:false,
				minDate:"2014-09-19 00:00:00", //最小时间
				okfun:function(val){}
			})
		});
	</script>

	<script>
		var ctx = "${ctx}";
		var customertype = "${customertype}";
	</script>
</body>

</html>