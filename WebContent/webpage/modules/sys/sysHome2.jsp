<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>区域管理</title>
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/lianxun/popup/css/jquery.my-modal.1.1.winStyle.css" />
    <script src="${ctxStatic}/lianxun/rightcorner/jquery.messager.js"></script>
    <script src="${ctxStatic}/layer-v3.0.3/layer/layer.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/lianxun/lower-right-corner/css/style.css">
    <link rel="stylesheet" href="${ctxStatic}/lianxun/layui/src/css/layui.css">
    <script src="${ctxStatic}/lianxun/layui/src/layui.js"></script>
    <%@include file="/webpage/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function() {
            var datas = ${fns:toJson(list)};
            var html = '';
            for(var i=0;i<datas.length;i++){
                doData(datas[i],"#treeTableList"+i);
                $("#treeTableList"+i).treeTable({expandLevel : 5});
            }
        });

        function doData(data,target){
            var ids = [], rootIds = [];
            var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
            for (var i=0; i<data.length; i++){
                ids.push(data[i].id);
            }
            ids = ',' + ids.join(',') + ',';
            for (var i=0; i<data.length; i++){
                if (ids.indexOf(','+data[i].parentId+',') == -1){
                    if ((','+rootIds.join(',')+',').indexOf(','+data[i].parentId+',') == -1){
                        rootIds.push(data[i].parentId);
                    }
                }
            }
            for (var i=0; i<rootIds.length; i++){
                addRow(target, tpl, data, rootIds[i], true);
            }

        }

        function addRow(list, tpl, data, pid, root){

            for (var i=0; i<data.length; i++){
                var row = data[i];
                if ((${fns:jsGetVal('row.parentId')}) == pid){
                    $(list).append(Mustache.render(tpl, {
                        dict: {
                            blank123:0}, pid: (root?0:pid), row: row
                    }));
                    addRow(list, tpl, data, row.id);
                }
            }
        }

        function refresh(){//刷新

            window.location="${ctx}/home";
        }
    </script>

    <script type="text/javascript">
        //百度地图API功能
        function loadJScript() {
            var script = document.createElement("script");
            script.type = "text/javascript";
            script.src = "http://api.map.baidu.com/api?v=2.0&ak=G2gGMljPXi0YA5TWlmAgXcX2HVTgKU7T&callback=init";
            document.body.appendChild(script);
        }

        function init() {
            var map = new BMap.Map("allmap");            // 创建Map实例
            var point = new BMap.Point(116.404,39.915); // 创建点坐标
            map.centerAndZoom(point,15);
            map.enableScrollWheelZoom();                 //启用滚轮放大缩小

        }

        window.onload = loadJScript;  //异步加载地图

        var x1 = 116.404;
        var y1  = 39.915;
        function clickPoint(m,n){
            if(m != null && n !=null){
                x1 = m;
                y1 = n;
                var x2 = m + 0.013;
                var y2 = n - 0.006;
                var map = new BMap.Map("allmap");            // 创建Map实例
                var point = new BMap.Point(x1, y1); // 创建点坐标
                map.centerAndZoom(point,15);
                map.enableScrollWheelZoom();                 //启用滚轮放大缩小
                //创建标注
                var pt = new BMap.Point(x1, y1);
                var myIcon = new BMap.Icon("${ctxStatic}/lianxun/img/label48.png", new BMap.Size(48,48));
                var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
                map.addOverlay(marker2);              // 将标注添加到地图中
            }else{
                var map = new BMap.Map("allmap");            // 创建Map实例
                var point = new BMap.Point(x1,y1); // 创建点坐标
                map.centerAndZoom(point,15);
                map.enableScrollWheelZoom();                 //启用滚轮放大缩小
            }
        }

        var openMap = function (m,n) {
            var x1 = 116.404;
            var y1  = 39.915;
            if(m != null && n !=null){
                x1 = m;
                y1 = n;
                var x2 = m + 0.013;
                var y2 = n - 0.006;
                var map = new BMap.Map("openMap");            // 创建Map实例
                var point = new BMap.Point(x1, y1); // 创建点坐标
                map.centerAndZoom(point,15);
                map.enableScrollWheelZoom();                 //启用滚轮放大缩小
                //创建标注
                var pt = new BMap.Point(x1, y1);
                var myIcon = new BMap.Icon("${ctxStatic}/lianxun/img/label48.png", new BMap.Size(48,48));
                var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
                map.addOverlay(marker2);              // 将标注添加到地图中
            }else{
                var map = new BMap.Map("openMap");            // 创建Map实例
                var point = new BMap.Point(x1,y1); // 创建点坐标
                map.centerAndZoom(point,15);
                map.enableScrollWheelZoom();                 //启用滚轮放大缩小
            }
        }

    </script>

    <%--右下角报警信息--%>
    <script>
        var url = "${ctx}/lu/alarms/findAlarmsInfoAcd";
        var i = 0;
        var t = 0;
        var flag = 0;

        function setFlag() {
            if(flag == 0){
                flag = 1;
                $('.buttonClass').html('暂停');
                showImg(flag);
            }else{
                flag = 0;
                $('.buttonClass').html('开始');
                showImg(flag);
            }
        }

        function showImg(flag) {
            if(flag == 1){
                findAlarms();
                t = setTimeout("showImg(flag)",10*1000);
            }else{
                clearTimeout(t);
            }
        }

        var findAlarms = function () {
            var html = "<table id='contentTable' class='table table-striped table-bordered table-hover table-condensed dataTables-example dataTable'>";
            html += "<tr>";
            html += "<th>客户名称</th>";
            html += "<th>联系电话</th>";
            html += "<th>报警防区</th>";
            html += "<th>报警类型</th>";
            html += "<th>报警时间</th>";
            html += "<th>操作</th>";
            html += "</tr>";
            var html2 = html;
            $.post(url,function (alarmsInfoAcdList) {
                var flag = 0;
                var count1 = 0;
                var count2 = 0;
                $.each(alarmsInfoAcdList,function (index,alarmsInfoAcd) {
                    if(alarmsInfoAcd.state == 1){//未处理列表
                        count1++;
                        //播放报警信息
                        if(flag == 0){
                            PlayMedia();
                            flag = 1;
                        }
                        html += "<tr>"
                        html += "<td onclick='clickPoint("+alarmsInfoAcd.point+")'><a href='#2' style='color: blue;'> "+alarmsInfoAcd.cname+"</a></td>"
                        html += "<td>"+alarmsInfoAcd.phone+"</td>";
                        html += "<td>"+alarmsInfoAcd.dname+"</td>";
                        html += "<td>"+alarmsInfoAcd.alarmTypeName+"</td>";
                        html += "<td>"+alarmsInfoAcd.alarmTime+"</td>";
                        html += "<td>";
                        html += "<button class='layui-btn' onclick=openWindow(" + alarmsInfoAcd.point + ",'" + alarmsInfoAcd.aid + "')>处理</button>";
                        html += "</td>";
                        html += "</tr>";
                    }else{
                        count2++;
                        html2 += "<tr>"
                        html2 += "<td onclick='clickPoint("+alarmsInfoAcd.point+")'><a href='#2' style='color: blue;'>"+alarmsInfoAcd.cname+"</a></td>"
                        html2 += "<td>"+alarmsInfoAcd.phone+"</td>";
                        html2 += "<td>"+alarmsInfoAcd.dname+"</td>";
                        html2 += "<td>"+alarmsInfoAcd.alarmTypeName+"</td>";
                        html2 += "<td>"+alarmsInfoAcd.alarmTime+"</td>";
                        html2 += "<td>";
                        html2 += "<button class='layui-btn' onclick=openWindow2('" + alarmsInfoAcd.aid + "')>完成</button>";
                        html2 += "</td>";
                        html2 += "</tr>";
                    }

                });

                html += "</table>";
                html += "table111111111111";
                html += "<a id='baiduId' href='#' onclick='baiduName()'>baiduId</a>";
                html2 += "</table>";
                html2 += "table22222222222";
                if(count1 == 0){
                    html = "</br>&nbsp;&nbsp;&nbsp;<span style='background-color: white;font-size: 20px;'>没有待处理的报警信息</span></br>";
                }
                if(count2 == 0){
                    html2 = "</br>&nbsp;&nbsp;&nbsp;<span style='background-color: white;font-size: 20px;'>没有已处理的报警信息</span></br>";
                }
                var html12 = "&nbsp;<button id='clickPending' class='layui-btn layui-btn-warm' onclick='clickPending()'>待处理</button>&nbsp;&nbsp;";
                html12 += "<button id='clickProcessed' class='layui-btn layui-btn-primary' onclick='clickProcessed()'>已处理</button>";
                html12 += "<div id='pendingId' style='display: inline'>";
                html12 += html;
                html12 += "</div>";
                html12 += "<div id='processedId' style='display: none'>";
                html12 += html2;
                html12 += "</div>";
                <%--html12 += "<table:page page='${page}'></table:page>";--%>
                $(".extbkboxb").html(html12);
            });
        }

        $(document).ready(function () {
            setFlag();
        });

        var clickPending = function () {
            $("#pendingId").show();
            $("#processedId").hide();
            $("#clickPending").attr('class','layui-btn layui-btn-warm');
            $("#clickProcessed").attr('class','layui-btn layui-btn-primary');

        }

        var clickProcessed = function () {
            $("#pendingId").hide();
            $("#processedId").show();
            $("#clickProcessed").attr('class','layui-btn layui-btn-warm');
            $("#clickPending").attr('class','layui-btn layui-btn-primary');
        }

        var baiduName = function () {
            $("#baiduId").html("我是百度");
        }
    </script>

    <%--右下角-处理--%>
    <script>
        var testTemp = function (a) {
            console.log(a);
        }
        var openWindow = function (m,n,aid) {
            var urlPersons = "${ctx}/lu/alarms/findServicePersons";
            $("#openMap").width(300);
            $("#openMap").height(300);
            var html = "<input class='aidClass' type='hidden' value='" + aid + "' >";
            html += "<strong>&nbsp;分配服务人员：</strong></br>";
            html += "&nbsp;<select class='serviceNameClass ' name='serviceName' style='width: 180px; height: 30px;'>";
            $.post(urlPersons,function (ServicePersonsPartListJson) {
                $.each(ServicePersonsPartListJson,function (index, servicePersonsPart) {
                    html += "<option value='" + servicePersonsPart.id + "'>" + servicePersonsPart.name + "</option>";
                });
                html += "</select>&nbsp;&nbsp;&nbsp;";
                html += "</br></br>"
                html += "&nbsp;<button class='layui-btn' type='button' onclick='addServiceName()'>确定</button>";
                $("#worker").html(html);
                $("#worker").show();
            });

            openMap(m,n);
            layer.open({
                type: 1,
                title: "分配服务人员",
                skin: 'layui-layer-rim', //加上边框
                area: ['550px', '320px'], //宽高
                content: $(".openWindow"),
                cancel: function () {
                    //右上角关闭回调
                    //return false 开启该代码可禁止点击该按钮关闭
                    $("#openMap").width(0);
                    $("#openMap").height(0);
                    $("#worker").hide();
                }

            });


        }

        var addServiceName = function () {
            var aid = $(".aidClass").val();
            var servicePersonsId = $(".serviceNameClass").val();
            var url = "${ctx}/lu/alarms/addServicePersonForAlarms?aid=" + aid + "&servicePersonsId=" + servicePersonsId;
            $.post(url,function (date) {
                if(date == 'ok'){

                }else{

                }
            });
            $("#openMap").width(0);
            $("#openMap").height(0);
            $("#worker").hide();
            layer.closeAll('page');
        }
    </script>

    <%--右下角-完成--%>
    <script>
        var openWindow2 = function (aid) {
            var html = "&nbsp;<div>评论：</div>"
            html += "&nbsp;<textarea class='ratedClass layui-textarea' name='rated'></textarea>";
            html += "</br>";
            html += "&nbsp;<button class='layui-btn' type='button' onclick=addReted('" + aid + "')>确定</button>";
            layer.open({
                type: 1,
                title: "评论",
                skin: 'layui-layer-rim', //加上边框
                area: ['550px', '320px'], //宽高
                content: html
            });
        }

        var addReted = function (aid) {
            var rated = $(".ratedClass").val();
            var url = "${ctx}/lu/alarms/completeAlarms?aid=" + aid +"&rated=" + rated;
            $.post(url, function (date) {
                if(date == 'ok'){

                }else{

                }
            });
            layer.closeAll('page');
        }
    </script>

    <script type="text/javascript">
        function extbkboxnar(){
            var narrowmen = document.getElementById("extbkboxnar");
            var narrowbox = document.getElementById("extbkboxb");
            if (narrowbox.style.display == "block"){
                narrowbox.style.display = "none";
                narrowmen.className = "extbkboxnarove";
            }
            else{
                narrowbox.style.display = "block";
                narrowmen.className = "extbkboxnar";
            }
        }

        function extbkboxove(str){document.getElementById("extbkbox").style.display = "none";}
    </script>

    <script>
        function OpenAlarmLight() {
            try {
                document.getElementById("ctrlLight").Open("COM3");
            }
            catch (e) {
                // alert(e);
            }
        }
        function CloseAlarmLight() {
            try {
                document.getElementById("ctrlLight").Close("COM3");
            }
            catch (e) {
                //   alert(e);
            }
        }


        function PlayMedia() {
            var ck = $("#chkPlay")[0];
            if (ck.checked) {
                var srcVal = "${ctxStatic}/lianxun/alarm/ALARM8.swf";
                var html = "<embed src='"+srcVal+"' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='0' height='0'></embed>";
                $('#wav').html(html);
                    OpenAlarmLight();
            } else {
                $('#wav').html("");
                CloseAlarmLight();
            }
        }
    </script>

</head>
<body>
    <%--<div class="wrapper wrapper-content">--%>
        <div id="extbkbox" class="extbkbox" style="display:block; width: auto;">

            <div class="extbkboxm">
                <strong style="float:left;padding-left:10px;">报警信息</strong><label>&nbsp;&nbsp;&nbsp;
                <input id="chkPlay" name="alarm" type="checkbox" value="" />声音 </label>&nbsp;&nbsp;&nbsp;
                <div class="extmore">
                    <span class="extbkboxnar" id="extbkboxnar" onclick="extbkboxnar();"></span>
                </div>
            </div>

            <div class="extbkboxb" id="extbkboxb" style="display:block;">
                <span>没有报警信息</span>
            </div>
        </div>

        <div class="openWindow">
            <div id="openMap" style="float: left"></div>
            <div id="worker" style="float: left"></div>
        </div>

        <div class="ibox">
            <%--<div class="ibox-content">--%>
                <div style="width: 20%;height: 100%; overflow: scroll;max-height: 800px;float: left">
                    <table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
                        <thead>
                        <tr>
                            <th>区域及客户</th>
                        </tr>
                        </thead>
                        <tbody id="treeTableList0"></tbody>
                        <tbody id="treeTableList1"></tbody>
                        <tbody id="treeTableList2"></tbody>
                        <tbody id="treeTableList3"></tbody>
                        <tbody id="treeTableList4"></tbody>
                        <tbody id="treeTableList5"></tbody>
                        <tbody id="treeTableList6"></tbody>
                    </table>
                    <script type="text/template" id="treeTableTpl">
                        <tr id="{{row.id}}" pId="{{pid}}">
                            <td>
                                <span onclick="clickPoint({{row.point}})"><a href="#4">{{row.name}}</a></span>
                            </td>
                        </tr>
                    </script>
                </div>
                <div id="allmap" style="width: 80%; height: 800px;"></div>
            <%--</div>--%>
        </div>
    <%--</div>--%>
    <object id="ctrlLight" classid="clsid:C6B41616-6238-42B6-BB05-F6C87A26B9F4" width="0"
            height="20">
    </object>
    <div id="wav">
    </div>
</body>

</html>