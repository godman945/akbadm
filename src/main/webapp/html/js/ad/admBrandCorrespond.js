$(document).ready(function(){
	
	// 查詢勾選的checkbox選其中一個
	$('input[name=checkboxButton]').click(function() {
		if ($(this).prop('checked')) {
			$('input[name=checkboxButton]').prop('checked', false);
			$(this).prop('checked', true);
		}
	});

	// 每頁顯示數量選擇
	$("#pageSizeSelect").change(function() {
		$("#page").val("1");
		$("#queryFrom").submit();
	});
	
	//每頁顯示數量正確數量顯示判斷
	$("#pageSizeSelect").val($("#excerptFrom #fpageSize").val());
	
	//勾選欄位調整回按查詢前的參數值
	$.each($('input[name=checkboxButton]') ,function(index, checkboxButton) {
		if($('#hiddenCheckboxVal').val() == $(checkboxButton).val()){
			$(checkboxButton).prop('checked', true);
		}
	});
	
	processPageBtn();
});

/**
 * 查詢功能
 */
function query() {
	$("#page").val("1");
	$('#queryFrom').submit();
}

/**
 * 進入新增畫面
 */
function addView() {
	window.location = "admBrandCorrespondAdd.html";
}

/**
 * 下載報表
 */
function download(){
	$('#excerptFrom').submit();
}

/**
 * 進入修改畫面
 * @param id
 */
function modifyView(id) {
	window.location = "admBrandCorrespondModify.html?id=" + id;
}

/**
 * 執行刪除
 * @param id
 */
function deleteBrandCorrespondValue(id) {
	$("#dialog").html("是否要刪除這筆資料?");
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
	            window.location = "admBrandCorrespondDelete.html?id=" + id;
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

/**
 * 處理上下頁按鈕功能
 */
function processPageBtn(){
	// 目前頁數
	var page = parseInt($('#excerptFrom #formPage').val());
	// 總頁數
	var totalPage = $('#excerptFrom #ftotalPage').val();

	// 最前頁(第一頁)
	$('#fpage').click(function() {
		$("#page").val("1");
		$("#queryFrom").submit();
	});

	// 上一頁
	$('#ppage').click(function() {
		// 頁數減1後，小於第一頁則回到第一頁
		page = page - 1;
		if (page < 1) {
			page = 1;
		}
		$("#page").val(page);
		$("#queryFrom").submit();
	});

	// 下一頁
	$('#npage').click(function() {
		// 頁數加1後，超過總數頁則到總數頁
		page = page + 1;
		if (page > totalPage) {
			page = totalPage;
		}
		$("#page").val(page);
		$("#queryFrom").submit();
	});

	// 最末頁(依總頁數到最後頁)
	$('#epage').click(function() {
		$("#page").val($('#totalPage').val());
		$("#queryFrom").submit();
	});
	
	if (totalPage == 0 || (page == 1 && totalPage == 1)) { // 總頁數為0時(表示沒資料)或僅只有一頁時
		$("#fpage").attr("src", $('#contentPath').val() + "page_first_disable.gif");
		$("#ppage").attr("src", $('#contentPath').val() + "page_pre_disable.gif");
		$("#epage").attr("src", $('#contentPath').val() + "page_end_disable.gif");
		$("#npage").attr("src", $('#contentPath').val() + "page_next_disable.gif");
		$("#fpage, #ppage, #epage, #npage").unbind(); // 移除事件
	} else if (page == 1) { // 目前頁數為第一頁時
		// 最前頁、上一頁按鈕改為灰色
		$("#fpage").attr("src", $('#contentPath').val() + "page_first_disable.gif");
		$("#ppage").attr("src", $('#contentPath').val() + "page_pre_disable.gif");
		$("#fpage, #ppage").unbind(); // 移除事件
		$("#epage, #npage").css("cursor", "pointer"); // 下一頁、最末頁，鼠標改為點擊手指
	} else if (page == totalPage) { // 目前頁數為最後一頁時
		// 最末頁、下一頁按鈕改為灰色
		$("#epage").attr("src", $('#contentPath').val() + "page_end_disable.gif");
		$("#npage").attr("src", $('#contentPath').val() + "page_next_disable.gif");
		$("#epage, #npage").unbind(); // 移除事件
		$("#fpage, #ppage").css("cursor", "pointer"); // 最前頁、上一頁，鼠標改為點擊手指
	} else { // 其他頁(第二到倒數第二頁)
		$("#fpage, #ppage, #epage, #npage").css("cursor", "pointer");
	}
}
