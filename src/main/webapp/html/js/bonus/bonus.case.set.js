$(document).ready(function(){
	
	$("#saveBtn").click(function(){

		var selCaseBonusSet = [];
		
		for(var i=1;i<=$("#caseBonusSetForm input:checkbox").length;i++){
			
			if($("#chkBonusSet"+i).attr('checked')){	
				
				if($("input[name=rdoBonusSet"+i+"]:checked").val() != null){
					
					//alert($("#bonusCase"+i).val()+" , "+$("input[name=bonusBean"+i+"]:checked").val());
					
					var selSet = $("#chkBonusSet"+i).val()+"-"+$("input[name=rdoBonusSet"+i+"]:checked").val();
					
					selCaseBonusSet.push(selSet);
				}
				 
			}
		}
		$("#selCaseBonusSet").val(selCaseBonusSet);		
		$("#caseBonusSetForm").submit();
	});
});

function bonusContractSetList(){
	window.location = "bonusSetList.html";
}