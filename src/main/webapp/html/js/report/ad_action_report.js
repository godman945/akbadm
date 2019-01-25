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

});

function doQuery() {
	document.forms[0].action = "adActionReport.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("adType").options[0].selected = true;
	document.getElementById("customerInfoId").value = "";
	document.getElementById("pfdCustomerInfoId").options[0].selected = true;
	document.getElementById("payType").options[0].selected = true;
}

function downloadReport() {
	$("#adTypeText").val($("#adType option:selected").text());
	$("#pfdCustomerInfoIdText").val($("#pfdCustomerInfoId option:selected").text());
	$("#payTypeText").val($("#payType option:selected").text());
	document.forms[0].action = "downloadAdAction.html";
	document.forms[0].submit();
}

function adActionDetailReport(reportDate) {
	var adType = document.getElementById("adType");
    var adTypeVal = adType.options[adType.selectedIndex].value;
    var customerInfoIdVal = document.getElementById("customerInfoId").value;
    var pfdCustomerInfoId = document.getElementById("pfdCustomerInfoId");
    var pfdCustomerInfoIdVal = pfdCustomerInfoId.options[pfdCustomerInfoId.selectedIndex].value;
    var payType = document.getElementById("payType");
    var payTypeVal = payType.options[payType.selectedIndex].value;

    var url = encodeURI("adActionReportDetail.html?reportDate=" + reportDate +
    		"&adType=" + adTypeVal + "&customerInfoId=" + customerInfoIdVal +
			"&pfdCustomerInfoId=" + pfdCustomerInfoIdVal + "&payType=" + payTypeVal);

	window.open(url, "adActionDetailReport", "height=600,width=1000,top=0,left=0,scrollbars=yes");
}

function everydayRecognizeReport(reportDate) {
    var pfdCustomerInfoId = document.getElementById("pfdCustomerInfoId");
    var pfdCustomerInfoIdVal = pfdCustomerInfoId.options[pfdCustomerInfoId.selectedIndex].value;
    var payType = document.getElementById("payType");
    var payTypeVal = payType.options[payType.selectedIndex].value;
	
	var url = "everydayRecognizeReport.html?date=" + reportDate +
            "&pfdCustomerInfoId=" + pfdCustomerInfoIdVal + "&payType=" + payTypeVal;

	window.open(url, "everydayRecognizeReport", "height=600,width=850,top=0,left=0,scrollbars=yes");
}

function checkBillReport(reportDate) {
	
	var url = "checkBillReport.html?date=" + reportDate;

	window.open(url, "checkBillReport", "height=650,width=900,top=0,left=0,scrollbars=yes");
}

//alex測試
function downloadDailyReport(){
	$(location).attr( 'href' , 'dailyReportDownload.html' );
}
