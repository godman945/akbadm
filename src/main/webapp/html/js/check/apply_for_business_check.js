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
});

function doQuery() {
	document.forms[0].action = "doApplyForBusinessQuery.html";
	document.forms[0].submit();
}

function doApprove() {
    var objs = document.getElementsByName("applyForSeqs");
    
    var flag = false;
    for (var i=0; i<objs.length; i++) {
    	if (objs[i].checked) {
    		flag = true;
    	}
    }

    if (!flag) {
    	alert("請選擇審核資料！");
    	return;
    } else {
    	if (confirm("確定核准 ?")) {
    	    document.forms[0].action = "doApplyForBusinessApprove.html";
    	    document.forms[0].submit();
    	}
    }
}

function doReject() {
	var objs = document.getElementsByName("applyForSeqs");
	var objAdRejectReasons = document.getElementsByName("rejectReason");

    var flag = false;
    for (var i=0; i<objs.length; i++) {
    	if (objs[i].checked) {
    		flag = true;
    		if (objAdRejectReasons[i].value == "") {
    			alert("請填寫退件原因！");
    			return;
    		}
    	}
    }

    if (!flag) {
    	alert("請選擇審核資料！");
    } else {
    	if (confirm("確定拒絕 ?")) {
            document.forms[0].action = "doApplyForBusinessReject.html";
            document.forms[0].submit();
        }
    }
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
	document.forms[0].action = "doApplyForBusinessQuery.html";
	document.forms[0].submit();
}

function controlAdUI(obj, componentId) {
	if (obj.checked) {
		document.getElementById(componentId).disabled = false;
	} else {
		document.getElementById(componentId).disabled = true;
	}
}

function selectAll_new(obj, chkName, rejectReason) {
    if (obj.checked) {

    	$("input[name=" + chkName + "]").each(function() {
            $(this).attr("checked", true);
        });

        $("input[name=" + rejectReason + "]").each(function() {
            $(this).attr("disabled", false);
        });

    } else {

    	$("input[name=" + chkName + "]").each(function() {
            $(this).attr("checked", false);
        });

        $("input[name=" + rejectReason + "]").each(function() {
            $(this).attr("disabled", true);
        });

    }
}
