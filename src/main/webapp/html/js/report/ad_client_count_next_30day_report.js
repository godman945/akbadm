//下載報表
function doDownlaod() {
	$('#downloadFlag').val("yes");
    document.forms[0].action = "admClientCountForNext30DayReportDownload.html";
	document.forms[0].submit();
}