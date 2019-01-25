$(document).ready(function(){ 
	
	$("#viewBtn").click(function(){
		window.location.href = "managerList.html";
	});
	

});

function selAll(id){	

	
	if ($("#table_"+id+" input:checkbox[name='pfpAccount']").attr('checked')) {
		$("#table_"+id+" input:checkbox[name='pfpAccount']").attr('checked', false);
	}else{
		$("#table_"+id+" input:checkbox[name='pfpAccount']").attr('checked', true);
	}
}
