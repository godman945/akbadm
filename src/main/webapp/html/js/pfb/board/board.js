$(document).ready(function(){

	// 開始日期 
    $("#startDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd"
    });
    
	// 結束日期    
    $("#endDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd"	
    });

    var boardType = $('#boardType');
    var pfbCustomerInfoId = $('#pfbCustomerInfoId');

    boardType.bind('change', function() {
    	if (boardType.val() == "0") {
    		pfbCustomerInfoId.attr("disabled", true);
    	} else {
    		pfbCustomerInfoId.attr("disabled", false);
    	}
    });
   
    var hidBoardTypeValue = $('#hidBoardType').val();
    if (hidBoardTypeValue != undefined) { //新增時沒此參數，只有修改時才有
        if (hidBoardTypeValue == "0") {
    		pfbCustomerInfoId.attr("disabled", true);
    	} else {
    		pfbCustomerInfoId.attr("disabled", false);
    	}
    }
});

function addBoard() {
	window.location = "pfbBoardAdd.html";
}

function doAdd() {

    var boardType = $('#boardType');
    var pfbCustomerInfoId = $('#pfbCustomerInfoId');
    var startDate = $('#startDate');
    var endDate = $('#endDate');
    
    if(startDate.val() == ""){
    	alert("請選擇上線日期");
    	return false;
    }
    
    if(endDate.val() == ""){
    	alert("請選擇下線日期");
    	return false;
    }

	/*if (boardType.val() != "0") {
		if (pfbCustomerInfoId.val() == "") {
			alert("請選擇聯播網帳戶！");
			return;
		}
	}*/

	document.forms[0].submit();
}

function modifyBoard(id) {
    var boardType = $('#boardType');
    var pfbCustomerInfoId = $('#pfbCustomerInfoId');

	if (boardType.val() != "0") {
		if (pfbCustomerInfoId.val() == "") {
			alert("請選擇經銷商！");
			return;
		}
	}

	window.location = "pfbBoardUpdate.html?boardId="+id;
}

function doModify() {

    var boardType = $('#boardType');
    var pfbCustomerInfoId = $('#pfbCustomerInfoId');

	if (boardType.val() == "0") {
		if (pfbCustomerInfoId.val() == "") {
			alert("請選擇經銷商！");
			return;
		}
	}

	document.forms[0].submit();
}


function deleteBoard(id){
	if (confirm("是否要刪除這筆公告 ?")) {
		window.location = "doPfbBoardDelete.html?boardId="+id;
	}
}
