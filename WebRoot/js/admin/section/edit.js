$(function() {
    $("#saveForm").validate({
        rules: {
            name: {
                required: true
            }
        }
    });

    $("#btnSave").bindSubmit();
});