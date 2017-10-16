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
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/lianxun/lower-right-corner/css/style.css?6=6">
    <link rel="stylesheet" href="${ctxStatic}/lianxun/layui/src/css/layui.css">
    <%--<script src="${ctxStatic}/lianxun/layui/src/layui.js?1=2"></script>--%>
    <%@include file="/webpage/include/treetable.jsp" %>

    <%--右下角表格样式修改--%>
    <style type="text/css">
        th{
            background: lightgrey;
        }

        tr{
            height: 30px;
        }
    </style>

    <script>
        $(function () {
            $("#allmap").css("width","84%");
            $("#allmap").css("height",$(window).height());
            $("#switchSysBarId").css("height",$(window).height());
            $("#switchPoint").css("line-height", $(window).height() + "px");
            $("#frmtitle").css("height",$(window).height());
        });

        $(window).resize(function() {
            clickPoint(x1,y1,addressTemp,cnameTemp,phoneTemp);
        });
    </script>


    <script type="text/javascript">

        var datas;

        $(document).ready(function() {
            isOnline(2);
        });

        var areasCustomer = function () {
            var html = '';
            for(var i=0;i<datas.length;i++){
                $("#treeTableList"+i).html(html);
                doData(datas[i],"#treeTableList"+i);
                $("#treeTableList"+i).treeTable({expandLevel : 5});
            }
        }

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

        //刷新
        function refresh(){
            window.location="${ctx}/home";
        }
    </script>

    <%--百度地图--%>
    <script type="text/javascript">
        //百度地图API功能
        function loadJScript() {
            var script = document.createElement("script");
            script.type = "text/javascript";
            script.src = "http://api.map.baidu.com/api?v=2.0&ak=F454f8a5efe5e577997931cc01de3974&callback=init";
            document.body.appendChild(script);
        }

        function init() {
            var map = new BMap.Map("allmap");            // 创建Map实例
            var point = new BMap.Point(116.404,39.915); // 创建点坐标
            map.centerAndZoom(point,15);
            map.enableScrollWheelZoom();                 //启用滚轮放大缩小

        }

        window.onload = loadJScript;  //异步加载地图

        var x1 = 0;
        var y1 = 0;
        var addressTemp;
        var cnameTemp;
        var phoneTemp;
        var isClick = 1;

        function clickPoint(m,n,address,cname,phone){
            $("#frmtitle").css("height",$(window).height());
            $("#allmap").css("height",$(window).height());
            $("#switchSysBarId").css("height",$(window).height());
            $("#switchPoint").css("line-height", $(window).height() + "px");
            x1 = 116.404;
            y1  = 39.915;
            if(m != null && n !=null && address != null){
                x1 = m;
                y1 = n;
                addressTemp = address;
                cnameTemp = cname;
                phoneTemp = phone;
//                var x2 = m + 0.013;
//                var y2 = n - 0.006;
                var map = new BMap.Map("allmap");            // 创建Map实例
                var point = new BMap.Point(x1, y1); // 创建点坐标
                //创建标注
                var myIcon = new BMap.Icon("${ctxStatic}/lianxun/img/label48.png", new BMap.Size(48,48));
                var marker2 = new BMap.Marker(point,{icon:myIcon});  // 创建标注
                var opts = {
                    width : 200,     // 信息窗口宽度
                    height: 50,     // 信息窗口高度
                }
                var customerInfo = "姓名:" + cname;
                customerInfo += "</br>电话:" + phone;
                customerInfo += "</br>地址:" + address;
                var infoWindow = new BMap.InfoWindow(customerInfo, opts);  // 创建信息窗口对象
                marker2.addEventListener("click", function(){
                    isClick = 0;
                    clickPoint(x1,y1,addressTemp,cnameTemp,phoneTemp);
                });
                if(isClick == 0){
                    isClick = 1;
                    map.openInfoWindow(infoWindow,point); //开启信息窗口
                }
                map.addOverlay(marker2);              // 将标注添加到地图中
                map.centerAndZoom(point,15);
                map.enableScrollWheelZoom();                 //启用滚轮放大缩小
            }else{
                var map = new BMap.Map("allmap");            // 创建Map实例
                var point = new BMap.Point(x1,y1); // 创建点坐标
                map.centerAndZoom(point,15);
                map.enableScrollWheelZoom();                 //启用滚轮放大缩小
            }
        }

        var openMap = function (m,n) {
            x1 = 116.404;
            y1  = 39.915;
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
                whichOne = 0;
                findAlarms(1);
                t = setTimeout("showImg(flag)",30*1000);
            }else{
                clearTimeout(t);
            }
        }

        var whichOne = 0;
        var endPage = 0;

        var findAlarms = function (currentPage) {
            if(endPage != 0){
                if(currentPage < 1){
                    currentPage = 1;
                }else if(endPage < currentPage ){
                    currentPage = endPage;
                }
            }
            var url = "${ctx}/lu/alarms/findAlarmsInfoAcd?currentPage=" + currentPage;
            var html = "<table id='contentTable' class='table table-striped table-bordered table-hover table-condensed dataTables-example dataTable'>";
            html += "<tr>";
            html += "<th>客户名称</th>";
            html += "<th>联系电话</th>";
            html += "<th>报警防区</th>";
            html += "<th>报警类型</th>";
            html += "<th>报警时间</th>";
            html += "<th>操作</th>";
            html += "</tr>";

            $.ajaxSetup({
                async : false
            });

            $.post(url + "&state=" + (whichOne + 1), function (resultMap) {
                var alarmsInfoAcdList = resultMap[0].alarmsInfoAcdList;
                var currentPage = resultMap[0].currentPage;
                endPage = resultMap[0].endPage;
                var countTemp = 0;
                $.each(alarmsInfoAcdList, function (index, alarmsInfoAcd) {
                    countTemp++;
                    html += "<tr>";
                    html += "<td onclick=clickPoint(" + alarmsInfoAcd.point + ",'" + alarmsInfoAcd.address + "','" + alarmsInfoAcd.cname + "','" + alarmsInfoAcd.phone +"')><a href='#2' style='color: blue;'> " + alarmsInfoAcd.cname + "</a></td>"
                    html += "<td>" + alarmsInfoAcd.phone + "</td>";
                    html += "<td>" + alarmsInfoAcd.dname + "</td>";
                    html += "<td>" + alarmsInfoAcd.alarmTypeName + "</td>";
                    html += "<td>" + alarmsInfoAcd.alarmTime + "</td>";
                    html += "<td>";
                    if(whichOne == 0) {
                        html += "<button class='layui-btn layui-btn-small' onclick=openWindow(" + alarmsInfoAcd.point + ",'" + alarmsInfoAcd.aid + "')>处理</button>";
                    }
                    if(whichOne == 1){
                        html += "<button class='layui-btn layui-btn-small' onclick=openWindow2('" + alarmsInfoAcd.aid + "')>完成</button>";
                    }
                    html += "</td>";
                    html += "</tr>";
                });
                html += "</table>";
                html += "<button class='layui-btn layui-btn-primary layui-btn-small' onclick=findAlarms(" + 1 + ")>首页</button>";
                html += "<button class='layui-btn layui-btn-primary layui-btn-small' onclick=findAlarms(" + (currentPage - 1) + ")><i class='layui-icon'></i></button>";
                if (currentPage + 2 <= endPage) {
                    for (var i = currentPage - 2, count = 1; i <= endPage; i++, count ++) {
                        if(i < 1) i = 1;
                        if(count > 5) break;
                        if (currentPage != i) {
                            html += "<button id='pageClassId" + i + "' class='layui-btn layui-btn-primary layui-btn-small' onclick=findAlarms('" + i + "')>" + i + "</button>";
                        } else {
                            html += "<button id='pageClassId" + i + "' class='layui-btn layui-btn-small' onclick=findAlarms('" + i + "')>" + i + "</button>";
                        }
                    }
                }else {
                    for (var i = endPage - 4; i <= endPage; i++) {
                        if(i < 1) i = 1;
                        if (currentPage != i) {
                            html += "<button id='pageClassId" + i + "' class='layui-btn layui-btn-primary layui-btn-small' onclick=findAlarms('" + i + "')>" + i + "</button>";
                        } else {
                            html += "<button id='pageClassId" + i + "' class='layui-btn layui-btn-small' onclick=findAlarms('" + i + "')>" + i + "</button>";
                        }
                    }
                }
                html += "<button class='layui-btn layui-btn-primary layui-btn-small' onclick=findAlarms(" + (currentPage + 1) + ")><i class='layui-icon'></i></button>";
                html += "<button class='layui-btn layui-btn-primary layui-btn-small' onclick=findAlarms(" + endPage + ")>末页</button>";
                html += "<button class='layui-btn layui-btn-primary layui-btn-small'>共" + endPage + "页</button>";
                if (whichOne == 0 && countTemp == 0) {
                    html = "</br>&nbsp;&nbsp;&nbsp;<span style='background-color: white;font-size: 20px;'>没有待处理的报警信息</span></br>";
                }
                if (whichOne == 1 && countTemp == 0) {
                    html = "</br>&nbsp;&nbsp;&nbsp;<span style='background-color: white;font-size: 20px;'>没有已处理的报警信息</span></br>";
                }
                //播放报警信息
                if (whichOne == 0 && countTemp > 0) {
                    PlayMedia();
                    var narrowmen = document.getElementById("extbkboxnar");
                    var narrowbox = document.getElementById("extbkboxb");
                    narrowbox.style.display = "block";
                    narrowmen.className = "extbkboxnar";
                }else{
                    $('#wav').html("");
                }
            });



            var html12 = "&nbsp;<button id='clickPending' class='layui-btn layui-btn-warm layui-btn-small' onclick='clickPending()'>待处理</button>&nbsp;&nbsp;";
            html12 += "<button id='clickProcessed' class='layui-btn layui-btn-primary layui-btn-small' onclick='clickProcessed()'>已处理</button>";
            html12 += "<div style='display: inline'>";
            html12 += html;
            html12 += "</div>";
            $(".extbkboxb").html(html12);

            if(whichOne == 0){
                $("#clickPending").attr('class','layui-btn layui-btn-warm layui-btn-small');
                $("#clickProcessed").attr('class','layui-btn layui-btn-primary layui-btn-small');
            }

            if(whichOne == 1){
                $("#clickProcessed").attr('class','layui-btn layui-btn-warm layui-btn-small');
                $("#clickPending").attr('class','layui-btn layui-btn-primary layui-btn-small');
            }
        }

        $(document).ready(function () {
            setFlag();
//            console.log("doc whichOne:" + whichOne);
//            findAlarms(1);
        });

        var clickPending = function () {
            whichOne = 0;
            findAlarms(1);
        }

        var clickProcessed = function () {
            whichOne = 1;
            findAlarms(1);
        }
    </script>

    <%--右下角-处理按钮--%>
    <script>
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
                area: ['550px', '360px'], //宽高
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
            if(aid == null || aid == '') return false;
            var servicePersonsId = $(".serviceNameClass").val();
            if(servicePersonsId == null || servicePersonsId == '') return false;
            var url = "${ctx}/lu/alarms/addServicePersonForAlarms?aid=" + aid + "&servicePersonsId=" + servicePersonsId;
            $.post(url,function (date) {
                if(date == 'ok'){
                    whichOne = 0;
                    findAlarms(1);
                }else{

                }
            });
            $("#openMap").width(0);
            $("#openMap").height(0);
            $("#worker").hide();
            layer.closeAll('page');
        }
    </script>

    <%--右下角-完成按钮--%>
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
                    whichOne = 1;
                    findAlarms(1);
                }else{

                }
            });
            layer.closeAll('page');
        }
    </script>

    <%--右下角最小化--%>
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
    </script>

    <%--报警--%>
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


    <%--折叠--%>
    <style type="text/css">
        *{margin:0;padding:0;list-style-type:none; font-size:12px}
    </style>

    <script type="text/javascript">
        function switchSysBar(){
            if(switchPoint.innerText==3){
                switchPoint.innerText=4
                document.all("frmtitle").style.display="none"
                $("#allmap").css("width","99%");
            }else{
                switchPoint.innerText=3
                document.all("frmtitle").style.display=""
                $("#allmap").css("width","84%");
            }
            $("#allmap").css("height",$(window).height());
            $("#switchSysBarId").css("height",$(window).height());
            $("#switchPoint").css("line-height", $(window).height() + "px");
            $("#frmtitle").css("height",$(window).height());
            clickPoint(x1,y1,addressTemp,cnameTemp,phoneTemp);
        }
    </script>

    <%-- 客户在线离线 --%>
    <script>
        $.ajaxSetup({
            async : false
        });

        var isOnline = function (showSign) {
            if(showSign == 1){
                $("#b1").attr('class','layui-btn layui-btn-warm layui-btn-small');
                $("#b0").attr('class','layui-btn layui-btn-primary layui-btn-small');
                $("#b2").attr('class','layui-btn layui-btn-primary layui-btn-small');
            }else if(showSign == 0){
                $("#b1").attr('class','layui-btn layui-btn-primary layui-btn-small');
                $("#b0").attr('class','layui-btn layui-btn-warm layui-btn-small');
                $("#b2").attr('class','layui-btn layui-btn-primary layui-btn-small');
            }else {
                $("#b1").attr('class','layui-btn layui-btn-primary layui-btn-small');
                $("#b0").attr('class','layui-btn layui-btn-primary layui-btn-small');
                $("#b2").attr('class','layui-btn layui-btn-warm layui-btn-small');
            }

            $.get("${ctx}/getAreasCustomers?showSign=" + showSign, function (data) {
                datas = data;
                areasCustomer();
            });
        }
    </script>
</head>
<body>
<%--右下角报警框--%>
<div id="extbkbox" class="extbkbox" style="display:block; width: auto;">
    <div class="extbkboxm">
        <strong style="float:left;padding-left:10px;">报警信息</strong><label>&nbsp;&nbsp;&nbsp;
        <input id="chkPlay" name="alarm" type="checkbox" checked value="" />声音 </label>&nbsp;&nbsp;&nbsp;
        <div class="extmore">
            <span class="extbkboxnar" id="extbkboxnar" onclick="extbkboxnar();"></span>
        </div>
    </div>

    <div class="extbkboxb" id="extbkboxb" style="display:block;">
        <%--<span>没有报警信息</span>--%>
    </div>
</div>

<div class="openWindow">
    <div id="openMap" style="float: left"></div>
    <div id="worker" style="float: left"></div>
</div>

<div class="ibox" style="height: calc(100% - 100px);">
    <div id="frmtitle" style="width: 15%;height: 0px; overflow: scroll;float: left;">
        <table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable" style="margin-top: 0;">
            <thead>
            <tr>
                <th>区域及客户</th>
            </tr>
            <tr>
                <th>
                    <button id="b1" onclick="isOnline(1)" class="layui-btn layui-btn-primary layui-btn-small">上线</button>&nbsp;
                    <button id="b0" onclick="isOnline(0)" class="layui-btn layui-btn-primary layui-btn-small">离线</button>&nbsp;
                    <button id="b2" onclick="isOnline(2)" class='layui-btn layui-btn-warm layui-btn-small'>全部</button>
                </th>
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
                    <span onclick="clickPoint({{row.point}},'{{row.address}}','{{row.cname}}','{{row.phone}}')"><a href="#4">{{row.name}}</a></span>
                </td>
            </tr>
        </script>
    </div>
    <%--折叠--%>
    <div id="switchSysBarId" onClick="switchSysBar()" style="height:800px;width: 1%;text-align:center;cursor:pointer;float: left;background: lightgrey;">
        <span id="switchPoint" title="打开/关闭区域及客户" style="color:#666;cursor:hand;font-family:Webdings;font-size:12px;line-height: 800px;">3</span>
    </div>
    <%--地图--%>
    <div id="allmap" style="width: 84%; height: 800px;"></div>
</div>
<div style="display: none;">
    <object id="ctrlLight" classid="clsid:C6B41616-6238-42B6-BB05-F6C87A26B9F4" width="0"
            height="0">
    </object>
</div>
<div id="wav">
</div>
</body>

</html>