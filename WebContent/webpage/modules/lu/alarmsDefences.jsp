<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>报警信息详单</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jedate/jedate.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $(document).ready(function(){
                /*获取当前系统时间并转化为指定格式*/
                var date = new Date();
                var mon = date.getMonth() + 1;
                var day = date.getDate();
                var hour = date.getHours();
                var minutes = date.getMinutes();
                var seconds = date.getSeconds();
                var nowDay = date.getFullYear() + "-" + (mon<10?"0"+mon:mon) + "-" +(day<10?"0"+day:day) + " " +
                        (hour<10?"0"+hour:hour) + ":" + (minutes<10?"0"+minutes:minutes) + ":" + (seconds<10?"0"+seconds:seconds);
                /*选择查询开始时间*/
                jeDate({
                    dateCell:"#startTime",
                    format:"YYYY-MM-DD hh:mm:ss",
                    isinitVal:false, /*初始值*/
                    isTime:true, //isClear:false,
                    minDate:"2014-09-19 00:00:00", //最小时间
                    okfun:function(val){}
                })

                /*选择查询结束时间*/
                jeDate({
                    dateCell:"#endTime",
                    format:"YYYY-MM-DD hh:mm:ss",
                    isinitVal:false, /*初始值*/
                    isTime:true, //isClear:false,
                    minDate:"2014-09-19 00:00:00", //最小时间
                    okfun:function(val){}
                })
            });
        });
    </script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="ibox">
        <div class="ibox-title">
            <h5>报警信息详单 </h5>
        </div>

        <div class="ibox-content">
            <sys:message content="${message}"/>

            <!--查询条件-->
            <div class="row">
                <div class="col-sm-12">
                    <form:form id="searchForm" modelAttribute="alarmsdefences" action="${ctx}/lu/alarms/alarmsdef" method="post" class="form-inline">
                        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <span>报警时间：</span>
                                <input type="datetime" name="startTime" value="${alarmsDefences.startTime}" id="startTime" placeholder="请选择" class="laydate-icon form-control layer-date input-sm" />
                        </div>
                        <div class="form-group">
                            <span>—</span>
                                <input type="datetime" name="endTime" value="${alarmsDefences.endTime}" id="endTime" placeholder="请选择" class="laydate-icon form-control layer-date input-sm" />
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
                            <table:addRow url="${ctx}/lu/alarms/form" title="记录报警信息"></table:addRow><!-- 增加按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="lu:alarms:edit">
                            <table:editRow url="${ctx}/lu/alarms/form" title="记录报警信息" id="contentTable"></table:editRow><!-- 编辑按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="lu:alarms:import">
                            <table:importExcel url="${ctx}/lu/alarms/import"></table:importExcel><!-- 导入按钮 -->
                        </shiro:hasPermission>
                        <shiro:hasPermission name="lu:alarms:export">
                            <table:exportExcel url="${ctx}/lu/alarms/export"></table:exportExcel><!-- 导出按钮 -->
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
                        <th>客户类别</th>
                        <th>报警防区</th>
                        <th>报警类型</th>
                        <th>报警时间</th>
                        <th>处理结果</th>
                        <th>备注</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.list}" var="alarmsdefences">
                    <tr>
                        <td>
                                ${alarmsdefences.customersName}
                        </td>
                        <td>
                                ${alarmsdefences.customerTypeStr}
                        </td>

                        <td>
                                ${alarmsdefences.defencesName}
                        </td>
                        <td>
                                ${alarmsdefences.typeName}
                        </td>
                        <td>
                                <fmt:formatDate value="${alarmsdefences.date}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                                ${alarmsdefences.state}
                        </td>
                        <td>
                                ${alarmsdefences.remark}
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