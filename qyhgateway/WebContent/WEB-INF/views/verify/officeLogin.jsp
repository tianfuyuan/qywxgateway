<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta name="viewport"content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta charset="UTF-8">
    <meta name="format-detection" content="telephone=no"/>
    <title>企业号</title>
    <link rel="stylesheet" type="text/css" href="${ctx }/static/verify/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx }/static/verify/css/enterprise.css"/>
</head>
<body class="enterprise_bg">
	<img src="${ctx }/static/verify/img/img_enterprise_logo.png" alt="" class="enterprise_logo">
	<section class="enterprise_tips"></section>
	<form action="">
		<ul class="enterprise_main">
            <li><i></i><p><input type="text" placeholder="OA用户名" name="userName" id="userId"></p></li>
			<li><i></i><p><input type="text" placeholder="姓名" name="userName" id="userName"></p></li>
			<li><i></i><p><input type="tel" placeholder="手机号" name="phoneNum" id="phoneNum"></p></li>
			<li class="code"><i></i><p><input type="text" placeholder="验证码" name="idCode" id="idCode"></p><a href="javascript:;" class="send" id="idCheckBtn">获取验证码</a></li>
			<li style="border:none"><input type="hidden" id="msg_id" name="msg_id"></li>
		</ul>
	</form>
	<input type="submit" value="绑 定" onclick="validateLogin()" class="enterprise_btn" id="bind">
</body>
<script type="text/javascript">
//发送验证码
function sendMessage() {
    var mobile = $("#phoneNum").val();
    mobile = mobile.replace(/\s+/g, "");
    var uerId = $("#userId").val();
    uerId = uerId.replace(/\s+/g, "");
    id = 'error';
    if(!checkInfoNoYZM()){
      return;
    }
    $.ajax({
        type : 'post',
        dataType : 'json',
        url : "${ctx}/api/v1/sms/code/send?mobile="+mobile+"&userid="+uerId,
        success : function(data) {
            if(data.result_code == 'suc'){
              alert("发送验证码成功！");
              $("#msg_id").val(data.result_msg_id);
              isLogin = true;
            }else{
            	alert(data.result_msg);
            }
        }
    });
}
//校验登陆信息
function validateLogin(){
  if(!checkInfo()){
      return;
    }
    var reg = /^[0-9A-z]+$/;
    var userId = $("#userId").val();
    var userName = $("#userName").val();
    var idCode = $("#idCode").val();
    var mobile = $("#phoneNum").val();
    var msg_id = $("#msg_id").val();
    msg_id = msg_id.replace(/\s+/g, "");
    mobile = mobile.replace(/\s+/g, "");
    userId = userId.replace(/\s+/g, "");
    userName = userName.replace(/\s+/g, "");
    idCode = idCode.replace(/\s+/g, "");
    if(reg.test(userId)){
        $.ajax({
            url:"${ctx}/api/v1/message/verify?userId="+userId+"&userName="+userName+"&mobile="+mobile+"&idCode="+idCode+"&msg_id="+msg_id,
            type:"POST",
            dataType:"json",
            success:function(data){
                if(data.result_code == 'suc'){
                    $("#bind").attr("style", "background-color:#b7b7b7;");
                    $("#bind").removeAttr("onclick");
                    window.location.href="${ctx}/api/v1/wechat/openid?userid=" + userId;
                  }else{
                  	alert(data.result_msg);
                  }
            }   
        });
      }else{
          alert("您填写的OA账号不符合规范！");
      }
}
</script>
<script type="text/javascript" src="${ctx }/static/verify/js/jquery-1.9.1.min.js" ></script>
<script type="text/javascript" src="${ctx }/static/verify/js/enterprise.js"></script>
</html>