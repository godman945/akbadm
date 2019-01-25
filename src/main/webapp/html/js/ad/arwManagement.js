$(document).ready(function(){
	
	// 新增頁，儲存按鈕submit前資料檢查
	$("#saveBtn").click(function() {
		return checkValue();
	});
	
	// 修改頁，儲存按鈕submit前資料檢查
	$("#updateBtn").click(function() {
		return checkValue();
	});
	
	// 開始日期
    $("#startDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd"
    });
    
	// 結束日期
    $("#endDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
		changeMonth: false,
		changeYear: false,
        dateFormat: "yy-mm-dd"	
    });
    
    checkDateFlag(); // 進入時檢查
    changeDateFlagAndStartDateEndDate(); // 改變下拉選單時判斷

    //依據選擇的pfd經銷商代出相對應的pfp
    $('#pfdCustomerSelect').change(function() {
    	
    	//選擇請選擇，清除連動
    	var pfdCustomer = $("#pfdCustomerSelect").val();
    	if(!pfdCustomer){
    		$("#customerInfoId option").remove();
    		$("#customerInfoId").append('<option value="">請選擇</option>');
//    		$("#customerInfoTitle").val(""); 隱藏欄位
    		return false;
    	}
    	
    	$.ajax({
    		url:"selectPfpCustomerListAjax.html",
    		data:{
    			"pfdCustomerInfoId":pfdCustomer
    		},
    		type:"post",
    		dataType:"json",
    		success:function(response, status){
    			if(response.status == "SUCCESS"){
    				var dataMap = response.dataMap;
//    				console.log(dataMap);
//    				console.log(dataMap[0]);
    				$("#customerInfoId option").remove();
    	    		$("#customerInfoId").append('<option value="">請選擇</option>');
    				$.each(dataMap ,function(index, list) {
    					$("#customerInfoId").append('<option value="' + list.customerInfoId + '">' + list.customerInfoId + ' / ' + list.customerInfoTitle + '</option>');
    	        		console.log(index + ":" + list.customerInfoId + "/" + list.customerInfoTitle);
    	        	});
    			}
    		},
    		error: function(xtl) {
    			alert("系統繁忙，請稍後再試！");
    		}
    	});
	});
    
    //處理PFP名稱
    $('#customerInfoId').change(function() {
    	var name = $('#customerInfoId').find(":selected").text();
    	name = name.substring(name.indexOf('/') + 2);
    	$("#customerInfoTitle").val(name);
	});
    
});

// 下拉選單依選項改變走期狀態與日期起迄值
function changeDateFlagAndStartDateEndDate() {
	$("#dateFlag").change(function() {
		checkDateFlag();
	});
}

// 檢查目前下拉選單值，做相對應改變
function checkDateFlag() {
	if ($('#dateFlag').val() == 0) {
		$(".ui-datepicker-trigger, .hasDatepicker").hide();
		$(".hasDatepicker").val("");
	} else {
		$(".ui-datepicker-trigger, .hasDatepicker").show();
	}
};

// 檢查畫面上欄位是否都輸入正確
function checkValue() {
	var arw = $("#arwValue").val();
	
	if (!$("#pfdCustomerSelect").val()) {
		alert("經銷商帳號尚未選擇。");
		return false;
	} else if (!$("#customerInfoId").val()) {
		alert("PFP 帳號尚未選擇。");
		return false;
	} else if (!arw) {
		alert("請輸入ＡＲＷ值。");
		return false;
	} else if (parseInt(arw) < 2 || parseInt(arw) > 20) {
		alert("ＡＲＷ值超出範圍。");
		return false;
	} else if ($("#dateFlag").val() == "1"
			&& (!$("#startDate").val() || !$("#endDate").val())) {
		alert("起迄日期未輸入完整。");
		return false;
	}
}

// 進入新增畫面
function addView() {
	window.location = "arwManagementAdd.html";
}

// 進入修改畫面
function modifyView(id) {
	window.location = "arwManagementModify.html?customerInfoId=" + id;
}

// 查詢列表頁(arwManagement.html)，刪除按鈕
function deleteArwValue(id) {
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
	            window.location = "arwManagementDelete.html?customerInfoId="+id;
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