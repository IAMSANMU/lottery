var isInit = false; // 是否为初始化


function setTransType(type){
    setSingleSelect("transType", type);
    formSubmit();
}


/**
 * 表单提交
 */
function formSubmit(){
	if (!isInit) {
		$("#form").submit();
	}
}

/**
 * 设置日期类型
 */
function setDateType(type){
	setSingleSelect("dateType", type);
    
    if (type == 4) {
    	$("#selectDate").css("display", "");
    } else {
    	$("#selectDate").css("display", "none");
    }
    
    if (!isInit) {
    	setStartAndEndDate(type);
    }
    if (type != 4) {
    	formSubmit();
    }
}

/**
 * 设置开始日期和结束日期
 */
function setStartAndEndDate(type){
	var startDate = "";
    var nowDate = new Date();
    
    if (type == 1) {
    	startDate = getStartDate(nowDate, 7);
	} else if (type == 2) {
		startDate = getStartDate(nowDate, 30);
	} else if (type == 3) {
		startDate = getStartDate(nowDate, 180);
	} else if (type == 4) {
		startDate = getStartDate(nowDate, 90);
	}
    
    $("#startDateStr").val(startDate);
	$("#endDateStr").val(getDateString(nowDate));
}

/**
 * 获取当前日期days天之前的日期
 */
function getStartDate(nowDate, days){
	return getDateString(new Date(nowDate.getTime() - days*24*3600*1000));
}

/**
 * 获取日期对象yyyy-MM-dd格式的字符串
 */
function getDateString(dateObj){
	var year = dateObj.getFullYear();
	var month = formatNum(dateObj.getMonth() + 1);
	var date = formatNum(dateObj.getDate());
	
	return year + "-" + month + "-" + date;
}

/**
 * 1-9在前面加0
 */
function formatNum(num){
	if (num >= 1 && num <= 9) {
		return '0' + num;
	}
	
	return num;
}

/**
 * 搜索条件单选设置
 */
function setSingleSelect(idPrefix, value){
	var obj = $("#" + idPrefix + "_" + value);
	obj.attr("class", "select");
	obj.parent().siblings().find("a").attr("class", "default");
	
	$("#" + idPrefix).val(value);
}

/**
 * 隐藏或显示DIV
 */
function hideOrShowDiv(id) {
	var obj = $("#" + id);
	var display = obj.css("display");
	
	if (display == "none") {
		obj.css("display", "block");
	} else {
		obj.css("display", "none");
    }
}

