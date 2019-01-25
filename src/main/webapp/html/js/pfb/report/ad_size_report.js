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

    var size = $("#searchSize").val();
    $("#size option").each(function(){
    	var value = $(this).val();
    	if(size == value){
    		$(this).attr("selected","selected");
    	}
    });
    
    var searchCategory = $("#category").val();
    $("#searchCategory option").each(function(){
    	var value = $(this).val();
    	if(searchCategory == value){
    		$(this).attr("selected","selected");
    	}
    });
    
    var searchOption = $("#option").val();
    $("#searchOption option").each(function(){
    	var value = $(this).val();
    	if(searchOption == value){
    		$(this).attr("selected","selected");
    	}
    });
    
    page();
    tableSorter();
});

function doQuery() {
	$('#downloadFlag').val("");
	document.forms[0].action = "pfbSizeSearch.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("searchText").value = "";
	document.getElementById("searchCategory").value = "";
	document.getElementById("searchOption").value = "";
	document.getElementById("searchSize").value = "";
	document.getElementById("category").value = "";
	document.getElementById("option").value = "";
	document.getElementById("size").value = "";
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    $('#downloadFlag').val("");
    document.forms[0].action = "pfbSizeSearch.html";
	document.forms[0].submit();
}

function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			0 : { sorter: 'text' },
			5 : { sorter: 'fancyNumber' },
			6 : { sorter: 'fancyNumber' },
			7 : { sorter: 'fancyNumber' },
			8 : { sorter: 'rangesort' },
			9 : { sorter: 'rangesort' },
			10 : { sorter: 'rangesort' }
			}
	});
}

//下載報表
function doDownlaod() {
	$('#downloadFlag').val("yes");
    document.forms[0].action = "pfbSizeReportDownload.html";
	document.forms[0].submit();
}

function openClick(pfbxCustomerInfoId,adPvclkDate,size){
	$.ajax({
		url : "pfbSizeReportChart.html",
		type : "POST",
		dataType:"html",
		data : {
			"pfbxCustomerInfoId" : pfbxCustomerInfoId,
			"adPvclkDate": adPvclkDate,
			"size" : size
		},
		success : function(response) {
			$('#adSizeChartDiv').html(response);
			$("#adSizeChartDiv").dialog(
					{
						autoOpen : false,
						show : "blind",
						hide : "explode",
						width : 1000,
						height : 700,
						resizable : false,
						modal : true,
						closeOnEscape : true,
						draggable : false,
						title : "點擊座標預覽圖",
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
			$("#adSizeChartDiv").dialog("open");
		}
	});
	
	
}