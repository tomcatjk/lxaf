<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>记录充值信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>记录充值信息列表 </h5>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="recharges" action="${ctx}/lu/recharges/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="lu:recharges:del">
				<table:delRow url="${ctx}/lu/recharges/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:recharges:import">
				<table:importExcel url="${ctx}/lu/recharges/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:recharges:export">
	       		<table:exportExcel url="${ctx}/lu/recharges/export"></table:exportExcel><!-- 导出按钮 -->
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
				<%--<th  >客户</th>--%>
				<th>用户</th>
				<th>充值金额</th>
				<th>备注</th>
				<th>受理人</th>
				<th>受理时间</th>
				<th>记录人</th>
				<th>记录时间</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="recharges">
            <input type="hidden" id="customerid" name="customerid" value="${recharges.customerid}">
            <input type="hidden" id="userid" name="userid" value="${recharges.userid}">
			<tr>
				<td> <input type="checkbox" id="${recharges.rid}" class="i-checks"></td>
				<td>
					${recharges.userName}
				</td>
				<td>
					${recharges.price}
				</td>
				<td>
					${recharges.remark}
				</td>
				<td>
					${recharges.receiver}
				</td>
				<td>
					<fmt:formatDate value="${recharges.createtime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${recharges.userName}
				</td>
				<td>
					<fmt:formatDate value="${recharges.createtime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${recharges.state == 1 ? "正在处理" : "到账"}
				</td>
				<td>
					<shiro:hasPermission name="lu:recharges:view">
						<a href="#" onclick="openDialogView('查看记录充值信息', '${ctx}/lu/recharges/form?id=${recharges.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="lu:recharges:edit">
    					<a href="#" onclick="openDialog('修改记录充值信息', '${ctx}/lu/recharges/form?id=${recharges.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="lu:recharges:del">
						<a href="${ctx}/lu/recharges/delete?rid=${recharges.rid}" onclick="return confirmx('确认要删除该记录充值信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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