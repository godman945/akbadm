$(document).ready(function(){
	if($("#adGroupSeq").val() != "") {
		findTableView();
    }

	//自定義隱藏欄位
    $.tablesorter.addParser({
        // set a unique id
        id: 'rangesort',
        is: function(s) {
        	s = s.replace("$ ", "");
            return /^[0-9]?[0-9,\.]*$/.test(s);
        },
        format: function(s) {
        	s = s.replace("$ ", "");
            return $.tablesorter.formatFloat(s.replace(/,/g, ''));
        },
        // set type, either numeric or text
        type: 'numeric'
    });
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

var changeSelect = false;
function findTableView(){
	var adclkDevice = $("#adclkDevice").val();
	var date = $("#IT_dateRange").val().split("~");
	var startDate = date[0];
	var endDate = date[1];
	var dateType = $("#dateType").val();
	var userAccount = $("#userAccount").val();
	var searchAdStatus = $("#searchAdStatus").val();
	var searchType = $("#searchType").val();
	var keyword = $("#keyword").val();
	var adKeywordPvclkDevice = $("#adKeywordPvclkDevice").val();
	var pageNo = $("#pageNo").val();
	var pageSize = $("#pageSize").val();
	var adGroupSeq = $("#adGroupSeq").val();
	$("#adKeywordPvclkDevice").change(function(){
		changeSelect = true;
	});
	$.ajax({
		url: "adKeywordViewTable.html",
		data:{
			 "startDate": startDate,
			 "endDate": endDate,
			 "dateType": dateType,
			 "userAccount":userAccount,
			 "searchAdStatus":searchAdStatus,
			 "searchType": searchType,
			 "keyword": keyword,
			 "adKeywordPvclkDevice": adKeywordPvclkDevice,
			 "pageNo": pageNo,
			 "pageSize": pageSize,
			 "adGroupSeq": adGroupSeq,
			 "adclkDevice":adclkDevice,
			 "changeSelect":changeSelect
		},
		type:"post",
		dataType:"html",
		success:function(response, status){				
			$("#tableList").html(response);			
			page();
			loadValidate();
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
	var adKeywordPvclkDevice = $("#adKeywordPvclkDevice").val();
	var url = "adAdView.html?adGroupSeq=" + adGroupSeq + "&adStartDate=" + adStartDate + "&adEndDate=" + adEndDate + "&dateType=" + dateType;

	if(userAccount != "")		url += "&userAccount=" + userAccount;
	if(searchAdStatus != "")	url += "&searchAdStatus=" + searchAdStatus;
	if(searchType != "0")		url += "&searchType=" + searchType;
	if(adKeywordPvclkDevice != "")		url += "&adPvclkDevice=" + adKeywordPvclkDevice;
	location.href = url;
}

function loadValidate(){
	$("#tableForm").validate({  
		unhighlight:function(element, errorClass, validClass){
			$("#br").remove();
			$("label").remove(".labsu");
	    },
		highlight:function(element, errorClass, validClass){
			$("#br").remove();
			$("label").remove(".labsu");
	    },
		errorPlacement:function(error, element){
			error.insertAfter(element.next());
		}
	});
}

function adExcludeKeyword(seq){
	
	$.fancybox({
		'href'     :'adExcludeKeyword.html?adGroupSeq='+seq		                    
	});
}

function tableSorter(){
	
	$("#tableView").tablesorter({
		headers:{
	    	    0 : { sorter: false },
	    	    8 : {sorter:false},
				9 : {sorter:false},
				10 : {sorter:false},
				11 : {sorter:false},
				12 : {sorter:false},
				13 : {sorter:false},
				14 : {sorter:false},
				15 : {sorter:false},
				16 : {sorter:false},
				17 : {sorter:false},
				21 : { sorter: 'rangesort' },
				22 : { sorter: 'rangesort' },
				23 : { sorter: 'rangesort' },
				24 : { sorter: 'fancyNumber' },
				25 : { sorter: 'fancyNumber' },
				26 : { sorter: 'fancyNumber' },
				27 : { sorter: 'fancyNumber' },
				28 : { sorter: 'fancyNumber' },
				29 : { sorter: 'fancyNumber' },
				30 : { sorter: 'fancyNumber' },
				31 : { sorter: 'fancyNumber' },
				32 : { sorter: 'fancyNumber' },
				33 : { sorter: 'fancyNumber' },
				34 : { sorter: 'fancyNumber' },
				35 : { sorter: 'fancyNumber' },
				36 : { sorter: 'fancyNumber' },
				37 : { sorter: 'fancyNumber' },
				38 : { sorter: 'fancyNumber' },
				39 : { sorter: 'fancyNumber' },
				40 : { sorter: 'rangesort' },
				41 : { sorter: 'rangesort' },
				42 : { sorter: 'rangesort' },
				43 : { sorter: 'rangesort' },
				44 : { sorter: 'rangesort' },
				45 : { sorter: 'rangesort' },
				46 : { sorter: 'rangesort' },
				47 : { sorter: 'rangesort' },
				48 : { sorter: 'rangesort' },
				49 : { sorter: 'rangesort' },
				50 : { sorter: 'rangesort' },
				51 : { sorter: 'rangesort' },
				52 : { sorter: 'fancyNumber' },
				53 : { sorter: 'fancyNumber' },
				54 : { sorter: 'fancyNumber' }
		}
	});
}

//明細按鈕(展開/隱藏)
function toggleTd(tdClass){
	var number = $("." + tdClass + "Th").attr("colspan");
	if(number == 1){
		$("." + tdClass + "Button").val("隱藏");
		$("." + tdClass + "Th").attr("colspan","4");
	} else {
		$("." + tdClass + "Button").val("展開");
		$("." + tdClass + "Th").attr("colspan","1");
	}
	
	$("." + tdClass).toggle();
}