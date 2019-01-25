$(document).ready(function(){ 

	$("#deleteBtn").click(function(){
		
		$.ajax({
			url:"deleteTestAccount.html",
			data:{
				"pcId":$("#pcId").val()
			},
			type:"post",
			dataType:"json",
			success:function(response, status){
				$("#showMsg").html(response.showMsg);	
			},
			error: function(xtl) {
				
				alert("錯誤訊息","系統繁忙，請稍後再試！");
			}
		});
		
	});
	
    
});

