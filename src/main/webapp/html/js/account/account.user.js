$(document).ready(function(){ 
	
	$("#modifyBtn").click(function(){
		$("#userForm").submit();
	});
    
	$("#userLog").click(function(){
		$("#userLog").attr("href","accountUserAccesslog.html?userId="+$("#userId").val());
	});
	
	//$("#logHref").attr("href","findUserAccesslog.html?userId="+$("#userId").val());
});

