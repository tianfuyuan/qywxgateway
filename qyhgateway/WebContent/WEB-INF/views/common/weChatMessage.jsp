<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
<title>企业号</title>
<link href="${ctx}/static/common/css/reset.css" rel="stylesheet" type="text/css">
<link href="${ctx}/static/common/css/style.css" rel="stylesheet" type="text/css">
</head>

<body style="background: #fffdfa">
	<div class="wrap_index insure_success">
		<header>
			<div class="header_bg clearfix">
				<p class="fl clearfix">
					<span class="icon_tab_pen"></span> <span class="h2_word fl">企业号</span>
				</p>
			</div>
		</header>
		<div class="insure_success_con">
			<h2>${message }</h2>
			<p>如有任何疑问，欢迎您咨询</p>
			<p>客服热线：95300</p>
		</div>
		<a href="javascript:WeixinJSBridge.call('closeWindow');" class="default_btn">关&nbsp;&nbsp;闭</a>
	</div>
</body>
</html>