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
	document.forms[0].action = "keywordReport.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("keywordType").options[0].selected = true;
	document.getElementById("sortMode").options[0].selected = true;
	document.getElementById("displayCount").options[0].selected = true;
	document.getElementById("pfdCustomerInfoId").options[0].selected = true;
}

function kwDetail(keyword) {
	var startDateVal = document.getElementById("startDate").value;
	var endDateVal = document.getElementById("endDate").value;
    var keywordType = document.getElementById("keywordType");
    var keywordTypeVal = keywordType.options[keywordType.selectedIndex].value;
    var sortMode = document.getElementById("sortMode");
    var sortModeVal = sortMode.options[sortMode.selectedIndex].value;
    var pfdCustomerInfoId = document.getElementById("pfdCustomerInfoId").value;

	var url = encodeURI("keywordReportDetail.html?startDate=" + startDateVal + "&endDate=" + endDateVal + "&keywordType=" + keywordTypeVal + "&sortMode=" + sortModeVal + "&keyword=" + keyword + "&pfdCustomerInfoId=" + pfdCustomerInfoId);

	window.open(url, "kwReportDetail", "height=600,width=850,top=0,left=0,scrollbars=yes");
}

function downloadReport() {
	document.forms[0].action = "downloadKeyword.html";
	document.forms[0].submit();
}
