$(document).ready(function(){

	searchCostRankReport();
	
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
    	searchCostRankReport();
    });
    
    $("#download").click(function(){
    	$("#costRankForm").attr("action", "downloadCostRankReport.html");
    	$("#costRankForm").submit();
    });
});

function searchCostRankReport(){
	
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var pageSize = $("#pageSize").val();
	
	$.ajax({
		url:"searchCostRankReport.html",
		data:{
			"startDate":startDate,
			"endDate":endDate,
			"pageSize":pageSize
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#costRankTable").html(response);	
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
	
}
