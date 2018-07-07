$(function () {
    $("#saveForm").validate({
        rules: {
        	"oldPwd":{
                required: true,
                rangelength: [6, 64]
            },
            "pwd":{
                required: true,
                rangelength: [6, 64]
            },
            "pwdSure": {
                required: true,
                rangelength: [6, 64],
                equalTo:"#pwd"
            }
        }
    });
    $.ajaxForm('#btnSave', $("#saveForm"), {
        success: function(result) {
            $.alert("保存成功,请重新登录", function() {
                $.closeWindow(function () {
                   location.href="/admin";
               });
            });
        }
    });

});
