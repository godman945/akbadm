$(document).ready(function(){ 
	
	chooseType();	

	
	$("#now").click(function(){
		$("#giftDate").datepicker('setDate', new Date());		
	});
	
	$("#giftDate").datepicker({
    	dateFormat: "yy-mm-dd",
    	minDate: "0"
    });

	$("#inviledDate").datepicker({
    	dateFormat: "yy-mm-dd",
    	minDate: "+1"
    });

	$("#viewBtn").click(function(){
    	location.href = 'feedbackRecord.html';
    });
	
	 $("#memberId").keyup(function(){
			$("#errorMessage").html("");
		}).blur(function(){
			if($(this).val() != ""){
				$("#errorMessage").html("");
			}
		});
	 
	 //目前先只有"PChome直客管理經銷商"可選擇，其他先鎖定
	 $('input[name=pfdAccount]').each(function(){
		if($(this).val() != 'PFDC20140520001'){
			$(this).attr("disabled","disabled");
		} 
	 });
	 
	 //判斷經銷商有無取消勾選，若沒有經銷商被勾選，則最後一個取消勾選的經銷商會被選取
	 $('input[name=pfdAccount]').change(function (){
		 var check = false;
		 $('input[name=pfdAccount]').each(function(){
			 if($(this).attr('checked')){
				 check = true;
			 }
		 });
		 
		 if(!check){
			 $(this).attr('checked','checked');
		 } 
	 });
});

function chooseType(){
	
	$("#all").click(function(){
		$("#memberId").attr('disabled', true);
		$("#memberId").val("");
		$("#errorMessage").html("");
		$("#pfdIdTr").show();
	});
	
	$("#only").click(function(){
		$("#memberId").attr('disabled', false);
		$("#pfdIdTr").hide();
	});
	
}

//function searchRecord(){
//	
//	$.ajax({
//		url:"searchFeedbackRecord.html",
//		data:{
//			"accountName":$("#accountName").val(),
//			"giftDate":$("#giftDate").val()
//		},
//		type:"post",
//		dataType:"html",
//		success:function(response, status){
//			$("#feedbackRecordTable").html(response);	
//			//page();
//		},
//		error: function(xtl) {
//			
//			alert("錯誤訊息","系統繁忙，請稍後再試！");
//		}
//	});
//}
//
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

