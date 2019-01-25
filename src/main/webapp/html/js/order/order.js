$(document).ready(function(){ 
	
	searchTrans();
	
	// 訂單開始日期 
    $("#ordStrDate").datepicker({
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
    
	// 訂單結束日期    
    $("#ordEndDate").datepicker({
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
    
//    $("#reset").click(function(){
//    	
//    });
});

function searchTrans(){
	
	
	$.ajax({
		url:"orderQuery.html",
		data:{
			"ordNo":$("#ordNo").val(),
			"account":$("#account").val(),
			"ordStrDate": $("#ordStrDate").val(),
			"ordEndDate": $("#ordEndDate").val(),
			"ordStatus": $("#ordStatus").val()
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

function billingTransDetail(ordSno){
	window.open($("#billingUrl").val() + ordSno, "detail", "height=768,width=1024,top=0,left=0,scrollbars=yes");
}