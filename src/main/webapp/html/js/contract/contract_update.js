$(document).ready(function(){

	var msg = $('#messageId');
	if (msg.val()!="") {
		alert(msg.val());
	}

});

function backToList() {
	document.forms[0].action = "contractList.html";
	document.forms[0].submit();
}

function doUpdate() {
	document.forms[0].action = "doContractUpdate.html";
	document.forms[0].submit();
}

function doClose() {
	if (document.getElementById("closeReason").value == "") {
		alert("請輸入作廢原因！");
		return false;
	}
	document.forms[0].action = "doContractClose.html";
	document.forms[0].submit();
}
