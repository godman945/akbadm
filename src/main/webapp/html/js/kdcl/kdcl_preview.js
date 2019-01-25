$(document).ready(function(){
	if($("#defaultcustomerInfoId").val() != ""){
		$("#customerInfoSelect option").each(function() {
			if($("#defaultcustomerInfoId").val()  == this.value){
				$(this).attr('selected', 'selected');
			}
		});
	}
	
	
	if($("#defaultPId").val() != ""){
		$("#positionNameSelect option").each(function() {
			console.log(this.value);
			if($("#defaultPId").val()  == this.value){
				$(this).attr('selected', 'selected');
			}
		});
	}
	if($("#pfbxCustomerId").val() == "" && $("#defaultcustomerInfoId").val() != "" ){
		$("#pfbxCustomerId").val($("#defaultcustomerInfoId").val());
	}
	
	
	
	
	
	
	
	//輸入高度
	$("#padHeightTextId").blur(function(){
		console.log($("#padHeightTextId").val());
		$("#padHeight").val($("#padHeightTextId").val());
	});
	//輸入寬度
	$("#padWidthTextId").blur(function(){
		$("#padWidth").val($("#padWidthTextId").val());
	});
	
	
	
	
})

var obj = null;
function memberIdSelect(){
	$("#pfbxCustomerId").val($("#customerInfoSelect option:selected" ).val());
	 $.ajax({
			url : "adKdclPosition.html",
			type : "post",
			dataType : "json",
			data : {
	    		"pfbxCustomerId" : $("#customerInfoSelect option:selected" ).val()
			},
			success : function(respone) {
				console.log(respone);
				obj  = JSON.parse(respone);
				
			},
			error : function() {
	    		alert("儲存錯誤!!");
			}
		}).done(function() {
			$("#positionNameSelect").children().slice(1).detach();
			for (var i = 0; i < obj["data"].length ; i++) {
				$("#positionNameSelect").append('<option value='+obj["data"][i].pId+'>'+obj["data"][i].pName+'</option>');
			}
		});
}

function positionNameTypeSelect(){
	$("#pid").val($("#positionNameSelect option:selected" ).val());
	$("#kdclAdFormId").submit();
}


function submitSize(){
	$("#pid").val($("#defaultPId").val());
	$("#kdclAdFormId").submit();
}

