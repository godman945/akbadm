$(document).ready(function(){

	var msg = $('#messageId');
	if (msg.val()!="") {
		alert(msg.val());
	}

	var outputHtml = $('#outputHtmlId');
	var showPrivilegeDiv = document.getElementById('showPrivilege');
	if (outputHtml!=null && showPrivilegeDiv!=null && outputHtml.val()!="") {
		showPrivilegeDiv.innerHTML = outputHtml.val();
	}

	var selectModelId = $('#paramModelId');
	selectModelId.bind('change', function() {
		var modelIdVal = $('#paramModelId').val();
        $.ajax({
            url: 'showPrivilegeAJAX.html',
            data : {'modelId' : modelIdVal},
            type: 'post',
            beforeSend: function() {
            },
            dataType: 'json',
            success: function(response, status) {
            	var htmlContent = response.outputHtml;
            	document.getElementById('showPrivilege').innerHTML = htmlContent;
            },
            error: function(xtl) {
                alert("系統繁忙，請稍後再試！");
            }
        });
    });

	var parentDeptId = $('#paramParentDeptId');
	parentDeptId.bind('change', function() {
		var parentDeptIdVal = parentDeptId.val();
        $.ajax({
            url: 'changeChildDeptAJAX.html',
            data : {'parentDeptId' : parentDeptIdVal},
            type: 'post',
            beforeSend: function() {
            	if (parentDeptIdVal=="0") {
            		alert("請選擇部門！");
            		return false;
            	}
            },
            dataType: 'json',
            success: function(response, status) {
            	var htmlContent = response.outputHtml2;
            	document.getElementById('childDeptOptionId').innerHTML = htmlContent;
            },
            error: function(xtl) {
                alert("系統繁忙，請稍後再試！");
            }
        });
    });
});
