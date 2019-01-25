$(document).ready(function(){
  if($("#size").val() !=""){
	  $("#sizeSelect option").each(function () {
		  if(this.text == $("#size").val()){
			  $(this).attr("selected","true");
		  }
	  });
  }
  
  if($("#adType").val() != ""){
	  $("#adTypeSelect option").each(function () {
		  if(this.value == $("#adType").val()){
			  $(this).attr("selected","true");
			  templateTypeSelect();
		  }
	  });
  }
  
  if($("#templateAdType").val() != ""){
	  $("#templateContent option").each(function () {
		  if(this.value == $("#templateAdType").val()){
			  $(this).attr("selected","true");
		  }
	  });
  }
  
  if($("#keyWordText").val() != ""){
	  $("#keyWord").val($("#keyWordText").val());
  }
});
function templateTypeSelect(){
	if($("#adTypeSelect option:selected" ).val() == "0"){
		return false;
	}
	
	if($("#adTypeSelect option:selected" ).val() == "1"){
		$("#content").css("display","none");
		$("#search").css("display","block");
		
	}
	
	if($("#adTypeSelect option:selected" ).val() == "2"){
		$("#content").css("display","block");
		$("#search").css("display","none");
		$("#keyWord").attr("value","");
	}
}

function searchBtn(){
	if($("#adTypeSelect option:selected" ).val() == "1" && $("#keyWord").val() == ""){
		alert("請輸入關鍵字");
		return false;
	}
	if($("#adTypeSelect option:selected" ).val() == "2" && $("#templateContent option:selected" ).val() == "0"){
		alert("選擇廣告類型");
		return false;
	}
	
	if($("#adTypeSelect option:selected" ).val() == "0"){
		alert("請選擇類型");
		return false;
	}
	$("#templateAdType").val($("#templateContent option:selected" ).val());
	$("#adType").val($("#adTypeSelect option:selected" ).val());
	$("#keyWordText").val($("#keyWord" ).val());
	$("#size").val($("#sizeSelect option:selected" ).text());
	$("#templateProductSearchForm").submit();
}
