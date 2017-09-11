<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>主机信息管理</title>
	<meta name="decorator" content="default"/>
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
	<form:form id="searchForm" modelAttribute="masters" action="${ctx}/lu/masters/" method="post" class="form-inline">
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
			<shiro:hasPermission name="lu:masters:add">
				<table:addRow url="${ctx}/lu/masters/form?customerid=${customerid}&masterFlag=1" title="主机信息"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:masters:import">
				<table:importExcel url="${ctx}/lu/masters/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:masters:export">
	       		<table:exportExcel url="${ctx}/lu/masters/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th> <input type="checkbox" class="i-checks"></th>
				<th >编号</th>
				<th >名称</th>
				<th >SIM卡号</th>
				<th >主机型号</th>
				<th >版本</th>
				<th >状态</th>
				<th >创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="masters">
			<tr>
				<td> <input type="checkbox" id="${masters.id}" class="i-checks"></td>
				<td>
					${masters.code}
				</td>
				<td>
					${masters.name}
				</td>
				<td>
					${masters.sim}
				</td>
				<td>
					${masters.maintype}
				</td>
				<td>
					${masters.version}
				</td>
				<td>
					${masters.stateStr}
				</td>
				<td>
					<fmt:formatDate value="${masters.createtime}" pattern="yyyy-MM-dd "/>
				</td>
				<td>
					<shiro:hasPermission name="lu:masters:view">
						<a href="#" onclick="openDialogView('查看主机信息', '${ctx}/lu/masters/form?mid=${masters.mid}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="lu:masters:edit">
    					<a href="#" onclick="openDialog('修改主机信息', '${ctx}/lu/masters/form?mid=${masters.mid}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="lu:masters:del">
						<a href="${ctx}/lu/masters/delete?mid=${masters.mid}&customerid=${customerid}" onclick="return confirmx('确认要删除该主机信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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