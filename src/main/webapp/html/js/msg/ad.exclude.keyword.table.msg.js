$(document).ready(function(){
		
	$("#exit").click(function(){
		parent.$.fancybox.close();
	});
		
});

function checkAll(){
	
	if ($("#keywordTableView input:checkbox[name='chk']").attr('checked')) {
		$("#keywordTableView input:checkbox[name='chk']").attr('checked', false);
	}else{
		$("#keywordTableView input:checkbox[name='chk']").attr('checked', true);
	}
}

function modifyAdExcludeKeywordStatus(status){

	var adExcludeKeywordSeq = [];
	
	for(var i=0;i<$("#keywordTableView input:checkbox").length;i++){
		if($("#chkExcludeKeyword_"+i).attr('checked')){			
			adExcludeKeywordSeq.push($("#chkExcludeKeyword_"+i).val());
		}
	}
	
	if(adExcludeKeywordSeq.length > 0){
		
		$("#adExcludeKeywordSeq").val(adExcludeKeywordSeq.toString());
		$("#adExcludeKeywordStatus").val(status);
		
		
		if(parseInt(status) == 0){			
			$.fancybox({
				'href'     :'closeExcludeKeywordMsg.html' 		                    
			});
		}
		else{
			updateAdExcludeKeywordStatus();
		}
		
	}	
}

function updateAdExcludeKeywordStatus(){

	//$("#keywordTableForm").attr("action","updAdExcludeKeywordStatus.html");
	//$("#keywordTableForm").submit();
	//alert($("#adExcludeKeywordSeq").val()+"  , "+$("#adExcludeKeywordStatus").val());
	
	$.ajax({
		url: "updAdExcludeKeywordStatus.html",
		data:{
			 "adExcludeKeywordSeq": $("#adExcludeKeywordSeq").val(),
			 "adExcludeKeywordStatus": $("#adExcludeKeywordStatus").val()
		},
		type:"post",
		dataType:"html",
		success:function(response, status){				
			$.fancybox({
				'href'     :'adExcludeKeyword.html?adGroupSeq='+$("#adGroupSeq").val()		                    
			});
		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
	
	
	
}