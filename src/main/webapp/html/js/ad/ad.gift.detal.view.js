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

function addFreeGift() {
	
	//檢查活動是否已結束
	if(!checkDate()){
		alert("活動已結束，無法新增序號");
		return;
	}
	
	var giftSnoHead = $("#giftSnoHead").val();
	if(giftSnoHead == ""){
		alert("請輸入序號開頭前兩碼");
		return;
	}
	
	if(giftSnoHead.length != 2){
		alert("序號開頭不足兩碼");
		return;
	}
	
	var reg = new RegExp("^[A-Z]+$"); 
	if(!reg.test(giftSnoHead)){
		alert("序號開頭必須為英文字母大寫");
		return;
	}
	
	var snoCount = $("#snoCount").val();
	if(snoCount == ""){
		alert("請輸入新增序號組數");
		return;
	}
	
	$("#addFreeGiftForm").submit();
}

function returnView() {
	window.location = "adGiftView.html";
}

function checkDate(){
	var date = $("#actionEndDate").val() +  " 23:59:59";    
	var endDate = new Date(date.replace(/-/g,'/'));
	var nowDate = new Date();
	
	if(endDate > nowDate){
		return true;
	}
	
	return false;
}

//下載報表
function doDownlaod() {
	var actionId = $("#actionId").val();
	var actionEndDate = $("#actionEndDate").val();
	var downloadFlag = "yes";
	window.location = "adGiftDetalDownload.html?actionId=" + actionId + "&actionEndDate=" + actionEndDate + "&downloadFlag=" + downloadFlag;
}