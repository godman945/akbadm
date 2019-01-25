$(document).ready(function(){ 

	$("#system").change(function(){
		checkMemberId();
	});
	
	$("#memberId").hover(function(){
		checkMemberId();
	});
	
	$("#viewBtn").click(function(){
		window.location.href = "managerList.html";
	});
});

function checkMemberId() {
	if($.trim($("#memberId").val()) != ""){
		checkManager();
	}
}

function checkManager(){
	
	$.ajax({
		url: "checkManager.html",
		data: {
			"sys": $("#system").val(),
			"memberId": $("#memberId").val()
		},
		type: "post",
		dataType: "json",
		success: function(response, status) {			
			
			if(response.exist == "Y"){
				$("#showMsg").text("這帳號在這該系統已被使用了!!");
				$("#addBtn").attr("disabled", true);;
			}
			else{
				$("#showMsg").text("");
				$("#addBtn").attr("disabled", false);;
			}
		},
		error: function(xtl) {
			errorDialogBtn("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
}