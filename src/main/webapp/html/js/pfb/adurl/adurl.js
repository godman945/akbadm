$(function() {
	
	//datepicker
	$( "#startDate" ).datepicker({
		format : "yyyy-mm-dd",
		language : "zh-TW",
		showOn: "focus",
	    defaultDate: "+1w",
	    changeMonth: true,
	    numberOfMonths: 1,
	    onClose: function( selectedDate ) {
	      $( "#endDate" ).datepicker( "option", "minDate", selectedDate );
	    }
	});
    $( "#endDate" ).datepicker({
    	format : "yyyy-mm-dd",
		language : "zh-TW",
    	showOn: "focus",
    	defaultDate: "+1w",
    	changeMonth: true,
    	numberOfMonths: 1,
    	onClose: function( selectedDate ) {
    		$( "#startDate" ).datepicker( "option", "maxDate", selectedDate );
    	}
    });
    
    $("#fastBlockUrlBT").click(function(){
    	var url = $("#fastBlockUrl").val();
    	var desc = $("#fastBlockDesc").val();
    	
    	if(url == "" || desc == ""){
    		alert("請輸入封鎖網址及原因");
    	}
    	else{
    		$("#blockStatus").val("block");
        	$("#blockDesc").val(desc);
        	$("#blockUrl").val(url);
        	
        	$("#reSearchForm").attr("action" , "pfbFastBlockurl.html");
        	$("#reSearchForm").submit();
    	}
    });
    
    $(".blockUrlBT").click(function(){
    	var url = $(this).attr("url");
    	var desc = $(this).prev().val();
    	
    	if(url != "" && desc != ""){
    		$("#blockStatus").val("block");
        	$("#blockDesc").val(desc);
        	$("#blockUrl").val(url);
        	$("#reSearchForm").attr("action" , "pfbBlockurl.html");
        	$("#reSearchForm").submit();
        	return true;
    	}
    	
		alert("請輸入封鎖原因");
		return false;
    	
    });
    
    $(".unblockUrlBT").click(function(){
    	var unblockId = $(this).attr("unblockId");
    	var url = $(this).attr("url");
    	
    	$("#blockStatus").val("unblock");
    	$("#unblockId").val(unblockId);
    	$("#blockUrl").val(url);
    	
    	$("#reSearchForm").attr("action" , "pfbUnBlockurl.html");
    	$("#reSearchForm").submit();
    });
	
	
	$("#searchForm_SubBT").click(function(){
		$("#searchForm").submit();
	});
	
	$("#reSearchForm_subBT").click(function(){
		$("#reSearchForm").submit();
	});
	
	$("#detail_bk_listBT").click(function(){
		
		$("#reSearchForm").attr("action" , "searchAdUrlList.html");
		$("#reSearchForm").submit();
		
		//location.href = "searchAdUrlList.html?id=" + $("#id").val() + "&keyword=" + $("#keyword").val() + "&category=" + $("#category").val();
	});
	
	//$("#tableView").tablesorter();
	$("#tableView").tablesorter({widgets: ['zebra']});
	
});

var admApp = angular.module('admApp', [ 'ngDialog' ]);
