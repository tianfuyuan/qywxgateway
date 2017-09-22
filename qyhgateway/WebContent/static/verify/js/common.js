document.getElementsByTagName("html")[0].style.fontSize=document.documentElement.clientWidth/15+"px";
//改变窗口的时候重新计算大小
window.onresize = function(){
	document.getElementsByTagName("html")[0].style.fontSize=document.documentElement.clientWidth/15+"px";
}

// 对浏览器的UserAgent进行正则匹配，不含有微信独有标识的则为其他浏览器
 var useragent = navigator.userAgent;
 if (useragent.match(/MicroMessenger/i) != 'MicroMessenger') {
        // 这里警告框会阻塞当前页面继续加载
        alert('已禁止本次访问：您必须使用微信内置浏览器访问本页面！');
        // 以下代码是用javascript强行关闭当前页面
        var opened = window.open('about:blank', '_self');
        opened.opener = null;
        opened.close();
        window.close();
 }

 
//禁止分享
function onBridgeReady(){
 WeixinJSBridge.call('hideOptionMenu');
}

if (typeof WeixinJSBridge == "undefined"){
    if( document.addEventListener ){
        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
    }else if (document.attachEvent){
        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
    }
}else{
    onBridgeReady();
}