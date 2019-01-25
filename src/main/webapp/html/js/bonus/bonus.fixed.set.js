$(document).ready(function(){
	
	$("#saveBtn").click(function(){

		var selFixedBonusSet = [];
		
		for(var i=1;i<=$("#fixedBonusSetForm input:checkbox").length;i++){
			
			if($("#chkBonusSet"+i).attr('checked')){	
				
				
				if($("input[name=rdoBonusSet"+i+"]:checked").val() != null){
					
					//alert($("#chkBonusSet"+i).val()+" , "+$("input[name=rdoBonusSet"+i+"]:checked").val());
					
					var selSet = $("#chkBonusSet"+i).val()+"-"+$("input[name=rdoBonusSet"+i+"]:checked").val();
					
					selFixedBonusSet.push(selSet);
				}
				 
			}
		}
		$("#selFixedBonusSet").val(selFixedBonusSet);		
		$("#fixedBonusSetForm").submit();
	});
});

function bonusContractSetList(){
	window.location = "bonusSetList.html";
}