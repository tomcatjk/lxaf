<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>联讯安防智能预警监控管理系统</title>

	<%@ include file="/webpage/include/head.jsp"%>
	<script src="${ctxStatic}/common/inspinia.js?v=3.2.0"></script>
	<script src="${ctxStatic}/common/contabs.js"></script>
    <meta name="keywords" content="JeePlus快速开发平台">
    <meta name="description" content="JeePlus，采用spring mvc+mybatis+shiro+bootstrap，集成代码生成器的快速开发平台">
    <script type="text/javascript">
	$(document).ready(function() {
		 if('${fns:getDictLabel(cookie.theme.value,'theme','默认主题')}' == '天蓝主题'){
			    // 蓝色主题
			        $("body").removeClass("skin-2");
			        $("body").removeClass("skin-3");
			        $("body").addClass("skin-1");
		 }else  if('${fns:getDictLabel(cookie.theme.value,'theme','默认主题')}' == '橙色主题'){
			    // 黄色主题
			        $("body").removeClass("skin-1");
			        $("body").removeClass("skin-2");
			        $("body").addClass("skin-3");
		 }else {
			 // 默认主题
			        $("body").removeClass("skin-2");
			        $("body").removeClass("skin-3");
			        $("body").removeClass("skin-1");
		 };
	 });

	</script>

</head>

<body class="fixed-sidebar full-height-layout gray-bg">
    <div id="wrapper">
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation" style="height: 500px;">
            <div class="nav-close"><i class="fa fa-times-circle"></i>
            </div>
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <div class="dropdown profile-element">
                            <span><img alt="image" class="img-circle" style="height:64px;width:64px;" src="${fns:getUser().photo }" /></span>
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                               <span class="block m-t-xs"><strong class="font-bold">${fns:getUser().name}</strong></span>
                               <span class="text-muted text-xs block">${fns:getUser().roleNames}<b class="caret"></b></span>
                                </span>
                            </a>
                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
                                <li><a class="J_menuItem" href="${ctx}/sys/user/imageEdit">修改头像</a>
                                </li>
                                <li><a class="J_menuItem" href="${ctx }/sys/user/info">个人资料</a>
                                </li>
                                <li class="divider"></li>
                                <li><a href="${ctx}/logout">安全退出</a>
                                </li>
                            </ul>

                        </div>
                        <div class="logo-element">JP
                        </div>
                    </li>

                    <t:menu  menu="${fns:getTopMenu()}"></t:menu>

                </ul>
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row border-bottom">
                <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                    <div class="navbar-header">
                        <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
                        <h1 style="margin: 14px 5px 5px 30px;font-size: 30px;">联讯安防智能预警监控管理系统</h1>

                    </div>
                </nav>
            </div>
            <div class="row content-tabs">
                <nav class="page-tabs J_menuTabs" style="margin-left: 0;">
                    <div class="page-tabs-content">
                        <a href="javascript:;" class="active J_menuTab" data-id="${ctx}/home">首页</a>
                    </div>
                </nav>
                <div class="btn-group roll-nav roll-right">
                    <button class="dropdown J_tabClose"  data-toggle="dropdown" style="margin-left: 0;">关闭操作<span class="caret"></span>

                    </button>
                    <ul role="menu" class="dropdown-menu dropdown-menu-right">
                        <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                        </li>
                        <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                        </li>
                    </ul>
                </div>
                <a href="${ctx}/logout" class="roll-nav roll-right J_tabExit"><i class="fa fa fa-sign-out"></i> 退出</a>
            </div>
            <div class="row J_mainContent" id="content-main">
                <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="${ctx}/home" frameborder="0" data-id="${ctx}/home" seamless></iframe>
            </div>
        </div>
        <!--右侧部分结束-->


    </div>
</body>

<!-- 语言切换插件，为国际化功能预留插件 -->
<%--<script type="text/javascript">--%>

<%--$(document).ready(function(){--%>

	<%--$("a.lang-select").click(function(){--%>
		<%--$(".lang-selected").find(".lang-flag").attr("src",$(this).find(".lang-flag").attr("src"));--%>
		<%--$(".lang-selected").find(".lang-flag").attr("alt",$(this).find(".lang-flag").attr("alt"));--%>
		<%--$(".lang-selected").find(".lang-id").text($(this).find(".lang-id").text());--%>
		<%--$(".lang-selected").find(".lang-name").text($(this).find(".lang-name").text());--%>

	<%--});--%>


<%--});--%>

<%--function changeStyle(){--%>
   <%--$.get('${pageContext.request.contextPath}/theme/ace?url='+window.top.location.href,function(result){   window.location.reload();});--%>
<%--}--%>

<%--</script>--%>



<!-- 即时聊天插件  开始-->
<%--<link href="${ctxStatic}/layer-v2.3/layim/layui/css/layui.css" type="text/css" rel="stylesheet"/>--%>
<%--<script type="text/javascript">--%>
<%--var currentId = '${fns:getUser().loginName}';--%>
<%--var currentName = '${fns:getUser().name}';--%>
<%--var currentFace ='${fns:getUser().photo}';--%>
<%--var url="${ctx}";--%>
<%--var static_url="${ctxStatic}";--%>
<%--var wsServer = 'ws://'+window.document.domain+':8668'; --%>

<%--</script>--%>
<%--<!--webscoket接口  -->--%>
<%--<script src="${ctxStatic}/layer-v2.3/layim/layui/layui.js"></script>--%>

<%--<script src="${ctxStatic}/layer-v2.3/layim/layim.js"></script>--%>
<!-- 即时聊天插件 结束 -->
<style>
/*签名样式*/
/*.layim-sign-box{*/
	/*width:95%*/
/*}*/
/*.layim-sign-hide{*/
  /*border:none;background-color:#F5F5F5;*/
/*}*/
</style>

</html>