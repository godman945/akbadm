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
    var pfdCustomerInfoId = $('#pfdCustomerInfoId');
    var pfdUserId = $("#pfdUserId");

    boardType.bind('change', function() {
    	if (boardType.val() == "0") {
    		pfdCustomerInfoId.attr("disabled", true);
    		pfdUserId.attr("disabled", true);
    	} else {
    		pfdCustomerInfoId.attr("disabled", false);
    		pfdUserId.attr("disabled", false);
    	}
    });
   
    pfdCustomerInfoId.bind('change', function() {
        $.ajax({
            url: 'findPfdUserIdAjax.html',
            data : {'paramPfdCustomerInfoId' : pfdCustomerInfoId.val()},
            type: 'post',
            dataType: 'json',
            beforeSend: function() {
            	pfdUserId.empty();
            },
            success: function(response, status) {

                var resultCode = response.jsonData;

                $.each(jQuery.parseJSON(resultCode), function(value, text) {
                	pfdUserId.append($("<option></option>").attr("value", value).text(text));
                });
            },
            error: function(xtl) {
                alert("系統繁忙，請稍後再試！");
            }
        });
    });

    var hidBoardTypeValue = $('#hidBoardType').val();
    if (hidBoardTypeValue != undefined) { //新增時沒此參數，只有修改時才有
        if (hidBoardTypeValue == "0") {
    		pfdCustomerInfoId.attr("disabled", true);
    		pfdUserId.attr("disabled", true);
    	} else {
    		pfdCustomerInfoId.attr("disabled", false);
    		pfdUserId.attr("disabled", false);
    	}
    }
});

function addBoard() {
	window.location = "pfdBoardAdd.html";
}

function doAdd() {

    var boardType = $('#boardType');
    var pfdCustomerInfoId = $('#pfdCustomerInfoId');

	if (boardType.val() != "0") {
		if (pfdCustomerInfoId.val() == "") {
			alert("請選擇經銷商！");
			return;
		}
	}

	document.forms[0].submit();
}

function modifyBoard(id) {
    var boardType = $('#boardType');
    var pfdCustomerInfoId = $('#pfdCustomerInfoId');

	if (boardType.val() != "0") {
		if (pfdCustomerInfoId.val() == "") {
			alert("請選擇經銷商！");
			return;
		}
	}

	window.location = "pfdBoardUpdate.html?boardId="+id;
}

function doModify() {

    var boardType = $('#boardType');
    var pfdCustomerInfoId = $('#pfdCustomerInfoId');

	if (boardType.val() == "0") {
		alert("pfdCustomerInfoId.val() = " + pfdCustomerInfoId.val());
		if (pfdCustomerInfoId.val() == "") {
			alert("請選擇經銷商！");
			return;
		}
	}

	document.forms[0].submit();
}


function deleteBoard(id){
	if (confirm("是否要刪除這筆公告 ?")) {
		window.location = "doPfdBoardDelete.html?boardId="+id;
	}
}
