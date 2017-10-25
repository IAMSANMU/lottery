$(function () {
    $("#form").validate({
        rules: {
            password: {
                required: true
            },
            name: {
                required: true
            }
        }
    });
    $("#btn_login").click(function() {
        if ($("#form").valid()) {
        	var pwd=$("[name='password']").val();
        	pwd=hex_md5(pwd);
        	$("[name='password']").val(pwd);
            var data = $("#form").serialize();
            var url="/admin/login";
            $.post(url, data, function (result) {
                if (result.success) {
                    location.href ="/admin/user";
                } else {
                    $.alert(result.message);
                }
               
            }, "json");
        }
    });
    if (parent.frames.length > 0) {
        parent.window.location.href = window.location.href;
    }
});
