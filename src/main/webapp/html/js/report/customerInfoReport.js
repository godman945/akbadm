$(document).ready(function(){

	searchCustomerInfoReport();
	
    // 開始日期 
    $("#startDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd",
        firstDay: 1,
		minDate: '2010-07-01',
        maxDate: 0	
    });

    // 結束日期    
    $("#endDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd",
        firstDay: 1,
		minDate: '2010-07-01',
        maxDate: 0	
    });

    $("#search").click(function(){
    	searchCustomerInfoReport();
    });
    
    $("#download").click(function(){
    	$("#customerInfoForm").attr("action", "downloadCustomerInfoReport.html");
    	$("#customerInfoForm").submit();
    });
});

function searchCustomerInfoReport(){
	
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var pfdCustomerInfoId = $("#pfdCustomerInfoId").val();
	var orderType = $("#orderType").val();
	var payType = $("#payType").val();
	
	$.ajax({
		url:"searchCustomerInfoReport.html",
		data:{
			"startDate": startDate,
			"endDate": endDate,
			"pfdCustomerInfoId": pfdCustomerInfoId,
			"orderType": orderType,
			"payType": payType
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#customerInfoTable").html(response);	
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
	
}
