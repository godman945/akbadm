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
	document.forms[0].action = "keywordOfferPriceReport.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("keywordType").options[0].selected = true;
	document.getElementById("searchText").value = "";
	document.getElementById("displayCount").options[0].selected = true;
	document.getElementById("pfdCustomerInfoId").options[0].selected = true;
}

function downloadReport() {
	document.forms[0].action = "downloadKeywordOfferPrice.html";
	document.forms[0].submit();
}
