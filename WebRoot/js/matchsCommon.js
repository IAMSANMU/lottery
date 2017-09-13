var _awardCast=[0,0];
$(document).ready(function(){
	var buying=false;
	var gameNameStr=_config.gameNameStr;
	_config.playType=$("#playType_hide").val();
	$.setIs2c1(_config.playType=="44");
	
	//显示隐藏"已隐藏场数"
	var changeRecover=function(){
		if($("#raceTable > tbody.race_date").filter(":visible").length<1){
			$("#recover,#worldcupGames,#rqcheck").hide();
		}else{
			$("#recover,#worldcupGames,#rqcheck").show();
		}
	}
	//停售赛事`
	$("#stopCheck").click(function(){
		var flag=this.checked;
		var raceDates=$("#raceTable  b.time");
		var stopBodys=$("#raceTable > tbody.STOP_BUY");
		if(flag){
			stopBodys.removeClass("hidden");
		}
		else{
			stopBodys.addClass("hidden");
		}
		for(var i=0;i<raceDates.length;i++){
			var val=raceDates.eq(i).attr("val");
			$("#date_"+val).disabled=!flag;
		}
		synRaceDate();
		changeRecover();
		$("#recover").click();
	})
	//同步"已隐藏场数的"位置
	var synOffset=function(){
		var topOffset=$("#raceTable > tbody.race_date").filter(":visible").eq(0).offset().top;
		topOffset=topOffset+5;
		$("#recover,#worldcupGames,#rqcheck").offset({top:topOffset})
	}
	//同步race_date "显示/隐藏"
	var synRaceDate=function(){
		var flag=$("#stopCheck")[0].checked;
		$("#raceTable >tbody.STOP_BUY").each(function(){
			var _this=$(this);
			_this.data("status",!flag);
			if(flag){
				$(this).find("a.hiding").html("隐藏");
			}else{
				$(this).find("a.hiding").html("显示");
			}
		})
	}
	//开赛时间与截止时间切换事件
	$("a.turntab").click(function(){
		if($(this).hasClass("current")){
			return false;
		}
		$(this).addClass("current").siblings().removeClass("current");
		var type=$(this).attr("data");
		if(type=="matchTime"){
			$("#raceTable").find("span.matchTime").show();
			$("#raceTable").find("span.endTime").hide();
		}else{
			$("#raceTable").find("span.matchTime").hide();
			$("#raceTable").find("span.endTime").show();
		}
	});
//	//投注选项点击事件
	$("#raceTable").on("tbody.race","hover",function(e){
		if(e.type=="mouseenter"){
			$(this).css("background","#fffbd8");
			$(this).find("td.race_num  a.game_num").addClass("game_num_hover");
			$(this).find("td.race_num  a.game_num > s").addClass("s_hover");
			$(this).find("td.race_game span.r_game").css("display","none");
			$(this).find("td.race_game a.att_text").css("display","inline-block");
			$(this).find("td.race_game a.att_text_down").css("display","inline-block");
		}else if(e.type=="mouseleave"){
			var type=$("#timeChangeTab").data("type");
			$(this).css("background","#ffffff");
			$(this).find("td.race_game span.r_game").css("display","inline-block");
			$(this).find("td.race_game a.att_text").css("display","none");
			$(this).find("td.race_game a.att_text_down").css("display","none");
			$(this).find("td.race_num  a.game_num").removeClass("game_num_hover");
			$(this).find("td.race_num  a.game_num > s").removeClass("s_hover");
		}
	}).delegate("a.game_num","click",function(){ //隐藏场次
		var matchKey=$(this).closest("tbody").attr("matchkey");
		if($.matchInfo(matchKey).top){
			return;
		}
		$.matchInfo(matchKey).del=true;
		$($.matchInfo(matchKey).dom).hide();
		$("#hideCount,#hideCount_filte").html($("#hideCount").html()*1+1);
	}).delegate("a.att_text","click",function(){//置顶
		var matchKey=$(this).parent().parent().parent().attr("matchkey");
		if($.matchInfo(matchKey)){
			$.matchInfo(matchKey).top=true;
			var dom=$($.matchInfo(matchKey).dom);
			dom.find("a.att_text").removeClass("att_text").addClass("att_text_down").text("↓");
			var showKey=$.matchInfo(matchKey).showKey;
			var cloneDom=$("<tbody></tbody>");
			cloneDom.addClass("raceClone").attr("showKey",showKey).hide();
			dom.before(cloneDom);
			$("#raceTable>tbody.race_date").eq(0).before(dom);
			synOffset();
		}
	}).delegate("a.att_text_down","click",function(){//取消置顶
		var matchKey=$(this).parent().parent().parent().attr("matchkey");
		if($.matchInfo(matchKey)){
			$.matchInfo(matchKey).top=false;
			var dom=$($.matchInfo(matchKey).dom);
			dom.find("a.att_text_down").removeClass("att_text_down").addClass("att_text").text("↑");
			var showKey=$.matchInfo(matchKey).showKey;
			$("tbody.raceClone[showKey='"+showKey+"']").after(dom).remove();
			synOffset();
		}
	}).delegate("div.race_date_sprite > b","click",function(){
		var _this=$(this).parent().parent().parent().parent();
		var status=_this.data("status");
		if(status){
			var trs=_this.nextUntil("tbody.race_date");
			if(!$("#stopCheck").is(":checked")){
				trs=trs.not("tbody.STOP_BUY");
			}
			trs.each(function(index,tr){
				if(!$.matchInfo($(this).attr("matchKey")).del){
					$(this).show();
				}
			})
			_this.find("a.hiding").html("隐藏");
			_this.data("status",false);
		}else{
			var trs=_this.nextUntil(".race_date");
			if(!$("#stopCheck").is(":checked")){
				trs=trs.not(".STOP_BUY");
			}
			trs.each(function(index,tr){
				if(!$.matchInfo($(this).attr("matchKey")).del){
					$(this).hide();
				}
			})
			_this.find("a.hiding").html("显示");
			_this.data("status",true);
		}
	});
	//全包按钮点击事件
	var allTd=function(tdObj){
		var raceDom =tdObj.parents("tbody.race");
		var trObj=tdObj.parent();
		var matchKey = raceDom.attr("matchKey");
		var tempDetail=raceDom.find("tr[class^='detail_']:visible");
		var lotype=$(tempDetail).attr("name").split("_")[0];
		var tempShowTag=raceDom.find("[name='"+lotype+"_show_tag']");
		if($.trim(tdObj.text())=="全"){
			tdObj.html("<span class=\"a\">清</span>");
			trObj.children("td.term").not(".stop").addClass("current");
		}else{
			tdObj.html("<span class=\"a\">全</span>");
			trObj.children("td.term").not(".stop").removeClass("current");
		}
		var selectedOptions = tempDetail.find("td.current");
		//该场次无选项显示默认信息
		if(selectedOptions.size() <= 0){
			$(tempShowTag).find("span.txt").show();
		}else{
			$(tempShowTag).find("span.txt").hide();
		}
		//该场次选项个数大于4显示统计信息否则显示具体选项
		if(selectedOptions.size() > _config.maxShowOption){
			$(tempShowTag).find("span.beat_sel").hide();
			$(tempShowTag).find("span.beat_many").show();
			$(tempShowTag).find("span.beat_many").html("已选<b>"+ selectedOptions.size() +"</b>项");
		}else{
			var optionHTML="";
			$(selectedOptions).each(function(option){
				ctxt = $(this).find(".a").html();
				ctxt = ctxt.replace("-",":");
				optionHTML+="<a href='javascript:;'>"+ctxt+"</a>"
			});
			$(tempShowTag ).find("span.beat_sel").html(optionHTML);
			$(tempShowTag).find("span.beat_sel").show();
			$(tempShowTag).find("span.beat_many").hide();
		}
		refreshBetCache(matchKey);
		refreshBetItem();
	};
	//右侧投注篮事件绑定开始
	$("#betting_box").delegate("a.beatbox","click",function(e){//投注选项
		clickSelectOption(this);
	}).delegate("li.addgame","click",function(){
		var matchKey=$(this).parents("tbody:first").attr("matchKey");
		var matchInfo=$.matchInfo(matchKey);
		var dom=$(matchInfo.dom);
		var scroll=dom.offset().top;
		
		var startTop=$("#float_div").offset().top;//到了这个值float_div会置顶头部
		scroll=scroll-$("#float_div").height();		
		if($("#float_div").css("top")!="0px" && scroll >=startTop){ //会导致float_Div变化位置
			scroll=scroll-$("#float_div").height();
		}
		$(document).scrollTop(scroll);
		function shan(){
			lightAttention(dom.offset(),dom.height(),dom.width());
		}
		setTimeout(shan,200);
	}).delegate("tr.team","hover",function(e){
		$(this).find("div.race").toggleClass("racehover");
	}).delegate("a[name='clear']","click",function(e){//删掉1场次
		var matchKey=$(this).parents("tbody:first").attr("matchKey");
		var dom=$($.matchInfo(matchKey).dom);
		dom.find(".current[option]").removeClass("current");
		dom.find("td.other").html("<span class=\"a\">全</span>");
		dom.find("td.show_tag,div.show_tag").each(function(){
			$(this).find("span.txt").html("显示SP投注<i class='icon'></i>").show();
			$(this).find("span.beat_sel").hide();
			$(this).find("span.beat_many").hide();
			$(this).removeClass("show_current");
		});
		dom.find("tr[class^='detail_']").hide();
		refreshBetCache(matchKey);
		refreshBetItem();
	}).delegate("span.setdan > a","click",function(){//胆点击事件
		$(this).toggleClass("default select");
		var isSelect=this.className=="select";
		var matchKey=$(this).parents("div:first").attr("matchKey");
		var allOptions=$.options(matchKey);
		if(!isSelect){
			delete allOptions.dan;
		}else{
			allOptions.dan=isSelect;
		}
		$.options(matchKey,undefined,allOptions);
		buildPassType();
	}).delegate("span[name='passType']","click",function(){
		$(this).find("a").toggleClass("default select");
		refreshPassType();	
	}).delegate("#cqdy","click",function(){
		if(this.className=="disable"){
			return false;
		}
		$(this).toggleClass("default select");
		refreshBetInfo();
	}).delegate("#clearAllBtn","click",function(){
		var tbodys=$("#itemTable > tbody");
		for(var i=0,len=tbodys.length;i<len;i++){
			var tbody=tbodys.eq(i);
			var matchKey=tbody.attr("matchKey");
			var dom=$($.matchInfo(matchKey).dom);
			dom.find("[option]").removeClass("current");
			dom.find("td.other").html("<span class=\"a\">全</span>");
			dom.find("td.show_tag,div.show_tag").each(function(){
				$(this).find("span.txt").html("显示SP投注<i class='icon'></i>").show();
				$(this).find("span.beat_sel").hide();
				$(this).find("span.beat_many").hide();
				$(this).removeClass("show_current");
			});
			dom.find("tr[class^='detail_']").hide();
		}
		$.removeOptions();
		refreshBetItem();
	});
	$("#result_step").delegate("span.cllradio","click",function(){
		if($("#result_step").hasClass("step_disable")){
			return false;
		}
		$(this).find("a").attr("class","select");
		$(this).siblings().find("a").removeClass("select").addClass("default");
		var id=this.id || "";
		if(id=="dgbtn"){
			$("#sportHmBox").hide();
			optDefault(false);
		}else if(id=="hmbtn"){
			$("#sportHmBox").show();
			optDefault(true);
		}
	});
	//投注篮选项点击事件
    var clickSelectOption = function(option){
    	var matchKey=$(option).parents("tbody[matchKey]:first").attr("matchKey");
    	var optionValue=$(option).attr("option");
		var raceDom=$($.matchInfo(matchKey).dom);
		var tempDetail=raceDom.find("tr[class='detail_"+optionValue.split("_")[0]+"']");
		var optionDom=null;
		if(tempDetail.size()>0){
			var tempShowTag=raceDom.find("[name='"+optionValue.split("_")[0]+"_show_tag']");
			optionDom=$(tempDetail).find("[option=\""+optionValue+"\"]");
			var otherBtn=optionDom.parent().find("td.other");
			if(otherBtn.text()=="清"){
				otherBtn.html("<span class=\"a\">全</span>");
			}
			optionDom.removeClass("current");
			var selectedOptions = tempDetail.find("td.current");
			//该场次无选项显示默认信息
			if(selectedOptions.size() <= 0){
				$(tempShowTag).find("span.txt").show();
			}else{
				$(tempShowTag).find("span.txt").hide();
			}
			//该场次选项个数大于4显示统计信息否则显示具体选项
			if(selectedOptions.size() > _config.maxShowOption){
				$(tempShowTag).find("span.beat_sel").hide();
				$(tempShowTag).find("span.beat_many").show();
				$(tempShowTag).find("span.beat_many").html("已选<b>"+ selectedOptions.size() +"</b>项");
			}else{
				var optionHTML="";
				$(selectedOptions).each(function(option){
					ctxt = $(this).find(".a").html();
					ctxt = ctxt.replace("-",":");
					optionHTML+="<a href='javascript:;'>"+ctxt+"</a>"
				});
				$(tempShowTag ).find("span.beat_sel").html(optionHTML);
				$(tempShowTag).find("span.beat_sel").show();
				$(tempShowTag).find("span.beat_many").hide();
			}
		}else{
			optionDom=$(raceDom).find("div.beat_box a[option=\""+optionValue+"\"]");
			optionDom.removeClass("current");
		}
		refreshBetCache(matchKey);
		refreshBetItem();
    };      
    var refreshBetCache=function(matchKey){
    	var selectedOptions=$($.matchInfo(matchKey).dom).find(".current[option]");
		//将当前投注项选择情况记录到内存
		var nowAllOptions = null;
		$.each(selectedOptions,function(index,optionDom){
			if(!nowAllOptions){
				nowAllOptions = {};
			}
			var $optionDom=$(optionDom);
			var option = $optionDom.attr("option");
			var optionPrefix = option.split("_")[0];
			if(!nowAllOptions[optionPrefix]){
				nowAllOptions[optionPrefix] = [];
			}
			var award=$optionDom.find("em.pl").text();
			award=award.replace("↑","").replace("↓","")*1;
			var text=$optionDom.find(".s_show").html();
			nowAllOptions[optionPrefix].push({"option":option.split("_")[1],"award":award,"text":text});
		});
		$.options(matchKey,undefined,nowAllOptions);
    };
	//右侧投注篮绑定事件结束
	//高亮提醒
	var lightAttention=function(offset,height,width){
		var started=$("#attention").data("started") || false;
		if(started){
			return false;
		}
		$("#attention").height(height).width(width).css(offset);
		twinkle(3);
	}
	function twinkle(cnt,speed){
		speed=speed || 100;
		$("#attention").data("started",true);
		function start(){
			$("#attention").fadeIn(speed).fadeOut(speed,function(){
				cnt--;
				if(cnt>0){
//					setTimeout(start,speed);
					start();
				}else{
					$("#attention").data("started",false);
				}
			});
		};
		start();
//		setTimeout(start,speed);
	}
	
	////默认选中公开 flag"不公开"是否显示
	var optDefault=function(flag){
		var cll_radio=$("#publicStatus span.cllradio").find("a");
		//默认选中公开
		if(flag){
			$("#isHidden").removeClass("hidden");
			cll_radio.filter("[status=1]").addClass("hidden");
			cll_radio.filter("[status=2]").click();
			cll_radio.filter("[status=3]").removeClass("hidden");
			cll_radio.filter("[status=4]").removeClass("hidden");
		}else{
			$("#isHidden").addClass("hidden");
			cll_radio.addClass("hidden");
			cll_radio.filter("[status=1]").click();
		}
	}
	//提成选项点击事件
	$("#sportHmBox td").click(function(){
		if($("#result_step").hasClass("step_disable")){
			return false;
		}
		$("#sportHmBox td").removeClass("select");
		$(this).addClass("select");
	});
	//倍数金额变化
	$("#multiple").keyup(function(){
		var multiple=$(this).val();
		multiple=multiple.replace(/\D/g,"");
		$(this).val(multiple);
		
		var amount=$("#count").text()*2*multiple;
		$("#amount").text("￥"+amount);
		var awardCast=_awardCast;
		var minAward=awardCast[0]*2*multiple;
		var maxAward=awardCast[1]*2*multiple;
		if(_config.isBD){
			minAward=minAward*0.65;
			maxAward=maxAward*0.65;
		}
		
		$("#minAndMaxPrize").html("￥"+minAward.toFixed(2) + "~" + maxAward.toFixed(2));
		$("#founderAmount").keyup();
	});
	//合买认购金额与保底金额百分比变化
	$("#founderAmount").keyup(function(){
		var amount = $("#amount").text().substring(1)*1; 
		var val=$(this).val();
		val=val.replace(/\D/g,"")*1;
		
		if(amount==0){
			$(this).val(0);
			$(this).parent().next().html("(0%)");
			return ;
		}else if(val==0){
			val=$(this).data("defaultValue")*1;			
			$(this).val(val).data("changed",false);
		}
		if(val>amount){ 
			val=amount;
			$(this).val(val);
		}
		var rate=(val/amount*100).toFixed(2)+"%";
		$(this).parent().next().html("("+rate+")");
		$("#founderBdAmount").keyup();
	});
		//合买认购金额与保底金额百分比变化
	$("#founderBdAmount").keyup(function(){
		var amount = $("#amount").text().substring(1)*1; 
		var rg=$("#founderAmount").val()*1;
		var bd=amount-rg;
		var val=$(this).val();
		val=val.replace(/\D/g,"")*1;
		if(val==0||amount==0){
			$(this).val(0);
			$(this).parent().next().html("(0%)");
			return ;
		}
		if(val>bd){ 
			val=bd;
			$(this).val(val);
		}
		var rate=(val/amount*100).toFixed(2)+"%";
		$(this).parent().next().html("("+rate+")");
	});
	
	
	//修改投注篮数据
	var refreshBetItem=function(){
		var $itemTable=$("#itemTable");
		var _tbody=$itemTable.data("tbody");
		if(!_tbody){
			_tbody=$itemTable.children();
			$itemTable.data("tbody",_tbody); //缓存
		}
		//渲染右侧投注篮
		var betItemsHTML=[];
		var matchCount=0;
		var selectedItems=$.options();
		var matchKeys=[];
		for(var matchKey in selectedItems){
			if(selectedItems[matchKey]){
				matchKeys.push(matchKey);
				matchCount++;
			}
		}
		matchKeys.sort();
		var optionHtml=[];
		var headHtml=[];
		for(var i=0;i<matchKeys.length;i++){
			var matchKey = matchKeys[i];
			var allOptions = selectedItems[matchKey];
			var matchObj=$.matchInfo(matchKey);
			var sheDan=false;
			if(allOptions){
				var $tbody=_tbody.clone();
				$tbody.attr("matchKey",matchKey);
				var optCnt=0;//场次的投注选项个数
				for(var key in allOptions){
					if(key=="dan" && allOptions.dan){
						sheDan=true;
						continue;
					}
					var optArr=allOptions[key];
					optCnt+=optArr.length;
					for(var j=0;j<optArr.length;j++){
						var optObj=optArr[j];
						var s_result=optObj.text || _config.betOptions[key][optObj.option];//投注内容
						var s_sp=optObj.award;//sp
	     			    optionHtml[optionHtml.length]="<li class=\""+key+"\"><a href=\"javascript:;\" class=\"beatbox\" option=\""+key+"_"+optObj.option+"\"><span class=\"s_result\">"+s_result+"</span><span class=\"s_sp\">"+s_sp+"</span></a></li>";
					}
				}
				var showKey=matchObj.showKey;
				var matchTeam=matchObj.homeTeam+"VS"+matchObj.guestTeam;
				headHtml[0]="<div matchkey=\""+matchKey+"\" class=\"race\"><span class=\"oper\"><span class=\"count\"><em>&nbsp;&nbsp;"+optCnt+"&nbsp;&nbsp;</em></span><a class=\"iconfont\" name=\"clear\" href=\"javascript:;\">&#xe60c;</a></span><span class=\"subprime\"><span class=\"serial\">"+showKey+"</span><span class=\"against\">"+matchTeam+"</span></span><span class=\"hiding status_show\"><i class=\"arrow\"><s class=\"s1\"></s><s class=\"s2\"></s></i></span>";
				if(isPass){//过关才有胆
					var className="default";//未设胆
					if(sheDan){
						className="select";
					}
					headHtml[headHtml.length]="<span class=\"cllcheckbox setdan\"><a href=\"javascript:;\" class=\""+className+"\"><i></i><em>胆</em></a></span>";
				}
				headHtml[headHtml.length]="</div>";
				optionHtml[optionHtml.length]="<li class=\"addgame\"><a href=\"javascript:;\" class=\"iconfont\">&#xe663;</a></li>";
				$tbody.find("tr.team > td").html(headHtml.join(""));
				$tbody.find("ul.sel_ball").html(optionHtml.join(""));
				headHtml.length=0;
				optionHtml.length=0;//重置,回收内存
				betItemsHTML.push($tbody[0].outerHTML);
			}
		}
		
		if(matchCount>0){
			$("#downthrowItemDIV").show().prev().hide();
			$itemTable.html(betItemsHTML.join(""));
			$("#passType_step").removeClass("step_disable").find("div.nodata").hide();
			$("#passType_step").find("div.operate").show();
			betItemsHTML.length=0;
		}else{
			$itemTable.html("");
			$("#downthrowItemDIV ").hide().prev().show();
			$("#passType_step").addClass("step_disable").find("div.nodata").show();
			$("#passType_step").find("div.operate").hide();
		}
		$("#matchCount").text(matchCount);
		buildPassType();
	};
	refreshAllBetCache=function(){
		for(var i in $.options()){
			refreshBetCache(i,$.options()[i]);
		}
		//刷新预测
		refreshBetItem();
	}
	var refreshPassType=function(){
		var $passTypeDoms=getPassTypeDoms();
		var selectedPassTypes=[];
		$passTypeDoms.find("a.select").each(function(){
			selectedPassTypes.push($(this).attr("matchSize")*1);
		});
		$.passTypes(selectedPassTypes);
		if(selectedPassTypes.length>0){
			$("#result_step").removeClass("step_disable");
			$("#detail").show();
			$("#result_step").find("span.cllinput > input").removeClass("disable").addClass("default");
		}else{
			$("#detail").hide();
			$("#result_step").addClass("step_disable");
			$("#result_step").find("span.cllinput > input").removeClass("default").addClass("disable");
		}
		refreshBetInfo();
	}
	//重新构建过关方式
	var buildPassType = function() {
		var $passTypeDoms=getPassTypeDoms();
		var passTypeSize =1;
		var start=0;
		if(_config.isBD){
			start=0;
			passTypeSize=$.passTypeSize();
		}else{
			if(isPass){
				start=1;
				passTypeSize = $.passTypeSize();
			}else{
				start=0;
				passTypeSize = 1;
			}
		}
		var danCount = 0;
		var count = 0;
		var allOptions=$.options();
		for(var matchKey in allOptions){
			var option=allOptions[matchKey];
			if(option){
				if(option.dan){
					danCount++;
				}
				count++;
			}
		}
//		//构造投注篮过关方式
		if(count>1){
			$("#cqdy").show();
		}else{
			$("#cqdy").hide();
		}
		$passTypeDoms.hide();
		var lotType=_config.playType;
		for(var i=start;i<passTypeSize;i++){
			if(i+1>=danCount&&i<count){
				if(lotType=="25"){//胜负过关
					var matchSize=$passTypeDoms.eq(i).find("a").attr("matchSize")*1;
					if(_config.passTypeList["sfgg"]){
						var str=","+_config.passTypeList["sfgg"].toString()+",";
						if(str.indexOf(","+matchSize+",")>-1){
							$passTypeDoms.eq(i).show();
						}
					}
				}else{
					$passTypeDoms.eq(i).show();
				}
			}else{
				$passTypeDoms.eq(i).hide().find("a").attr("class","default");
			}
		}
		refreshPassType();
		floatDownthrowDIV_Base2($("#betting_box"));
	}
	var checkCQ=function(){
		var selectedItems=$.options();
		var map=new Map();
		for(var i in selectedItems){
			if(selectedItems[i]){
				for(var j in selectedItems[i]){
					if(j!="dan"){
						map.put(j,1)
					}
				}
			}
		}
		return $("#cqdy").is(":visible")&&map.size()>=2;
	};
	//刷新投注信息（注数，金额，奖金预测）
	var refreshBetInfo=function(){
		//判断是否可以点击去除单一
		var $cqdy=$("#cqdy");
		if($cqdy.length>0){
			if(!checkCQ()){
				$cqdy[0].className="disable";
				$.setCqdy(false);
			}else if($cqdy[0].className=="disable"){
				$cqdy[0].className="default";
			}
			$.setCqdy($cqdy[0].className=="select");
		}
		awardDetailYc();
		
	};
	if(typeof(awardDetailYc)=="undefined"){
		awardDetailYc=function(){
			var awardCast=$.minAndMaxPrize();
			_awardCast=awardCast;
			calcAward(awardCast)
		}
	}
	calcAward=function(awardCast){
		var count=$.betCount();
		var multiple=$("#multiple").val();
		var minAward=awardCast[0]*2*multiple;
		var maxAward=awardCast[1]*2*multiple;
		if(_config.isBD){
			minAward=minAward*0.65;
			maxAward=maxAward*0.65;
		}
		$("#count").html(count);
		var amount=count*2*multiple;
		$("#amount").text("￥"+amount);
		$("#minAndMaxPrize").html("￥"+minAward.toFixed(2) + "~" + maxAward.toFixed(2));
		$("#founderAmount").keyup();
		buildFilterBtn();
	}
	
	var getPassTypeDoms=function(){
		var $passTypeDoms=_config.passTypeDoms;
		if($passTypeDoms==""){
			$passTypeDoms=$("#passTypes").children();
			_config.passTypeDoms=$passTypeDoms;
		}
		return $passTypeDoms;
	}
	//购买事件
	$("#buyBtn").click(function(){
		if($("#result_step").hasClass("step_disable")){
			return false;
		}
		checkLogin(function(){
			var betForm=buildBetForm();
			if(betForm){
				try {
					checkBetForm(betForm);
				} catch (e) {
					if (typeof e == 'string')
						cll_alert(e);
					else
						cll_alert(e.message);
					buying=false;
					return;
				}
				//此处仅支持单一玩法，不支持混投
				cll_confirm(constructConfirm(betForm),function() {
					if (buying) {
						cll_alert("正在提交");
						return;
					}
					buying = true;
	
					var formVlaues=$(betForm).serializeArray();
					var request = $.ajax({
						 url: $(betForm).attr("action"),
						 type: "POST",
						 data: function(){
								var param={};
								for( var i=0,length=formVlaues.length;i<length;i++){
									param[formVlaues[i].name]=formVlaues[i].value;
								}
								return param;
							}(),
						 dataType: "json"
					});
					request.done(function(jsonObj) {
						buying=false;
						if(jsonObj.code=='4002'){//充值
							cll_confirm_to_charge(jsonObj.message,function (){ open("/uc/chargeInput.php");},null);
						}else{
							cll_alert(jsonObj.message);	
						}
					});
					request.fail(function(jqXHR, textStatus) {
						cll_alert('网络故障，投注失败！');
						buying=false;
					});
				},function(){buying = false;return;});
			}
		})
	});
	
	// 免费保存方案事件
	$("#saveBtn").click(function(){
		if ($("#result_step").hasClass("step_disable")) {
			return;
		}
		
		checkLogin(function(){
			if (buying) {
				return;
			}
			buying = true;
			
			var betForm = buildBetForm();
			if (betForm) {
				try {
					checkBetForm(betForm);
				} catch (e) {
					if (typeof e == 'string')
						cll_alert(e);
					else
						cll_alert(e.message);
					buying = false;
					return;
				}
                
				var formVlaues = $(betForm).serializeArray();
				var request = $.ajax({
					 url: "/trade/dg.php?tradeType=1",
					 type: "POST",
					 data: function(){
							var param={};
							for( var i=0,length=formVlaues.length;i<length;i++){
								param[formVlaues[i].name]=formVlaues[i].value;
							}
							return param;
						}(),
					 dataType: "json"
				});
				request.done(function(jsonObj) {
					buying = false;
					
					if (jsonObj.code == '0') {
						cll_confirm_defined('温馨提示', '方案内容已保存，您可以在<a href="/uc/planList.php?action=savedPlanList" target="_blank" onclick="closeDragWindow(confirmWindowDIV)">已保存的方案</a>中查看方案详情', '查看详情', '知道了',
							function(){
			    		        open("/member/savedPlanShow.php?planNo=" + jsonObj.no);
			    	        },
		    			    function(){}
				    	);
					} else {
						cll_alert(jsonObj.message);
					}
				});
				request.fail(function(jqXHR, textStatus) {
					cll_alert('提交失败');
					buying = false;
				});
			} else {
				buying = false;
			}
		});
	});
	
	// 积分投注事件
	$("#scoreBetBtn").click(function(){
		if ($("#result_step").hasClass("step_disable")) {
			return;
		}
		
		checkLogin(function(){
			if (buying) {
				return;
			}
			buying = true;
			
			var betForm = buildBetForm();
			if (betForm) {
				try {
					checkBetForm(betForm);
				} catch (e) {
					if (typeof e == 'string')
						cll_alert(e);
					else
						cll_alert(e.message);
					buying = false;
					return;
				}
                
				var confirmMsg = constructConfirm(betForm);
				confirmMsg = confirmMsg.replace(/元/g, "积分");
				
				cll_confirm(confirmMsg, function(){
					var formVlaues = $(betForm).serializeArray();
					var request = $.ajax({
						 url: "/trade/dg.php?tradeType=2",
						 type: "POST",
						 data: function(){
								var param={};
								for( var i=0,length=formVlaues.length;i<length;i++){
									param[formVlaues[i].name]=formVlaues[i].value;
								}
								return param;
							}(),
						 dataType: "json"
					});
					request.done(function(jsonObj) {
						buying = false;
						
						if (jsonObj.code == '0') {
							cll_alert('方案已提交');
							checkLogin_scoreBet(); // 刷新当前积分
						} else {
							cll_alert(jsonObj.message);
						}
					});
					request.fail(function(jqXHR, textStatus) {
						cll_alert('提交失败');
						buying = false;
					});
				}, function(){buying = false;});
			} else {
				buying = false;
			}
		});
	});
	
	var checkBetForm=function(betForm) {
		var amount=$(betForm).find("input[name='amount']").val();
		var multiple=$(betForm).find("input[name='multiple']").val();
		var betCount=$(betForm).find("input[name='betCount']").val();
		var count = 0;
		for(var matchKey in $.options()){
			if($.options(matchKey)){
				count++;
			}
		}
		// 检查过关方式是否选中
		var passTypeSize =$.passTypes().length;
		if (passTypeSize == 0) {
			throw "请选择过关方式";
		}
		
		// 检查倍数是否为数字
		if (!multiple.isPositiveNumber()) {
			throw "倍数必须为正整数！";
		}
		// 检查倍数是否大于0
		if ( multiple <= 0) {
			throw "倍投数数值必须大于0";
		}
		if (amount <= 0) {
			throw "投注金额必须大于0";
		}
		if (betCount <= 0) {
			throw "投注注数必须大于0";
		}
		if(betCount>10000){
			throw  "复式方案注数最大10000注，如大于10000注可选择单式上传";
		}
		if($("#hmbtn > a").hasClass("select")){
			if (amount > 2000000) {
				throw "投注金额最大不超过200万";
			}
			var val = $("#founderAmount").val();
			if(!val.isPositiveNumber()){
				throw "认购数必须为正整数！";
			}
			if(!val || ''== val || val<1 ){
				throw "发起人必须认购1元以上";
			}
			if(Number(val)>Number(amount)){
				throw "认购金额不能大于方案金额";
			}
			var bVal = $("#founderBdAmount").val();
			if(!bVal.isPositiveNumber()){
				throw "保底数必须为正整数！";
			}
			if(!bVal || ''==bVal || Number(bVal)+Number(val)>Number(amount)){
					throw "保底金额和认购金额之和必须小于方案金额";
			}
		}
	};
	var filte=function(){
		var flag=$("#stopCheck")[0].checked;//true/false 是否影响所有赛事
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
		var projects=[];
		$("#projectFilte input:checked").each(function(){
			projects.push($(this).val());
		});
		$.each($.matchInfo(),function(matchKey,matchInfo){
			var dom=$(matchInfo.dom);
			if(!flag){
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
				if(!isHide && $("#rqFilte").length>0){
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
				//项目判断
				if(!isHide && $("#projectFilte").length>0){
					var gameHideFlag=true;
					for(var i=0;i<projects.length;i++){
						if(dom.attr("project")==projects[i]){
							gameHideFlag=false;
							break;
						}
					}
					isHide=gameHideFlag;
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
		
		var race_dates=$("b.time");
		$("tbody.race_date").each(function(){
			var len=$(this).nextUntil("tbody.race_date").filter(":visible").length;
			if(len==0){
				$(this).data("status",true);
				$(this).find("a.hiding").html("显示");
			}else{
				$(this).data("status",false);
				$(this).find("a.hiding").html("隐藏");
			}
		})
		var flag=true;
		for(var i=0;i<gameNames.length;i++){
			var val=","+gameNames[i]+",";
			if(gameNameStr.indexOf(val)>-1){
			}else{
				flag=false;
				break;
			}
		}
		if(flag){
			$("#worldcupGames").addClass("wc_select");
		}else{
			$("#worldcupGames").removeClass("wc_select");
		}
	};
	$("#filter input").click(filte);
	$("#recover,#recover_filte").click(function(){
		$("#filter input").attr("checked","checked");
		$.each($.matchInfo(),function(matchKey,matchInfo){
			matchInfo.del=false;
		});
		$("#raceTable >tbody.race_date").each(function(){
			$(this).data("status",false);
			$(this).find("a.hiding").html("隐藏");
		})
		filte();
	});
	$("#filter div.oper_line a").click(function(){
		var optionValue=$(this).attr("option");
		switch(optionValue){
			case "1":
				$(this).parent().parent().find("input").attr("checked",true);
				break;
			case "2":
				$(this).parent().parent().find("input").each(function(){
					$(this).attr("checked",!$(this).attr("checked"));
				});
				break;
			case "0":
				$(this).parent().parent().find("input").attr("checked",false);
				break;
			default:
				;
		};
		filte();
	});
	$("#filterSwitch").click(function(){
		var status=$(this).data("status");
		if(status){
			$("#filterSelBox").attr("class","filter_sel f_def");
			$(this).data("status",false);
		}else{
			$("#filterSelBox").attr("class","filter_sel f_hover");
			$(this).data("status",true);
		}
	});
	$("#filterClose").click(function(){
		$("#filterSwitch").data("status",false);
		$('#filterSelBox').attr("class","filter_sel f_def");
	
	});
	
	function divMove(){
		var divTop = 0;
		var divTop2 = 0;
		var div_l = $("#float_div");
		var div_t=$("#betting_box");
		div_l.css({"position":"relative"});
		div_t.css({"position":"relative"});
		if(isIE6()){
			try{
				divTop2=div_t.offset().top;
			}catch(e){}
		}else{
			try{
				divTop = div_l.offset().top;
				divTop2=div_t.offset().top;
			}catch(e){}
		}
		function floatDownthrowDIV(){
			try{
				floatDownthrowDIV_Base(div_l,divTop,"");
				floatDownthrowDIV_Base2(div_t);
			}catch(e){}
		}
		$(window).unbind().bind("scroll",function(){
			floatDownthrowDIV()
		}).bind("resize",function(){
			floatDownthrowDIV();
		});
	}
	function floatDownthrowDIV_Base(div,divtop,divleft){
		var top = $(document).scrollTop();
		if(isIE6()){
			var t = div.offset().top - top;
			if(t <= 0){
				div.css({"position":"absolute"})
				div.css({top:top+'px'});
			}else{
				div.css({top:''});
				div.css({"position":"relative"});
			}
		}else{
			if(divtop < top){
				div.css({top:"0px",position:"fixed"});
			}else{
				div.css({top:"",position:"relative"});
			}
		}
		return;
	}
	var divtop=$("#betting_box").offset().top;
	function floatDownthrowDIV_Base2(div){
		var lastTop=$("#raceTable").offset().top+$("#raceTable").height();//最终位置
		var top = $(document).scrollTop();//滚动条位置
		var selfHeight=div.height();
		var startTop=divtop+selfHeight;//div原来的位置
		if(startTop<top){ 
			var tmp=$("#float_div").offset().top+$(window).height();
			tmp=tmp>lastTop?lastTop:tmp;
			tmp=tmp-selfHeight;
			tmp=tmp>lastTop?lastTop:tmp;
			div.offset({top:tmp})
		
		}else{
			div.offset({top:divtop})
		}
		return false;
	}

	$("#worldcupGames").click(function(){
		var flag=true;
		if($(this).hasClass("wc_select")){
			flag=false;
		}
		$("#gameNameFilte input").each(function(){
			var val=","+$(this).val()+",";
			if(gameNameStr.indexOf(val)>-1){
				this.checked=true;
			}else{
				this.checked=!flag;
			}
		});
		filte();
	});
	function getUrlParam(name){
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r!=null) {
			return unescape(r[2]); 
		}
		return null;
	} 
	
	//外部跳转选中
	function init_select(){
		var outMatch=getUrlParam("match");
		if(outMatch){
			var infoArr=outMatch.split("|"); //matchKey|option_betMoney|option_betMoney|...
			if(infoArr.length>=2){
				var matchKey=infoArr[0];
				var matchInfo=$.matchInfo(matchKey);
				if( matchInfo){
					if(matchInfo.dom){
						var $dom=$(matchInfo.dom);
						for(var i=1; i < infoArr.length;i++){
							var option=infoArr[i];
							var pt=option.split("_")[0];
							var showTag=$dom.find("[name='"+pt+"_show_tag']");
							if(showTag.length>0 && !showTag.hasClass("show_current")){
								showTag.click();							
							}
							$dom.find("[option='"+option+"']").click();
						}//end for
					}//end if
				}//end if
			}//end if
		}//end if
	}//end init_select
	
	divMove();
	init_select();
});

