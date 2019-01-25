$(document).ready(function(){
	
	ready();
	
	// 日期區間選擇
    $('#dateRangeSelect').click(function(){
    	$.fancybox({
    		'onStart'	: function() { $("#dateSelectDialog").css("display","block"); },            
            'onClosed'	: function() { $("#dateSelectDialog").css("display","none"); },
    		'href'      :'#dateSelectDialog'  		                    
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
		
		//findTableView();
		      
		$.fancybox.close();
    });
	    
    // 日期區間選擇 cancel
    $('#dateSelectCancel').click(function(){
    	$.fancybox.close();
    });
    
	// 日期區間 datepicker    
    $("#startDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
    	yearRange:"-10:+10",
		onClose:function() {
			$('#endDate').datepicker( "option", "minDate", new Date(Date.parse($(this).val().replace(/-/g,"/"))) );
		}
    });        
	
    $("#endDate").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
    	yearRange:"-10:+10",
		onClose:function() {
			$('#startDate').datepicker( "option", "maxDate", new Date(Date.parse($(this).val().replace(/-/g,"/"))) );
		}
    		
    });
    
 // 日期區間第二組 datepicker    
    $("#startDate2").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
    	yearRange:"-10:+10",
		onClose:function() {
			$('#endDate2').datepicker( "option", "minDate", new Date(Date.parse($(this).val().replace(/-/g,"/"))) );
		}
    });        
	
    $("#endDate2").datepicker({
    	showOn: "button",
    	buttonImage: "html/img/icon_cal.gif",
    	buttonImageOnly: true,
    	yearRange:"-10:+10",
		onClose:function() {
			$('#startDate2').datepicker( "option", "maxDate", new Date(Date.parse($(this).val().replace(/-/g,"/"))) );
		}
    		
    });
    
    $("#search").click(function(){
		findTableView();
    });
     
    
});

function ready(){
	
	var dateRange = $("#startDate").val()+"~"+$("#endDate").val();
	$("#IT_dateRange").val(dateRange);
}

function checkAll(){	
	
	if ($("#tableView input:checkbox[name='chkY']").attr('checked')) {
		$("#tableView input:checkbox[name='chkY']").attr('checked', false);
	}else{
		$("#tableView input:checkbox[name='chkY']").attr('checked', true);
	}
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    findTableView();
}
