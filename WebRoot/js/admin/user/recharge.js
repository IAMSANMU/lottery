$(function () {
    $("#saveForm").validate({
        rules: {
            "amount":{
                required: true,
                digits:true
            }
            
        }
    });
    $("#btnSave").bindSubmit();

});
