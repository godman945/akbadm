$(document).ready(function(){
	$("#allReject").change(function(){
		if($(this).attr("checked")){
			$("#allRejectReason").removeAttr("disabled");
		} else {
			$("#allRejectReason").val("");
			$("#allRejectReason").attr("disabled","disabled");
		}
	});

    page();
});

//分頁功能
function wantSearch(pageNo) {
	if (pageNo != null) {
		$("#pageNo").val(pageNo);
	}
	document.forms[0].action = "catalogLogo.html";
	document.forms[0].submit();
}

function doQuery() {
	document.forms[0].action = "catalogLogo.html";
	document.forms[0].submit();
}


function doApprove() {
	if (confirm("確定核准 ?")) {
        document.forms[0].action = "catalogLogoDoApprove.html";
        document.forms[0].submit();
    }
}

function doReject() {
	var flag = check("doReject");
	
	if (!flag){
		return;
	}
	else {
		if (confirm("確定拒絕 ?")) {
            document.forms[0].action = "catalogLogoDoReject.html";
            document.forms[0].submit();
        }
	}
}

//核准檢查
function check (checkType) {
	if (checkType == "doReject") {
		if ($("#allRejectReason").val() == "") {
			alert("請填寫退件原因！"); 
			return false;
		}
	}
	
	return true;
}

//全選控制
function selectAll_new(obj, chkName) {
	if (obj.checked) {
		$("input[name=" + chkName + "]").each(function() {
			$(this).attr("checked", true);
	    });
	}
	else {
		$("input[name=" + chkName + "]").each(function() {
			$(this).attr("checked", false);
	    });
	}
}

function preview(img) {
    $.fancybox({
        'href':img,
        'showCloseButton' :true,
        'autoSize':true,
        'autoHeight':true,
        'autoScale':true,
        'transitionIn':'none',
        'transitionOut':'none',
        'padding':0,
        'overlayOpacity':.75,
        'overlayColor':'#fff',
        'scrolling':'no'
    });
}
