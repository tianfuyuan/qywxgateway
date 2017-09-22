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
//input样式
$('input').bind('input propertychange', function() {
    if (isDefine($(this).val())) {
        $(this).css("border-color", "#3887e1");
    } else {
        $(this).css("border-color", "#CCCCCC");
    }
});
/**
 *
 * 功能：查询页面头部处于不变位置
 * 
 **/ 
 function setListPos(Id){
     var oHeight = $(window).height() - $("#"+Id).offset().top-13;
     $("#"+Id).css("height", oHeight);
 }
 /**
  * 
  *  获取URL中的参数
  *
  **/
function getQueryString(name) {
	//alert(window.location.searche.substr(1));
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}
/**
 * 
 * 功能：时间格式转换 
 * 
 * 转换前格式： YYYY/MM/DD
 * 转换后格式： YYYY-MM-DD
 * 
 **/
function changeDateFormF(date) {
    //var regEx = new RegExp("\\/","gi"); 
   // date = date.replace("/[\/]/","-");
    date = date.replace(/\//g,"-"); 	
    date = date.substring(0, 10);
    return date;
}
/**
 * 
 * 功能：时间格式转换 
 * 
 * 转换前格式：YYYY-MM-DD
 * 转换后格式：  YYYY/MM/DD
 * 
 **/
function changeDateFormB(date) {
//    var regEx = new RegExp("\\-","gi"); 
//    date = date.replace(regEx,"/"); 
    date = date.replace(/-/g,"/"); 	
    return date;
}
/**
 * 
 * 功能：loading效果
 * 参数：父节点Id
 * 
 **/
function loading(Id){
    var str = '<div class="spinner"><div class="rect1"></div><div class="rect2"></div><div class="rect3"></div><div class="rect4"></div><div class="rect5"></div></div>';
    $("#"+Id).html(str);
    return false;
}
/**
 * 
 * 功能：alert
 * 参数: 提示信息
 * 
 **/
function alertMsg(msg){
	var str = '<div class="weui_dialog_alert" id="alert"><div class="weui_mask"></div><div class="weui_dialog"><div class="weui_dialog_hd"><strong class="weui_dialog_title">提示</strong></div><div class="weui_dialog_bd">'+msg+'</div><div class="weui_dialog_ft"><a href="javascript:;" class="weui_btn_dialog primary">确定</a></div></div></div>';
	$("body").append(str);
	$(".primary").click(function(){
		$("#alert").remove();
	});
}







