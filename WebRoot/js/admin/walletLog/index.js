$(function () {
    function initConfig() {
    	 var tabType = $("#_tab").val();
    	 var  btns = [
	             { type: "refresh" },
	         ];
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
            url: "/admin/walletLog/list",
            type: "POST",
            dataType: "json"
        },
        "order": [[5, "desc"]],
        "columns": [
            { data: "account" },
            { data: "id" },
            { data: "transtype",render:function(data){
            	return data==0?"消费":"充值";
            } },
            { data: "transMoney",render:function(data){
            	return data+"元";
            } },
            { data: "balance",render:function(data){
            	return data+"元";
        	} },
            { data: "createTime" },
            { data:"remark"}
        ]
    });
});