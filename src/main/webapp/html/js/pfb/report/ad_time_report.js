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

    var period = $("#seachPeriod").val();
    $("#period option").each(function(){
    	var value = $(this).val();
    	if(period == value){
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
	$("#pageNo").val("1");
	$("#pageSize").val("50");
	$('#downloadFlag').val("");
	document.forms[0].action = "pfbTimeReport.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("period").value = "Day";
	document.getElementById("searchCategory").value = "";
	document.getElementById("searchOption").value = "";
	document.getElementById("seachPeriod").value = "";
	document.getElementById("category").value = "";
	document.getElementById("option").value = "";
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("searchText").value = "";
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    $('#downloadFlag').val("");
    document.forms[0].action = "pfbTimeReport.html";
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
    document.forms[0].action = "pfbTimeReportDownload.html";
	document.forms[0].submit();
}
