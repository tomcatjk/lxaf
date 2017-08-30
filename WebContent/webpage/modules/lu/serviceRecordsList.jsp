<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>服务记录管理</title>
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
		<h5>服务记录列表 </h5>
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
	<form:form id="searchForm" modelAttribute="serviceRecords" action="${ctx}/lu/serviceRecords/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>id：</span>
				<form:input path="id" htmlEscape="false" maxlength="11"  class=" form-control input-sm"/>
			<span>serverid：</span>
				<form:input path="serverid" htmlEscape="false" maxlength="11"  class=" form-control input-sm"/>
			<span>devicecode：</span>
				<form:input path="devicecode" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
			<span>deviceid：</span>
				<form:input path="deviceid" htmlEscape="false" maxlength="11"  class=" form-control input-sm"/>
			<span>creator：</span>
				<form:input path="creator" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
			<span>createtime：</span>
				<form:input path="createtime" htmlEscape="false"  class=" form-control input-sm"/>
			<span>state：</span>
				<form:input path="state" htmlEscape="false" maxlength="11"  class=" form-control input-sm"/>
			<span>remark：</span>
				<form:input path="remark" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
			<span>rated：</span>
				<form:input path="rated" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
			<span>star：</span>
				<form:input path="star" htmlEscape="false" maxlength="11"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="lu:serviceRecords:add">
				<table:addRow url="${ctx}/lu/serviceRecords/form" title="服务记录"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:serviceRecords:edit">
			    <table:editRow url="${ctx}/lu/serviceRecords/form" title="服务记录" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:serviceRecords:del">
				<table:delRow url="${ctx}/lu/serviceRecords/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:serviceRecords:import">
				<table:importExcel url="${ctx}/lu/serviceRecords/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="lu:serviceRecords:export">
	       		<table:exportExcel url="${ctx}/lu/serviceRecords/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column id">id</th>
				<th  class="sort-column serverid">serverid</th>
				<th  class="sort-column devicecode">devicecode</th>
				<th  class="sort-column deviceid">deviceid</th>
				<th  class="sort-column creator">creator</th>
				<th  class="sort-column createtime">createtime</th>
				<th  class="sort-column state">state</th>
				<th  class="sort-column remark">remark</th>
				<th  class="sort-column rated">rated</th>
				<th  class="sort-column star">star</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="serviceRecords">
			<tr>
				<td> <input type="checkbox" id="${serviceRecords.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看服务记录', '${ctx}/lu/serviceRecords/form?id=${serviceRecords.id}','800px', '500px')">
					${serviceRecords.id}
				</a></td>
				<td>
					${serviceRecords.serverid}
				</td>
				<td>
					${serviceRecords.devicecode}
				</td>
				<td>
					${serviceRecords.deviceid}
				</td>
				<td>
					${serviceRecords.creator}
				</td>
				<td>
					<fmt:formatDate value="${serviceRecords.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${serviceRecords.state}
				</td>
				<td>
					${serviceRecords.remark}
				</td>
				<td>
					${serviceRecords.rated}
				</td>
				<td>
					${serviceRecords.star}
				</td>
				<td>
					<shiro:hasPermission name="lu:serviceRecords:view">
						<a href="#" onclick="openDialogView('查看服务记录', '${ctx}/lu/serviceRecords/form?id=${serviceRecords.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="lu:serviceRecords:edit">
    					<a href="#" onclick="openDialog('修改服务记录', '${ctx}/lu/serviceRecords/form?id=${serviceRecords.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="lu:serviceRecords:del">
						<a href="${ctx}/lu/serviceRecords/delete?id=${serviceRecords.id}" onclick="return confirmx('确认要删除该服务记录吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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