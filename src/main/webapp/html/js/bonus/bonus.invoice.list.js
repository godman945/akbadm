﻿$(document).ready(function(){
	
	$('#invoiceStrDate').datePicker({
		followOffset:[0, 24],
		altFormat : 'yyyy-mm',
		showMode : 1
	});
	
	$('#invoiceEndDate').datePicker({
		followOffset:[0, 24],
		altFormat : 'yyyy-mm',
		showMode : 1
	});
    
	searchInvoice();
	
	$("#search").click(function(){
		searchInvoice();		
	});
	
	$("#balanceBtn").click(function(){
		balanceInvoice();
	});
});

function searchInvoice(){
	
	$.ajax({
		url:"searchBonusInvoice.html",
		data:{
			"invoiceStrDate": $("#invoiceStrDate").val(),
			"invoiceEndDate": $("#invoiceEndDate").val(),
			"pfdCustomerInfoId": $("#pfdCustomerInfoId").val()
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#invoiceTable").html(response);	
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
}

function modifyBonusInvoice(pfdCustomerInfoId,closeYear,closeMonth) {
	
	$.fancybox({
		'href'     :'modifyBonusInvoice.html?pfdCustomerInfoId='+pfdCustomerInfoId+'&closeYear='+closeYear+'&closeMonth='+closeMonth 	
	});
	
}

function downloadBonusBill(id, year, month) {
	
	$("#pfdId").val(id);
	$("#year").val(year);
	$("#month").val(month);
		
	$("#formDownloadBonusBill").attr("action","downloadBonusBill.html");
	
	$("#formDownloadBonusBill").submit();
}

function balanceInvoice(){
	
	for(var i=0;i<$("#tableInvoice input:checkbox").length;i++){
		if($("#chk_"+i).attr('checked')){	
			updateBalanceInvoice($("#chk_"+i).val());
			
		}
	}
}

function updateBalanceInvoice(balancePfdYearMonth) {
	
	$.ajax({
		url:"updateBalanceInvoice.html",
		data:{
			"balancePfdYearMonth": balancePfdYearMonth
		},
		type:"post",
		dataType:"html",
		async: false,
		success:function(response, status){
			searchInvoice();
			alert("沖帳成功!!");
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
}

function balanceReport(){
	var id = $('#pfdCustomerInfoId').val();
	var strDate = $('#invoiceStrDate').val();
	var endDate = $('#invoiceEndDate').val();
	window.open ('balanceInvoiceReport.html?pfdCustomerInfoId='+id+'&invoiceStrDate='+strDate+'&invoiceEndDate='+endDate ,'_blank');
}
