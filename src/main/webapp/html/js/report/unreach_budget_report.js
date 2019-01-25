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

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("displayCount").options[0].selected = true;
	document.getElementById("pfdCustomerInfoId").options[0].selected = true;
}

function doQuery() {
	document.forms[0].action = "unReachBudgetReport.html";
	document.forms[0].submit();
}

function downloadReport() {
	document.forms[0].action = "downloadUnReachBudget.html";
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
			3 : { sorter: 'rangesort' },
			4 : { sorter: 'rangesort' },
			5 : { sorter: 'fancyNumber' },
			6 : { sorter: 'fancyNumber' },
			7 : { sorter: 'fancyNumber' },
			8 : { sorter: 'rangesort' },
			9 : { sorter: 'rangesort' },
			10 : { sorter: 'rangesort' }
			}
	});
}