<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>报警信息统计列表</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function() {
        });
    </script>
    <script type="text/javascript">
        function searchLu(){
            document.getElementById("searchForm").submit();
        }
    </script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>报警信息统计列表 </h5>
        </div>

        <div class="ibox-content">
            <sys:message content="${message}"/>

            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="alarms" action="${ctx}/lu/alarms/count" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <span>客户名称：</span>
                            <input name="customerid" id="customerid"   value="${alarmscount.name}"class=" form-control input-sm"/>
                        </div>
                    </form:form>
                    <br/>
                </div>
            </div>

            <!-- 工具栏 -->
           <div class="row">
               <div class="col-sm-12">
                   <div class="pull-left">
                        <shiro:hasPermission name="lu:alarms:add">
                            <table:addRow url="${ctx}/lu/alarms/form" title="报警信息"></table:addRow><!-- 增加按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="lu:alarms:edit">
                            <table:editRow url="${ctx}/lu/alarms/form" title="报警信息" id="contentTable"></table:editRow><!-- 编辑按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="lu:alarms:import">
                            <table:importExcel url="${ctx}/lu/alarms/import"></table:importExcel><!-- 导入按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="lu:alarms:export">
                            <table:exportExcel url="${ctx}/lu/alarms/exportcount"></table:exportExcel><!-- 导出按钮 -->
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
                    <th>客户</th>
                    <c:forEach items="${alarmTypeList}" var="alarmType" varStatus="status">
                        <c:if test="${status.index < 32}">
                            <th>${alarmType}</th>
                        </c:if>
                    </c:forEach>
                </tr>
                <tr>
                    <th></th>
                    <c:forEach items="${alarmTypeList}" var="alarmType" varStatus="status">
                        <c:if test="${status.index > 31}">
                            <th>${alarmType}</th>
                        </c:if>
                    </c:forEach>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="alarmscount">
                    <tr>
                        <td>
                                ${alarmscount.name}
                        </td>
                        <td>
                                ${alarmscount.WARNING1}
                        </td>
                        <td>
                                ${alarmscount.WARNING2}
                        </td>
                        <td>
                                ${alarmscount.WARNING3}
                        </td>
                        <td>
                                ${alarmscount.WARNING4}
                        </td>
                        <td>
                                ${alarmscount.WARNING5}
                        </td>
                        <td>
                                ${alarmscount.WARNING6}
                        </td>
                        <td>
                                ${alarmscount.WARNING7}
                        </td>
                        <td>
                                ${alarmscount.WARNING8}
                        </td>
                        <td>
                                ${alarmscount.WARNING9}
                        </td>
                        <td>
                                ${alarmscount.WARNING10}
                        </td>
                        <td>
                                ${alarmscount.WARNING11}
                        </td>
                        <td>
                                ${alarmscount.WARNING12}
                        </td>
                        <td>
                                ${alarmscount.WARNING13}
                        </td>
                        <td>
                                ${alarmscount.WARNING14}
                        </td>
                        <td>
                                ${alarmscount.WARNING15}
                        </td>
                        <td>
                                ${alarmscount.WARNING16}
                        </td>
                        <td>
                                ${alarmscount.WARNING17}
                        </td>
                        <td>
                                ${alarmscount.WARNING18}
                        </td>
                        <td>
                                ${alarmscount.WARNING19}
                        </td>
                        <td>
                                ${alarmscount.WARNING20}
                        </td>
                        <td>
                                ${alarmscount.WARNING21}
                        </td>
                        <td>
                                ${alarmscount.WARNING22}
                        </td>
                        <td>
                                ${alarmscount.WARNING23}
                        </td>
                        <td>
                                ${alarmscount.WARNING24}
                        </td>
                        <td>
                                ${alarmscount.WARNING25}
                        </td>
                        <td>
                                ${alarmscount.WARNING26}
                        </td>
                        <td>
                                ${alarmscount.WARNING27}
                        </td>
                        <td>
                                ${alarmscount.WARNING28}
                        </td>
                        <td>
                                ${alarmscount.WARNING29}
                        </td>
                        <td>
                                ${alarmscount.WARNING30}
                        </td>
                        <td>
                                ${alarmscount.WARNING31}
                        </td>
                        <td>
                                ${alarmscount.WARNING32}
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                                ${alarmscount.WARNING33}
                        </td>
                        <td>
                                ${alarmscount.WARNING34}
                        </td>
                        <td>
                                ${alarmscount.WARNING35}
                        </td>
                        <td>
                                ${alarmscount.WARNING36}
                        </td>
                        <td>
                                ${alarmscount.WARNING37}
                        </td>
                        <td>
                                ${alarmscount.WARNING38}
                        </td>
                        <td>
                                ${alarmscount.WARNING39}
                        </td>
                        <td>
                                ${alarmscount.WARNING40}
                        </td>
                        <td>
                                ${alarmscount.WARNING41}
                        </td>
                        <td>
                                ${alarmscount.WARNING42}
                        </td>
                        <td>
                                ${alarmscount.WARNING43}
                        </td>
                        <td>
                                ${alarmscount.WARNING44}
                        </td>
                        <td>
                                ${alarmscount.WARNING45}
                        </td>
                        <td>
                                ${alarmscount.WARNING46}
                        </td>
                        <td>
                                ${alarmscount.WARNING47}
                        </td>
                        <td>
                                ${alarmscount.WARNING48}
                        </td>
                        <td>
                                ${alarmscount.WARNING49}
                        </td>
                        <td>
                                ${alarmscount.WARNING50}
                        </td>
                        <td>
                                ${alarmscount.WARNING51}
                        </td>
                        <td>
                                ${alarmscount.WARNING52}
                        </td>
                        <td>
                                ${alarmscount.WARNING53}
                        </td>
                        <td>
                                ${alarmscount.WARNING54}
                        </td>
                        <td>
                                ${alarmscount.WARNING55}
                        </td>
                        <td>
                                ${alarmscount.WARNING56}
                        </td>
                        <td>
                                ${alarmscount.WARNING57}
                        </td>
                        <td>
                                ${alarmscount.WARNING58}
                        </td>
                        <td>
                                ${alarmscount.WARNING59}
                        </td>
                        <td>
                                ${alarmscount.WARNING60}
                        </td>
                        <td>
                                ${alarmscount.WARNING61}
                        </td>
                        <td>
                                ${alarmscount.WARNING62}
                        </td>
                        <td>
                                ${alarmscount.WARNING63}
                        </td>
                        <td>
                                ${alarmscount.WARNING64}
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