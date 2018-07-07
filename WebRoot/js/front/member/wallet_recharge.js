$(function(){
	$("[name='scanType']").click(function(){
		$("[name='scanType']").removeClass("cur")
		$(this).addClass("cur");
	});
	
	$(".cllradio_sys").click(function(){
		$(".cllradio_sys a").removeClass("select");
		$(this).find("a").addClass("select")
		
	})
	
	$("#doRecharge").click(function(){
		var $money=$('.recharge_money .select');
		
		if($money.length==0){
			$.alertError("请选择一个充值金额");
		}else{
			var amount=$money.data("money");
			var rechargeType=$("[name='scanType']").filter(".cur").data("type");
			$.confirm("是否确认充值?",function(){
				$.ajaxCommit("/wallet/doRecharge",{"amount":amount,"type":rechargeType},function(){
					$.close();
					
					$("#pay img").attr("src","/img/wechat/"+amount+".jpg");
					//弹出收款码
					layer.open({
						  type: 1,
						  title: false,
						  closeBtn: 1,
						  shadeClose: false,
						  skin: 'layui-layer-nobg', //没有背景色
						  content: $("#pay"),
						  area:['420px','420px']
						});
				});
			});
		}
	});
	
	
	
})