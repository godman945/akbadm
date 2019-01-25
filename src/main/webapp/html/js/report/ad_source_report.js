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

    var style = $("#searchStyle").val();
    $("#style option").each(function(){
    	var value = $(this).val();
    	if(style == value){
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
	document.forms[0].action = "adSourceSearch.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("searchText").value = "";
	document.getElementById("searchCategory").value = "";
	document.getElementById("searchOption").value = "";
	document.getElementById("category").value = "";
	document.getElementById("option").value = "";
	document.getElementById("searchAdDevice").value = "allDevice";
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    $('#downloadFlag').val("");
    document.forms[0].action = "adSourceSearch.html";
	document.forms[0].submit();
}

function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			0 : { sorter: 'text' },
			6 : { sorter: false },
			7 : { sorter: 'fancyNumber' },
			8 : { sorter: 'fancyNumber' },
			9 : { sorter: 'fancyNumber' },
			0 : { sorter: 'rangesort' },
			11 : { sorter: 'rangesort' },
			12 : { sorter: 'rangesort' }
			}
	});
}

//下載報表
function doDownlaod() {
	$('#downloadFlag').val("yes");
    document.forms[0].action = "adSourceReportDownload.html";
	document.forms[0].submit();
}

//開啟明細
function fundDetalView(pfbCustomerInfoId,pfbCustomerInfoName,adPvclkDate,allowUrl,allowUrlName){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var searchCategory = $("#searchCategory").val();
	var searchOption = $("#searchOption").val();
	var searchText = $("#searchText").val();
	var searchAdDevice = $("#searchAdDevice").val();
	
	$.ajax({
		url: "adSourceDetalReport.html",
		data:{
			 "startDate": startDate,
			 "endDate": endDate,
			 "searchCategory": searchCategory,
			 "searchOption": searchOption,
			 "searchText": searchText,
			 "searchAdDevice": searchAdDevice,
			 "pfbCustomerInfoId": pfbCustomerInfoId,
			 "pfbCustomerInfoName": pfbCustomerInfoName,
			 "adPvclkDate": adPvclkDate,
			 "allowUrl": allowUrl,
			 "allowUrlName": allowUrlName
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$('#adSourceDetalDiv').html(response);
			$("#adSourceDetalDiv").dialog(
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
						title : "廣告客戶明細",
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
			$("#adSourceDetalDiv").dialog("open");
				
			detalTableSorter();

		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
}

function detalTableSorter(){
	$(".tableDetalView").tablesorter({
		headers:{
			0 : { sorter: 'text' },
			2 : { sorter: 'fancyNumber' },
			3 : { sorter: 'fancyNumber' },
			4 : { sorter: 'fancyNumber' },
			5 : { sorter: 'rangesort' },
			6 : { sorter: 'rangesort' }
			}
	});
}