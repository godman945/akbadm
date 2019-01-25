$(document).ready(function(){
	// 日期區間選擇
    $('#dateRangeSelect').click(function(){
    
        $.blockUI({
            message: $("#dateSelectDialog"),
            css: {
                width: "300px"
            },
            focusInput: false
        });
        
    });
	
	// 日期區間選擇 OK
    $('#dateSelectOk').click(function(){
    	
	    var selectValue = $('#selectRange').val();
	    
		if(selectValue=="self"){
			$("#IT_dateRange").val($("#startDate").val()+"~"+$("#endDate").val());
		}else{		
			$("#IT_dateRange").val($("#selectRange").val().split(",")[0]+"~"+$("#selectRange").val().split(",")[1]);			
		}
		
        $.unblockUI();       
        
        return false;
    });
	    
    // 日期區間選擇 cancel
    $('#dateSelectCancel').click(function(){
        $.unblockUI();
        return false;
    });
    
	// 日期區間 datepicker    
    $("#startDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
    	yearRange:"-10:+10"
	});
	
    $("#endDate").datepicker({
		showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
    	yearRange:"-10:+10"
    });

	$('#adActionStartDate').datepicker({
		showOn: "button",
		buttonImage: "html/img/icon_cal.gif",
		buttonImageOnly: true,
		yearRange:"-10:+10",
		rangeSelect: true, 
		minDate: 0,
		maxDate: '+1y',
		onClose:function() {
			$('#adActionEndDate').datepicker( "option", "minDate", new Date(Date.parse($(this).val().replace(/-/g,"/"))) );
		}
	});

	$('#adActionEndDate').datepicker({
		showOn: "button",
		buttonImage: "html/img/icon_cal.gif",
		buttonImageOnly: true,
		yearRange:"-10:+10",
		rangeSelect: true, 
		maxDate: '+1y',
		onClose:function() {
			document.getElementsByName("selAdActionEndDate")[1].checked = true;
		}
	});
    
});
