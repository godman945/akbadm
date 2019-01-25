$(document).ready(function(){
		$("#searchAdStatus").attr("value","");
    if($("#adActionSeq").val() != "") {
    	findTableView();
    }

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
	var keyword = $("#keyword").val();
	var adPvclkDevice = $("#adPvclkDevice").val();
	var pageNo = $("#pageNo").val();
	var pageSize = $("#pageSize").val();
	var adActionSeq = $("#adActionSeq").val();
	
	$.ajax({
		url: "adGroupViewTable.html",
		data:{
			 "startDate": startDate,
			 "endDate": endDate,
			 "dateType": dateType,
			 "userAccount":userAccount,
			 "searchAdStatus":searchAdStatus,
			 "searchType": searchType,
			 "keyword": keyword,
			 "adPvclkDevice": adPvclkDevice,
			 "pageNo": pageNo,
			 "pageSize": pageSize,
			 "adActionSeq": adActionSeq
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

function goAd(adGroupSeq) {
	
	var dateType = $("#dateType").val();
	var date = $("#IT_dateRange").val().split("~");
	var adStartDate = date[0];
	var adEndDate = date[1];
	var userAccount = $("#userAccount").val();
	var searchAdStatus = $("#searchAdStatus").val();
	var searchType = $("#searchType").val();
	var adPvclkDevice = $("#adPvclkDevice").val();
	
	var url = "adAdView.html?adGroupSeq=" + adGroupSeq + "&adStartDate=" + adStartDate + "&adEndDate=" + adEndDate + "&dateType=" + dateType+ "&adSeq=" + $("#adActionSeq").val();
	if(userAccount != "")		url += "&userAccount=" + userAccount;
	if(searchAdStatus != "")	url += "&searchAdStatus=" + searchAdStatus;
	if(searchType != "0")		url += "&searchType=" + searchType;
	if(adPvclkDevice != "")		url += "&adPvclkDevice=" + adPvclkDevice;
	location.href = url;
}

function goKW(adGroupSeq) {
	var dateType = $("#dateType").val();
	var date = $("#IT_dateRange").val().split("~");
	var adStartDate = date[0];
	var adEndDate = date[1];
	var userAccount = $("#userAccount").val();
	var searchAdStatus = $("#searchAdStatus").val();
	var searchType = $("#searchType").val();
	var adPvclkDevice = $("#adPvclkDevice").val();
	var url = "adKeywordView.html?adGroupSeq=" + adGroupSeq + "&adStartDate=" + adStartDate + "&adEndDate=" + adEndDate + "&dateType=" + dateType;

	if(userAccount != "")		url += "&userAccount=" + userAccount;
	if(searchAdStatus != "")	url += "&searchAdStatus=" + searchAdStatus;
	if(searchType != "0")		url += "&searchType=" + searchType;
	if(adPvclkDevice != "")		url += "&adKeywordPvclkDevice=" + adPvclkDevice;
	location.href = url;
}

function tableSorter(){
	
	$("#tableView").tablesorter({
		headers:{
			0 : { sorter: 'shortDate'},
			6 : { sorter: 'fancyNumber' },
			7 : { sorter: 'fancyNumber' },
			8 : { sorter: 'fancyNumber' },
			9 : { sorter: 'fancyNumber' },
			10 : { sorter: 'fancyNumber' },
			11 : { sorter: 'fancyNumber' },
			12 : { sorter: 'fancyNumber' },
			13 : { sorter: 'fancyNumber'}
		}
	});
}
	