$(function () {
    function initConfig() {
        var tabType = $("#_tab").val();
        var btns;
        if (tabType === "recover") {
        	 btns = [
                 { type: "refresh" },
                 { type: "restore",  action: "/admin/news/restore" }
             ];
        } else {
        	 btns = [
                 { type: "refresh" },
                 { type: "add",  action: "/admin/news/add" },
                 { type: "logicDelete", action: "/admin/news/delete" }
             ];
           
        }
        $.baseConfig.btns = btns;
        $.baseConfig.tab = tabType;
    }
    initConfig();


    $("#tbBars").ectoolbar({
        btns: $.baseConfig.btns,
        searchGrid: "#tbList"
    });
    $("#tbList").tbInit();
    $("#tbList").DataTable({
        "ajax": {
            url: "/admin/news/list",
            type: "POST",
            dataType: "json"
        },
        "order": [[7, "desc"]],
        "columns": [
            { data: "" },
            { data: "" },
            { data: "sectionName" },
            { data: "title" },
            {
                data: "isShow",
                render: function (data) {
                    return data ? "显示" : "隐藏";
                }
            },
            {
                data: "isFree",
                render: function (data,type,row) {
                    return data ? "免费" : row.money+"元";
                }
            },
            {
                data: "isTop",
                render: function (data) {
                    return data ? "置顶" : "";
                }
            },
            { data: "pushTime" },
            { data:"remark" }
        ],
        "columnDefs": [
            {
                "render": function(data, type, row) {
                    //创建按钮
                    var html = [];
                    html.push("<div class=\"checkbox \"><input type=\"checkbox\" class=\"i-checks\" value=\"" +row.id +"\"></input></div>");
                    return html.join("");
                },
                "targets": 0,
                "orderable": false
            },
            {
                "render": function(data, type, row) {
                    //创建按钮
                    var html = [];
                    if ($.baseConfig.tab !== "recover") {
                            html.push("<button type=\"button\" class=\"btn btn-primary btn-xs btnEdit\" data-id=\"" +
                                row.id +
                                "\"><i class=\"fa fa-edit\"></i> 编辑</button>");
                            html.push("<a target='_blank' class=\"btn btn-danger btn-xs btnInfo\" href=\"/news/info/"+row.id+"\"><i class=\"fa fa-edit\"></i> 预览</button>");
                    }
                    return html.join("");
                },
                "targets": 1,
                "orderable": false
            }
        ]
    }).on("click", ".btnEdit", function () {
        var id = $(this).data("id");
        $.openWindow("/admin/news/edit?id=" + id, "修改信息");

    });


});