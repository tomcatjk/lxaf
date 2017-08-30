<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
    <head>
        <title>客户添加</title>
        <meta name="decorator" content="default"/>
        <script type="text/javascript">
            function lu() {
                var div1=document.getElementById("div1");
                var div2=document.getElementById("div2");
                var div3=document.getElementById("div3");
                if (div2.style.display=="none"){
                    div2.style.display="block";
                    div1.style.display="none";
                    div3.style.display="none";
                }
            }
            function hua() {
                var div1=document.getElementById("div1");
                var div2=document.getElementById("div2");
                var div3=document.getElementById("div3");
                if (div3.style.display=="none"){
                    div3.style.display="block";
                    div1.style.display="none";
                    div2.style.display="none";
                }
            }
            function jie() {
                var div1=document.getElementById("div1");
                var div2=document.getElementById("div2");
                var div3=document.getElementById("div3");
                if (div1.style.display=="none"){
                    div1.style.display="block";
                    div2.style.display="none";
                    div3.style.display="none";
                }
            }
        </script>

        <script>
            $(document).ready(function () {
                console.log("customertype --》 ${customertype}");
                console.log("${cid}");
                console.log("${masterFlag}");
                if(${masterFlag == '1'}){
                    lu();
                }else if(${masterFlag == '2'}){
                    hua();
                }
            })
        </script>
    </head>
    <body class="hideScroll">
        <table>
            <tr>
                <td >
                    <div style="height: 40px">
                        <button  type="button" style="outline: none " class="btn btn-link"  onclick="jie()"><h4 align="center">客户基本信息</h4></button>
                    </div>
                </td>
                <td >
                    <div style="height: 40px">
                        <button  type="button" style="outline: none " class="btn btn-link" onclick="lu()"><h4 align="center">主机管理</h4></button>
                    </div>
                </td>
                <td >
                    <div style="height: 40px">
                        <button type="button" style="outline: none " class="btn btn-link"  onclick="hua()"><h4 align="center">设备管理</h4></button>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    <div class="div1" id="div1" style="display: block" >
                        <iframe name="myframe" width="800px" height="530px" class="ifra" src="${ctx}/lu/customers/formadd?customertype=${customertype}" > </iframe>
                    </div>
                    <div class="div1" id="div2" style="display: none" >
                        <%--<iframe name="myframe1" width="800px" height="350px" class="ifra" src="${ctx}/lu/masters/list?customerid=${cid}"> </iframe>--%>
                        <iframe name="myframe1" width="800px" height="530px" class="ifra" src="${ctx}/lu/masters/list?customerid=${cid}"> </iframe>
                    </div>
                    <div class="div1" id="div3" style="display: none" >
                        <%--<iframe name="myframe2" width="800px" height="350px" class="ifra"src="${ctx}/lu/devices/list?customerid=${cid}" > </iframe>--%>
                        <iframe name="myframe2" width="800px" height="530px" class="ifra"src="${ctx}/lu/devices/list?customerid=${cid}" > </iframe>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>
