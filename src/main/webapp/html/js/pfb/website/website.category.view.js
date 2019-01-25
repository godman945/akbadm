var titleMsg = "<a href=\"javascript:findTableView('','無');\">第一層</a>";

$(document).ready(function(){
	findTableView('','無');
});
$(document).ajaxStart(function(){
    $.blockUI({
    	message: "<b><font size=5>查詢中...</font></b>",
        css: {
            border: '3px solid #aaa',
            padding: '15px',
            backgroundColor: '#fff',
            '-webkit-border-radius': '10px',
            '-moz-border-radius': '10px',
            opacity: .9,
            textAlign:      'center',
            cursor:         'wait',
        }
    });
}).ajaxStop($.unblockUI);

function findTableView(parentId,parentName){
	
	$.ajax({
		url: "websiteCategoryViewTable.html",
		data:{
			 "parentId": parentId,
			 "parentName": parentName
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$("#tableList").html(response);
			if(parentId != ''){
				var titleMsgList = titleMsg.split(" / ");
				titleMsg = "";
				for(var i=0;i<titleMsgList.length;i++){
					if(titleMsgList[i].indexOf(">" + parentName + "<") != -1){
						break;
					}
					
					if(i != 0){
						titleMsg += " / "
					}
					
					titleMsg += titleMsgList[i];
				}
				
				
				$("#nameText").html(titleMsg + " / " + parentName);
				titleMsg += " / <a href=\"javascript:findTableView('" + parentId + "','" + parentName + "');\">" + parentName + "</a>"
			} else {
				$("#nameText").html("無");
				titleMsg = "<a href=\"javascript:findTableView('','無');\">第一層</a>";
			}
		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
	
}

function addWebsiteCategory(){
	
	 $.fancybox(
	    		$('#websiteCategoryDiv').html(),
	    		{
	    			'modal'             : true,
	    			'autoDimensions'	: false,
	    			'width'         	: 'auto',
	    			'height'        	: 'auto',
	    			'autoSize'			: true,
	    			'autoHeight'		: true,
	    			'autoScale'			: true,
	    			'transitionIn'		: 'none',
	    			'transitionOut'		: 'none',
	    			'padding'			: 0,
	    			'overlayOpacity'    : .75,
	    			'overlayColor'      : '#fff',
	    			'scrolling'			: 'no'
	    		}
	    );
}

function saveBtn(){
	var categoryName = $("#fancybox-content #categoryName").val();
	var parentId = $("#parentId").val();
	var parentName = $("#parentName").val();
	
	
	$.ajax({
		url: "addWebsiteCategory.html",
		data:{
			 "parentId": parentId,
			 "parentName": parentName,
			 "categoryName": categoryName
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			parent.$.fancybox.close();
			findTableView(parentId,parentName);
			alert("新增成功!!");
		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
}

function updateWebsiteCategory(id,name){
	
	 $.fancybox(
	    		$('#websiteCategoryDiv').html(),
	    		{
	    			'modal'             : true,
	    			'autoDimensions'	: false,
	    			'width'         	: 'auto',
	    			'height'        	: 'auto',
	    			'autoSize'			: true,
	    			'autoHeight'		: true,
	    			'autoScale'			: true,
	    			'transitionIn'		: 'none',
	    			'transitionOut'		: 'none',
	    			'padding'			: 0,
	    			'overlayOpacity'    : .75,
	    			'overlayColor'      : '#fff',
	    			'scrolling'			: 'no'
	    		}
	    );
	 
	 $('#fancybox-content #websiteCategoryDivTitle').html("編輯分類");
	 $('#fancybox-content #saveButton').attr("onclick","updateBtn('" + id + "')");
	 $("#fancybox-content #categoryName").val(name);
}

function updateBtn(id){
	var categoryName = $("#fancybox-content #categoryName").val();
	var parentId = $("#parentId").val();
	var parentName = $("#parentName").val();
	
	
	$.ajax({
		url: "updateWebsiteCategory.html",
		data:{
			 "updateId": id,
			 "categoryName": categoryName
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			parent.$.fancybox.close();
			findTableView(parentId,parentName);
			alert("修改成功!!");
		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
}

function closeBtn(){
	parent.$.fancybox.close();
}