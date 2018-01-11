<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>设备统计</title>
    <meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>设备统计 </h5>
        </div>

        <div class="ibox-content">
            <sys:message content="${message}"/>

            <!--查询条件-->
            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="devices" action="${ctx}/lu/devices/customerdevices" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <span>所属客户：</span>
                            <input name="customersName" id="customersName" value="${devicesCustomers.customersName}"class=" form-control input-sm"/>
                        </div>
                        <div class="form-group">
                            <span>设备类型：</span>
                            <select name="devicesType" id="devicesType" class="form-control ">
                                <c:forEach items="${deviceTypeNameMapList}" var="deviceTypeNameMap">
                                    <option value="${deviceTypeNameMap.deviceType}" <c:if test="${currentType == deviceTypeNameMap.deviceType}">selected</c:if> >
                                        ${deviceTypeNameMap.deviceTypeName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </form:form>
                    <br/>
                </div>
            </div>

            <!-- 工具栏 -->
            <div class="row">
                <div class="col-sm-12">
                    <div class="pull-left">
                        <shiro:hasPermission name="lu:devices:import">
                            <table:importExcel url="${ctx}/lu/devices/import"></table:importExcel><!-- 导入按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="lu:devices:export">
                            <table:exportExcel url="${ctx}/lu/devices/export"></table:exportExcel><!-- 导出按钮 -->
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
                        <th>序号</th>
                        <th>设备名称</th>
                        <th>设备类型</th>
                        <th>所属客户</th>
                        <th>状态</th>
                        <th>安装时间</th>
                        <th>到期时间</th>
                        <th>安装人</th>
                        <th>质检人</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="devicescustomers" varStatus="s">
                    <tr>
                        <td>
                                ${s.count}
                        </td>
                        <td>
                                ${devicescustomers.devicesName}
                        </td>
                        <td>
                                ${devicescustomers.devicesType}
                        </td>
                        <td>
                                ${devicescustomers.customersName}
                        </td>
                        <td>
                                ${devicescustomers.state}
                        </td>
                        <td>
                                <fmt:formatDate value="${devicescustomers.installTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                                <fmt:formatDate value="${devicescustomers.dueTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                                ${devicescustomers.installPerson}
                        </td>
                        <td>
                                ${devicescustomers.qualityPerson}
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