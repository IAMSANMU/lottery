$(function () {
    function initConfig() {
    	 var tabType = $("#_tab").val();
         var btns;
         if (tabType === "list") {
             btns = [
                 { type: "refresh" }
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
            url: "/admin/log/list",
            type: "POST",
            dataType: "json"
        },
        "order": [[5, "desc"]],
        "columns": [
            { data: "account" },
            { data: "name" },
            { data: "tel" },
            { data: "startTime" },
            { data: "endTime" },
            { data: "createTime" },
            { data: "remark" }
        ],
        "columnDefs": [
            {
            }
        ]
    });


});