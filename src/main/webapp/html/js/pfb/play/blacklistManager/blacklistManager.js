$(document).ready(function(){
	checkPage();
	checkSelect();
	if($("#search_url").val() != ""){
		$("#searchUrl").val($("#search_url").val());
	}
});

function rejectSubmit(){
	if($("#rejectReason").val() == ""){
		alert("請輸入拒絕理由!")
		return false;
	}
	var checkFlag = false; 
	var rejectUrl = [];
	$("input[name=rejectCheckbox]").each(function() {
      	 if(this.checked){
      		checkFlag = true;
      		rejectUrl.push(this.id);
      	 }
   	 });
	
	if(!checkFlag){
		alert("請勾選一筆資料!")
		return false;
	}
	
	var blacklistManagerJsonMap = {
			 rejectUrl : rejectUrl
    		}
    var blacklistManagerJsonObj = JSON.stringify(blacklistManagerJsonMap);
	$(function() {
		$.ajax({
			url : "blacklistManagerAjax.html",
			data : {
				"blacklistManagerJsonObj" : blacklistManagerJsonObj,
				"rejectReason" : $("#rejectReason").val()
			},
			type : "post",
			dataType : "json",
			success : function(respone) {
//				console.log("success>>>>>>>>"+respone);
				if(respone == null){
//					console.log("=============");
					changePanel(rejectUrl);
					
				}else{
					alert('error>>>'+respone)
				}
			},
			error : function() {
//				console.log(">>>>>>>>error");
			}
		});
	});
}

function cancelReject(){
	var checkFlag = false; 
	var cancelRejectUrl = [];
	$("input[name=rejectCheckbox]").each(function() {
      	 if(this.checked){
      		checkFlag = true;
      		cancelRejectUrl.push(this.id);
      	 }
   	 });
	
	if(!checkFlag){
		alert("請勾選一筆資料!")
		return false;
	}
	var blacklistManagerJsonMap = {
			 cancelRejectUrl : cancelRejectUrl
   		}
   var blacklistManagerJsonObj = JSON.stringify(blacklistManagerJsonMap);
	$(function() {
		$.ajax({
			url : "blacklistManagerCancelAjax.html",
			data : {
				"blacklistManagerJsonObj" : blacklistManagerJsonObj
			},
			type : "post",
			dataType : "json",
			success : function(respone) {
//				console.log("success>>>>>>>>"+respone);
				if(respone == null){
					 location.reload();
					
				}else{
					alert('error>>>'+respone)
				}
			},
			error : function() {
//				console.log(">>>>>>>>error");
			}
		});
	});
	
}

function changePanel(obj){
	$("input[name=rejectCheckbox]").each(function() {
		for(var i=0;i<obj.length;i++){
			if(this.id == obj[i] && this.checked){
				$($(this).parent().parent().children()[4]).text("封鎖");
				$($(this).parent().parent().children()[5]).text($("#rejectReason").val());
				break;
			}
		}
  	 });
	$("#rejectReason").attr("value","");
}


//查詢網址
function searchBtn(){
	$(location).attr( 'href' , 'blacklistManagerUrlSearch.html?searchUrl='+$.trim($("#searchUrl").val())+'&pageSize='+$("#pageSize").val()+'&page=0'+'&pageType=');
}

//依據頁數查詢
function searchByPage(obj){
		if($(obj).attr("src").indexOf("disable") > 0){
			return false;
		}
		$(location).attr( 'href' , 'blacklistManagerUrlSearch.html?searchUrl='+$.trim($("#searchUrl").val())+'&pageSize='+$("#pageSize").val()+'&page='+$("#page").val()+'&pageType='+obj.id);
}

function checkPage(){
	if($("#page").val() > 1){
		$("#pageUp").attr("src","./html/img/page_pre.gif");
		$("#firstPage").attr("src","./html/img/page_first.gif");
	}else{
		$("#pageUp").attr("src","./html/img/page_pre_disable.gif");
		$("#firstPage").attr("src","./html/img/page_first_disable.gif");
	}
	
	if($("#page").val() == $("#totalPage").val()){
		$("#nextPage").attr("src","./html/img/page_next_disable.gif");
		$("#lastPage").attr("src","./html/img/page_end_disable.gif");
	}else{
		$("#nextPage").attr("src","./html/img/page_next.gif");
		$("#lastPage").attr("src","./html/img/page_end.gif");
	}
	
}

//下一頁
function checkSelect(){
	$("#pageSize > option").each(function() {
		if($("#pageSize").val() == 0 ){
			if(this.value == 20){
				$(this).attr("selected", "true");
				return false;
			}
		}else{
			if($("#page_size").val() == this.value){
				$(this).attr("selected", "true");
				return false;
			}
		}
	});
}