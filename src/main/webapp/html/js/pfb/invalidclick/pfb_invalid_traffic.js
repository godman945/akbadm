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
    
    //結算日期    
	$("#closeDate").datePicker({
		followOffset:[0, 24],
		altFormat : 'yyyy-mm',
		showMode : 1
	});
    
    page();
    tableSorter();
});

function doQuery() {
	$('#downloadFlag').val("");

	if($("#groupPositionIdBox").prop("checked")){
		$("#groupPositionId").val("N");
	} else {
		$("#groupPositionId").val("");
	}
	
	document.forms[0].action = "pfbxInvalidTrafficQuery.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("closeDate").value = "";
	document.getElementById("pfbxCustomerInfoId").value = "";
	document.getElementById("selectType").value = "";
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    
    $('#downloadFlag').val("");
    document.forms[0].action = "pfbxInvalidTrafficQuery.html";
	document.forms[0].submit();
}

//開啟明細
function fundDetalView(invId){
	$.ajax({
		url: "pfbxInvalidTrafficDetailQuery.html",
		data:{
			 "invId": invId
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$('#invalidTrafficDetalDiv').html(response);
			$("#invalidTrafficDetalDiv").dialog(
					{
						autoOpen : false,
						show : "blind",
						hide : "explode",
						width : 950,
						height : 800,
						resizable : false,
						modal : true,
						closeOnEscape : true,
						draggable : false,
						title : "無效流量明細",
						open : function(event, ui) {
							// 隱藏「x」關閉按鈕
							$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
						},
						buttons : {
							"OK" : function() {
								$(this).dialog("close");
							}
						}
					});
			$("#invalidTrafficDetalDiv").dialog("open");

		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
}

function deleteTraffic(invId) {
	
	if( confirm ("確定要刪除該筆無效流量嗎?") ){
		$.ajax({
			url : "deleteInvalidTraffic.html",
			type : "POST",
			async : false,
			dataType:'json',
			data : {
				"invId" : invId
			},
			success : function(respone) {
				doQuery();
			},
			error: function(xtl) {
				alert("系統繁忙，請稍後再試！");
			}
		});
	}
}

//下載報表
function doDownlaod() {
	$('#downloadFlag').val("yes");
    document.forms[0].action = "pfbxInvalidTrafficDownload.html";
	document.forms[0].submit();
}

function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			4 : { sorter: 'rangesort' },
			5 : { sorter: 'rangesort' },
			6 : { sorter: false },
			7 : { sorter: false }
			}
	});
}