document.getElementsByTagName("html")[0].style.fontSize=document.documentElement.clientWidth/15+"px";
//改变窗口的时候重新计算大小
window.onresize = function(){
  document.getElementsByTagName("html")[0].style.fontSize=document.documentElement.clientWidth/15+"px";
  }
$(".enterprise_bg").height($(window).height());
/**
 * 判断是否是空
 * @param value
 */
function isDefine(value) {
    if (value == null || value == "" || value == "undefined" || value == undefined || value == "null" || value == "(null)" || value == 'NULL' || typeof (value) == 'undefined') {
        return false;
    } else {
        value = value + "";
        value = value.replace(/\s/g, "");
        if (value == "") {
            return false;
        }
        return true;
    }
}
//是否可以登录标识
var isLogin = false;
var isCheck="false";//验证码是否可点击
$('#phoneNum').bind('input propertychange', function() {  
    var uerId = $("#userId").val(),
        tel = $(this).val();
    uerId = uerId.replace(/\s+/g, "");
    tel = tel.replace(/\s+/g, "");
    var reg = /^1[3|4|5|7|8]\d{9}$|^0085[23]\d{8}$|^00886\d{8}$/;
    if(reg.test(tel)&& isDefine(userId)){
      $("#idCheckBtn").addClass("active");
        isCheck="true";
    }else{
      $("#idCheckBtn").removeClass("active");
        isCheck="false";
    } 
}); 
$("#idCheckBtn").click(function(){
    if(isCheck=="true"){
      //置灰
      timeCountDown();
      //发送验证码
      sendMessage();
    }
});

//触发验证后事件
var wait=60; 
function timeCountDown() {  
    if (wait == 0) {  
        $("#idCheckBtn").addClass("active");            
        $('#idCheckBtn').html("获取验证码"); 
        isCheck="true";
        wait = 60;  
    } else {  
        $("#idCheckBtn").removeClass("active");
        $('#idCheckBtn').html("重新发送"+wait); 
        isCheck="false";
        wait--;  
        setTimeout(function() {  
            timeCountDown();  
        },  
        1000);  
    }  
}  

function checkInfo(){
   var userId = $("#userId").val();
   var userName = $("#userName").val();
   var idCode = $("#idCode").val();
   var mobile = $("#phoneNum").val();
   var msg_id = $("#msg_id").val();
   var reg = /^1[3|4|5|7|8]\d{9}$|^0085[23]\d{8}$|^00886\d{8}$/;
   var regNum = /^[0-9]+$/;
   msg_id = msg_id.replace(/\s+/g, "");
   mobile = mobile.replace(/\s+/g, "");
   userId = userId.replace(/\s+/g, "");
   userName = userName.replace(/\s+/g, "");
   idCode = idCode.replace(/\s+/g, "");
   if(userId == "" || userId.length == 0||!regNum.test(userId)){
     alert("请填写正确的工号");
     return false;
   }
   if(userName == "" || userName.length == 0){
     alert("请填写姓名");
     return false;
   }
   if(mobile == "" || mobile.length == 0||!reg.test(mobile)){
     alert("请填写正确的手机号");
     return false;
   }
   if(idCode == "" || idCode.length == 0){
     alert("请填写验证码");
     return false;
   }
   if(msg_id == null || msg_id == "" ||  msg_id.length == 0){
	 alert("请先获取验证码");
	 return false;
   }
   return true;
}
function checkInfoNoYZM(){
   var userId = $("#userId").val();
   var userName = $("#userName").val();
   var mobile = $("#phoneNum").val();
   var reg = /^1[3|4|5|7|8]\d{9}$|^0085[23]\d{8}$|^00886\d{8}$/;
   var regNum = /^[0-9]+$/;
   mobile = mobile.replace(/\s+/g, "");
   userId = userId.replace(/\s+/g, "");
   userName = userName.replace(/\s+/g, "");
   if(userId == "" || userId.length == 0||!regNum.test(userId)){
     alert("请填写正确的工号");
     return false;
   }
   if(userName == "" || userName.length == 0){
     alert("请填写姓名");
     return false;
   }
   if(mobile == "" || mobile.length == 0||!reg.test(mobile)){
     alert("请填写正确的手机号");
     return false;
   }
   return true;
}
$("#idCheckBtn").click(function(){
	  checkInfoNoYZM();
})
