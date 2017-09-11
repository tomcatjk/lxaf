<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>AFCustomers 管理</title>
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
		<h5>AFCustomers 列表 </h5>
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
	<form:form id="searchForm" modelAttribute="afcustomers" action="${ctx}/lu/afcustomers/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>编号：</span>
				<form:input path="code" htmlEscape="false" maxlength="50"  class=" form-control input-sm"/>
			<span>姓名：</span>
				<form:input path="name" htmlEscape="false" maxlength="50"  class=" form-control input-sm"/>
			<span>年龄：</span>
				<form:input path="age" htmlEscape="false" maxlength="4"  class=" form-control input-sm"/>
			<span>性别：</span>
				<form:input path="gender" htmlEscape="false" maxlength="10"  class=" form-control input-sm"/>
			<span>电话：</span>
				<form:input path="phone" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>地址：</span>
				<form:input path="address" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
			<span>坐标经度：</span>
				<form:input path="latitude" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>坐标纬度：</span>
				<form:input path="longitude" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>头像：</span>
				<form:input path="imgurl" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="lu:afcustomers:add">
				<table:addRow url="${ctx}/lu/afcustomers/form" title="AFCustomers "></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:afcustomers:edit">
			    <table:editRow url="${ctx}/lu/afcustomers/form" title="AFCustomers " id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:afcustomers:del">
				<table:delRow url="${ctx}/lu/afcustomers/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:afcustomers:import">
				<table:importExcel url="${ctx}/lu/afcustomers/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:afcustomers:export">
	       		<table:exportExcel url="${ctx}/lu/afcustomers/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column code">编号</th>
				<th  class="sort-column name">姓名</th>
				<th  class="sort-column age">年龄</th>
				<th  class="sort-column gender">性别</th>
				<th  class="sort-column phone">电话</th>
				<th  class="sort-column address">地址</th>
				<th  class="sort-column latitude">坐标经度</th>
				<th  class="sort-column longitude">坐标纬度</th>
				<th  class="sort-column imgurl">头像</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="afcustomers">
			<tr>
				<td> <input type="checkbox" id="${afcustomers.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看AFCustomers ', '${ctx}/lu/afcustomers/form?id=${afcustomers.id}','800px', '500px')">
					${afcustomers.code}
				</a></td>
				<td>
					${afcustomers.name}
				</td>
				<td>
					${afcustomers.age}
				</td>
				<td>
					${afcustomers.gender}
				</td>
				<td>
					${afcustomers.phone}
				</td>
				<td>
					${afcustomers.address}
				</td>
				<td>
					${afcustomers.latitude}
				</td>
				<td>
					${afcustomers.longitude}
				</td>
				<td>
					${afcustomers.imgurl}
				</td>
				<td>
					<shiro:hasPermission name="lu:afcustomers:view">
						<a href="#" onclick="openDialogView('查看AFCustomers ', '${ctx}/lu/afcustomers/form?id=${afcustomers.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="lu:afcustomers:edit">
    					<a href="#" onclick="openDialog('修改AFCustomers ', '${ctx}/lu/afcustomers/form?id=${afcustomers.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="lu:afcustomers:del">
						<a href="${ctx}/lu/afcustomers/delete?id=${afcustomers.id}" onclick="return confirmx('确认要删除该AFCustomers 吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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