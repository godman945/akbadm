$(document).ready(function(){

	var msg = $('#messageId');
	if (msg.val()!="") {
		alert(msg.val());
	}

	var statusBtn = $('#statusBtn');
	statusBtn.bind('click', function() {
		var emailVal = $('#hidEmail').val();
        var statusVal = $('#status').val();
        var reasonVal = $('#reason').val();

        $.ajax({
            url: 'updateMemberStatus.html',
            data : {'hidEmail' : emailVal, 'status' : statusVal, 'reason' : reasonVal},
            type: 'post',
            beforeSend: function() {
            	if (reasonVal == "") {
            		alert("請輸入修改狀態原因！");
            		return false;
            	}
            },
            dataType: 'json',
            success: function(response, status) {
            	var msg = response.message;
            	alert(msg);
            },
            error: function(xtl) {
                alert("系統繁忙，請稍後再試！");
                checkPchomeId.removeAttr("disabled");
            }
        });
    });
});
