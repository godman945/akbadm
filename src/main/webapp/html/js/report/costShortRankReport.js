$(document).ready(function(){

	searchCostShortRankReport();
	
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
    	searchCostShortRankReport();
    });
    
    $("#download").click(function(){
    	$("#costShortRankForm").attr("action", "downloadCostShortRankReport.html");
    	$("#costShortRankForm").submit();
    });
});

function searchCostShortRankReport(){
	
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var pageSize = $("#pageSize").val();
	
	$.ajax({
		url:"searchCostShortRankReport.html",
		data:{
			"startDate":startDate,
			"endDate":endDate,
			"pageSize":pageSize
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#costShortRankTable").html(response);	
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
	
}
