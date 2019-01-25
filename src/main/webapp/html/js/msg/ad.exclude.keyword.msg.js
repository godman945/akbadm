$(document).ready(function(){
		
	$("#closeExcludeKeywordStatus input:button[name='yes']").click(function(){
		parent.updateAdExcludeKeywordStatus();
	});
	
	$("#closeExcludeKeywordStatus input:button[name='no']").click(function(){
		//parent.$.fancybox.close();
		$.fancybox({
			'href'     :'adExcludeKeyword.html?adGroupSeq='+parent.$("#adGroupSeq").val()		                    
		});
	});
		
});



