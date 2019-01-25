$(document).ready(function(){ 

	$("#modifyBtn").click(function(){
		if ($("#black").attr('checked')) {
			if ($("#blackReason").val() == "") {
				alert("請輸入黑名單原因");
				return;
			}
		}
		$("#accountForm").submit();
	});

	$("#accountLog").click(function(){
		$("#accountLog").attr("href","accountAccesslog.html?customerInfoId="+$("#customerInfoId").val());
	});

});

function userInfo(id){
    $("#userId").val(id);
    $("#userForm").submit();
}

function controlBlackReasonUI(obj) {
    if (obj.checked) {
        document.getElementById("blackReason").disabled = false;
    } else {
        document.getElementById("blackReason").disabled = true;
    }
}
