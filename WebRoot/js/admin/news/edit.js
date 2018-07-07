$(function() {
    $("#saveForm").validate({
    	ignore:[],
    	rules: {
            title: {
                required: true
            },
            money:{
            	digits:true
            }
        }
    });
    $(".i-checks").ickbox();
    $(".i-radios").iradio();
    $('.summernote').Textarea({ type: "context" });

    $("#btnSave").bindSubmit({
        before: function () {
        	$(".summernote").each(function(){
        		var target=$($(this).data("target"))
        		 var html =$(this).summernote("code");
        		 html = $.base64.encode(html, "UTF-8");
        		 target.val(html);
        	});
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

    $("[name='template']").change(function () {
    	var $this=$(this);
        var target =$($this.data("target"));
        if ($this.val()) {
            $.confirm("是否替换编辑的内容",
                function() {
                    var html = $this.find("option:selected").data("info");
                    html=$.base64.decode(html, "UTF-8");
                    target.summernote("code", html);
                    $.close();
            });
        }
    });
});