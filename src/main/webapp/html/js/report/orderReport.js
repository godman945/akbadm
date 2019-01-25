$(document).ready(function(){

	searchOrderReport();
	
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
    	searchOrderReport();
    });
    
    $("#download").click(function(){
    	$("#orderForm").attr("action", "downloadOrderReport.html");
    	$("#orderForm").submit();
    });
});

function searchOrderReport(){
	
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var pfdCustomerInfoId = $("#pfdCustomerInfoId").val();
	var payType = $("#payType").val();
	
	$.ajax({
		url:"searchOrderReport.html",
		data:{
			"startDate":startDate,
			"endDate":endDate,
			"pfdCustomerInfoId": pfdCustomerInfoId,
			"payType": payType
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#orderTable").html(response);	
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
	
}
