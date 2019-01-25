$(document).ready(function(){

});
$(document).ajaxStart(function(){
    $.blockUI({
    	message: "<b><font size=5>查詢中...</font></b>",
        css: {
            border: '3px solid #aaa',
            padding: '15px',
            backgroundColor: '#fff',
            '-webkit-border-radius': '10px',
            '-moz-border-radius': '10px',
            opacity: .9,
            textAlign:      'center',
            cursor:         'wait',
        }
    });
}).ajaxStop($.unblockUI);

function findTableView(){

	var date = $("#IT_dateRange").val().split("~");
	var startDate = date[0];
	var endDate = date[1];
	var dateType = $("#dateType").val();
	var userAccount = $("#userAccount").val();
	var searchAdStatus = $("#searchAdStatus").val();
	var searchType = $("#searchType").val();
	var adActionName = $("#adActionName").val();
	var adPvclkDevice = $("#adPvclkDevice").val();
	var pageNo = $("#pageNo").val();
	var pageSize = $("#pageSize").val();
	
	$.ajax({
		url: "adActionViewTable.html",
		data:{
			 "startDate": startDate,
			 "endDate": endDate,
			 "dateType": dateType,
			 "userAccount":userAccount,
			 "searchAdStatus":searchAdStatus,
			 "searchType": searchType,
			 "adActionName": adActionName,
			 "adPvclkDevice": adPvclkDevice,
			 "pageNo": pageNo,
			 "pageSize": pageSize
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#tableList").html(response);	
			page();
			tableSorter();
		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
	
}
function goAdGroup(adActionSeq,adclkDevice) {
	var dateType = $("#dateType").val();
	var date = $("#IT_dateRange").val().split("~");
	var adStartDate = date[0];
	var adEndDate = date[1];
	var userAccount = $("#userAccount").val();
	var searchAdStatus = $("#searchAdStatus").val();
	var searchType = $("#searchType").val();
	var adPvclkDevice = $("#adPvclkDevice").val();
	var url = "adGroupView.html?adActionSeq=" + adActionSeq + "&adStartDate=" + adStartDate + "&adEndDate=" + adEndDate + "&dateType=" + dateType + "&adclkDevice=" + adclkDevice;
	if(userAccount != "")		url += "&userAccount=" + userAccount;
	if(searchAdStatus != "")	url += "&searchAdStatus=" + searchAdStatus;
	if(searchType != "0")		url += "&searchType=" + searchType;
	if(adPvclkDevice != "")		url += "&adPvclkDevice=" + adPvclkDevice;
	
	location.href = url;
}

function tableSorter(){
	
	$("#tableView").tablesorter({
		headers:{
			0 : { sorter: 'shortDate'},
			4 : { sorter: 'fancyNumber' },
			8 : { sorter: 'fancyNumber' },
			9 : { sorter: 'fancyNumber' },
			10 : { sorter: 'fancyNumber' },
			11 : { sorter: 'fancyNumber' },
			12 : { sorter: 'fancyNumber' },
			13 : { sorter: 'fancyNumber' },
			14 : { sorter: 'fancyNumber' },
			15 : { sorter: 'fancyNumber' },
			16 : { sorter: 'fancyNumber'}
			}
	});
}