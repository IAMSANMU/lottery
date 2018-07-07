$(function () {
    function initConfig() {
    	 var tabType = $("#_tab").val();
         var btns=[ { type: "refresh" }];
         if (tabType === "list") {
             btns.push(
                 { type: "logicDelete", action: "/admin/recharge/delete" }
             )
         } else if(tabType==="recover") {
        	 btns.push(
        			 { type: "restore", action: "/admin/recharge/restore" }
                 )
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
            url: "/admin/recharge/list",
            type: "POST",
            dataType: "json"
        },
        "order": [[5, "desc"]],
        "columns": [
            { data: "" },
            { data: "" },
            { data: "account" },
            { data: "money" },
            { data: "rechargeType",render:function(data){
            	return data=="WECHAT_RECHARGE"?"微信":"支付宝";
            } },
            { data: "createTime" },
            { data: "updateTime",render:function(data,type,row){
            	return row.status==1?"未到账":data;
            } }
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
                    if( $.baseConfig.tab==="list"){
                    	html.push("<button type=\"button\" class=\"btn btn-primary btn-xs btnEdit\" data-id=\"" +
                        row.id +
                        "\"><i class=\"fa fa-edit\"></i>到账</button>");
                    }
                    
                    return html.join("&nbsp;");
                },
                "targets": 1,
                "orderable": false
            }
        ]
    }).on("click", ".btnEdit", function () {
        var id = $(this).data("id");
        $.confirm("是否确认? 该操作不会增加余额, 请到会员管理手动充值",function(){
        	$.ajaxCommit("/admin/recharge/ok/"+id,function(){
        		$.close();
        		location.reload();
        	});
        })
    });


});