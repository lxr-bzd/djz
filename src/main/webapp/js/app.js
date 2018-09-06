
Date.prototype.format = function (format) 
{
    var o = {
        "M+": this.getMonth() + 1, //month 
        "d+": this.getDate(),    //day 
        "h+": this.getHours(),   //hour 
        "m+": this.getMinutes(), //minute 
        "s+": this.getSeconds(), //second 
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter 
        "S": this.getMilliseconds() //millisecond 
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
    (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
      RegExp.$1.length == 1 ? o[k] :
        ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}



$app = {loginUrl:'/djt/login.html',imgUpload:"/djt/image/upload.do"};

$app.getContext = function(){
	
	var context = window;
	if(window.top)context = window.top;
	return context;
}

$app.request = function(url,call,param){
	$.ajax({ 
		type: "post", 
		dataType:"json",
		url: url, 
		data:((param&&param.param)?param.param:""),	
		beforeSend: function(XMLHttpRequest){ 
		
		}, 
		success: function(data, textStatus){
			if(data&&data.code==5)window.location.href = $app.loginUrl;
			if(data&&data.code==7){$app.alert(data.msg); return;}
			call(data,textStatus);
		}, 
		complete: function(XMLHttpRequest, textStatus){ 
		if(param&&param.complete)
			param.complete(XMLHttpRequest, textStatus);
		}, 
		error: function(){ 
			if(param&&param.error)
				param.error();
		} 
		}); 
	
}


$app.alert = function(msg,type,call){
	
	var context = this.getContext();
	var icon = {};
	if(!type){
		if(type=="w")icon.icon=7;
		if(type=="s")icon.icon=1;
		if(type=="e")icon.icon=2;
	}
	
	context.layer.alert(msg,icon,function(index){
		if(call)call();
		context.layer.close(index);
		
	});
}

$app.prompt = function(title,call,formType){
	
	if(!formType)formType = 2;
	 
	layer.prompt({title: title, formType: formType}, function(pass, index){
		  layer.close(index);
		  if(call)call(pass);
		 
		});
}

$app.msg = function(msg){
	layer.msg(msg);
}

