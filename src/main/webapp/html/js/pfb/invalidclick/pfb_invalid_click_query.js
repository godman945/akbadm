var listMap = new Map();

$(document).ready(function(){

    //開始日期 
    $("#startDate").datepicker({
	    showOn: "button",
	    buttonImage: "html/img/icon_cal.gif",
	    buttonImageOnly: true,
	    changeMonth: false,
	    changeYear: false,
        dateFormat: "yy-mm-dd",
        maxDate: "-1D"
    });

    //結束日期    
    $("#endDate").datepicker({
	    showOn: "button",
	    buttonImage: "html/img/icon_cal.gif",
	    buttonImageOnly: true,
	    changeMonth: false,
	    changeYear: false,
        dateFormat: "yy-mm-dd",
        maxDate: "-1D"
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
	
	document.forms[0].action = "pfbxInvalidClickQuery.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("pfbxCustomerInfoId").value = "";
	document.getElementById("pfbxPositionId").value = "";
	document.getElementById("groupPositionId").value = "";
	document.getElementById("selectType").value = "1";
	document.getElementById("mount").value = "5";
	listMap = new Map();
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    
    if($("#groupPositionIdBox").prop("checked")){
		$("#groupPositionId").val("N");
	} else {
		$("#groupPositionId").val("");
	}
    
    $('#downloadFlag').val("");
    document.forms[0].action = "pfbxInvalidClickQuery.html";
	document.forms[0].submit();
}

//下載報表
function doDownlaod() {
	$('#downloadFlag').val("yes");
    /*document.forms[0].action = "admPortalBonusReportDownload.html";
	document.forms[0].submit();*/
}

function select_one(number,aaa){
	if(!listMap.has(number)){
		listMap.set(number,aaa);
	} else {
		listMap.delete(number);
	}
	//console.log(listMap);
}

function selectAll_new(obj){
	if (obj.checked) {
		$(".selectbox").each(function() {
			if(!$(this).prop("checked")){
				$(this).click();
			}
	     });
	} else {
		$(".selectbox").each(function() {
			 $(this).attr("checked", false);
	     });
		listMap = new Map();
	}
}

function doAdd(){
	if(listMap.size == 0){
		alert("請選擇流量記錄");
	}

	listMap.forEach(function(value, key) {
		var dataArray = value;
		
		var selectType = "1";
		var mount = "5";
		var groupPositionId = "N";
		var date = "";
		var pfbId = "";
		var pfbxPositionId = "";
		var invalidNote1 = "";
		var invalidNote2 = "";
		var count = "0";
		
		selectType = dataArray[0];
		mount = dataArray[1];
		groupPositionId = dataArray[2];
		date = dataArray[3];
		pfbId = dataArray[4];
		
		if(groupPositionId != "N"){
			pfbxPositionId = dataArray[5];
			invalidNote1 = dataArray[6];
			if(selectType == "7"){
				invalidNote2 = dataArray[7];
				count = dataArray[8];
			} else {
				count = dataArray[7];
			}
		} else {
			invalidNote1 = dataArray[5];
			if(selectType == "7"){
				invalidNote2 = dataArray[6];
				count = dataArray[7];
			} else {
				count = dataArray[6];
			}
		}
		
		
		if(parseInt(mount) < parseInt(count)){
			$.ajax({
				url : "addInvalidTraffic.html",
				type : "POST",
				async : false,
				dataType:'json',
				data : {
					"invSelectType" : selectType,
					"invMount": mount,
					"invGroupPositionId" : groupPositionId,
					"invDate" : date,
					"invPfbId" : pfbId,
					"invPfbxPositionId" : pfbxPositionId,
					"invInvalidNote1" : invalidNote1,
					"invInvalidNote2" : invalidNote2
				},
				success : function(respone) {
					
				}
			});
		}
	  
		doQuery();
	});
	
}

function tableSorter(){
	var selectType = $("#selectType").val();
	
	if(selectType == "7"){
		if($("#groupPositionIdBox").prop("checked")){
			$("#tableView").tablesorter({
				headers:{
					0 : { sorter: false },
					5 : { sorter: 'fancyNumber' },
					6 : { sorter: 'rangesort' }
				}
			});
		} else {
			$("#tableView").tablesorter({
				headers:{
					0 : { sorter: false },
					6 : { sorter: 'fancyNumber' },
					7 : { sorter: 'rangesort' }
				}
			});
		}
	} else {
		if($("#groupPositionIdBox").prop("checked")){
			$("#tableView").tablesorter({
				headers:{
					0 : { sorter: false },
					4 : { sorter: 'fancyNumber' },
					5 : { sorter: 'rangesort' }
				}
			});
		} else {
			$("#tableView").tablesorter({
				headers:{
					0 : { sorter: false },
					5 : { sorter: 'fancyNumber' },
					6 : { sorter: 'rangesort' }
				}
			});
		}
	}
	
}