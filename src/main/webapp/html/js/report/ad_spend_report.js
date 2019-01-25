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

    page();
    tableSorter();
});

function doQuery() {
	document.forms[0].action = "adSpendReport.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("displayCount").options[0].selected = true;
	document.getElementById("pfdCustomerInfoId").options[0].selected = true;
}

function downloadReport() {
	document.forms[0].action = "downloadAdSpend.html";
	document.forms[0].submit();
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    doQuery();
}

function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			4 : { sorter: 'rangesort' },
			5 : { sorter: 'rangesort' },
			6 : { sorter: 'rangesort' },
			7 : { sorter: 'fancyNumber' },
			8 : { sorter: 'fancyNumber' },
			9 : { sorter: 'fancyNumber' },
			10 : { sorter: 'rangesort' },
			11 : { sorter: 'rangesort' },
			12 : { sorter: 'rangesort' },
			13 : { sorter: 'fancyNumber' }
			}
	});
}
