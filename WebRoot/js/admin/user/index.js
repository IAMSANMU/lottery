$(function () {
    function initConfig() {
    	 var tabType = $("#_tab").val();
         var btns;
         if (tabType === "list") {
             btns = [
                 { type: "refresh" },
                 { type: "add", action: "/admin/user/add" },
                 { type: "logicDelete", action: "/admin/user/logicDelete" }
             ];
         } else {
             btns = [
                 { type: "refresh" },
                 { type: "restore", action: "/admin/user/restore" }
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
            url: "/admin/user/list",
            type: "POST",
            dataType: "json"
        },
        "order": [[7, "desc"]],
        "columns": [
            { data: "" },
            { data: "" },
            { data: "account" },
            { data: "name" },
            { data: "tel" },
            { data: "email" },
            { data: "isStop" },
            { data: "createTime" }
        ],
        "columnDefs": [
            {
                "render": function(data, type, row) {
                    //创建按钮
                    var html = [];
                    html.push("<div class=\"checkbox \"><input type=\"checkbox\" class=\"i-checks\" value=\"" +row.Id +"\"></input></div>");
                    return html.join("");
                },
                "targets": 0,
                "orderable": false
            },
            {
                "render": function(data, type, row) {
                    //创建按钮
                    var html = [];
                    html.push("<button type=\"button\" class=\"btn btn-primary btn-xs btnEdit\" data-id=\"" +
                        row.id +
                        "\"><i class=\"fa fa-edit\"></i> 编辑</button>");
                    html.push("<button type=\"button\" class=\"btn btn-danger btn-xs btnPwd\" data-id=\"" +
                            row.id +
                            "\"><i class=\"fa fa-edit\"></i> 修改密码</button>");
                    return html.join("&nbsp;");
                },
                "targets": 1,
                "orderable": false
            }, {
                "render": function(data) {
                    return data ? "是" : "否";
                },
                "targets":6
            }
        ]
    }).on("click", ".btnEdit", function () {
        var id = $(this).data("id");
        $.openWindow("/admin/user/edit?id=" + id, "修改信息");
    }).on("click", ".btnPwd", function () {
        var id = $(this).data("id");
        $.openWindow("/admin/user/pwd?id=" + id, "修改信息");
    });


});