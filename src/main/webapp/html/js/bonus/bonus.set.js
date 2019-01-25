$(document).ready(function(){

	
});

function fixedBonusSet(id){

	$("#formBonusSet").attr("action","fixedBonusSet.html");
	$("#pfdContractId").val(id);
	$("#formBonusSet").submit();
}

function caseBonusSet(id){
	
	$("#formBonusSet").attr("action","caseBonusSet.html");
	$("#pfdContractId").val(id);
	$("#formBonusSet").submit();
	
}