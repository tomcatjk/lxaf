<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>客户统计列表</title>
    <meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>客户统计列表 </h5>
        </div>

        <div class="ibox-content">
            <sys:message content="${message}"/>

            <!--查询条件-->
            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="customersalarms" action="${ctx}/lu/customers/listcustomer" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <span>客户名称：</span>
                            <input type="text" name="name" id="name" value="${customers.name}"  class="form-control " />
                        </div>

                        <div class="form-group">
                            <span>客户类别：</span>
                            <select name="customersTypeStr" id="customersTypeStr" class="form-control "  >
                                <option>${customerTypeTemp}</option>
                                <c:forEach items="${customersTypeList}" var="customerType">
                                    <c:if test="${customerType != customerTypeTemp}">
                                        <option>
                                                ${customerType}
                                        </option>
                                    </c:if>
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
                        <shiro:hasPermission name="lu:customers:import">
                            <table:importExcel url="${ctx}/lu/customers/import"></table:importExcel><!-- 导入按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="lu:customers:export">
                            <table:exportExcel url="${ctx}/lu/customers/export"></table:exportExcel><!-- 导出按钮 -->
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
                    <th >客户名称</th>
                    <th >客户类别</th>
                    <th >质检人</th>
                    <th >安装人</th>
                    <th >安装时间</th>
                    <th >主机</th>
                    <c:forEach items="${deviceTypeNameList}" var="deviceTypeName">
                        <th>${deviceTypeName}</th>
                    </c:forEach>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="customersalarms">
                    <tr>
                        <td>
                                ${customersalarms.name}
                        </td>
                        <td>
                                ${customersalarms.customersType}
                        </td>
                        <td>
                                ${customersalarms.qualityPerson}
                        </td>
                        <td>
                                ${customersalarms.installPerson}
                        </td>
                        <td>
                                <fmt:formatDate value="${customersalarms.installTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                                ${customersalarms.masterNum}
                        </td>
                        <td>${customersalarms.DEVICETYPE1}</td>
                        <td>${customersalarms.DEVICETYPE2}</td>
                        <td>${customersalarms.DEVICETYPE3}</td>
                        <td>${customersalarms.DEVICETYPE4}</td>
                        <td>${customersalarms.DEVICETYPE5}</td>
                        <td>${customersalarms.DEVICETYPE6}</td>
                        <td>${customersalarms.DEVICETYPE7}</td>
                        <td>${customersalarms.DEVICETYPE8}</td>
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