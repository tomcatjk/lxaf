]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
			<h5>角色列表 </h5>
	</div>
    <div class="ibox-content">
	<sys:message content="${message}"/>

		<!-- 查询条件 -->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="role" action="${ctx}/sys/role/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>角色名称：</span>
				<form:input path="name" value="${role.name}"  htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>

		 </div>
	</form:form>
	<br/>
	</div>
	</div>

		<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="sys:role:add">
				<table:addRow url="${ctx}/sys/role/form" title="角色"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:role:edit">
			    <table:editRow url="${ctx}/sys/role/form" id="contentTable"  title="角色"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:role:del">
				<table:delRow url="${ctx}/sys/role/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>

			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>

	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th><input type="checkbox" class="i-checks"></th>
				<th>名称</th>
				<th>创建人</th>
				<th>创建日期</th>
				<th>备注</th>
				<shiro:hasPermission name="sys:role:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="role">
				<tr>
					<td> <input type="checkbox" id="${role.id}" class="i-checks"></td>
					<td><%--<a  href="#" onclick="openDialogView('查看角色', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')">--%>${role.name}<%--</a>--%></td>
					<td>${role.createName}</td>
					<td>${role.createTime}</td>
					<td>${role.remarks}</td>
					<td>
						<%--<shiro:hasPermission name="sys:role:view">
						<a href="#" onclick="openDialogView('查看角色', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
						</shiro:hasPermission>--%>
						<shiro:hasPermission name="sys:role:edit">
						<c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
							<a href="#" onclick="openDialog('修改角色', '${ctx}/sys/role/form?id=${role.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
						</c:if>
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:role:del">
						<a href="${ctx}/sys/role/delete?id=${role.id}" onclick="return confirmx('确认要删除该角色吗？', this.href)" class="btn  btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:role:assign">
							<a href="#" onclick="openDialog('权限设置', '${ctx}/sys/role/auth?id=${role.id}','350px', '700px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i> 权限设置</a>
						</shiro:hasPermission>
                        <shiro:hasPermission name="sys:role:assign">
                            <a href="#" onclick="openDialog('区域管理', '${ctx}/lu/roleArea/areaList?roleId=${role.id}','350px', '700px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i> 区域管理</a>
                        </shiro:hasPermission>
						<%--<shiro:hasPermission name="sys:role:assign">
						<a href="#" onclick="openDialogView('分配用户', '${ctx}/sys/role/assign?id=${role.id}','800px', '600px')"  class="btn  btn-warning btn-xs" ><i class="glyphicon glyphicon-plus"></i> 分配用户</a>
						</shiro:hasPermission>--%>
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