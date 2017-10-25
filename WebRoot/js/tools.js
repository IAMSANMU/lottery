/**
*扩展
*
**/
$.extend({
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
            maxmin: true
        }
        layer.open(def);
    },
    openSmall: function(action, title) {
        $.openWindow(action,title,"60%","60%");
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
                if (form.valid()) {
                    var _data = form.serialize();
                    if (option.data) {
                        _data = option.data(_data);
                    }
                    var url = form.attr("action");
                    $.post(url,
                        _data,
                        function (result) {
                            instance.stop();
                            if (result.success) {
                                if (option.success) {
                                    option.success(result);
                                }
                            } else {
                                $.alertWarn(result.Message);
                            }
                        },
                        "json");
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
$.fn.loadTree= function(url) {
    var option= {
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
        $(this).eq(0).after(show);
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
                if (data.Success) {
                    if (orgClick) {
                        orgClick();
                    } else {
                        $.alert("操作成功", function () {
                            dataTable.ajax.reload();
                        });
                    }
                } else {
                    $.alertError(data.Message, function () {
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
                return createBatActionButton(target, options, single);
            case "unactive":
                if (single.text === "") { single.text = "禁 用"; }
                return createBatActionButton(target, options, single);
            case "restore":
                if (single.text === "") { single.text = "还 原"; }
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
            case "menu"://不做处理
                break;
            default:
                return _buildBtn(single);
        }
    }
    if (single.type === "menu") {
        return createMenuButton(target, options, single);
    }
}
$(function(){
	$('#myTabs a[data-toggle="tab"]').on('show.bs.tab', function (e) {
        var tabType = $(this).attr("data-type");
        location.href = location.pathname + "?tab=" + tabType;
    });
	$("#_modifyPwd").click(function(){
		$.openWindow("/admin/admin/pwd","修改密码");
	})
})