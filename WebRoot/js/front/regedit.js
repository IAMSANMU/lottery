$(function(){
	$("#saveForm").validate({
		rules: {
			"account":{
				required: true,
				rangelength: [4, 16]
			},
			"pwd": {
				required: true,
				rangelength: [6, 64]
			},"randomCode":{
				required: true,
				maxlength: 4
			},"pwdSure": {
				required: true,
				rangelength: [6, 64],
				equalTo:"#pwd"
			}
		}
	});
	$("#regedit-btn").click(function(){
		var form=$("#saveForm");
		  if (form.valid()) {
			  var data=form.serialize();
			  $.post("/login/doRegedit",data,function(result){
				  if(result.success){
					  layer.alert("注册成功,现在去登录",function(){
						  location.href="/login";
					  });
				  }else{
					  //更换验证码
					  $("#validateImg")[0].src="/login/img?t="+new Date().getTime()
					  layer.alert(result.message);
				  }
			  },"json");
		  }
	});
});
