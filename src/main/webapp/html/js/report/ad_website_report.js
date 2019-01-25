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
    
    var searchCategory = $("#category").val();
    $("#searchCategory option").each(function(){
    	var value = $(this).val();
    	if(searchCategory == value){
    		$(this).attr("selected","selected");
    	}
    });
    
    var searchAdDevice = $("#adDevice").val();
    $("#searchAdDevice option").each(function(){
    	var value = $(this).val();
    	if(searchAdDevice == value){
    		$(this).attr("selected","selected");
    	}
    });
    
    page();
    tableSorter();
});

function doQuery() {
	$('#downloadFlag').val("");
	document.forms[0].action = "adWebsiteSearch.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("searchCategory").value = "";
	document.getElementById("category").value = "";
	document.getElementById("searchAdDevice").value = "allDevice";
	document.getElementById("adDevice").value = "allDevice";
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    $('#downloadFlag').val("");
    document.forms[0].action = "adWebsiteSearch.html";
	document.forms[0].submit();
}

function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			2 : { sorter: false },
			4 : { sorter: 'fancyNumber' },
			5 : { sorter: 'fancyNumber' },
			6 : { sorter: 'fancyNumber' },
			7 : { sorter: 'rangesort' },
			8 : { sorter: 'rangesort' },
			9 : { sorter: 'rangesort' }
			}
	});
}

//下載報表
function doDownlaod() {
	$('#downloadFlag').val("yes");
    document.forms[0].action = "adWebsiteReportDownload.html";
	document.forms[0].submit();
}

//開啟明細
function fundDetalView(websiteCategoryCode,websiteCategoryName,adPvclkDate){

	var searchAdDevice = $("#searchAdDevice").val();
	
	$.ajax({
		url: "adWebsiteDetalReport.html",
		data:{
			 "startDate": adPvclkDate,
			 "endDate": adPvclkDate,
			 "searchCategory": websiteCategoryCode,
			 "searchAdDevice": searchAdDevice,
			 "websiteCategoryCode": websiteCategoryCode,
			 "websiteCategoryName": websiteCategoryName,
			 "adPvclkDate": adPvclkDate
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$('#adWebsiteDetalDiv').html(response);
			$("#adWebsiteDetalDiv").dialog(
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
			$("#adWebsiteDetalDiv").dialog("open");
				
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