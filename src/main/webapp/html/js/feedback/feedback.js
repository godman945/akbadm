$(document).ready(function(){ 
	
	searchRecord();
	
	$("#sDate").datepicker({
    	dateFormat: "yy-mm-dd"
    });
	
	$("#eDate").datepicker({
    	dateFormat: "yy-mm-dd"
    });
	
    $("#add").click(function(){
    	location.href = 'feedbackRecordAdd.html';
    });
    
    $("#search").click(function(){
    	searchRecord();
    });
    
    $("#delete").click(function(){
    	deleteRecord();
    });
    
});

function searchRecord(){
	
	$.ajax({
		url:"searchFeedbackRecord.html",
		data:{
			"accountName":$("#accountName").val(),
			"sDate":$("#sDate").val(),
			"eDate":$("#eDate").val()
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#feedbackRecordTable").html(response);	
			//page();
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
}

function deleteRecord(){
	
	var recordId = [];
	
	for(var i=0;i<$("#tableView input:checkbox").length;i++){
		if($("#chk_"+i).attr('checked')){			
			recordId.push($("#chk_"+i).val());
		}
	}
	
	$.ajax({
		url:"feedbackRecordDelete.html",
		data:{
			"accountName":$("#accountName").val(),
			"sDate":$("#sDate").val(),
			"eDate":$("#eDate").val(),
			"recordIds":recordId.toString()
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			alert("刪除資料成功!!");
			$("#feedbackRecordTable").html(response);	
			//page();
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
}

//function accountInfo(id){
//	//$("#accountInfo").attr("href","./accountModify.html?customerInfoId="+id);
//	location.href="accountModify.html?customerInfoId="+id;
//}
//
////分頁功能
//function wantSearch(pageNo) {
//
//    if (pageNo != null) {
//        $("#pageNo").val(pageNo);
//    }
//    searchAccount();
//}

