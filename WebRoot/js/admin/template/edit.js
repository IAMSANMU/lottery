$(function() {
    $("#saveForm").validate({
        rules: {
            Name: {
                required: true
            }
        }
    });

    $("#btnSave").bindSubmit({
        before: function() {
            var html = $('.summernote').summernote("code");
            html = $.base64.encode(html, "UTF-8");
            $("#info").val(html);
            return true;
        }
    });
    $('.summernote').Textarea({ type: "template" });
});