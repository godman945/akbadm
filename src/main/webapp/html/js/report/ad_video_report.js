﻿﻿﻿﻿$(document).ready(function() {
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
    
    page();
    tableViewSorter();
});

function doQuery() {
	document.forms[0].action = "adVideoReport.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("pfdCustomerInfoId").value = "";
	document.getElementById("memberId").value = "";
	document.getElementById("adPriceType").value = "";
	document.getElementById("adDevice").value = "";
	document.getElementById("adSize").value = "";
}

//分頁功能
function wantSearch(pageNo) {
    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    document.forms[0].action = "adVideoReport.html";
	document.forms[0].submit();
}

function tableSorter() {
	$("#tableView").tablesorter({
		headers: {
			3 : { sorter: false },
			15 : { sorter: false },
		}
	});
}

//下載報表
function doDownlaod() {
    document.forms[0].action = "adVideoReportDownload.html";
	document.forms[0].submit();
}

//開啟明細
function fundDetalView(adVideoDate, pfdCustomerInfoId, pfpCustomerInfoId, adId) {
	var adPriceType = $("#adPriceType").val();
	var adDevice = $("#adDevice").val();
	var adSize = $("#adSize").val();
	
	$.ajax({
		url: "adVideoReportDetail.html",
		data:{
			 "adVideoDate": adVideoDate,
			 "pfdCustomerInfoId": pfdCustomerInfoId,
			 "pfpCustomerInfoId": pfpCustomerInfoId,
			 "adId": adId,
			 "adPriceType": adPriceType,
			 "adDevice": adDevice,
			 "adSize": adSize
		},
		type:"post",
		dataType:"html",
		success:function(response, status) {
			$('#adVideoDetalDiv').html(response);
			$("#adVideoDetalDiv").dialog({
				autoOpen : false,
				show : "blind",
				hide : "explode",
				width : 950,
				resizable : false,
				modal : true,
				closeOnEscape : true,
				draggable : false,
				title : "影音廣告播放成效",
				open : function(event, ui) {
					// 隱藏「x」關閉按鈕
					$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
				},
				buttons : {
					"Ok" : function() {
						$(this).dialog("close");
					}
				}
			});
			$("#adVideoDetalDiv").dialog("open");
		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
}

function tableViewSorter(){
	$("#tableView").tablesorter({
		headers:{
			7 : { sorter: 'fancyNumber' },
			8 : { sorter: 'fancyNumber' },
			9 : { sorter: 'fancyNumber' },
			10 : { sorter: 'rangesort' },
			11 : { sorter: 'rangesort' },
			12 : { sorter: 'rangesort' },
			13 : { sorter: 'fancyNumber' },
			14 : { sorter: 'rangesort' },
			15 : { sorter: 'fancyNumber' },
			16 : { sorter: false }
			}
	});
}
