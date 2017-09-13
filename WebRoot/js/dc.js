/**
 * 
 */
var _config={
	topZone:"",
	stopRace:[],
	stopRaceDate:[],
	showStop:false,
	fiveGameStr:",英超,德甲,西甲,意甲,法甲,"
}

$(function(){
	//加载数据
	function loadInit(){
		var raceList=$("#raceTable > tbody.race");
		var len=raceList.length;
		for(var i=0;i<len;i++){
			var _this=raceList.eq(i);
			var matchInfo={};
			var matchKey=_this.attr("matchKey");
			var isStop=_this.hasClass("STOP_BUY");
			matchInfo.gameName=_this.attr("gameName");
			matchInfo.dom=_this[0];
			matchInfo.endTime=_this.attr("endTime");
			matchInfo.matchKey=matchKey;
			matchInfo.top=false;
			matchInfo.del=false;
			matchInfo.stop=isStop!=null;
			$.matchInfo(matchKey,matchInfo);
			if(isStop){
				_config.stopRace.push(_this[0]);
			}
		}
		
		
		var stopRaceDate=$("#raceTable>tbody.race_date.STOP_BUY");
		_config.stopRaceDate=stopRaceDate;
		_config.topZone=$("#raceTable>tbody.race_date").eq(0);
		_config.showStop=$("#stopCheck")[0].checked;
	}
	
	loadInit();
	
	function domToggle(matchKey){
		var $infos=$.matchInfo();
		var hideRaces=[];
		var showRaces=[];
		$.each($infos,function(key,info){
			if(info.del){
				hideRaces.push(info.dom);
			}else{
				showRaces.push(info.dom);
			}
		});
		if(matchKey){
			$($.matchInfo(matchKey).dom).hide();
		}else{
			$(hideRaces).hide();
			$(showRaces).show();
			synOffset();
			$("#worldcupGames").removeClass("wc_select");
		}
		$("#hideCount,#hideCount_filte").html(hideRaces.length);
	}
	
	//还原隐藏
	function reShow(){
		$("#filter :checkbox").attr("checked",true);
		var $infos=$.matchInfo();
		$.each($infos,function(key,info){
			info.del=false;
		});
		domToggle();
	}
	//同步"已隐藏场数的"位置
	function synOffset(){
		var showRaceDate=$("#raceTable > tbody.race_date:visible");
		if(showRaceDate.length<1){
			$("#recover,#worldcupGames").hide();
		}else{
			var topOffset=showRaceDate.eq(0).offset().top;
			topOffset=topOffset+7;
			$("#recover,#worldcupGames").offset({top:topOffset})
		}
	}
	
	//加载亚盘信息
	function loadYpOdd(matchId){
		$(".ypDiv").hide();
		$("#ypDiv_"+matchId).show();
		$("#ypList_"+matchId).load("/Lottery/dc/ypOdd",{matchId:matchId},function(){
			
		});
	}
	//点击已停售
	$("#stopCheck").click(function(){
		_config.showStop=this.checked;
		if(_config.showStop){ //显示
			$(_config.stopRace).removeClass("hidden");
			$(_config.stopRaceDate).removeClass("hidden");
		}else{
			$(_config.stopRace).addClass("hidden");
			$(_config.stopRaceDate).addClass("hidden");
		}
		//还原隐藏的场次
		reShow();
	});
	$("#worldcupGames").click(function(){
		//勾选5大联赛
		var isShow=!$(this).hasClass("wc_select");
		$("#gameNameFilte :checkbox").each(function(){
			var val=","+$(this).val()+",";
			if(_config.fiveGameStr.indexOf(val)>-1){
				this.checked=true;
			}else{
				this.checked=!isShow;
			}
		});
		filte();
	});
	//日期回查
	$("#termValue").change(function(){
		var term=$(this).val();
		location.href=location.pathname+"?term="+term;
	});
	
	//开赛时间与截止时间切换事件
	$("a.turntab").click(function(){
		if($(this).hasClass("current")){
			return false;
		}
		$(this).addClass("current").siblings().removeClass("current");
		var show=$(this).data("show");
		var hide=$(this).data("hide");
		$("#raceTable").find("."+show).show();
		$("#raceTable").find("."+hide).hide();
	});
	
	//赛事赛选begin
	$("#filterSelBox").on("click","#filterSwitch",function(){
		$("#filterSelBox").toggleClass("f_hover");
	}).on("click","#filterClose",function(){
		$('#filterSelBox').removeClass("f_hover");
	}).on("click","div.oper_line a",function(){
		var opt=$(this).attr("option");
		var ckboxs=$(this).closest("dd").find(":checkbox");
		switch(opt){
			case "1":
				ckboxs.attr("checked",true);
				break;
			case "2":
				ckboxs.each(function(){
					this.checked=!this.checked;
				});
				break;
			case "0":
				ckboxs.attr("checked",false);
				break;
			default:
				;
		};
		filte();
	}).on("click",":checkbox",function(){
		filte();
	}).on("click","#recover_filte",function(){
		reShow();
	})
	//end 赛事筛选
	$("#recover").click(function(){
		reShow();
	});

	//绑定table事件
	$("#raceTable").on("hover","tbody.race",function(e){
		if(e.type=="mouseenter"){
			$(this).find("td.race_num  a.game_num").addClass("game_num_hover");
			$(this).find("td.race_num  a.game_num > s").addClass("s_hover");
			$(this).find("td.race_game span.r_game").hide();
			$(this).find("td.race_game a").show();
		}else if(e.type=="mouseleave"){
			$(this).find("td.race_game span.r_game").show();;
			$(this).find("td.race_game a").hide();
			$(this).find("td.race_num  a.game_num").removeClass("game_num_hover");
			$(this).find("td.race_num  a.game_num > s").removeClass("s_hover");
		}
	}).on("click","a.game_num",function(){//删除
		var matchKey=$(this).closest("tbody").attr("matchkey");
		var $info=$.matchInfo(matchKey);
		if($info.top){
			return;
		}
		$info.del=true;
		domToggle(matchKey);
		
	}).on("click","a.att_text",function(){//置顶
		$(this).removeClass("att_text").addClass("att_text_down").html("↓").attr("title","取消置顶");
		var matchKey=$(this).closest("tbody").attr("matchkey");
		var $info=$.matchInfo(matchKey);
		$info.top=true;
		
		//插入个标记,记录原来位置
		var dom=$($info.dom);
		var cloneDom=$("<tbody></tbody>");
		cloneDom.addClass("raceClone").attr("showKey",matchKey).hide();
		dom.before(cloneDom);
		_config.topZone.before(dom);
		synOffset();
	}).on("click","a.att_text_down",function(){//取消置顶
		$(this).removeClass("att_text_down").addClass("att_text").html("↑").attr("title","置顶");
		
		var matchKey=$(this).closest("tbody").attr("matchkey");
		var $info=$.matchInfo(matchKey);
		$info.top=false;
		var dom=$info.dom;
		$("tbody.raceClone[showKey='"+matchKey+"']").after(dom).remove();
		synOffset();
	}).on("click","div.race_date_sprite",function(){
		var _this=$(this).closest("tbody");
		var isShow=_this.data("isShow");
		var trs=_this.nextUntil("tbody.race_date");
		if(!_config.showStop){
			trs=trs.not("tbody.STOP_BUY");
		}
		if(isShow){
			trs.each(function(index,tr){
				var matchKey=$(this).attr("matchKey");
				if(!$.matchInfo(matchKey).del){
					$(this).show();
				}
			})
			_this.data("isShow",false);
		}else{
			trs.each(function(index,tr){
				var matchKey=$(this).attr("matchKey");
				if(!$.matchInfo(matchKey).del){
					$(this).hide();
				}
			})
			_this.data("isShow",true);
		}
	}).on("click",".yptd",function(){
		var tbody=$(this).closest("tbody");
		var matchId=tbody.data("matchid");
		console.log(matchId)
		loadYpOdd(matchId);
	})
	//raceTable 绑定时间end
	
	function divMove(){
		var $div=$("#float_div");
		$div.css({"position":"relative"})
		var divTop=$div.offset().top;
		divMove_base($div,divTop);
	}
	//表头 跟随事件
	function divMove_base($div,divtop){
		var top = $(document).scrollTop();
		if(divtop < top){
			$div.css({top:"0px",position:"fixed"});
		}else{
			$div.css({top:"",position:"relative"});
		}
		return;
	}
	$(window).bind("scroll",function(){
		divMove()
	});
	//赛事筛选
	function filte(){
		var filteStop=_config.showStop;//是否筛选已截止的赛事
		var showRace=[];
		var hideRace=[];
		
		var gameNames=[];
		$("#gameNameFilte input:checked").each(function(){
			gameNames.push($(this).val());
		});
		var endTimes=[];
		$("#endTimeFilter input:checked").each(function(){
			endTimes.push($(this).val());
		});
		var rqs=[];
		$("#rqFilte input:checked").each(function(){
			rqs.push($(this).val());
		});
		
		$.each($.matchInfo(),function(matchKey,matchInfo){
			var dom=$(matchInfo.dom);
			if(!filteStop){
				if(dom.hasClass("STOP_BUY")){
					return ;
				}
			}
			if(matchInfo.top){
				showRace.push(matchInfo.dom);
			}else{
				var isHide=matchInfo.del;
				if(!isHide ){
					var gameHideFlag=true;
					for(var i=0;i<gameNames.length;i++){
						if(matchInfo.gameName==gameNames[i]){
							gameHideFlag=false;
							break;
						}
					}
					isHide=gameHideFlag;
				}
				//若赛事名无限制则判断时间
				if(!isHide ){
					var timeHideFlag=true;
					var endTime=dom.attr("endTime");
					for(var i=0;i<endTimes.length;i++){
						if(endTime==endTimes[i]){
							timeHideFlag=false;
							break;
						}
					}
					isHide=timeHideFlag;
				}
				//让球判断
				if(!isHide ){
					var timeHideFlag=true;
					for(var i=0;i<rqs.length;i++){
						var concede=dom.attr("concede");
						if(rqs[i]=="rq"){
							if(concede!=0){
								timeHideFlag=false;
								break;
							}
						}else if(rqs[i]=="brq"){
							if(concede==0){
								timeHideFlag=false;
								break;
							}
						}
					}
					isHide=timeHideFlag;
				}
			
				if(isHide){
					hideRace.push(matchInfo.dom);
				}else{
					showRace.push(matchInfo.dom);
				}
			}
		});
		$(showRace).show();
		$(hideRace).hide();
		$("#hideCount,#hideCount_filte").html(hideRace.length);
		synOffset();
		
		var flag=true;
		for(var i=0;i<gameNames.length;i++){
			var val=","+gameNames[i]+",";
			if(_config.fiveGameStr.indexOf(val)==-1){
				flag=false;
				break;
			}
		}
		if(flag){
			$("#worldcupGames").addClass("wc_select");
		}else{
			$("#worldcupGames").removeClass("wc_select");
		}
	}
	
});