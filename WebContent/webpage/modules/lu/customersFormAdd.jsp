<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>记录客户信息管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=F454f8a5efe5e577997931cc01de3974"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jedate/jedate.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/jqueryesayform/css/platform-1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/jqueryesayform/js/easyform/easyform.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/jqueryesayform/css/tab.css">
    <script src="${pageContext.request.contextPath}/static/jqueryesayform/js/easyform/easyform.js"></script>
    <script src="${ctxStatic}/lianxun/layui/src/layui.js" charset="utf-8"></script>
    <link rel="stylesheet" href="${ctxStatic}/lianxun/layui/src/css/layui.css" media="all">
    <style>
        .formtips{
            color: red;
        }
    </style>
</head>
<body class="hideScroll">
    <ul class="layui-tab-title">
        <div class="layui-btn-group">
            <button id="customersFormAddLi" class="layui-btn layui-btn-normal">添加客户</button>
            <button id="mastersFormLi" class="layui-btn layui-btn-primary">添加主机</button>
            <button id="devicesFormLi" class="layui-btn layui-btn-primary">添加设备</button>
        </div>
    </ul>
    <div class="layui-tab-content" >
        <div id="customersFormAdd">
            <form action="" id="reg-form" method="post" class="customersFormAddFormClass">
                <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
                    <tbody>
                        <tr>
                            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>客户名称：</label></td>
                            <td class="width-35">
                                <input type="text" name="name" id="name" class="form-control requiredClass"/>
                            </td>
                            <td class="width-15 active"><label class="pull-right">区域：</label></td>
                            <td class="width-35">
                                <sys:treeselect id="areaid" name="areaid" value="${areas.parent.id}" labelName="parent.name" labelValue="${areas.parent.name}"
                                                title="选择区域" url="/lu/areas/treeData" extId="${areas.id}" cssClass="form-control " allowClear="true"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>客户类别：</label></td>
                            <td class="width-35">
                                <input type="hidden" name = "customertype" id="customerTypeId" value = "${customertype}">
                                <input type="text" name="customerTypeStr" id="customerTypeStr" value="${customertype == 1 ? "企业客户" : (customertype == 2 ? "个体客户" : "政府客户")}"  readonly  class="form-control "
                                       data-easyform="null;"/>
                            </td>
                            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>地址：</label></td>
                            <td class="width-35">
                                <input type="text"  name="address" id="address"  onBlur="searchByStationName();"  class="form-control requiredClass"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15 active"><label class="pull-right">坐标：</label></td>
                            <td class="width-35">
                                <input type="text" name="point" id="point" class="form-control "/>
                            </td>
                            <td class="width-15 active"><label class="pull-right">质检人：</label></td>
                            <td class="width-35">
                                <input  type="text" name="qualityperson" id="qualityperson"  class="form-control"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15 active"><label class="pull-right">联系人：</label></td>
                            <td class="width-35">
                                <input type="text" name="contacts"  id="contacts"   class="form-control"/>
                            </td>
                            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系电话：</label></td>
                            <td class="width-35">
                                <input type="text" name="phone" id="phone" class="form-control requiredClass"></td>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15 active"><label class="pull-right">安装人：</label></td>
                            <td class="width-35">
                                <input type="text" name="installperson"  id="installperson" class="form-control"/>
                            </td>
                            <td class="width-15 active"><label class="pull-right">填表人：</label></td>
                            <td class="width-35">
                                <input type="text"  name="preparer" id="preparer" class="form-control "/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15 active"><label class="pull-right">安装时间：</label></td>
                            <td class="width-35">
                                <input type="datetime" name="installtime" id="installtime" placeholder="请选择"  readonly  class="form-control" />
                            </td>
                            <td class="width-15 active"><label class="pull-right">到期时间：</label></td>
                            <td class="width-35">
                                <input type="text" name="duetime" id="duetime"  placeholder="请选择"  readonly  class="form-control"/>
                            </td>
                        </tr>
                        <tr style="display: none">
                            <td class="width-15 active"><label class="pull-right">创建时间：</label></td>
                            <td class="width-35">
                                <input type="text"  name="createtime" id="createtime" placeholder="请选择"  readonly  class="form-control"/>
                            </td>
                            <td class="width-15 active"></td>
                            <td class="width-35" ></td>
                        </tr>
                        <tr >
                            <td class="width-15 active" ><label class="pull-right">备注：</label></td>
                            <td class="width-35" colspan="6" >
                                    <textarea rows="3" name="remark" id="remark" class="form-control "
                                              data-easyform="null;"></textarea>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div id="allmap" style="display: none;"></div>
            </form>
        </div>
        <div id="mastersForm" style="display: none;">
            <form id="reg-form" modelAttribute="masters" action="" method="post" class="mastersFormClass">
                <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
                    <tbody>
                        <tr>
                            <input type="hidden" name="masterFlag" value="${masterFlag}">
                            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>编号：</label></td>
                            <td class="width-35">
                                <input type="text" name="code" id="code" class="form-control requiredClass" value="${masters.code}"/>
                            </td>
                            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>主机名称：</label></td>
                            <td class="width-35">
                                <input type="text" name="name" id="name" class="form-control requiredClass" value="${masters.name}"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>SIM卡号：</label></td>
                            <td class="width-35">
                                <input type="text" name="sim" id="sim" class="form-control requiredClass" value="${masters.sim}"></td>
                            </td>
                            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
                            <td class="width-35">
                                <select id="masterStateSelectId" class="form-control requiredClass" name="state" id="state">
                                    <c:forEach items="${mastersStateNameMap}" var="mastersStateName">
                                        <option value="${mastersStateName.key}">${mastersStateName.value}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15 active"><label class="pull-right">主机型号：</label></td>
                            <td class="width-35">
                                <input type="text"  name="maintype" id="maintype"   class="form-control" value="${masters.maintype}"/>
                            </td>
                            <td class="width-15 active"><label class="pull-right">版本：</label></td>
                            <td class="width-35">
                                <input type="text"  name="version" id="version"   class="form-control" value="${masters.version}"/>
                            </td>
                        </tr>
                        <tr style="display: none">
                            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>创建时间：</label></td>
                            <td class="width-35">
                                <input type="text"  name="createtime" id="createtime" placeholder="请选择"  readonly  class="form-control required"/>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
        <div id="devicesForm" style="display: none;">
            <form id="inputForm" modelAttribute="devices" action="" method="post" class="devicesFormClass">
                <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
                    <tbody>
                        <tr>
                            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>设备名称：</label></td>
                            <td class="width-35">
                                <input type="text" id="name" name="name" class="requiredClass" style='width: 250px; height: 30px;'>
                            </td>
                            <td class="width-15 active"><label class="pull-right">设备类型：</label></td>
                            <td class="width-35">
                                <select id="deviceTypeSelectId" name="devicetype" style="width: 250px; height: 30px;">
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15 active"><label class="pull-right">所属主机：</label></td>
                            <td class="width-35">
                                <select name="masterid" id="masterSelect" style='width: 250px; height: 30px;'></select>
                            </td>
                            <td></td>
                                <%--<td><input type="hidden" name="customerid" value="${devices.customerid}"></td>--%>
                        </tr>
                        <tr>
                            <td class="width-15 active"><label class="pull-right">防区编号：</label></td>
                            <td class="width-35">
                                <select name="defenceid" id="defenceSelect" style='width: 250px; height: 30px;'></select>
                            </td>
                            <td class="width-15 active"><label class="pull-right">防区名称：</label></td>
                            <td class="width-35">
                                <div id="defencesName"></div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>

    <%--地址转化为经纬度--%>
    <script type="text/javascript">
        var map = new BMap.Map("allmap");
        map.centerAndZoom("北京", 11);

        map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
        map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用

        map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
        map.addControl(new BMap.OverviewMapControl()); //添加默认缩略地图控件
        map.addControl(new BMap.OverviewMapControl({ isOpen: true, anchor: BMAP_ANCHOR_BOTTOM_RIGHT }));   //右下角，打开

        var localSearch = new BMap.LocalSearch(map);
        localSearch.enableAutoViewport(); //允许自动调节窗体大小

        function searchByStationName() {
            var keyword = document.getElementById("address").value;
            localSearch.setSearchCompleteCallback(function (searchResult) {
                var poi = searchResult.getPoi(0);
                document.getElementById("point").value = poi.point.lng + "," + poi.point.lat; //获取经度和纬度，将结果显示在文本框中
                map.centerAndZoom(poi.point, 13);
            });
            localSearch.search(keyword);
        }
    </script>

    <%--初始化时间插件--%>
    <script type="text/javascript">
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
            /*创建时间*/
            jeDate({
                dateCell:"#createtime",
                format:"YYYY-MM-DD hh:mm:ss",
                isinitVal:true, /*初始值*/
                isTime:true, //isClear:false,
                minDate:"2014-09-19 00:00:00", //最小时间
                maxDate:nowDay,                  //最大时间
                okfun:function(val){}
            })

            /*安装时间*/
            jeDate({
                dateCell:"#installtime",
                format:"YYYY-MM-DD hh:mm:ss",
                isinitVal:true, /*初始值*/
                isTime:true, //isClear:false,
                minDate:"2014-09-19 00:00:00", //最小时间
                maxDate:nowDay,                  //最大时间
                okfun:function(val){}
            })

            /*到期时间*/
            jeDate({
                dateCell:"#duetime",
                format:"YYYY-MM-DD hh:mm:ss",
                isinitVal:true, /*初始值*/
                isTime:true, //isClear:false,
                minDate:"2014-09-19 00:00:00", //最小时
                okfun:function(val){}
            })

        });
    </script>

    <%--devicesForm--%>
    <script>
        var findMastersListByCid = function () {
            var url = "${ctx}/lu/masters/findMastersListByCid?cid=" + cid;
            $.post(url,function (mastersList) {
                var options = "";
                $.each(mastersList,function (index,masters) {
                    options += "<option value="+masters.mid+">"+masters.name+"</option>"
                })
                $("#masterSelect").html(options);
                var mid = $("#masterSelect").val();
                var url = "${ctx}/lu/defences/findDefencesListByMasterId?masterId=" + mid;
                $.post(url,function (defencesListJson) {
                    var options = "";
                    $.each(defencesListJson,function (index,defences) {
                        options += "<option value="+defences.did+">"+defences.code+"</option>"
                    })
                    $("#defenceSelect").html(options);
                    var did = $("#defenceSelect").val();
                    var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
                    $.post(url,function (defences) {
                        var input = "<input type='text' name='defencesName' value='"+defences.name+"' class='form-control'>";
                        $("#defencesName").html(input);
                    })
                })
            })
        }

        $(document).ready(function(){
            $("#masterSelect").change(function(){
                var mid = $(this).val();
                var url = "${ctx}/lu/defences/findDefencesListByMasterId?masterId=" + mid;
                $.post(url,function (defencesListJson) {
                    var options = "";
                    $.each(defencesListJson,function (index,defences) {
                        options += "<option value="+defences.did+">"+defences.code+"</option>"
                    })
                    $("#defenceSelect").html(options);
                    var did = $("#defenceSelect").val();
                    var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
                    $.post(url,function (defences) {
                        var input = "<input type='text' name='defencesName' value='"+defences.name+"' class='form-control'>";
                        $("#defencesName").html(input);
                    })
                })
            });
        });

        $(document).ready(function(){
            $("#defenceSelect").change(function(){
                var did = $(this).val();
                var url = "${ctx}/lu/defences/findDefencesbyDid?did=" + did;
                $.post(url,function (defences) {
                    var input = "<input type='text' name='defencesName' value='"+defences.name+"' class='form-control'>";
                    $("#defencesName").html(input);
                })
            });
        });

    </script>

    <%--弹框选项卡--%>
    <script>
        var cid;
        var showTabId = 1;
        var showTab = function () {
            if(showTabId == 1){
                var url = "${ctx}/lu/customers/save";
                var obj = $(".customersFormAddFormClass").serialize();
                $.ajaxSetup({
                    async : false
                });

                $(".customersFormAddFormClass :input.requiredClass").trigger('blur');
                var numError = $('.customersFormAddFormClass .onErrorCustomers').length;

                if(!numError){
                    $.ajax({
                        type : "post",
                        url : url,
                        data : obj,
                        success : function (data) {
                            showTabId = 2;
                            $.post("${ctx}/lu/masters/mastersStateNameMap", function (mastersStateNameMaps) {
                                var selectHtml = "";
                                $.each(mastersStateNameMaps, function (index, mastersStateNameMap) {
                                    selectHtml += "<option value=" + mastersStateNameMap.state+ ">" + mastersStateNameMap.name + "</option>";
                                })
                                $("#masterStateSelectId").html(selectHtml);
                            });
                            cid = data;
                            $("#customersFormAdd").hide();
                            $("#mastersForm").show();
                            $("#devicesForm").hide();

                            $("#customersFormAddLi").attr("class", "layui-btn layui-btn-primary");
                            $("#mastersFormLi").attr("class", "layui-btn layui-btn-normal");
                            $("#devicesFormLi").attr("class", "layui-btn layui-btn-primary");
                        },
                        error : function () {

                        }
                    });
                }
            }else if(showTabId == 2){
                var url = "${ctx}/lu/masters/save";
                var obj = $(".mastersFormClass").serialize();
                obj = obj + "&customerid=" + cid;
                $.ajaxSetup({
                    async : false
                });

                $(".mastersFormClass :input.requiredClass").trigger('blur');
                var numError = $('.mastersFormClass .onErrorMasters').length;

                if(!numError){
                    $.ajax({
                        type : "post",
                        url : url,
                        data : obj,
                        success : function (data) {
                            showTabId = 3;
                            $.post("${ctx}/lu/devices/deviceTypeNameMap", function (deviceTypeNameMaps) {
                                var selectHtml = "";
                                $.each(deviceTypeNameMaps, function (index, deviceTypeNameMap) {
                                    selectHtml += "<option value=" + deviceTypeNameMap.type+ ">" + deviceTypeNameMap.name + "</option>";
                                });
                                $("#deviceTypeSelectId").html(selectHtml);
                            });
                            findMastersListByCid();
                            cid = data;
                            $("#customersFormAdd").hide();
                            $("#mastersForm").hide();
                            $("#devicesForm").show();

                            $("#customersFormAddLi").attr("class", "layui-btn layui-btn-primary");
                            $("#mastersFormLi").attr("class", "layui-btn layui-btn-primary");
                            $("#devicesFormLi").attr("class", "layui-btn layui-btn-normal");
                        },
                        error : function () {

                        }
                    });
                }
            }else{
                var url = "${ctx}/lu/devices/save";
                var obj = $(".devicesFormClass").serialize();
                obj += "&customerid=" + cid;
                $.ajaxSetup({
                    async : false
                });

                $(".devicesFormClass :input.requiredClass").trigger('blur');
                var numError = $('.devicesFormClass .onErrorDevices').length;

                if(!numError) {
                    $.ajax({
                        type: "post",
                        url: url,
                        data: obj,
                        success: function (data) {
                            showTabId = 0;
                        },
                        error: function () {

                        }
                    });
                }
            }
        }
    </script>

    <%--cutomersForm表单验证--%>
    <script>
        $(function () {
            //文本框失去焦点后
            $('.customersFormAddFormClass :input').blur(function(){
                var $parent = $(this).parent();
                $parent.find(".formtips").remove();
                //验证用户名
                if( $(this).is('#name') ){
                    if( this.value==""){
                        var errorMsg = '请输入用户名.';
                        $parent.append('<span class="formtips onErrorCustomers">'+errorMsg+'</span>');
                    }
                }

                //验证客户类别
                if( $(this).is('#customertypeId') ){
                    if( this.value==""){
                        var errorMsg = '请选择客户类别.';
                        $parent.append('<span class="formtips onErrorCustomers">'+errorMsg+'</span>');
                    }
                }

                //验证地址
                if( $(this).is('#address') ){
                    if( this.value==""){
                        var errorMsg = '请填写地址.';
                        $parent.append('<span class="formtips onErrorCustomers">'+errorMsg+'</span>');
                    }
                }

                //验证联系电话
                if( $(this).is('#phone') ){
                    if( this.value=="" || ( this.value!="" && !/^1[34578]\d{9}$/.test(this.value) ) ){
                        var errorMsg = '请输入11位联系电话.';
                        $parent.append('<span class="formtips onErrorCustomers">'+errorMsg+'</span>');
                    }
                }

            }).keyup(function(){
                $(this).triggerHandler("blur");
            }).focus(function(){
                $(this).triggerHandler("blur");
            });//end blur
        })
    </script>

    <%--masterForm表单验证--%>
    <script>
        $(function () {
            //文本框失去焦点后
            $('.mastersFormClass :input').blur(function(){
                var $parent = $(this).parent();
                $parent.find(".formtips").remove();
                //验证编号
                if( $(this).is('#code') ){
                    if( this.value==""){
                        var errorMsg = '请输入编号.';
                        $parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
                    }else{
                        $.get("${ctx}/lu/masters/masterExist?code=" + this.value, function (data) {
                            if(data == "exist"){
                                var errorMsg = '主机编号已存在.';
                                $parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
                            }
                        });
                    }
                }

                //验证主机名称
                if( $(this).is('#name') ){
                    if( this.value=="" ){
                        var errorMsg = '请输入主机名称.';
                        $parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
                    }
                }

                //验证sim卡号
                if( $(this).is('#sim') ){
                    if( this.value==""){
                        var errorMsg = '请输入sim卡号.';
                        $parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
                    }
                }

                //验证主机状态
                if( $(this).is('#masterStateSelectId') ){
                    if( this.value==""){
                        var errorMsg = '请选择主机状态.';
                        $parent.append('<span class="formtips onErrorMasters">'+errorMsg+'</span>');
                    }
                }

            }).keyup(function(){
                $(this).triggerHandler("blur");
            }).focus(function(){
                $(this).triggerHandler("blur");
            });//end blur
        })
    </script>

    <%--devicesForm表单验证--%>
    <script>
        $(function () {
            //文本框失去焦点后
            $('.devicesFormClass :input').blur(function(){
                var $parent = $(this).parent();
                $parent.find(".formtips").remove();
                //验证设备名称
                if( $(this).is('#name') ){
                    if( this.value==""){
                        var errorMsg = '请输入设备名称.';
                        $parent.append('<span class="formtips onErrorDevices">'+errorMsg+'</span>');
                    }
                }

            }).keyup(function(){
                $(this).triggerHandler("blur");
            }).focus(function(){
                $(this).triggerHandler("blur");
            });//end blur
        })
    </script>
</body>
</html>