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
	document.forms[0].action = "totalTransReport.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("pfdCustomerInfoId").options[0].selected = true;
}

function downloadReport() {
	document.forms[0].action = "downloadTotalTrans.html";
	document.forms[0].submit();
}

function totalTransDetail(reportDate) {
	var pfdCustomerInfoId = document.getElementById("pfdCustomerInfoId").value;
	var url = encodeURI("totalTransReportDetail.html?reportDate=" + reportDate + "&pfdCustomerInfoId=" + pfdCustomerInfoId);

	window.open(url, "totalTransReportDetail", "height=600,width=850,top=0,left=0,scrollbars=yes");
}
