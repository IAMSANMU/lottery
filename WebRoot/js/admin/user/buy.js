$(function () {
    $("#saveForm").validate({
        rules: {
            "startTime":{
                required: true
            },
            "endTime": {
                required: true,
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
