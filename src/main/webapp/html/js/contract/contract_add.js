$(document).ready(function(){

    //開始日期 
    $("#startDate").datepicker({
	    showOn: "button",
	    buttonImage: "html/img/icon_cal.gif",
	    buttonImageOnly: true,
	    changeMonth: false,
	    changeYear: false,
        dateFormat: "yy-mm-dd"
    });

    //結束日期    
    $("#endDate").datepicker({
	    showOn: "button",
	    buttonImage: "html/img/icon_cal.gif",
	    buttonImageOnly: true,
	    changeMonth: false,
	    changeYear: false,
        dateFormat: "yy-mm-dd"
    });

    //簽約日期    
    $("#contractDate").datepicker({
	    showOn: "button",
	    buttonImage: "html/img/icon_cal.gif",
	    buttonImageOnly: true,
	    changeMonth: false,
	    changeYear: false,
        dateFormat: "yy-mm-dd"
    });

	var msg = $('#messageId');
	if (msg.val()!="") {
		alert(msg.val());
	}

});

function controlAdUI(obj) {
	if (obj.checked) {
		document.getElementById("payAfterData").style.display = "";
	} else {
		document.getElementById("payAfterData").style.display = "none";
	}
}

function backToList() {
	document.forms[0].action = "contractList.html";
	document.forms[0].submit();
}

function doAdd() {

	if (document.getElementById("pfdCustomerInfoId").selectedIndex == 0) {
		alert("請選擇經銷商！");
		return false;
	}
	if (document.getElementById("contractId").value == "") {
		alert("請輸入合約編號！");
		return false;
	}
	if (document.getElementById("contractDate").value == "") {
		alert("請選擇簽約日期！");
		return false;
	}
	if (document.getElementById("startDate").value == "") {
		alert("請選擇起始日期！");
		return false;
	}
	if (document.getElementById("endDate").value == "") {
		alert("請選擇結束日期！");
		return false;
	}
	if (!document.getElementById("prePay").checked && !document.getElementById("payAfter").checked) {
		alert("請選擇付款方式！");
		return false;
	}
	if (document.getElementById("payAfter").checked) {
//		if (document.getElementById("totalQuota").value == "") {
//			alert("請輸入可使用總額度！");
//			return false;
//		}
		if (document.getElementById("payDay").value == "") {
			alert("請輸入付款條件！");
			return false;
		}
		if (document.getElementById("overdueFine").value == "") {
			alert("請輸入逾期罰金！");
			return false;
		}
	}
	
	//檢查合約
	$.ajax({
		url:"checkContractAddAjax.html",
		data:{
			"contractId":document.getElementById("contractId").value,
			"pfdCustomerInfoId":document.getElementById("pfdCustomerInfoId").value,
			"startDate":document.getElementById("startDate").value,
			"endDate":document.getElementById("endDate").value
		},
		type:"post",
		dataType:"json",
		
		success:function(response, status){			
			if (response.message == null || response.message == '') {		
				document.forms[0].action = "doContractAdd.html";
			    document.forms[0].submit();
			}else{
				alert(response.message);
			}
		},
		error: function(xtl) {
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
}
