function doQuery() {
	document.forms[0].action = "adOfflineReport.html";
	document.forms[0].submit();
}

function downloadReport() {
	document.forms[0].action = "downloadAdOffline.html";
	document.forms[0].submit();
}
