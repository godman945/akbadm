$(document).ready(function(){ 
	
	// 交易開始日期 
    $("#transStartDate").datepicker({
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
    
	// 交易結束日期    
    $("#transEndDate").datepicker({
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
    	searchTrans();
    });
    
});

function searchTrans(){
	
	$.ajax({
		url:"accountTransSearch.html",
		data:{
			"keyword":$("#keyword").val(),
			"transEndDate":$("#transEndDate").val(),
			"transStartDate":$("#transStartDate").val(),
			"pageNo": $("#pageNo").val(),
			"pageSize": $("#pageSize").val()
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#transTable").html(response);	
			page();
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
}



//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    searchTrans();
}

