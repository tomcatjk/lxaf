<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/common/jeeplus.js?16=10" type="text/javascript"></script>
	<script type="text/javascript">
		function submitFrom() {
			document.getElementById('searchForm').submit()
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
					<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/user/list" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
						<div class="form-group">
							<span>用户名：</span>
							<form:input path="loginName" htmlEscape="false" maxlength="50" cssStyle="width: 120px" class=" form-control input-sm"/>
							<span>登录状态：</span>
							<select id="loginFlag" name="loginFlag" style="font-size: 12px" class=" form-control input-sm">
								<option value=""></option>
								<c:forEach items="${loginTypeMapList}" var="loginTypeMap">
									<option value="${loginTypeMap.loginType}" <c:if test="${user.loginFlag != '' && loginTypeMap.loginType == user.loginFlag}">selected</c:if> >${loginTypeMap.loginTypeName}</option>
								</c:forEach>
							</select>
							<span>姓&nbsp;&nbsp;&nbsp;名：</span>
							<form:input path="name" htmlEscape="false" maxlength="50" cssStyle="width: 120px" class=" form-control input-sm"/>
						</div>
					</form:form>
					<br/>
				</div>
			</div>

			<!-- 工具栏 -->
			<div class="row">
				<div class="col-sm-12">
					<div class="pull-left">
						<shiro:hasPermission name="sys:user:add">
							<table:addRow url="${ctx}/sys/user/form" title="用户" width="800px" height="625px" target="officeContent"></table:addRow><!-- 增加按钮 -->
						</shiro:hasPermission>
						<shiro:hasPermission name="sys:user:del">
							<table:delRow url="${ctx}/sys/user/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
						</shiro:hasPermission>
						<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>

					</div>
					<div class="pull-right">
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="submitFrom()" ><i class="fa fa-search"></i> 查询</button>
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
					</div>
				</div>
			</div>
			<!-- 表格 -->
			<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
				<thead>
				<tr>
					<th><input type="checkbox" class="i-checks"></th>
					<th >姓名</th>
					<th >登录名</th>
					<th >角色</th>
					<th >状态</th>
					<th >所属客户</th>
					<th >备注</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="user">
					<tr>
						<td> <input type="checkbox" id="${user.id}" class="i-checks"></td>
						<td>${user.name}</td>
						<td>${user.loginName}</td>
						<td>${user.roleName}</td>
						<td>${user.loginFlag}</td>
						<td>${user.customerID}</td>
						<td>${user.remarks}</td>
						<td >
							<shiro:hasPermission name="sys:user:edit">
								<a href="#" onclick="openDialog('修改用户', '${ctx}/sys/user/form?id=${user.id}','800px', '700px', 'officeContent')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="sys:user:del">
								<a href="${ctx}/sys/user/delete?id=${user.id}" onclick="return confirmx('确认要删除该用户吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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