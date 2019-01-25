﻿$(document).ready(function(){

	
	// 發票日期
    $("#financeInvoiceDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd"
    });
    
	// 付款日期
    $("#financePayDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd"	
    });
    
    // 發票日期
    $("#pfdInvoiceDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd"	
    });
    
    // 支票到期日期
    $("#pfdCheckCloseDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd"	
    });
    
    // 折讓日期
    $("#debitDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd"	
    });
    
    //付款方式
    if($("#pfdCheck").attr("checked")){
    	$(".pfdCheckTr").show();
    }
    
    $("#pfdRemit").click(function(){	
    	$(".pfdCheckTr").hide();
    	$("#pfdCheckSno").val("");
    	$("#pfdCheckCloseDate").val("");
    });
    
    $("#pfdCheck").click(function(){	
    	$(".pfdCheckTr").show();
    });
    
    //付款憑證
    if($("#pfdDebit").attr("checked")){
    	$(".pfdFinanceTr").hide();
    	$(".pfdDebitTr").show();
    }
    
    $("#pfdFinance").click(function(){	
    	$(".pfdFinanceTr").show();
    	$(".pfdDebitTr").hide();
    	$("#debitMoney").val("");
    	$("#debitDate").val("");
    });
    
    $("#pfdDebit").click(function(){
    	$(".pfdDebitTr").show();
    	$(".pfdFinanceTr").hide();
    	$("#financeInvoiceSno").val("");
    	$("#financeInvoiceDate").val("");
    	$("#financeInvoiceMoney").val("");
    	$("#financePayDate").val("");
    });
    
	$("#update").click(function(){
		var pfdPayCategory = "1";
		$("[name=pfdPayCategory]").each(function(){
			if($(this).attr("checked")){
				pfdPayCategory = $(this).val();
			}
		});
		
		var bonusType = "1";
		$("[name=bonusType]").each(function(){
			if($(this).attr("checked")){
				bonusType = $(this).val();
			}
		});
		
		parent.$.ajax({
			url:"updateBonusInvoice.html",
			data:{
				"pfdCustomerInfoId": $(".pfdCustomerInfoId").val(),
				"closeYear": $("#closeYear").val(),
				"closeMonth": $("#closeMonth").val(),
				"financeInvoiceSno": $("#financeInvoiceSno").val(),
				"financeInvoiceDate": $("#financeInvoiceDate").val(),
				"financeInvoiceMoney": $("#financeInvoiceMoney").val(),
				"financePayDate": $("#financePayDate").val(),
				"pfdInvoiceSno": $("#pfdInvoiceSno").val(),
				"pfdInvoiceDate": $("#pfdInvoiceDate").val(),
				"pfdInvoiceMoney": $("#pfdInvoiceMoney").val(),
				"pfdCheckSno": $("#pfdCheckSno").val(),
				"pfdCheckCloseDate": $("#pfdCheckCloseDate").val(),				
				"bonusInvoiceId": $("#bonusInvoiceId").val(),
				"billStatus": $("#billStatus").val(),
				"billNote": $("#billNote").val(),
				"debitMoney": $("#debitMoney").val(),
				"debitDate": $("#debitDate").val(),
				"pfdPayCategory": pfdPayCategory,
				"bonusType": bonusType
			},
			type:"post",
			dataType:"html",
			success:function(response, status){
				
				parent.searchInvoice();
				parent.$.fancybox.close();
			},
			error: function(xtl) {
				
				alert("錯誤訊息","系統繁忙，請稍後再試！");
			}
		});

	});
	
	$("#cancel").click(function(){	

		parent.$.fancybox.close();
	});
	
});

