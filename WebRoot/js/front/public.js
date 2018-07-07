$.extend({
	alert : function(content, callback, type) {
		type = type || "info";
		swal({
			title : "",
			text : content,
			confirmButtonText : "关闭",
			type : type
		}, function() {
			if (callback) {
				callback();
			}
		});

	},
	alertWarn : function(content, callback) {
		$.alert(content, callback, "warning");
	},
	alertError : function(content, callback) {
		$.alert(content, callback, "error");
	},
	close : function() {
		swal.close();
	},
	confirm : function(content, okFn, cancelFn) {
		swal({
			title : "",
			text : content,
			showCancelButton : true,
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			type : "warning",
			closeOnConfirm : false
		}, function(isConfirm) {
			if (isConfirm) {
				if (okFn)
					okFn();
			} else {
				if (cancelFn)
					cancelFn();
			}
		});
	},

	ajaxCommit : function(url, data, success, fail) {
		$.post(url, data, function(result) {
			if (result.success) {
				if (success) {
					success(result);
				} else {
					$.alert("操作成功",function(){
						location.reload();
					});
				}
			} else {
				if (fail) {
					fial(result);
				} else {
					$.alertError(result.message);
				}
			}

		}, "json");
	}
});
