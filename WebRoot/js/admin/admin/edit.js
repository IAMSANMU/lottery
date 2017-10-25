$(function () {
    $("#saveForm").validate({
        rules: {
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
            $.alert("保存成功", function() {
                $.closeWindow(function () {
                    parent.location.reload();
                });
            });
        }
    });

});
