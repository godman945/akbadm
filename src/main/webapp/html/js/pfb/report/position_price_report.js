$(document).ready(function(){

});

//查詢
function doQuery() {
	if(!isNum($("#pPrice").val())){
		alert("版位價格請輸入整數數字。");
		return false;
	}
	document.forms[0].action = "pfbPositionPriceQuery.html";
	document.forms[0].submit();
}

//更新出價
function updatePrice(customerInfoId, pId) {
	var pPrince = $("[pid='" + pId +"']");
	var pPrinceVal = pPrince.val();
	
	if(!isNum(pPrinceVal)){
		alert("出價請輸入整數數字。");
		return false;
	}
	
	$.ajax({
		url:"updatePriceReportAjax.html",
		data:{
			"customerInfoId":customerInfoId,
			"pid":pId,
			"pprice":pPrinceVal
		},
		type:"post",
		dataType:"json",
		success:function(response, status){
			if(response.status == "SUCCESS"){
				pPrince.closest("tr").find("[date]").html(response.updateTime);
			}
			alert(response.msg);
		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
}

//清除
function doClear() {
	$('#customerInfoId, #pId').val("");
	$('#pPrice').val(3);
}