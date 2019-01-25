$(document).ready(function(){ 
	
	searchManager();
	
	$("#system").change(function(){
		searchManager();
	});
	
	$("#add").click(function(){
		window.location.href = "addManager.html";
	});
});

function searchManager() {
	
	$.ajax({
		url:"searchManager.html",
		data:{
			"sys":$("#system").val()
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#managerListTable").html(response);	
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
}

function modifyManager(id) {
	$("#formManager").attr("action","modifyManager.html");
	$("#managerId").val(id);
	$("#formManager").submit();
}