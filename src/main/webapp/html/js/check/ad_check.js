$(document).ready(function(){
    //開始日期 
    $("#startDate").datepicker({
	    showOn: "button",
	    buttonImage: "html/img/icon_cal.gif",
	    buttonImageOnly: true,
	    changeMonth: false,
	    changeYear: false,
        dateFormat: "yy-mm-dd"
    });

    //結束日期    
    $("#endDate").datepicker({
	    showOn: "button",
	    buttonImage: "html/img/icon_cal.gif",
	    buttonImageOnly: true,
	    changeMonth: false,
	    changeYear: false,
        dateFormat: "yy-mm-dd"
    });
    page();
	$.ajax({
	url : "AdCategoryNew.html",
	type : "post",
	dataType : "json",
	success : function(respone) {
//		console.log(respone + ">>>>>" + status + ">>>>>>>success");
		obj = JSON.parse(respone);
		var selectId = null;
		$("input[name=adCategoryCodeMapping]").each(function() {
			
			var selectIdjArray = [$(this).parent().children()[0].id,$(this).parent().parent().next().children().children()[0].id,$(this).parent().parent().next().next().children().children()[0].id];
			var codeArray = this.value.split(";");
			
			for(var k=0;k<selectIdjArray.length;k++){
				if(k < codeArray.length){
					for(var keys in obj){
						for(var j=0;j<obj[keys].length;j++){
							if(codeArray[k] == obj[keys][j].code){
								var level = obj[keys][j].level;
								//1.創建一階
								selectId = selectIdjArray[k];
								appendAdCategorySelectUI2(selectId);
								for(var j=0;j<obj[keys].length;j++){
									if(obj[keys][j].level==1){
										$("#"+selectId).val(obj[keys][j].name);
										var categoryLevel = getCategoryLevel(selectId,keys,obj[keys][j].name);
										var level =  categoryLevel.level;
										var groupId = categoryLevel.groupId;
										var childrenFlag = categoryLevel.childrenFlag;
										appendCategoryItems(selectId,keys,level,groupId,childrenFlag);
									}
									//判斷二階Code
									if(obj[keys][j].level == 2){
										if((obj[keys][j].code).slice(0,8) == (codeArray[k]).slice(0,8)){
											var select = selectId + "_1";
											$("#"+select).val(obj[keys][j].name);
											var categoryLevel = getCategoryLevel(select,keys,obj[keys][j].name);
											var level =  categoryLevel.level;
											var groupId = categoryLevel.groupId;
											var childrenFlag = categoryLevel.childrenFlag;
											appendCategoryItems(select,keys,level,groupId,childrenFlag);
										}
									}
									//判斷三階Code
									if(obj[keys][j].level == 3){
										if((obj[keys][j].code).slice(0,12) == (codeArray[k]).slice(0,12)){
											var select = selectId + "_1_2";
											$("#"+select).val(obj[keys][j].name);
											var categoryLevel = getCategoryLevel(select,keys,obj[keys][j].name);
											var level =  categoryLevel.level;
											var groupId = categoryLevel.groupId;
											var childrenFlag = categoryLevel.childrenFlag;
											appendCategoryItems(select,keys,level,groupId,childrenFlag);
										}
									}
									//判斷四階Code
									if(obj[keys][j].level == 4){
										if( obj[keys][j].code == codeArray[k]){
											var select = selectId + "_1_2_3";
											$("#"+select).val(obj[keys][j].name);
											var categoryLevel = getCategoryLevel(select,keys,obj[keys][j].name);
											var level =  categoryLevel.level;
											var groupId = categoryLevel.groupId;
											var childrenFlag = categoryLevel.childrenFlag;
											appendCategoryItems(select,keys,level,groupId,childrenFlag);
										}
									}
								}
								if(k ==0){
									selectAll_new($("#"+selectId).parent().parent().children()[2], "adSeqs", "adCategory", "rejectReason");
								}
							}
						}
					}
				}
			}
			
			$("[id*=adCategory_ad_]").attr("disabled", true);
			/*for(var keys in obj){
				for(var j=0;j<obj[keys].length;j++){
					if(this.value == obj[keys][j].code){
						var level = obj[keys][j].level;
						//1.創建一階
						selectId = $(this).parent().children()[0].id;
						appendAdCategorySelectUI2($(this).parent().children()[0].id);
						for(var j=0;j<obj[keys].length;j++){
							if(obj[keys][j].level==1){
								$("#"+$(this).parent().children()[0].id).val(obj[keys][j].name);
								var categoryLevel = getCategoryLevel($(this).parent().children()[0].id,keys,obj[keys][j].name);
								var level =  categoryLevel.level;
								var groupId = categoryLevel.groupId;
								var childrenFlag = categoryLevel.childrenFlag;
								appendCategoryItems($(this).parent().children()[0].id,keys,level,groupId,childrenFlag);
							}
							//判斷二階Code
							if(obj[keys][j].level == 2){
								if((obj[keys][j].code).slice(0,8) == (this.value).slice(0,8)){
									var select = $("#"+selectId).parent().children()[1].id;
									$("#"+select).val(obj[keys][j].name);
									var categoryLevel = getCategoryLevel(select,keys,obj[keys][j].name);
									var level =  categoryLevel.level;
									var groupId = categoryLevel.groupId;
									var childrenFlag = categoryLevel.childrenFlag;
									appendCategoryItems(select,keys,level,groupId,childrenFlag);
								}
							}
							//判斷三階Code
							if(obj[keys][j].level == 3){
								if((obj[keys][j].code).slice(0,12) == (this.value).slice(0,12)){
									var select = $("#"+selectId).parent().children()[2].id;
									$("#"+select).val(obj[keys][j].name);
									var categoryLevel = getCategoryLevel(select,keys,obj[keys][j].name);
									var level =  categoryLevel.level;
									var groupId = categoryLevel.groupId;
									var childrenFlag = categoryLevel.childrenFlag;
									appendCategoryItems(select,keys,level,groupId,childrenFlag);
								}
							}
							//判斷四階Code
							if(obj[keys][j].level == 4){
								if( obj[keys][j].code == this.value){
									var select = $("#"+selectId).parent().children()[3].id;
									$("#"+select).val(obj[keys][j].name);
									var categoryLevel = getCategoryLevel(select,keys,obj[keys][j].name);
									var level =  categoryLevel.level;
									var groupId = categoryLevel.groupId;
									var childrenFlag = categoryLevel.childrenFlag;
									appendCategoryItems(select,keys,level,groupId,childrenFlag);
								}
							}
						}
						selectAll_new($("#"+selectId).parent().parent().children()[2], "adSeqs", "adCategory", "rejectReason");
					}
				}
			}*/
		});
	},
	error : function() {
//		console.log(">>>>>>>error");
	}
		
	
	});
    
	$("#allReject").change(function(){
		if($(this).attr("checked")){
			$("#allRejectReason").removeAttr("disabled");
		} else {
			$("#allRejectReason").val("");
			$("#allRejectReason").attr("disabled","disabled");
		}
	});
	
	$("#allAdCategoryCheck").change(function(){
		if($(this).attr("checked")){
			disabledController("allAdCategory",false);
			findAdCategorySelect("allAdCategory");
		} else {
			disabledController("allAdCategory",true);
			
		}
	});
	
	
	$("#adPfdAllNameMapSelect").change(function(){
		$("#pfdCustomerInfoId").val($("#adPfdAllNameMapSelect").val());
	});
	
	
});

function doQuery() {
	document.forms[0].action = "doAdCheckQuery.html";
	document.forms[0].submit();
}


function doApprove() {
    var ategoryCheck =check("doApprove");
	var checkCategoryFlag = ategoryCheck.checkCategoryFlag;
	
	if(!checkCategoryFlag){
		return;
	}else{
		if (confirm("確定核准 ?")) {
            document.forms[0].action = "doAdCheckApprove.html";
            document.forms[0].submit();
        }
	  }
}

function doApproveAndPause() {
	var ategoryCheck = check("doApproveAndPause");
	var checkCategoryFlag = ategoryCheck.checkCategoryFlag;
	
	if(!checkCategoryFlag){
		return;
	}else{
		if (confirm("確定核准 ?")) {
            document.forms[0].action = "doAdCheckApproveAndPause.html";
            document.forms[0].submit();
        }
	  }
}


function doReject() {
	var ategoryCheck =check("doReject");
	var checkCategoryFlag = ategoryCheck.checkCategoryFlag;
	var checkRejectFlag = ategoryCheck.checkRejectFlag;
	
	if(!checkCategoryFlag  || !checkRejectFlag ){
		return;
	}else{
		if (confirm("確定拒絕 ?")) {
            document.forms[0].action = "doAdCheckReject.html";
            document.forms[0].submit();
        }
	  }
    }

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
	document.forms[0].action = "doAdCheckQuery.html";
	document.forms[0].submit();
}

function controlAdUI(obj, componentId, componentId2) {
	if (obj.checked) {
		document.getElementById(componentId).disabled = false;
		document.getElementById(componentId2).disabled = false;
	} else {
		document.getElementById(componentId).disabled = true;
		document.getElementById(componentId2).disabled = true;
	}
}


//核准檢查
function check (checkType) {
	 categoryCode();
	 checkCategoryFlag = true;
	 checkRejectFlag = true;
		 $("input[name=adSeqs]").each(function() {
			 if($(this).attr("checked")){
				 if($(this).parent().parent().children().children()[0].value == "" ||$(this).parent().parent().children().children()[0].value == null){
					 checkCategoryFlag = false;
					 if($("#allAdCategoryCheck").attr("checked")){
						 alert("請選擇廣告類別！");
					 } else {
						 alert("廣告序號:"+$(this)[0].value+" 請選擇廣告類別！");
					 }
					 return false;
				 }
				 if(checkType == "doReject"){
					 if($("#allReject").attr("checked")){
						 if($("#allRejectReason").val() == ""){
							 checkRejectFlag = false;
							 alert("請填寫退件原因！"); 
							 return false;
						 }
					 } else {
						 var rejectCell = $(this).parent().parent().children()[8];
						 var rejectId = $(rejectCell).children()[0].id;
						 if($("#"+rejectId).val() == ""){
							 checkRejectFlag = false;
							 alert("廣告序號:"+$(this)[0].value+" 請填寫退件原因！"); 
							 return false;
						 }
					 }
				 }
			 }
	       });
		 return {checkCategoryFlag:checkCategoryFlag,checkRejectFlag:checkRejectFlag};
}


//控制Select開放與上鎖
function controlAdSelectUI(obj, componentId, componentId2){
	if (obj.checked) {
		disabledController(componentId,false);
		document.getElementById(componentId2).disabled = false;
		findAdCategorySelect(componentId);
	}else{
		disabledController(componentId,true);
		document.getElementById(componentId2).disabled = true;
	}
}
function disabledController(componentId,selectFlag){
	for(var i=0;i<$('#'+componentId+"_1").parent().children().length;i++){
		$('#'+componentId+"_1").parent().children()[i].disabled = selectFlag;
	}
	for(var i=0;i<$('#'+componentId+"_2").parent().children().length;i++){
		$('#'+componentId+"_2").parent().children()[i].disabled = selectFlag;
	}
	for(var i=0;i<$('#'+componentId+"_3").parent().children().length;i++){
		$('#'+componentId+"_3").parent().children()[i].disabled = selectFlag;
	}
}


//全選控制
function selectAll_new(obj, chkName, selectName, rejectReason) {
	if (obj.checked) {
		for(var i=0;i<$(".adCategory").parent().children().length;i++){
			appendAdCategorySelectUI2($(".adCategory").parent().children()[i].id);
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


//code寫入隱藏text
function categoryCode(){
	
	var totalObjId = "";
	var totalcode = "";
	
	if($("#allAdCategoryCheck").attr("checked")){
		var allCheckObjArray = [$("#allAdCategory_1").parent(),$("#allAdCategory_2").parent(),$("#allAdCategory_3").parent()]
		
		for(var s=0;s<allCheckObjArray.length;s++){
			var allCheckObj = allCheckObjArray[s];
			
			
			if($($(allCheckObj)).children().length > 1){
				var key = $($(allCheckObj)).children()[0].value;
				var length = $($(allCheckObj)).children().length;

				if($("#"+$($(allCheckObj)).children()[length-1].id).text() == ""){
					length = length - 1;
				} else {
					if($("#"+$($(allCheckObj)).children()[length-1].id+" option:selected").text() == "-請選擇-"){
						length = length - 1;
					}
				}
				var objId = null;
				var code = null;
				for(var i = 0; i < length ; i++){
					var id = $($(allCheckObj)).children()[i].id;
					if(key != ""){
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
				}
				
				if(objId != null && code != null){
					if(s != 0 && totalObjId != "" && totalcode != ""){
						totalObjId += ";";
						totalcode += ";";
					}	
					totalObjId += objId;
					totalcode += code;
				}
				
			}
		}
	}
	
	$("input[name=adSeqs]").each(function() {
		if(this.checked){
			var trObjArray = [$(this).parent().parent(),$(this).parent().parent().next(),$(this).parent().parent().next().next()];
			
			if(!$("#allAdCategoryCheck").attr("checked")){
				totalObjId = "";
				totalcode = "";
				
				
				for(var k=0;k<trObjArray.length;k++){
					var trObj = trObjArray[k];
					
					var childrenNumber = 7;
					if( k != 0){
						childrenNumber = 0;
					}
					
					console.log($($(trObj).children()[childrenNumber]).children());
					
					if($($(trObj).children()[childrenNumber]).children().length > 1){
						var key = $($(trObj).children()[childrenNumber]).children()[0].value;
						var length = $($(trObj).children()[childrenNumber]).children().length;
						
						if($("#"+$($(trObj).children()[childrenNumber]).children()[length-1].id).text() == ""){
							length = length - 1;
						} else {
							if($("#"+$($(trObj).children()[childrenNumber]).children()[length-1].id+" option:selected").text() == "-請選擇-"){
								length = length - 1;
							}
						}
						var objId = null;
						var code = null;
						for(var i = 0; i < length ; i++){
							var id = $($(trObj).children()[childrenNumber]).children()[i].id;
							if(key != ""){
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
						}
						
						if(objId != null && code != null){
							if(k != 0 && totalObjId != "" && totalcode != ""){
								totalObjId += ";";
								totalcode += ";";
							}	
							totalObjId += objId;
							totalcode += code;
						}
						
					}
				}
				
			}
			
			
			//alert(totalObjId);
			//alert(totalcode);
			$("#"+$($(trObjArray[0]).children()[0]).children()[0].id).attr("value",totalcode);
			$("#"+$($(trObjArray[0]).children()[1]).children()[0].id).attr("value",totalObjId);
			
			/*var trObj = $(this).parent().parent();
			if($($(trObj).children()[6]).children().length > 1){
				var key = $($(trObj).children()[6]).children()[0].value;
				var length = $($(trObj).children()[6]).children().length;
				alert($($(trObj.parent())).html());
				if($("#"+$($(trObj).children()[6]).children()[length-1].id+" option:selected").text() == "-請選擇-"){
					length = length - 1;
				}
				var objId = null;
				var code = null;
				for(var i = 0; i < length ; i++){
					var id = $($(trObj).children()[6]).children()[i].id;
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
				alert(objId);
				alert(code);
				$("#"+$($(trObj).children()[0]).children()[0].id).attr("value",code);
				$("#"+$($(trObj).children()[1]).children()[0].id).attr("value",objId);
			}*/
		}
	});
}
 
var obj= null;
function findAdCategorySelect(componentId) {
	appendAdCategorySelectUI(componentId);
}


function appendAdCategorySelectUI(componentId) {
	if ($("#" + componentId + "_1").children().length == 1) {
		for(var keys in obj){
			$("#" + componentId + "_1").append('<option value='+ keys+'>' + keys + '</option>');
		}
	}
	if ($("#" + componentId + "_2").children().length == 1) {
		for(var keys in obj){
			$("#" + componentId + "_2").append('<option value='+ keys+'>' + keys + '</option>');
		}
	}
	if ($("#" + componentId + "_3").children().length == 1) {
		for(var keys in obj){
			$("#" + componentId + "_3").append('<option value='+ keys+'>' + keys + '</option>');
		}
	}
}

function appendAdCategorySelectUI2(componentId) {
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
	if(adCategorySelect == "--廣告類別--"){
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
