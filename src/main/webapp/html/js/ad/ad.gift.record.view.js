$(document).ready(function(){
	tableSorter();
});

function tableSorter(){
	
	$("#tableView").tablesorter({
		headers:{
			0 : { sorter: 'number' }
		}
	});
}

function returnView() {
	window.location = "adGiftView.html";
}

//下載報表
function doDownlaod() {
	var actionId = $("#actionId").val();
	var downloadFlag = "yes";
	window.location = "adGiftRecordDownload.html?actionId=" + actionId + "&downloadFlag=" + downloadFlag;
}