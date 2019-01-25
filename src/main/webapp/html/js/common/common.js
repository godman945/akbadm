$(document).ready(function(){
	$('#paramParentDeptId').change(function(){
		 var parentDeptId = $('#paramParentDeptId').val();
		 $.ajax({
	            url: 'changeChildDeptAJAX.html',
	            data : {'parentDeptId' : parentDeptId},
	            type: 'post',
	            beforeSend: function() {
	                if (parentDeptId == "0") {
	                    alert("請選擇部門！");
	                    return false;
	                }
	            },
	            dataType: 'json',
	            success: function(response, status) {
	                var htmlContent = response.outputHtml2;
	                document.getElementById('childDeptOptionId').innerHTML = htmlContent;
	            },
	            error: function(xtl) {
	                alert("系統繁忙，請稍後再試！");
	            }
	        });
	});
});

function isEngNum(text){
	if(text == ""){
		return false;
	}
	
	text = text.toLowerCase();
	var str = "abcdefghijklmnopqrstuvwxyz";
	var num = "0123456789";
	for(var i = 0; i < text.length; i++){
		var ch = text.charAt(i);
		if((num.indexOf(ch) < 0) && ((str.indexOf(ch) < 0))){
			return false;
		}
	}
	return true;
}

function isEng(text){
	if(text == ""){
		return false;
	}
	
	text = text.toLowerCase();
	var str = "abcdefghijklmnopqrstuvwxyz";
	for(var i = 0; i < text.length; i++){
		var ch = text.charAt(i);
		if(str.indexOf(ch) < 0){
			return false;
		}
	}
	return true;
}

function isNum(text){
	if(text == ""){
		return false;
	}
	
	var num = "0123456789";
	for(var i = 0; i < text.length; i++){
		var ch = text.charAt(i);
		if(num.indexOf(ch) < 0){
			return false;
		}
	}
	return true;
}

function selectAll(src, dest) {
    if (src.checked) {
        $("input[name=" + dest + "]").each(function() {
            $(this).attr("checked", true);
        });
    } else {
        $("input[name=" + dest + "]").each(function() {
            $(this).attr("checked", false);
        });
    }
}

function page(){
    var pageNo = parseInt($("#pageNo").val());
    var selectPageSize = parseInt($("#selectPageSize").val());
    var pageCount = parseInt($("#pageCount").val());
    var contentPath = $("#contentPath").val();
    
    //pageButton 顯示第一頁
    if (pageNo > 1) {
        $("#fpage").attr("src", contentPath + "page_first.gif")
                    .css("cursor", "pointer");
        $("#ppage").attr("src", contentPath + "page_pre.gif")
                    .css("cursor", "pointer");
        
        //pageButton 第一頁
        $("#fpage").click(function(){
            wantSearch(1);
        });
        
        //pageButton 上一頁
        $("#ppage").click(function(){
            wantSearch(pageNo - 1);
        });
    }
    
    //pageButton 顯示最後一頁
    if (pageNo < pageCount) {
        $("#npage").attr("src", contentPath + "page_next.gif")
                    .css("cursor", "pointer");
        $("#epage").attr("src", contentPath + "page_end.gif")
                    .css("cursor", "pointer");
        
        //pageButton 下一頁
        $("#npage").click(function(){
            wantSearch(pageNo + 1);
        });
        
        //pageButton 最後一頁
        $("#epage").click(function(){
            wantSearch(pageCount);
        });
    }
    
    //每頁顯示數量正確數量顯示判斷
    $("#pageSize").children().each(function(){
        if ($(this).text() == selectPageSize) {
            $(this).attr("selected", "true"); //或是給selected也可
        }
    });
    
    //每頁顯示數量選擇
    $("#pageSize").change(function(){
        wantSearch(1);
    });
}

