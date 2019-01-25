$(document).ready(function(){
	
	var msg = $('#messageId');

	if (msg.val()!="") {
		alert(msg.val());
	}

	$.datepicker.setDefaults($.datepicker.regional);
    $("#paramStartDate, #paramEndDate").datepicker({
    	dateFormat: "yy-mm-dd"
    });

});
