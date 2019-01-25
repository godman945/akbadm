$(document).ready(function(){
	
	
    
    $("input[name='adExcludeKeywordStop']").click(function(){
    	updateAdExcludeKeywordStatus(2);
    });
    
    $("input[name='adExcludeKeywordStart']").click(function(){
    	updateAdExcludeKeywordStatus(1);
    });
    
    $("input[name='adExcludeKeywordClose']").click(function(){   	
    	$.blockUI({ message: $('#closeAdExcludeKeyword')}); 
    });
    
    $("#closeAdExcludeKeyword input:button[name='yes']").click(function() { 
    	$.unblockUI();  
    	updateAdExcludeKeywordStatus(0);
    }); 

    $("#closeAdExcludeKeyword input:button[name='no']").click(function() { 
        $.unblockUI(); 
        return false; 
    });
    
    
});



function checkAll(){	
	if ($("#adExcludeKeywordTable input:checkbox").attr('checked')) {
		$("#adExcludeKeywordTable input:checkbox").attr('checked', false);
	}else{
		$("#adExcludeKeywordTable input:checkbox").attr('checked', true);
	}
}

function updateAdExcludeKeywordStatus(status){

	var adExcludeKeywordSeq = [];
	
	for(var i=0;i<$("#adExcludeKeywordTable input:checkbox").length;i++){
		if($("#chkAdExcludeKeyword_"+i).attr('checked')){			
			adExcludeKeywordSeq.push($("#chkAdExcludeKeyword_"+i).val());
		}
	}
	
	if(adExcludeKeywordSeq.length > 0){
		$("#tableForm").attr("action","updAdExcludeKeywordStatus.html");
		$("#adExcludeKeywordSeq").val(adExcludeKeywordSeq.toString());
		$("#status").val(status);
		$("#tableForm").submit();
	}	
}

