/**
*扩展
*
**/
$.extend({

    /**
     * funcUrl()获取完整search值(不包含问号)
     * funcUrl(name)返回 url 中 name 的值
     * funcUrl(name,value) 将search中name的值设置为value,并返回完整url 
     * funcUrl(name,value,type) 作用和第三条一样,但这只返回更新好的search字符串 
     * @param {} name 
     * @param {} value 
     * @param {} type 
     * @returns {} 
     */
    funcUrl: function (name, value, type) {
        var loca = window.location;
        var baseUrl = type == undefined ? loca.origin + loca.pathname + "?" : "";
        var query = loca.search.substr(1);
        // 如果没有传参,就返回 search 值 不包含问号
        if (name == undefined) { return query }
        // 如果没有传值,就返回要查询的参数的值
        if (value == undefined) {
            var val = query.match(new RegExp("(^|&)" + name + "=([^&]*)(&|$)"));
            return val != null ? decodeURI(val[2]) : null;
        };
        var url;
        if (query == "") {
            // 如果没有 search 值,则返回追加了参数的 url
            url = baseUrl + name + "=" + value;
        } else {
            // 如果没有 search 值,则在其中修改对应的值,并且去重,最后返回 url
            var obj = {};
            var arr = query.split("&");
            for (var i = 0; i < arr.length; i++) {
                arr[i] = arr[i].split("=");
                obj[arr[i][0]] = arr[i][1];
            };
            obj[name] = value;
            url = baseUrl + JSON.stringify(obj).replace(/[\"\{\}]/g, "").replace(/\:/g, "=").replace(/\,/g, "&");
        };
        return url;
    },
    baseConfig: {tab: "",btns: []},
    permStr: "",
    okFn: function (fileBtn, result) {
        fileBtn = $(fileBtn);
        if (result.isSuccess) {
            $("#"+fileBtn.data("target")).val(result.path);
        } else {
            $.alertError("上传失败");
        }
    },
    delFn: function (fileBtn) {
        fileBtn = $(fileBtn);
        $("#" + fileBtn.data("target")).val("");
    },
    alert: function (content, callback, type) {
        type = type || "info";
        swal({
            title: "",
            text: content,
            confirmButtonText: "关闭",
            type: type
        },
            function () {
                if (callback) {
                    callback();
                }
            });

    },
    alertWarn: function (content, callback) {
        $.alert(content, callback, "warning");
    }, alertError: function (content, callback) {
        $.alert(content, callback, "error");
    },
    close:function() {
        swal.close();
    },
    confirm: function (content, okFn, cancelFn) {
        swal({
            title: "",
            text: content,
            showCancelButton: true,
            confirmButtonText: "确认",
            cancelButtonText: "取消",
            type: "warning",
            closeOnConfirm: false
        },
        function (isConfirm) {
            if (isConfirm) {
                if (okFn)
                    okFn();
            } else {
                if (cancelFn)
                    cancelFn();
            }
        }
        );
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
	},
    hasPerm: function (perm) {
        var flag = true;
        if (perm) {
            if ($.permStr === "") {
                var tmpStr = $("#_perm").val().trim();
                $.permStr = $.base64.decode(tmpStr).toLowerCase();
            }
            var permStr = $.permStr;
            var tmpArr = permStr.split(",");
            flag = $.inArray(perm.toLowerCase(), tmpArr) !== -1;
        }
        return flag;
    },
    openWindow: function (action, title, width, height) {
        width = width || "90%";
        height = height || "90%";
        
        var def = {
            type: 2,
            shade: 0.5,
            area: [width, height],
            content: action,
            title: title,
            fixed:false,
            maxmin: true,
            success: function (layero, index) {
                $.autoFrame(layero);
            }
        }
        layer.open(def);
    },
    openSmall: function(action, title) {
        $.openWindow(action,title,"770px","70%");
    },
    closeWindow: function (callback) {
        parent.layer.closeAll('iframe');
        if (callback) {
            callback();
        }
    },
    ajaxForm: function (elm, form, option) {
        Ladda.bind(elm,
            {
                callback: function (instance) {
                	if (option.before) {
                        var flag = option.before();
                        if (!flag) {
                            instance.stop();
                            return false;
                        }
                    }
                    var pass = true;
                    try {
                        pass = form.valid();
                    } catch (e) {
                    }
                    if (pass) {
                        var _data = form.serialize();
                        if (option.data) {
                            _data = option.data(_data, elm);
                        }
                        if (!_data) {
                            instance.stop();
                        } else {
                            var url = form.attr("action");
                            $.post(url,
                                _data,
                                function(result) {
                                    instance.stop();
                                    if (result.success) {
                                        if (option.success) {
                                            option.success(result);
                                        }
                                    } else {
                                        $.alertWarn(result.message);
                                    }
                                },
                                "json");
                        }
                    } else {
                        instance.stop();
                    }
                }
        });
    },
    padLeft: function (value, width, paddingChar) {
        if (value.length < width) {
            var leftLen = width - value.length;
            var leftStr = "";
            for (var i = 0; i < leftLen; i++) {
                leftStr += paddingChar;
            }
            return leftStr + value;
        }
        return value;
    },
    padRight: function (value, width, paddingChar) {
        if (value.length < width) {
            var leftLen = width - value.length;
            var leftStr = "";
            for (var i = 0; i < leftLen; i++) {
                leftStr += paddingChar;
            }
            return value + leftStr;
        }
        return value;
    },
    formatJSONDate: function (value, showAll) {
        if (value) {
            if (value.startWith("/Date")) {
                var d = new Date(parseInt(value.substr(6), 10));
                var nullDate = new Date(1001, 0, 1);
                if (d.getTime() <= nullDate.getTime()) {
                    return "";
                }
                else {
                    var m = d.getMonth() + 1;
                    var s = d.getFullYear() + "-" + $.padLeft(m.toString(), 2, "0") + "-" + $.padLeft(d.getDate().toString(), 2, "0");
                    if (showAll) {
                        s += " " + $.padLeft(d.getHours().toString(), 2, "0") + ":" + $.padLeft(d.getMinutes().toString(), 2, "0") + ":" + $.padLeft(d.getSeconds().toString(), 2, "0");
                    }
                    return s;
                }
            } else {
                if (!showAll) {
                    return value.substr(0, 10);
                }
                return value;
            }
        } else {
            return "";
        }
    }
    , autoFrame: function (layero) {
        var iframe = $("iframe")[0];
        var bHeight = iframe.contentWindow.document.body.scrollHeight;
        var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
        var height = Math.max(bHeight, dHeight);
        $(iframe).height(height);
    }
});


String.prototype.endWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substring(this.length - s.length) == s)
        return true;
    else
        return false;
};

String.prototype.startWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substr(0, s.length) == s)
        return true;
    else
        return false;
};
$.fn.Textarea = function(option) {
    option = option || {};
    var target = $(this);
    if (!option.callback) {
        option.callback= function(urls) {
            for (var item in urls) {
                if (urls.hasOwnProperty(item)) {
                    target.summernote('insertImage', urls[item]);
                }
            }
        }
    }

    function UploadFiles(files,type, func) {
        //这里files是因为我设置了可上传多张图片，所以需要依次添加到formData中
        var formData = new FormData();
        for (var f in files) {
            if (files.hasOwnProperty(f)) {
                formData.append("file"+f, files[f]);
            }
        }
        $.ajax({
            url: "/admin/upload/"+type,//后台文件上传接口
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                if (result.success) {
                    func(result.data);
                } else {
                    $.alertError(result.message);
                }
            }
        });
    };

    target.summernote({
        lang: 'zh-CN',
        height: 300,
        callbacks: {
            onImageUpload: function(files) { //the onImageUpload API  
                UploadFiles(files,option.type,option.callback);
            }
        }
    });
};

$.fn.loadArea= function() {
    var tigger = $(this);
    var trggerName=tigger.attr("name");
    var $dom =$("<div class=\"bs-chinese-region flat dropdown\" data-min-level=\"3\" data-max-level=\"6\" data-def-val=\"[name='"+trggerName+"']\"><input type=\"text\" class=\"form-control\" id=\"address\" placeholder=\"选择你的地区\" data-toggle=\"dropdown\" readonly=\"\"><div class=\"dropdown-menu\" role=\"menu\" aria-labelledby=\"dLabel\"><div><ul class=\"nav nav-tabs\" role=\"tablist\"><li role=\"presentation\" class=\"active\"><a href=\"#province\" data-next=\"city\" role=\"tab\" data-toggle=\"tab\">省份</a></li><li role=\"presentation\"><a href=\"#city\" data-next=\"district\" role=\"tab\" data-toggle=\"tab\">城市</a></li><li role=\"presentation\"><a href=\"#district\" data-next=\"street\" role=\"tab\" data-toggle=\"tab\">县区</a></li><li role=\"presentation\"><a href=\"#street\" role=\"tab\" data-toggle=\"tab\">街道</a></li></ul><div class=\"tab-content\"><div role=\"tabpanel\" class=\"tab-pane active\" id=\"province\">--</div><div role=\"tabpanel\" class=\"tab-pane\" id=\"city\">--</div><div role=\"tabpanel\" class=\"tab-pane\" id=\"district\">--</div><div role=\"tabpanel\" class=\"tab-pane\" id=\"street\">--</div></div></div></div></div>");
    tigger.after($dom);
    $.getJSON("/Scripts/plugins/bootstrap-chinese-region/sql_areas.json", function(data) {
        $dom.chineseRegion('source', data).on('changed.bs.chinese-region', function (e, areas) {
            tigger.val(areas[areas.length - 1].id);
        });
    });
}
///selectChild 选中父级,是否选择子级
$.fn.loadTree = function (url, selectChild) {
    selectChild = selectChild || false;
    var option = {
        "checkbox": {
            "keep_selected_style": false
        },
        "plugins": ["checkbox", "types"],
        'types': {
            'default': {
                'icon': 'fa fa-folder'
            }
        },
        'core': {
            'data': {
                'url': url,
                "method": "post",
                "dataType": "json"
            }
        }
    }

    if (selectChild) {
        option.checkbox["three_state"] = false;
        option.checkbox["cascade"] = "up+undetermined";
    }
    this.jstree(option);
}


$.fn.imgUpload = function (action,options, okFn, delFn) {
    okFn = okFn || $.okFn;
    delFn = delFn || $.delFn;

    options = options || {};
    var params = $(this).data("params");
    if (params) {
        params = eval('(' + params + ')');
        options.params = params;
    }
    var prev = options.previewsContainer || "#_previews";

    var show = $(prev);
    if (show.length === 0) {
        var prevId = prev.replace("#", "");
        show = $("<div class=\"dropzone hidden\" id=\"" + prevId + "\"   ></div>");
        $(this).filter(":last").after(show);
    }
    $(this).data("previews", show);
    var def = {
        maxFiles: 1,
        maxFilesize: 4,
        acceptedFiles: "image/*",
        url: action,
        dictFileTooBig: "图片最大{{maxFilesize}}M",
        dictMaxFilesExceeded:"超出数量限制",
        dictInvalidFileType: "只能上传图片",
        dictRemoveFile: "移除",
        uploadMultiple: false,
        previewsContainer: "#_previews",
        addRemoveLinks:true,
        init: function() {
            this.on("success", function (file, response) {
                if (okFn) {
                    okFn(this.element,response);
                }
            }).on("removedfile", function (file) {
                var prev = $(this.element).data("previews");
                if (prev.find(".dz-preview").length === 0) {
                    prev.addClass("hidden");
                }
                if (delFn) {
                    delFn(this.element);
                }
            }).on("addedfile", function(file) {
                $(this.element).data("previews").removeClass("hidden");
            });
        }
    }
    Dropzone.autoDiscover = false;
    options = $.extend({}, def, options);
    this.dropzone(options);
}
$.fn.ickbox= function() {
    $(this).iCheck({
        checkboxClass: 'icheckbox_square-green'
    });
}
$.fn.iradio= function() {
    $(this).iCheck({
        radioClass: 'iradio_square-green'
    });

}
$.fn.bindSwitch = function () {
    $(this).each(function(index,elem) {
       var item= new Switchery(elem, { color: '#1AB394' });
    });
}


$.fn.ectoolbar = function (options) {
    this.each(function () {
        var tb = $.data(this, "toolbar");
        if (tb) {
            $.extend(tb.options, options);
        } else {
            $.data(this, "toolbar", {
                options: $.extend({}, $.fn.ectoolbar.defaults, options)
            });
        }
        createToolbars(this);
    });
}
$.fn.getCheckIds = function () {
    var ids = [];
    var ckbox = $(this).find("tbody :checkbox:checked");
    ckbox.each(function () {
        ids.push($(this).val());
    });
    return ids;
}

$.fn.tbInit = function () {
    $(this).preDataTableEvent({
        select: true
    }).on("draw.dt",
        function () {
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green'
            });
            $(".checkAll").iCheck("uncheck").on("ifChecked ifUnchecked",
                function (e) {
                    var table = $("#" + $(this).data("table"));
                    var ckboxs = table.find(":checkbox").not($(this));
                    if (e.type === "ifChecked") {
                        ckboxs.iCheck("check");
                    } else {
                        ckboxs.iCheck("uncheck");
                    }
                });
        });
}
/**
 * option={data:function(){},success:function(){} }
 * 
**/
$.fn.bindSubmit = function (option) {
    option=option || {}
    var formId =option.formId || "#saveForm";
    //禁用input 回车自动提交
    var btn = $(this);
//    $(formId).keydown(function(e) {
//        if (e.keyCode === 13) {
//            btn.click();
//            return false;
//        }
//    });
    if (!option.success) {
        option.success=function () {
            $.alert("提交成功", function () {
                $.closeWindow(function () {
                    parent.location.reload();
                });
            });
        }
    }
    var targets = this;
    for (var i = 0; i < targets.length; i++) {
        $.ajaxForm(targets[i], $(formId), option);
    }


   
}


function createToolbars(target) {
    var options = $.data(target, "toolbar").options;
    $(target).empty().hide();
    if (options.btns && options.btns.length > 0) {
        for (var i = 0; i < options.btns.length; i++) {
            var single = options.btns[i];
            var btn = createButton(target, single);
            if (btn != null) {
                $(target).append(btn).append("&nbsp;");
            }
        }
    }
    $(target).show();

}

function _buildBtn(single) {
    var className = single.className == null ? "" : single.className;
    return $("<button type=\"button\" class=\"btn btn-primary " + className + "\" name=\"" + single.type + "\" >" +
          single.text +
          "</button>")
      .on("click", single.click);
}

function createRefreshButton(target, options, single) {
    single.click = function () {
        var dataTable = $(options.searchGrid).DataTable();
        dataTable.ajax.reload();
    };
    return _buildBtn(single);
}
function createBatActionButton(target, options, single) {
    var orgClick = single.click;
    single.click = function () {
        var grid = $(options.searchGrid);
        var ids = grid.getCheckIds();
        var dataTable = grid.DataTable();

        if (ids.length === 0) {
            $.alert("请选择一项！");
            return;
        }
        $.confirm(single.confirmMsg, function () {
            $.post(single.action, { ids: ids.join(",") }, function (data) {
                if (data.success) {
                    if (orgClick) {
                        orgClick();
                    } else {
                        $.alert("操作成功", function () {
                            dataTable.ajax.reload();
                        });
                    }
                } else {
                    $.alertError(data.message, function () {
                        dataTable.ajax.reload();
                    });
                }
            }, "json");
        });
    };
    return _buildBtn(single);
}
function createAddButton(target, options, single) {
    single.click = function () {
        $.openWindow(single.action, single.text, single.width, single.height);
    }

    return _buildBtn(single);
}

function createExportButton(target, options, single) {
    single.click = function () {
        location.href = single.action;
    }
    return _buildBtn(single);
}


function createMenuButton(target, options, single) {
    var menuId = "#" + single.menuId;

    var btnGroup = $("<div class=\"btn-group\"></div>");
    var dropdown = $("<button type=\"button\" class='btn btn-primary dropdown-toggle'  data-toggle=\"dropdown\">批量操作<span class=\"caret\"></span></button>");
    var menu = $("<ul class=\"dropdown-menu animated fadeInRight m-t-xs\" role=\"menu\"></ul>");
    $(menuId).children().each(function () {
        var code = $(this).attr("data-type");
        var action = $(this).attr("data-action");
        if (!single.checkPermission || (single.checkPermission && $.hasPerm(single.type))) {
            var li = $("<li data-action=\"" + action + "\"><a href=\"#\">" + $(this).text() + "</a></li>");
            li.on("click", single.click);
            menu.append(li);
        }
    });
    btnGroup.append(dropdown).append(menu);
    return btnGroup;
}
function createButton(target, single) {
    var obj;
    var text = single.text;
    var options = $.data(target, "toolbar").options;
    var def = {
        type: "", //按钮类型
        text: "", //按钮显示文本
        confirmMsg: "", //按钮确认提示文本
        action: null, //action
        click: null,//点击事件
        menuId: "",//menu
        perm: "", //按钮权限
        width: "",//弹窗宽
        height:""//弹窗高
        
    }
    single = $.extend({}, def, single);
    if ($.hasPerm(single.perm)) {
        //刷新默认处理
        switch (single.type) {
            case "refresh":
                if (single.text === "") { single.text = "刷 新"; }
                return createRefreshButton(target, options, single);
            case "delete":
                if (single.text === "") { single.text = "删 除"; }
                single.confirmMsg = single.confirmMsg == "" ? "确定要执行删除操作吗？删除后的数据将不能恢复！" : single.confirmMsg;
                return createBatActionButton(target, options, single);
            case "logicDelete":
                if (single.text === "") { single.text = "放入回收站"; }
                single.confirmMsg = single.confirmMsg == "" ? "确定要执行此操作吗？删除后的数据将放入回收站！" : single.confirmMsg;
                return createBatActionButton(target, options, single);
            case "add":
                if (single.text === "") { single.text = "新 增"; }
                return createAddButton(target, options, single);
            case "active":
                if (single.text === "") { single.text = "启 用"; }
                single.confirmMsg = single.confirmMsg == "" ? "是否确定启用?" : single.confirmMsg;
                return createBatActionButton(target, options, single);
            case "unactive":
                if (single.text === "") { single.text = "禁 用"; }
                single.confirmMsg = single.confirmMsg == "" ? "是否确定禁用?" : single.confirmMsg;
                return createBatActionButton(target, options, single);
            case "restore":
                if (single.text === "") { single.text = "还 原"; }
                single.confirmMsg = single.confirmMsg == "" ? "是否确定还原?" : single.confirmMsg;
                return createBatActionButton(target, options, single);
            case "VerifySuccess":
                if (single.text === "") { single.text = "审核通过"; }
                return createBatActionButton(target, options, single);
            case "VerifyFail":
                if (single.text === "") { single.text = "审核不予通过"; }
                return createBatActionButton(target, options, single);
            case "SetRates":
                if (single.text === "") { single.text = "设置阿母币抵押比例"; }
                return createAddButton(target, options, single);
            case "export":
                if (single.text === "") { single.text = "导出"; }
                return createExportButton(target, options, single);
            case "menu"://不做处理
                break;
            default:
                return _buildBtn(single);
        }
    }
    if (single.type === "menu") {
        return createMenuButton(target, options, single);
    }
};
$(function(){
	$('#myTabs a[data-toggle="tab"]').on('show.bs.tab', function (e) {
        var tabType = $(this).attr("data-type");
	    location.href = $.funcUrl("tab", tabType);
	});
	function addResetBtn() {
	    var btn = $("<button type='button' class='btn btn-danger'>重置</button>");
	    btn.click(function () {
	        //重新加载
	        $(this).closest("form")[0].reset();
	        $("#search").click();
	    })
	    $("#search").after(btn).after("&nbsp;&nbsp;");
	}
	addResetBtn();
})