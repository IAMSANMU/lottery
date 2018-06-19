$(function() {
    $("#saveForm").validate({
        rules: {
            title: {
                required: true
            }
        }
    });
    $(".i-checks").ickbox();
    $(".i-radios").iradio();
    $('.summernote').Textarea({ type: "context" });

    $("#btnSave").bindSubmit({
        before: function () {
            var html = $('.summernote').summernote("code");
            html = $.base64.encode(html, "UTF-8");
            $("#info").val(html);
            return true;
        }
    });
    $("[name='type']").on("ifChanged", function (e) {
        var val = $(this).val();
        if (val == 0) {
            $(".link").addClass("hidden");
        } else {
            $(".link").removeClass("hidden");
        }
    });

    $("#template").change(function () {
        var target = $(this);
        if (target.val()) {
            $.confirm("是否替换编辑的内容",
                function() {
                    var html = target.find("option:selected").data("info");
                    html=$.base64.decode(html, "UTF-8");
                    $(".summernote").summernote("code", html);
                    $.close();
            });
        }
    });
});