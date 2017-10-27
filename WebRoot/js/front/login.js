$(function(){
	$("#saveForm").validate({
		rules: {
			"account":{
				required: true,
				rangelength: [4, 16]
			},
			"tmpPwd": {
				required: true,
				rangelength: [6, 64]
			},"randomCode":{
				required: true,
				maxlength: 4
			}
		}
	});
	$("#login-btn").click(function(){
		var form=$("#saveForm");
		  if (form.valid()) {
			  var pwd=$("#tmpPwd").val();
			  pwd=hex_md5(pwd);
			  $("#pwd").val(pwd);
			  $("#saveForm").submit();
		  }
	});
	$(".loginText").focus(function(){
		$(this).parents(".group:first").find(".cwrong").hide();
		
	})
});
