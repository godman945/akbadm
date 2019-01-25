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

    var agesex = $("#agesex").val();
    $("#searchAgesex option").each(function(){
    	var value = $(this).val();
    	if(agesex == value){
    		$(this).attr("selected","selected");
    	}
    });
    
    var device = $("#device").val();
    $("#searchDevice option").each(function(){
    	var value = $(this).val();
    	if(device == value){
    		$(this).attr("selected","selected");
    	}
    });
    
    page();
    tableSorter();
});

function doQuery() {
	$('#downloadFlag').val("");
	document.forms[0].action = "adAgesexSearch.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("searchAgesex").value = "";
	document.getElementById("searchDevice").value = "";
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    $('#downloadFlag').val("");
    document.forms[0].action = "adAgesexSearch.html";
	document.forms[0].submit();
}

function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			3 : { sorter: 'fancyNumber' },
			4 : { sorter: 'fancyNumber' },
			5 : { sorter: 'fancyNumber' },
			6 : { sorter: 'rangesort' },
			7 : { sorter: 'rangesort' },
			8 : { sorter: 'rangesort' }
			}
	});
}

//下載報表
function doDownlaod() {
	$('#downloadFlag').val("yes");
    document.forms[0].action = "adAgesexReportDownload.html";
	document.forms[0].submit();
}