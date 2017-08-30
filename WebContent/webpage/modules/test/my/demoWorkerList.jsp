<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>添加测试管理</title>
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
		<h5>添加测试列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="demoWorker" action="${ctx}/test/my/demoWorker/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>姓名：</span>
				<form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="test:my:demoWorker:add">
				<table:addRow url="${ctx}/test/my/demoWorker/form" title="添加测试"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="test:my:demoWorker:edit">
			    <table:editRow url="${ctx}/test/my/demoWorker/form" title="添加测试" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="test:my:demoWorker:del">
				<table:delRow url="${ctx}/test/my/demoWorker/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="test:my:demoWorker:import">
				<table:importExcel url="${ctx}/test/my/demoWorker/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="test:my:demoWorker:export">
	       		<table:exportExcel url="${ctx}/test/my/demoWorker/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column name">姓名</th>
				<th  class="sort-column code">编号</th>
				<th  class="sort-column company">所在公司</th>
				<th  class="sort-column email">电子邮件</th>
				<th  class="sort-column phone">电话</th>
				<th  class="sort-column photo">相片</th>
				<th  class="sort-column creatDate">创建日期</th>
				<th  class="sort-column createUser">创建人</th>
				<th  class="sort-column deleteFlag">删除标记</th>
				<th  class="sort-column updateDate"> 更新日期</th>
				<th  class="sort-column updateUser">更新人</th>
				<th  class="sort-column deleteUser">删除人</th>
				<th  class="sort-column password">密码</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="demoWorker">
			<tr>
				<td> <input type="checkbox" id="${demoWorker.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看添加测试', '${ctx}/test/my/demoWorker/form?id=${demoWorker.id}','800px', '500px')">
					${demoWorker.name}
				</a></td>
				<td>
					${demoWorker.code}
				</td>
				<td>
					${demoWorker.company}
				</td>
				<td>
					${demoWorker.email}
				</td>
				<td>
					${demoWorker.phone}
				</td>
				<td>
					${demoWorker.photo}
				</td>
				<td>
					${demoWorker.creatDate}
				</td>
				<td>
					${demoWorker.createUser}
				</td>
				<td>
					${demoWorker.deleteFlag}
				</td>
				<td>
					${demoWorker.updateDate}
				</td>
				<td>
					${demoWorker.updateUser}
				</td>
				<td>
					${demoWorker.deleteUser}
				</td>
				<td>
					${demoWorker.password}
				</td>
				<td>
					<shiro:hasPermission name="test:my:demoWorker:view">
						<a href="#" onclick="openDialogView('查看添加测试', '${ctx}/test/my/demoWorker/form?id=${demoWorker.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="test:my:demoWorker:edit">
    					<a href="#" onclick="openDialog('修改添加测试', '${ctx}/test/my/demoWorker/form?id=${demoWorker.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="test:my:demoWorker:del">
						<a href="${ctx}/test/my/demoWorker/delete?id=${demoWorker.id}" onclick="return confirmx('确认要删除该添加测试吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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