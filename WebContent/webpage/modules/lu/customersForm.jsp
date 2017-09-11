<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <title>记录客户信息管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=F454f8a5efe5e577997931cc01de3974"></script>
    <script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=F454f8a5efe5e577997931cc01de3974"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jedate/jedate.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/jqueryesayform/css/platform-1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/jqueryesayform/js/easyform/easyform.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/jqueryesayform/css/tab.css">
    <script src="${pageContext.request.contextPath}/static/jqueryesayform/js/easyform/easyform.js"></script>
    <style>
        .onError{
            color: red;
        }
    </style>

</head>
<body class="hideScroll">
    <form action="${ctx}/lu/customers/save" id="inputForm" method="post" class="form-horizontal">
        <input type="hidden" id="cid" name="cid" value="${customers.cid}" >
        <sys:message content="${message}"/>
        <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
            <tbody>
            <tr>
                <td class="width-15 active"><label class="pull-right"><font color="red">*</font>客户名称：</label></td>
                <td class="width-35">
                    <input type="text" name="name" id="name" value="${customers.name}" class="form-control requiredClass"/>
                </td>
                <td class="width-15 active"><label class="pull-right">区域：</label></td>
                <td class="width-35">
                    <sys:treeselect id="areaid" name="areaid" value="${areas.parent.id}" labelName="parent.name" labelValue="${customers.areaName}"
                                    title="选择区域" url="/lu/areas/treeData" extId="${areas.id}" cssClass="form-control " allowClear="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-15 active"><label class="pull-right"><font color="red">*</font>客户类别：</label></td>
                <td class="width-35">
                    <select id="customertypeId" name="customertype" class="requiredClass" style="width: 250px; height: 30px;">
                        <c:choose>
                            <c:when test="${customers.customertype == 1}">
                                <option value="1" selected = 'selected'>企业客户</option>
                            </c:when>
                            <c:otherwise>
                                <option value="1">企业客户</option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${customers.customertype == 2}">
                                <option value="2" selected = 'selected'>个体客户</option>
                            </c:when>
                            <c:otherwise>
                                <option value="2">个体客户</option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${customers.customertype == 3}">
                                <option value="3" selected = 'selected'>政府客户</option>
                            </c:when>
                            <c:otherwise>
                                <option value="3">政府客户</option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </td>

                <td class="width-15 active"><label class="pull-right"><font color="red">*</font>地址：</label></td>
                <td class="width-35">
                    <input type="text"  name="address" id="address" value="${customers.address}" onBlur="searchByStationName();" class="form-control"/>
                </td>
            </tr>
            <tr>
                <td class="width-15 active"><label class="pull-right">坐标：</label></td>
                <td class="width-35">
                    <input type="text" name="point" id="point" value="${customers.point}" class="form-control "
                           data-easyform="null;"/>
                </td>
                <td class="width-15 active"><label class="pull-right">质检人：</label></td>
                <td class="width-35">
                    <input  type="text" name="qualityperson" id="qualityperson" value="${customers.qualityperson}"  class="form-control"/>
                </td>
            </tr>
            <tr>
                <td class="width-15 active"><label class="pull-right">联系人：</label></td>
                <td class="width-35">
                    <input type="text" name="contacts"  id="contacts" value="${customers.contacts}" class="form-control"/>
                </td>
                <td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系电话：</label></td>
                <td class="width-35">
                    <input type="text" name="phone" id="phone" value="${customers.phone}" class="form-control requiredClass"></td>
                </td>
            </tr>
            <tr>
                <td class="width-15 active"><label class="pull-right">安装人：</label></td>
                <td class="width-35">
                    <input type="text" name="installperson"  id="installperson" value="${customers.installperson}" class="form-control"/>
                </td>
                <td class="width-15 active"><label class="pull-right">填表人：</label></td>
                <td class="width-35">
                    <input type="text"  name="preparer" id="preparer" value="${customers.preparer}" class="form-control "/>
                </td>
            </tr>
            <tr >
                <td class="width-15 active" ><label class="pull-right">备注：</label></td>
                <td class="width-35" colspan="6" >
                        <textarea rows="3" name="remark" id="remark" value="${customers.remark}" class="form-control "></textarea>
                </td>
            </tr>
            </tbody>
        </table>
        <div id="allmap"
             style="position: absolute;
                        width: 0px;
                        height: 0px;
                        top: 0;
                        overflow:hidden;
                        display:none;">
        </div>
    </form>

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

    <script>
        var saveCustomer = function () {
            var isTrue = true;
            var url = "${ctx}/lu/customers/save";
            var obj = $("#inputForm").serialize();
            $.ajaxSetup({
                async : false
            });

            $("#inputForm :input.requiredClass").trigger('blur');
            var numError = $('#inputForm .onError').length;

            if(!numError) {
                $.ajax({
                    type: "post",
                    url: url,
                    data: obj,
                    success: function (data) {
                        if (data == 'ok') {

                        } else {
                            isTrue = false;
                        }
                    },
                    error: function () {
                        isTrue = false;
                    }
                });
                isTrue = true
            }else {
                isTrue = false;
            }
            return isTrue;
        }
    </script>

    <%--表单验证--%>
    <script>
        $(function () {
            //如果是必填的，则加红星标识.
    //        $("#inputForm :input.requiredClass").each(function(){
    //            var $required = $("<strong class='high'> *</strong>"); //创建元素
    //            $(this).parent().append($required); //然后将它追加到文档中
    //        });
            //文本框失去焦点后
            $('#inputForm :input').blur(function(){
                var $parent = $(this).parent();
                $parent.find(".formtips").remove();
                //验证用户名
                if( $(this).is('#name') ){
                    if( this.value==""){
                        var errorMsg = '请输入用户名.';
                        $parent.append('<span class="formtips onError">'+errorMsg+'</span>');
                    }
                }

                //验证客户类别
                if( $(this).is('#customertypeId') ){
                    if( this.value==""){
                        var errorMsg = '请选择客户类别.';
                        $parent.append('<span class="formtips onError">'+errorMsg+'</span>');
                    }
                }

                //验证地址
                if( $(this).is('#address') ){
                    if( this.value==""){
                        var errorMsg = '请输入地址.';
                        $parent.append('<span class="formtips onError">'+errorMsg+'</span>');
                    }
                }

                //验证联系电话
                if( $(this).is('#phone') ){
                    if( this.value=="" || ( this.value!="" && !/^1[34578]\d{9}$/.test(this.value) ) ){
                        var errorMsg = '请输入11位联系电话.';
                        $parent.append('<span class="formtips onError">'+errorMsg+'</span>');
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