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
	$('#downloadFlag').val("");

	if($("#groupPositionIdBox").prop("checked")){
		$("#groupPositionId").val("N");
	} else {
		$("#groupPositionId").val("");
	}
	
	document.forms[0].action = "pfbxMaliceCheckQuery.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("pfbxCustomerInfoId").value = "";
	document.getElementById("pfbxPositionId").value = "";
	document.getElementById("maliceType").value = "";
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    
    $('#downloadFlag').val("");
    document.forms[0].action = "pfbxMaliceCheckQuery.html";
	document.forms[0].submit();
}

//下載報表
function doDownlaod() {
	$('#downloadFlag').val("yes");
    document.forms[0].action = "pfbxMaliceCheckDownload.html";
	document.forms[0].submit();
}

function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			1 : { sorter: 'fancyNumber' },
			11 : { sorter: 'fancyNumber' },
			12 : { sorter: 'fancyNumber' },
			13 : { sorter: 'fancyNumber' },
			14 : { sorter: 'fancyNumber' },
			15 : { sorter: 'rangesort' }
			}
	});
}
