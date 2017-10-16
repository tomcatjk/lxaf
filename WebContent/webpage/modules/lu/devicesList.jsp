<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备信息管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/common/jeeplus.js?100=2" type="text/javascript"></script>
	<script type="text/javascript">
		function submitFrom() {
			document.getElementById('searchForm').submit()
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="devices" action="${ctx}/lu/devices/" method="post" class="form-inline">
		<input type="hidden" name="customerid" value="${customerid}">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>名称：</span>
			<input type="text" name="name" value="${masters.name}" maxlength="20"  class=" form-control input-sm"/>
		 </div>
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="lu:devices:add">
				<table:addRow url="${ctx}/lu/devices/form?customerid=${customerid}" title="设备"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:masters:del">
				<table:delRow url="${ctx}/lu/devices/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<%--<shiro:hasPermission name="lu:devices:import">--%>
				<%--<table:importExcel url="${ctx}/lu/devices/import"></table:importExcel><!-- 导入按钮 -->--%>
			<%--</shiro:hasPermission>--%>
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
				<th> <input type="checkbox" class="i-checks"></th>
				<th  >名称</th>
				<th  >设备类型</th>
				<th  >防区名称</th>
				<th  >防区状态</th>
				<th  >所属主机</th>
				<th  >创建人</th>
				<th  >创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="devices">
			<tr>
				<td> <input type="checkbox" id="${devices.did}" class="i-checks"></td>
				<td>
					${devices.name}
				</td>
				<td>
					${devices.devicetypeStr}
				</td>
				<td>
					${devices.defenceid}
				</td>
				<td>
					${devices.state == 1 ? "布防" : "撤防"}
				</td>
				<td>
					${devices.masterid}
				</td>
				<td>
						${devices.createid}
				</td>
				<td>
					<fmt:formatDate value="${devices.createtime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<shiro:hasPermission name="lu:devices:view">
						<a href="#" onclick="openDialogView('查看设备信息', '${ctx}/lu/devices/form?did=${devices.did}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="lu:devices:edit">
    					<a href="#" onclick="openDialog('修改设备信息', '${ctx}/lu/devices/form?did=${devices.did}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="lu:devices:del">
						<a href="${ctx}/lu/devices/delete?did=${devices.did}&customerid=${customerid}" onclick="return confirmx('确认要删除该设备信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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
</body>
</html>