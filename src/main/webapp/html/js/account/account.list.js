$(document).ready(function(){ 
	
	searchAccount();
	
    $("#searchBtn").click(function(){
    	searchAccount();
    });
    
});

function searchAccount(){
	
	$.ajax({
		url:"accountSearch.html",
		data:{
			"keyword":$("#keyword").val(),
			"userAccount":$("#userAccount").val(),
			"userEmail":$("#userEmail").val(),
			"customerInfoStatus":$("#customerInfoStatus").val(),
			"pageNo": $("#pageNo").val(),
			"pageSize": $("#pageSize").val(),
			"pfdCustomerId": $("#pfdCustomerSelect").val(),
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#accountListTable").html(response);	
			page();
		},
		error: function(xtl) {
			
			alert("錯誤訊息","系統繁忙，請稍後再試！");
		}
	});
}

function accountInfo(id){
	//$("#accountInfo").attr("href","./accountModify.html?customerInfoId="+id);
	location.href="accountModify.html?customerInfoId="+id;
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    searchAccount();
}

