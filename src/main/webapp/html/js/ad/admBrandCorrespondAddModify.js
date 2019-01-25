$(document).ready(function(){
	
	// 新增儲存
	$('#saveBtn').click(function() {
		if (checkValue()) {
			return false;
		}
		
		url = "admBrandCorrespondSave.html";
		data = {
		    	"brandEng": $("#brandEng").val().trim(),
		    	"brandCh": $("#brandCh").val().trim()
		    	};
		ajax();
	});

	// 修改儲存
	$('#updateBtn').click(function() {
		if (checkValue()) {
			return false;
		}

		url = "admBrandCorrespondUpdate.html";
		data = {
				"id": $("#dataId").val(),
		    	"brandEng": $("#brandEng").val().trim(),
		    	"brandCh": $("#brandCh").val().trim()
		    	};
		ajax();
	});
	
});

/**
 * 相關輸入值檢查
 * @returns {Boolean} 錯誤:true 都正確:false
 */
function checkValue() {
	var eng = $("#brandEng").val().trim();
	var ch = $("#brandCh").val().trim();
	if (!eng && !ch) {
		alert("請輸入英文關鍵字或中文關鍵字。");
		return true;
	}
//	else if (!/^[a-zA-Z]*$/.test(eng)) {
//		alert("英文關鍵字請輸入英文。");
//		return true;
//	}else if (!/^[0-9\u4e00-\u9fa5]*$/.test(ch)) {
//		alert("中文關鍵字請輸入中文。");
//		return true;
//	}
	return false;
}

var url;
var data;
function ajax() {
	$.ajax({
	    type: "post",
	    dataType: "json",
	    url: url,
	    data: data,
	    timeout: 30000,
	    error: function(xhr){
	        alert('Ajax request 發生錯誤');
	    },
		success : function(response, status) {
			if (response.status == "ERROR") {
				alert(response.msg);
			} else {
				window.location = "admBrandCorrespond.html";
			}
		}
	});
}