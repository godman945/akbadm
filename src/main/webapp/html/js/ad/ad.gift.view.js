$(document).ready(function(){

	//自定義隱藏欄位
    $.tablesorter.addParser({
        // set a unique id
        id: 'rangesort',
        is: function(s) {
        	s = s.replace("$ ", "");
            return /^[0-9]?[0-9,\.]*$/.test(s);
        },
        format: function(s) {
        	s = s.replace("$ ", "");
            return $.tablesorter.formatFloat(s.replace(/,/g, ''));
        },
        // set type, either numeric or text
        type: 'numeric'
    });
	
    findTableView();
    /*if($("#adActionSeq").val() != "") {
    	findTableView();
    }*/

});
$(document).ajaxStart(function(){
    $.blockUI({
    	message: "<b><font size=5>查詢中...</font></b>",
        css: {
            border: '3px solid #aaa',
            padding: '15px',
            backgroundColor: '#fff',
            '-webkit-border-radius': '10px',
            '-moz-border-radius': '10px',
            opacity: .9,
            textAlign:      'center',
            cursor:         'wait',
        }
    });
}).ajaxStop($.unblockUI);

function findTableView(){

	var searchStartDate = $("#startDate").val();
	var searchEndDate = $("#endDate").val();
	var inviledStartDate = $("#startDate2").val();
	var inviledEndDate = $("#endDate2").val();
	var payment = $("#payment").val();
	var giftStyle = $("#giftStyle").val();
	var pageNo = $("#pageNo").val();
	var pageSize = $("#pageSize").val();

	$.ajax({
		url: "adGiftViewTable.html",
		data:{
			 "searchStartDate": searchStartDate,
			 "searchEndDate": searchEndDate,
			 "inviledStartDate": inviledStartDate,
			 "inviledEndDate":inviledEndDate,
			 "payment":payment,
			 "giftStyle": giftStyle,
			 "pageNo": pageNo,
			 "pageSize": pageSize
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#tableList").html(response);	
			page();
			tableSorter();
		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
	
}

function tableSorter(){
	
	$("#tableView").tablesorter({
		headers:{
			6 : { sorter: 'rangesort' },
			7 : { sorter: 'rangesort' },
			8 : { sorter: 'rangesort' },
			10 : { sorter: false },
			11 : { sorter: false },
			12 : { sorter: false }
		}
	});
}

function addFreeAction() {
	window.location = "adGiftAdd.html";
}

function updateFreeAction(actionId){
	window.location = "adGiftUpdate.html?actionId=" + actionId;
}

function openDetalView(actionId,actionEndDate,shared){
	window.location = "adGiftDetalView.html?actionId=" + actionId + "&actionEndDate=" + actionEndDate + "&shared=" + shared;
}

function openRecordView(actionId){
	window.location = "adGiftRecordView.html?actionId=" + actionId;
}