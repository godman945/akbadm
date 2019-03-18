$(document).ready(function() {
	//每頁顯示數量正確數量顯示判斷
	$("#pageSizeSelect").val($("#hiddenPageSize").val());

	// 每頁顯示數量選擇
	$("#pageSizeSelect").change(function() {
		$("#page").val("1");
		$("#queryFrom").submit();
	});

	processPageBtn();
});

function query() {
	$("#queryFrom").submit();
}

/**
* 處理上下頁按鈕功能
*/
function processPageBtn(){
	// 目前頁數
	var page = parseInt($('#pageNo').val());
	// 總頁數
	var totalPage = $('#pageCount').val();

	// 最前頁(第一頁)
	$('#fpage').click(function() {
		$("#pageNo").val("1");
		$("#queryFrom").submit();
	});

	// 上一頁
	$('#ppage').click(function() {
		// 頁數減1後，小於第一頁則回到第一頁
		page = page - 1;
		if (page < 1) {
			page = 1;
		}
		$("#pageNo").val(page);
		$("#queryFrom").submit();
	});

	// 下一頁
	$('#npage').click(function() {
		// 頁數加1後，超過總數頁則到總數頁
		page = page + 1;
		if (page > totalPage) {
			page = totalPage;
		}
		$("#pageNo").val(page);
		$("#queryFrom").submit();
	});

	// 最末頁(依總頁數到最後頁)
	$('#epage').click(function() {
		$("#pageNo").val($('#pageCount').val());
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