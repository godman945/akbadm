$(document).ready(function(){

	var updateFlag = $("#updateFlag").val();
    
    if(updateFlag == "n"){
    	$(":input").not("#returnBtn").attr("disabled","disabled");
    } else {
		// 日期區間 datepicker    
	    $("#actionStartDate").datepicker({
	    	showOn: "button",
	    	buttonImage: "html/img/icon_cal.gif",
	    	buttonImageOnly: true,
	    	yearRange:"-10:+10",
			onClose:function() {
				$('#actionEndDate').datepicker( "option", "minDate", new Date(Date.parse($(this).val().replace(/-/g,"/"))) );
			}
	    });        
		
	    $("#actionEndDate").datepicker({
	    	showOn: "button",
	    	buttonImage: "html/img/icon_cal.gif",
	    	buttonImageOnly: true,
	    	yearRange:"-10:+10",
			onClose:function() {
				$('#actionStartDate').datepicker( "option", "maxDate", new Date(Date.parse($(this).val().replace(/-/g,"/"))) );
			}
	    		
	    });
	
	    $("#inviledDate").datepicker({
	    	showOn: "button",
	    	buttonImage: "html/img/icon_cal.gif",
	    	buttonImageOnly: true,
	    	yearRange:"-10:+10",
			onClose:function() {
				
			}
	    		
	    });
    }
    
    $("#payment").change(function(){
    	var payment = $("#payment").val();
    	if(payment == "N"){
    		$("#giftCondition").val("0");
    		$("#giftCondition").attr("readonly","readonly");
    	} else {
    		$("#giftCondition").val("500");
    		$("#giftCondition").removeAttr("readonly");
    	}
    });
    
    $("[name=shared]").click(function(){
    	var value = $(this).val();
    	
    	if(value == "Y"){
    		$("#snoCount").val("1");
    		$("#snoCount").attr("readonly","readonly");
    	} else {
    		$("#snoCount").val("");
    		$("#snoCount").removeAttr("readonly");
    	}
    });
    
});

function addFreeAction(){
	
	var actionName = $("#actionName").val();
	if(actionName == ""){
		alert("活動名稱請勿空白");
		return;
	}
	
	var actionStartDate = $("#actionStartDate").val();
	if(actionStartDate == ""){
		alert("請選擇活動開始日期");
		return;
	}
	
	var actionEndDate = $("#actionEndDate").val();
	if(actionEndDate == ""){
		alert("請選擇活動結束日期");
		return;
	}
	
	var giftCondition = $("#giftCondition").val();
	if(giftCondition == ""){
		alert("儲值金額請勿空白(若為免儲值請填0)");
		return;
	}
	
	var payment = $("#payment").val();
	if(payment == "Y" && giftCondition < 500){
		alert("儲值金額至少$500以上");
		return;
	}
	
	var giftMoney = $("#giftMoney").val();
	if(giftMoney == ""){
		alert("贈送金額請務空白");
		return;
	}
	
	var inviledDate = $("#inviledDate").val();
	if(inviledDate == ""){
		alert("請選擇禮金失效日期");
		return;
	}
	
	var giftSnoHead = $("#giftSnoHead").val();
	if(giftSnoHead == ""){
		alert("請輸入序號開頭前兩碼");
		return;
	}

	if(giftSnoHead.length != 2){
		alert("序號開頭不足兩碼");
		return;
	}
	
	var reg = new RegExp("^[A-Z]+$"); 
	if(!reg.test(giftSnoHead)){
		alert("序號開頭必須為英文字母大寫");
		return;
	}
	
	var snoCount = $("#snoCount").val();
	if(snoCount == ""){
		alert("請輸入新增序號組數");
		return;
	}
	
	$("#addFreeActionForm").submit();
}

function updateFreeAction(){
	var actionName = $("#actionName").val();
	if(actionName == ""){
		alert("活動名稱請勿空白");
		return;
	}
	
	var giftCondition = $("#giftCondition").val();
	if(giftCondition == ""){
		alert("儲值金額請勿空白(若為免儲值請填0)");
		return;
	}
	
	var payment = $("#payment").val();
	if(payment == "Y" && giftCondition < 500){
		alert("儲值金額至少$500以上");
		return;
	}
	
	var giftMoney = $("#giftMoney").val();
	if(giftMoney == ""){
		alert("贈送金額請務空白");
		return;
	}
	
	var actionStartDate = $("#actionStartDate").val();
	if(actionStartDate == ""){
		alert("請選擇活動開始日期");
		return;
	}
	
	var actionEndDate = $("#actionEndDate").val();
	if(actionEndDate == ""){
		alert("請選擇活動結束日期");
		return;
	}
	
	var giftMoney = $("#giftMoney").val();
	if(giftMoney == ""){
		alert("贈送金額請務空白");
		return;
	}
	
	var inviledDate = $("#inviledDate").val();
	if(inviledDate == ""){
		alert("請選擇禮金失效日期");
		return;
	}
	
	$("#updateFreeActionForm").submit();
}

function returnView() {
	window.location = "adGiftView.html";
}