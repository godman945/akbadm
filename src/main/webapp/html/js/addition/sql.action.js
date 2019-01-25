$(document).ready(function(){ 

	$("#runBtn").click(function(){
		
		$.ajax({
			url:"updateAdGroupStatus.html",

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

