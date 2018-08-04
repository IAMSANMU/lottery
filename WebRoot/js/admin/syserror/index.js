$(function () {
    function initConfig() {
        var tabType = $("#_tab").val();
        var btns = [{ type: "refresh" }];
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
            url: "/admin/syserror/list",
            type: "POST",
            dataType: "json"
        },
        "order": [[3, "desc"]],
        "columns": [
            { data: "" },
            { data: "name" },
            { data: "url" },
            { data: "createTime" },
            { data: "remark" }
        ],
        "columnDefs": [
            {
                "render": function(data, type, row) {
                    //创建按钮
                    var html = [];
                    if ($.baseConfig.tab === "list") {
                        html.push("<button type=\"button\" class=\"btn btn-primary btn-xs btnEdit\" data-id=\"" +
                            row.id +
                            "\"><i class=\"fa fa-edit\"></i>已处理</button>");
                    }
                    return html.join("");
                },
                "targets": 0,
                "orderable": false
            }
        ]
    }).on("click", ".btnEdit", function () {
        var id = $(this).data("id");
        $.confirm("请确认联系开发人员已处理问题,是否确认设为已处理?",function(){
        	
        	$.ajaxCommit("/admin/syserror/handle/"+id,{},function(){
        		$.alert("处理成功",function(){
        			$.close();
        			location.reload();
        		})
        	})
        });
    });


});