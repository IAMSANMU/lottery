$(function () {
	$(".i-checks").ickbox();
    $("#saveForm").validate({
        rules: {
            "pwd":{
                required: true,
                rangelength: [5, 16]
            },
            "pwdSure": {
                required: true,
                rangelength: [6, 64],
                equalTo:"#pwd"
            },
            "account":{
            	 required: true,
                 rangelength: [6, 64]
            }
        }
    });
    $.ajaxForm('#btnSave', $("#saveForm"), {
        success: function(result) {
            $.alert("保存成功", function() {
                $.closeWindow(function () {
                    parent.location.reload();
                });
            });
        }
    });

});
