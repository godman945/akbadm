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

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("customerInfoId").value = "";
	document.getElementById("adMobileOS").options[0].selected = true;
}

function doQuery() {
	document.forms[0].action = "adMobileOsReport.html";
	document.forms[0].submit();
}

function downloadReport() {
	document.forms[0].action = "downloadAdMobileOs.html";
	document.forms[0].submit();
}

function getDetail(adMobileOS, customerInfoId) {
	var url = "adMobileOsReportDetail.html?startDate=" + document.getElementById("startDate").value +
		"&endDate=" + document.getElementById("endDate").value;
	if(customerInfoId != "")		url += "&customerInfoId=" + customerInfoId;
	if(adMobileOS != "all")				url += "&adMobileOS=" + adMobileOS;
	window.open(url, "adMobileOsReportDetail", "height=600,width=850,top=0,left=0,scrollbars=yes");
}