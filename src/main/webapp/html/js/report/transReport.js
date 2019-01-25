$(document).ready(function(){

	searchTransReport();
	
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
    	searchTransReport();
    });
    
    $("#download").click(function(){
    	$("#pfdCustomerInfoIdText").val($("#pfdCustomerInfoId option:selected").text());
    	$("#payTypeText").val($("#payType option:selected").text());
    	$("#transForm").attr("action", "downloadTransReport.html");
    	$("#transForm").submit();
    });
});

function searchTransReport(){
	
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var customerInfo = $("#customerInfo").val();
	var pfdCustomerInfoId = $("#pfdCustomerInfoId").val();
	var payType = $("#payType").val();
	
	$.ajax({
		url:"searchTransReport.html",
		data:{
			"startDate": startDate,
			"endDate": endDate,
			"customerInfo": customerInfo,
			"pfdCustomerInfoId": pfdCustomerInfoId,
			"payType": payType
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#transTable").html(response);	
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
	
}
