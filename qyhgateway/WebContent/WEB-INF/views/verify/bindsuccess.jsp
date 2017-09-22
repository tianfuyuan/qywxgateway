<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
  <meta name="format-detection" content="telephone=no">
  <meta name="format-detection" content="telephone=no,email=no">
  <title class="reportTitle">登录</title>
  <link href="${ctx }/static/verify/css/reset.css" rel="stylesheet" type="text/css">
  <link href="${ctx }/static/verify/css/bindsuccess.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="${ctx }/static/verify/js/jquery-1.9.1.min.js"></script>
</head>
<body>
  <div class="wrap">
  	 <img src="${ctx }/static/verify/img/img_bind_success.png" class="img_png">
     <h2>您已成功绑定企业号！</h2>
     <input type="button" value="完 成" class="btn_default small_btn" onclick="closejsp()">
  </div>
</body>
<script type="text/javascript" src="${ctx }/static/verify/js/common.js?v=1.0.0"></script>
<script type="text/javascript">
function closejsp() {
	WeixinJSBridge.call('closeWindow');
}
</script>
</html>
