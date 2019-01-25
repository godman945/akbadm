$(document).ready(function(){
	
	$("#addBtn").click(function(){
		window.location = "boardAdd.html";
	});

});



function modifyBoard(id){
	window.location = "boardModify.html?boardId="+id;
}

function deleteBoard(id){

	$("#dialog").html("是否要刪除這筆公告資料!!");
	$("#dialog").dialog({
		title: "刪除動件",
		closeOnEscape: false,
		height: 150,
		width: 250,
		modal: true,
		draggable: false,
		resizable: false, 
		buttons: {
            確定: function() {
                $(this).dialog("close");
                window.location = "boardDelete.html?boardId="+id;
            },
            取消: function() {
                $(this).dialog("close");
            }
        },
        open: function(event, ui) {
            //隱藏「x」關閉按鈕
            $(this).parent().children().children('.ui-dialog-titlebar-close').hide();
        }
	});
	
	
}