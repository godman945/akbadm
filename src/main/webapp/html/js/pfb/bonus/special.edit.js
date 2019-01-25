$(document).ready(function(){
	// 日期區間 datepicker 
	$('#startDate').datepicker({
		showOn: "button",
		buttonImage: "html/img/icon_cal.gif",
		buttonImageOnly: true,
		yearRange:"-10:+10",
		rangeSelect: true, 
		minDate: 0,
		maxDate: '+1y',
		onClose:function() {
			if(new Date(Date.parse($(this).val().replace(/-/g,"/"))) < new Date()){
    			$('#endDate').datepicker( "option", "minDate", new Date() );	
    		} else {
    			$('#endDate').datepicker( "option", "minDate", new Date(Date.parse($(this).val().replace(/-/g,"/"))) );
    		}
		}
	});

	$('#endDate').datepicker({
		showOn: "button",
		buttonImage: "html/img/icon_cal.gif",
		buttonImageOnly: true,
		yearRange:"-10:+10",
		rangeSelect: true, 
		maxDate: '+1y',
		onClose:function() {
			
		}
	});
});

function save(){
	$("#chkSpecialName").html("");
	$("#chkStartDate").html("");
	$("#chkEndDate").html("");
	$("#chkPfbOercent").html("");
	
	if($("#specialName").val().trim() == ""){
		$("#chkSpecialName").html("請輸入活動名稱!");
		$("#specialName").focus();
		//location.href = "#endDate";
		return false;
	}
	if($("#startDate").val().trim() == ""){
		$("#chkStartDate").html("請輸入開始日期!");
		$("#startDate").focus();
		//location.href = "#startDate";
		return false;
	}
	if($("#endDate").val().trim() == ""){
		$("#chkEndDate").html("請輸入結束日期!");
		$("#endDate").focus();
		//location.href = "#endDate";
		return false;
	}
	if($("#pfbPercent").val().trim() == ""){
		$("#chkPfbPercent").html("請輸入客戶分潤!");
		$("#pfbPercent").focus();
		//location.href = "#pfbOercent";
		return false;
	}
	
	var pfbOercent = $("#pfbPercent").val().trim();

	if(isNaN(pfbOercent)){
		$("#chkPfbPercent").html("客戶分潤只能輸入數字!");
		$("#pfbPercent").focus();
		//location.href = "#pfbOercent";
		return false;
	}
	
	if(pfbOercent > 100){
		$("#chkPfbPercent").html("客戶分潤最多只能為100%!");
		$("#pfbPercent").focus();
		//location.href = "#pfbOercent";
		return false;
	}
	
	$.ajax({
        url: "chkSpecialDateAjax.html",
        data: {
            "pfbId":$("#pfbId").val(),
            "startDate":$("#startDate").val(),
            "endDate":$("#endDate").val()
        },
        dataType: "json",
        success: function (response) {
        	if(response == ""){
        		$("#modifyForm").submit();
        	} else {
        		alert(response);
        	}
        }
	});
}

//取消
function cancerSubmit(){
	$(location).attr( 'href' , 'editPfbBonusSet.html?pfbId=' + $("#pfbId").val());
}