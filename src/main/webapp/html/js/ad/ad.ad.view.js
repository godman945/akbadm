$(document).ready(function(){

	if($("#adGroupSeq").val() != "") {
    	findTableView();
    }

	$.ajax({
		url : "AdCategoryNew.html",
		type : "post",
		dataType : "json",
		async: false,
		success : function(respone) {
//			console.log(respone + ">>>>>" + status + ">>>>>>>success");
			obj = JSON.parse(respone);
		},
		error : function() {
//			console.log(">>>>>>>error");
		}	
	});
	
	disabledController("adCategory",false);
	findAdCategorySelect("adCategory");
	
	$("[id*=adCategory]").change(function(){
		categoryCode();
	});
	
	$("[type=reset]").click(function(){
		$("[id*=adCategory_]").remove();
		$("#adCategoryCode").val("");
	});
});
$(document).ajaxStart(function(){
	var message = "";
	if($("#illegalize").val() == "" || $("#illegalize").val()== undefined){
		message = "查詢中...";
	}else{
		
		message = "下架中 請耐心等待..."
	}
    $.blockUI({
    	message: "<b><font size=5>"+message+"</font></b>",
        css: {
            border: '3px solid #aaa',
            padding: '15px',
            backgroundColor: '#fff',
            '-webkit-border-radius': '10px',
            '-moz-border-radius': '10px',
            opacity: .9,
            textAlign:      'center',
            cursor:         'wait',
        }
    });
}).ajaxStop($.unblockUI);

var changeSelect = false;
function findTableView(){
	
	var adclkDevice = $("#adclkDevice").val();
	var date = $("#IT_dateRange").val().split("~");
	var startDate = date[0];
	var endDate = date[1];
	var dateType = $("#dateType").val();
	var userAccount = $("#userAccount").val();
	var searchAdStatus = $("#searchAdStatus").val();
	var searchType = $("#searchType").val();
	var adPvclkDevice = $("#adPvclkDevice").val();
	var keyword = $("#keyword").val();
	var pageNo = $("#pageNo").val();
	var pageSize = $("#pageSize").val();
	var adGroupSeq = $("#adGroupSeq").val();
	var adSeq = $("#adSeq").val();
	var adCategoryCode = $("#adCategoryCode").val();
	
	
	$("#adPvclkDevice").change(function(){
		changeSelect = true;
	});
	
	$.ajax({
		url: "adAdViewTable.html",
		data:{
			 "startDate": startDate,
			 "endDate": endDate,
			 "dateType": dateType,
			 "userAccount":userAccount,
			 "searchAdStatus":searchAdStatus,
			 "searchType": searchType,
			 "keyword": keyword,
			 "adPvclkDevice": adPvclkDevice,
			 "pageNo": pageNo,
			 "pageSize": pageSize,
			 "adGroupSeq": adGroupSeq,
			 "adSeq": adSeq,
			 "adclkDevice":adclkDevice,
			 "changeSelect":changeSelect,
			 "adCategoryCode":adCategoryCode
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#tableList").html(response);	
			page();
			 tableSorter();
		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
	
}

function Illegalize(){
	var adAdSeq = [];
	var dataFlag = "";
	var check = false;
	$('input[type=checkbox]').each(function () {
		if($(this).attr('checked') && $("#illegalize").val()!=""){
			adAdSeq.push(this.value);
			check = true;
		}
		if($(this).attr('checked') && $("#illegalize").val()== ""){
			alert("請填寫下架原因!!");
			return false;
		}
	});
	if(!check && $("#illegalize").val()!= "" ){
		alert("請勾選要下架的廣告");
	}
	
	for(var i=0;i<adAdSeq.length;i++){
		if(dataFlag == adAdSeq[i]){
			delete adAdSeq[1];
		}
		dataFlag = adAdSeq[i];
	}
	
	if(adAdSeq.length > 0 && $("#illegalize").val() != ""){
		$.ajax({
			url: "illegalizeAd.html",
			data:{
				 "adAdSeq" : JSON.stringify(adAdSeq),
				 "illegalize" : $("#illegalize").val()
			},
			type:"post",
			dataType:"html",
			success:function(response, status){
				findTableView();
			},
			error: function(xtl) {
//				alert("系統繁忙，請稍後再試！");
			}
		});
		
		
		
		
		
	}
}

function checkAll() {
	$('input[type=checkbox]').each(function () {
		($(this).attr("checked",true));
	});
}

function goKW(adGroupSeq) {
	alert($("#searchAdStatus").val());
	var dateType = $("#dateType").val();
	var date = $("#IT_dateRange").val().split("~");
	var adStartDate = date[0];
	var adEndDate = date[1];
	var userAccount = $("#userAccount").val();
	var searchAdStatus = $("#searchAdStatus").val();
	var searchType = $("#searchType").val();
	var adPvclkDevice = $("#adPvclkDevice").val();
	var adclkDevice = $("#adclkDevice").val();
	var url = "adKeywordView.html?adGroupSeq=" + adGroupSeq + "&adStartDate=" + adStartDate + "&adEndDate=" + adEndDate + "&dateType=" + dateType+ "&adclkDevice=" + adclkDevice;

	if(userAccount != "")		url += "&userAccount=" + userAccount;
	if(searchAdStatus != "")	url += "&searchAdStatus=" + searchAdStatus;
	if(searchType != "0")		url += "&searchType=" + searchType;
	if(adPvclkDevice != "")		url += "&adKeywordPvclkDevice=" + adPvclkDevice;
	location.href = url;
}

function tableSorter(){
	
	$("#tableView").tablesorter({
		headers:{
			0:{sorter:false},
			6:{sorter:false}
		}
	});
}

function preview(img) {
    $.fancybox({
        'href':img,
        'showCloseButton' :true,
        'autoSize':true,
        'autoHeight':true,
        'autoScale':true,
        'transitionIn':'none',
        'transitionOut':'none',
        'padding':0,
        'overlayOpacity':.75,
        'overlayColor':'#fff',
        'scrolling':'no'
    });
}

function preViewHtml5(width,height,imgSrc,realUrl){
	
	 $.fancybox(
			 '<div style="position:absolute;z-index:10;border:0px;background:none;width:' + width + 'px;height:' + height + 'px;">' + 
			 '<a href="' + realUrl + '" target="_blank" style="display:block;width:' + width + 'px;height:' + height + 'px;background-color:transparent;"><img src="html/img/blank.gif" style="width:' + width + 'px;height:' + height + 'px;border:0px;"></a>'
			 + '</div>'
			 + '<iframe src="' + imgSrc + '" width="' + width + '" height="' + height + '"  allowtransparency="true" frameborder="0" scrolling="no" ></iframe>',
	    		{
	    			'autoDimensions'	: false,
	    			'width'         	: width,
	    			'height'        	: height,
	    			'autoSize'			: true,
	    			'autoHeight'		: true,
	    			'autoScale'			: false,
	    			'transitionIn'		: 'none',
	    			'transitionOut'		: 'none',
	    			'padding'			: 0,
	    			'overlayOpacity'    : .75,
	    			'overlayColor'      : '#fff',
	    			'scrolling'			: 'no'
	    		}
	    );
}

function disabledController(componentId,selectFlag){
	for(var i=0;i<$('#'+componentId).parent().children().length;i++){
		$('#'+componentId).parent().children()[i].disabled = selectFlag;
	}
}

//全選控制
function selectAll_new(obj, chkName, selectName, rejectReason) {
	if (obj.checked) {
		for(var i=0;i<$(".adCategory").parent().children().length;i++){
			findAdCategorySelect($(".adCategory").parent().children()[i].id);
			$("#"+$(".adCategory").parent().children()[i].id).attr("disabled", false);
		}
		 $("input[name=adSeqs]").each(function() {
			 $(this).attr("checked", true);
	        });
		 
		 $("input[name=rejectReason]").each(function() {
			 $(this).attr("disabled", false);
	        });
		 
	}else{
		for(i=0;i<$(".adCategory").parent().children().length;i++){
			$("#"+$(".adCategory").parent().children()[i].id).attr("disabled", true);
		}
		 $("input[name=adSeqs]").each(function() {
			 $(this).attr("checked", false);
	        });
		 $("input[name=rejectReason]").each(function() {
			 $(this).attr("disabled", true);
	     });
	}
}

var obj= null;
function findAdCategorySelect(componentId) {
	appendAdCategorySelectUI(componentId);
}


function appendAdCategorySelectUI(componentId) {
	if ($("#" + componentId).children().length == 1) {
		for(var keys in obj){
			$("#" + componentId).append('<option value='+ keys+'>' + keys + '</option>');
		}
	}
}

//判斷階層 
function getCategoryLevel(adCategorySelectId,adCategoryGroup,adCategorySelect){
	var level=0;
	var groupId = null;
	var childrenFlag = false;
	for(var i=0;i< obj[adCategoryGroup].length; i++){
		if(obj[adCategoryGroup][i].name == adCategorySelect){
			level  = parseInt($("#"+adCategorySelectId).index())+parseInt("1");
			if(obj[adCategoryGroup][i].level == level ){
				groupId = obj[adCategoryGroup][i].id;
			}
		}
		
		if( obj[adCategoryGroup][i].name == adCategorySelect){
			for(var j=0;j< obj[adCategoryGroup].length; j++){
				if(obj[adCategoryGroup][i].id == obj[adCategoryGroup][j].parentId && obj[adCategoryGroup][i].level == level){
					childrenFlag = true;
				}
			}
		}
	}
	return {level: level, groupId: groupId,childrenFlag: childrenFlag};
}

//更改選項內容
var categoryLevel = null;


function changeCategoryItems(adCategorySelectId){
	
	var groupId =  $("#"+adCategorySelectId).parent().children()[0].id;
	var adCategoryGroup = $("#"+groupId+" option:selected").text();
	var adCategorySelect = $("#"+adCategorySelectId+" option:selected").text();
	if(adCategorySelect == "全部廣告類別"){
		deleteSelectUI(adCategorySelectId,level);
		return;
	}
	var categoryLevel = getCategoryLevel(adCategorySelectId,adCategoryGroup,adCategorySelect);
	var level =  categoryLevel.level;
	var groupId = categoryLevel.groupId;
	var childrenFlag = categoryLevel.childrenFlag;
	appendCategoryItems(adCategorySelectId,adCategoryGroup,level,groupId,childrenFlag);
}

//更改Partner選項內容
function changeCategoryPartnerItems(adCategorySelectId,partnerName){
	var groupId =  $("#"+adCategorySelectId).parent().children()[0].id;
	var adCategoryGroup = $("#"+groupId+" option:selected").text();
	var adCategorySelect = $("#"+adCategorySelectId+" option:selected").text();
	var categoryLevel = getCategoryLevel(adCategorySelectId,adCategoryGroup,adCategorySelect);
	var level =  categoryLevel.level;
	var groupId = categoryLevel.groupId;
	var childrenFlag = categoryLevel.childrenFlag;
	appendCategoryItems(adCategorySelectId,adCategoryGroup,level,groupId,childrenFlag);
	
}


function appendCategoryItems(adCategorySelectId,adCategoryGroup,level,groupId,childrenFlag){
	var partnerId = null;
	if(level == 0 ){
		deleteSelectUI(adCategorySelectId,level);
		return false;
	}
	select = true;
	for(var i = 0; i< obj[adCategoryGroup].length; i++){
		if(childrenFlag && select){
			deleteSelectUI(adCategorySelectId,level);
			$("#" + adCategorySelectId).parent().append('<select id=\''+adCategorySelectId+'_'+level+ '\' onchange="changeCategoryItems(\''+adCategorySelectId+'_'+level+'\');" ><option value="">-請選擇-</option></select>');
			select=false;
			}
		if(groupId == obj[adCategoryGroup][i].parentId && childrenFlag){
			$("#"+adCategorySelectId+'_'+level).append('<option value='+obj[adCategoryGroup][i].name+' >'+obj[adCategoryGroup][i].name+'</option>');
		}

		if( obj[adCategoryGroup][i].partner_id != null && obj[adCategoryGroup][i].name == $("#"+adCategorySelectId+" option:selected").text()){
			partnerId = obj[adCategoryGroup][i].partner_id;
		}
	}
	
	var partnerName = null;
	if (!childrenFlag && partnerId!=null) {
		for ( var keys in obj) {
			for (var i = 0; i < obj[keys].length; i++) {
				if (obj[keys][i].id == partnerId) {
					partnerName = obj[keys][i].name;
				}
			}
		}
		level = 2;
		$("#" + adCategorySelectId).parent().append('<select id=\'adPartner_'+partnerId+ '\' onchange="changeCategoryPartnerItems(\'adPartner_'+partnerId+',\''+partnerName+'\);" ><option value="">-請選擇-</option></select>');
		for (var i = 0; i < obj[partnerName].length; i++) {
			if(obj[partnerName][i].level ==2){
				$("#adPartner_"+partnerId).append('<option value='+obj[partnerName][i].name+' >'+obj[partnerName][i].name+'</option>');
			}
		}
	}
	
	if (!childrenFlag && partnerId==null) {
		deleteSelectUI(adCategorySelectId,level);
	}
	
	$(document).ready(function(){
		$("[id*=adCategory]").change(function(){
			categoryCode();
		});
	});
}

//選項規零
function deleteSelectUI(adCategorySelectId,level){
	if(level == null){
		for(var i = $('#'+adCategorySelectId).parent().children().length; i>0; i--){
			$($('#'+adCategorySelectId).parent().children()[i]).remove();
		}
	}else{
		if(level>0){
			for(var i = $('#'+adCategorySelectId).parent().children().length; i>level-1; i--){
				$($('#'+adCategorySelectId).parent().children()[i]).remove();
			}
		}else{
			for(var i = $('#'+adCategorySelectId).parent().children().length; i>$("#"+adCategorySelectId).index(); i--){
				$($('#'+adCategorySelectId).parent().children()[i]).remove();
			}
		}
	}
} 

//code寫入隱藏欄位
function categoryCode(){
		var trObj = $("#divAdCategoryCode");
		if($(trObj).children().length > 1){
			var key = $(trObj).children()[0].value;
			var length = $(trObj).children().length;
			if($("#"+$(trObj).children()[length-1].id+" option:selected").text() == "全部廣告類別"){
				length = length - 1;
			}
			var objId = null;
			var code = null;
			for(var i = 0; i < length ; i++){
				var id = $(trObj).children()[i].id;
				for(var j = 0; j < obj[key].length; j++){
					if( obj[key][j].name == $("#"+id+" option:selected").text()){
						if(obj[key][j].level == "1"){
							objId = obj[key][j].id;
							code = obj[key][j].code;
						}else{
							if(obj[key][j].parentId == objId){
								objId = obj[key][j].id;
								code = obj[key][j].code;
							}
						}
					}
				}
			}
			$("#adCategoryCode").attr("value",code);
		}else {
			$("#adCategoryCode").removeAttr("value",code);
		}
}

//下載報表
function doDownlaod() {
	var adclkDevice = $("#adclkDevice").val();
	var date = $("#IT_dateRange").val().split("~");
	var startDate = date[0];
	var endDate = date[1];
	var dateType = $("#dateType").val();
	var userAccount = $("#userAccount").val();
	var searchAdStatus = $("#searchAdStatus").val();
	var searchType = $("#searchType").val();
	var adPvclkDevice = $("#adPvclkDevice").val();
	var keyword = $("#keyword").val();
	var adGroupSeq = $("#adGroupSeq").val();
	var adSeq = $("#adSeq").val();
	var adCategoryCode = $("#adCategoryCode").val();
	
	var url = "adDownload.html?startDate=" + startDate + "&endDate=" + endDate;
	
	if(dateType != "")		url += "&dateType=" + dateType;
	if(userAccount != "")		url += "&userAccount=" + userAccount;
	if(searchAdStatus != "")		url += "&searchAdStatus=" + searchAdStatus;
	if(searchType != "")		url += "&searchType=" + searchType;
	if(keyword != "")		url += "&keyword=" + keyword;
	if(adPvclkDevice != "")		url += "&adPvclkDevice=" + adPvclkDevice;
	if(adGroupSeq != "")		url += "&adGroupSeq=" + adGroupSeq;
	if(adSeq != "")		url += "&adSeq=" + adSeq;
	if(adclkDevice != "")		url += "&adclkDevice=" + adclkDevice;
	if(changeSelect != "")		url += "&changeSelect=" + changeSelect;
	if(adCategoryCode != "")		url += "&adCategoryCode=" + adCategoryCode;
	
	window.location = url;
}