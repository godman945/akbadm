$(document).ready(function () {
    var msg = $('#messageId');
    if (msg.val() != "") {
        alert(msg.val());
    }

    // 查詢開始日期
    $("#startDate").datepicker({
        showOn: "button",
        buttonImage: "html/img/icon_cal.gif",
        buttonImageOnly: true,
        changeMonth: false,
        changeYear: false,
        dateFormat: "yy-mm-dd"
    });

    // 查詢結束日期
    $("#endDate").datepicker({
        showOn: "button",
        buttonImage: "html/img/icon_cal.gif",
        buttonImageOnly: true,
        changeMonth: false,
        changeYear: false,
        dateFormat: "yy-mm-dd"
    });

    // 帳號結束日期
    $("#closeDate").datepicker({
        showOn: "button",
        buttonImage: "html/img/icon_cal.gif",
        buttonImageOnly: true,
        changeMonth: false,
        changeYear: true,
        minDate: +1,
        dateFormat: "yy-mm-dd"
    });

    // bank修改按鈕
    $(".doUpBstatusBT").click(function () {
        var voId = $(this).attr("voId");
        var text = $("#doUpBText_" + voId).val();
        var status = $("#doUpBSel_" + voId).val();

        $.post("doUpdateBankStatus.html", {
            "checkid": voId,
            "checkNote": text,
            "checkStatus": status
        }, function (rs) {
            if (rs == "success") {
            	alert("更新成功！");
                location.reload();
            }
        });
    });

    // personal修改按鈕
    $(".doUpPstatusBT").click(function () {
        var voId = $(this).attr("voId");
        var text = $("#doUpPText_" + voId).val();
        var status = $("#doUpPSel_" + voId).val();

        $.post("doUpdatePersonalStatus.html", {
            "checkid": voId,
            "checkNote": text,
            "checkStatus": status
        }, function (rs) {
            if (rs == "success") {
            	alert("更新成功！");
                location.reload();
            }
        });
    });

    // 網站狀態修改按鈕
    $(".doUpUrlStatusBT").click(function () {
        var voId = $(this).attr("voId");
        var urlStatus = $("#urlStatus_" + voId).val();
        var text = $("#doUpURN_" + voId).val();

        if(urlStatus == '1' || urlStatus == '3'){
        	if(text == ''){
        		alert("請填寫原因!");
        		return false;
        	}
        }
        
        $.post("doUpdateAllowUrlStatus.html", {
            "updAllowUrlId": voId,
            "updRefuseNote": text,
            "updUrlStatus": urlStatus
        }, function (rs) {
            if (rs == "success") {
            	alert("更新成功！");
                location.reload();
            } else if(rs == "repeat"){
            	alert("主網域請勿重覆！");
            } else if(rs == "nocode"){
            	alert("請選擇網站類別！");
            }
        });
    });
    
    // 主網域修改按鈕
    $(".doUpUrlBT").click(function () {
        var voId = $(this).attr("voId");
        var rootDomain = $("#rootDomain_" + voId).val();
        
        if(rootDomain == ''){
        	alert("主網域請勿空白!");
    		return false;
        }
        
        $.post("doUpdAllowUrlRootDomain.html", {
            "updAllowUrlId": voId,
            "updRootDomain": rootDomain
        }, function (rs) {
            if (rs == "success") {
            	alert("更新成功！");
                location.reload();
            } else if(rs == "repeat"){
            	alert("主網域請勿重覆！");
            }
        });
    });
    
    // 網站類別修改按鈕
    $(".doUpUrlCodeBT").click(function () {
        var voId = $(this).attr("voId");
        var categoryCode = $("#code_" + voId).val();
        
        if(categoryCode == ''){
        	alert("請選擇網站類別!");
    		return false;
        }
        
        $.post("doUpdAllowUrlCategoryCode.html", {
            "updAllowUrlId": voId,
            "updCategoryCode": categoryCode
        }, function (rs) {
            if (rs == "success") {
            	alert("更新成功！");
                location.reload();
            } else if(rs == "nocode"){
            	alert("請選擇網站類別！");
            }
        });
    });
    
    // 選擇狀態B
    $(".doUpBstatusSEL").change(function () {
        var voId = $(this).attr("voId");
        var thisValue = $(this).val();

        if (thisValue === "3") // 只有失敗要原因
        {
            $("#doUpBText_" + voId).show();
        } else {
            $("#doUpBText_" + voId).hide();
        }
    });

    // 選擇審核狀態狀態
    $(".doUpPstatusSEL").change(function () {
        var voId = $(this).attr("voId");
        var thisValue = $(this).val();

        if (thisValue === "3") // 審核失敗時要寫原因
        {
            $("#doUpPText_" + voId).show();
        } else {
            $("#doUpPText_" + voId).hide();
        }
    });

    // 選擇網站狀態改變
    $(".doUpUrlStatusST").change(function () {
        var voId = $(this).attr("voId");
        var thisValue = $(this).val();

        if (thisValue === "1" || thisValue === "3") // 審核失敗或封鎖時要寫原因
        {
            $("#doUpURN_" + voId).show();
        } else {
            $("#doUpURN_" + voId).hide();
        }
    });
    
    //帳戶審核狀態
    $("#status").change(function () {
        $("#accountStatus").val(this.value);
        var thisValue = $(this).val();

        if (thisValue === "3"
            || thisValue === "5" ) // 只有審核失敗要原因
        {
            $("#statusNote").show();
        } else {
            $("#statusNote").val("");
            $("#statusNote").hide();
        }

        if (thisValue === "1") // 開通時要清除結束時間
        {
            $("#closeDate").val("");
        }

        if ((thisValue == "2"   //若狀態為關閉 或封鎖時，結束日期為空，則協助帶入結束日期(今天)
            || thisValue == "3")
            && $("#closeDate").val() == ""){
            var today = new Date();
            var todayStr = today.getFullYear() + "-" + (today.getMonth()+1) + "-" +today.getDate();
            $("#closeDate").val(todayStr);
        }
    });

    if ($("#adType").val() == "1") {
        $.each($("#xTypeInfoTd li"), function (index, obj) {
            if ($(obj).children().attr("class") == "1") {
                $(obj).css("display", "");
            } else {
                $(obj).css("display", "none");
            }
        });
    } else if ($("#adType").val() == "2") {
        $.each($("#xTypeInfoTd li"), function (index, obj) {
            if ($(obj).children().attr("class") == "2") {
                $(obj).css("display", "");
            } else {
                $(obj).css("display", "none");
            }
        });
    } else if ($("#adType").val() == "4") {
        $.each($("#xTypeInfoTd li"), function (index, obj) {
            $(obj).css("display", "none");
        });
    } else if ($("#adType").val() == "3") {
        $.each($("#xTypeInfoTd li"), function (index, obj) {
            $(obj).css("display", "");
        });
    }

    $('#searchAd').click(function () {
        if ($(this).prop("checked") && $("#contentAd").prop("checked")) {
            $.each($("#xTypeInfoTd li"), function (index, obj) {
                $(obj).css("display", "");
            });
        } else if ($(this).prop("checked") && !$("#contentAd").prop("checked")) {
            $.each($("#xTypeInfoTd li"), function (index, obj) {
                if ($(obj).children().attr("class") == "1") {
                    $(obj).css("display", "");
                } else {
                    $(obj).css("display", "none");
                }
            });
        } else if (!($(this).prop("checked")) && !$("#contentAd").prop("checked")) {
            $.each($("#xTypeInfoTd li"), function (index, obj) {
                $(obj).css("display", "none");
            });
        } else if ($("#contentAd").prop("checked") && !($(this).prop("checked"))) {
            $.each($("#xTypeInfoTd li"), function (index, obj) {
                if ($(obj).children().attr("class") == "2") {
                    $(obj).css("display", "");
                } else {
                    $(obj).css("display", "none");
                    $(obj).children().prop("checked", false);
                }
            });
        }
    });

    $('#contentAd').click(function () {
        if ($(this).prop("checked") && $("#searchAd").prop("checked")) {
            $.each($("#xTypeInfoTd li"), function (index, obj) {
                $(obj).css("display", "");
            });
        } else if ($(this).prop("checked") && !$("#searchAd").prop("checked")) {
            $.each($("#xTypeInfoTd li"), function (index, obj) {
                if ($(obj).children().attr("class") == "2") {
                    $(obj).css("display", "");
                } else {
                    $(obj).css("display", "none");
                }
            });
        } else if (!($(this).prop("checked")) && !$("#searchAd").prop("checked")) {
            $.each($("#xTypeInfoTd li"), function (index, obj) {
                $(obj).css("display", "none");
                $(obj).children().prop("checked", false);
            });
        } else if ($("#searchAd").prop("checked") && !($(this).prop("checked"))) {
            $.each($("#xTypeInfoTd li"), function (index, obj) {
                if ($(obj).children().attr("class") == "1") {
                    $(obj).css("display", "");
                } else {
                    $(obj).css("display", "none");
                    $(obj).children().prop("checked", false);
                }
            });
        }
    });

    $("#cleanCloseDate").click(function () {
        if ($("#accountStatus").val() != '2'
            && $("#accountStatus").val() != '3') {
            $("#closeDate").val("");
        }
    })
    
    //tableSorter();
});

function xtypeCheckView(obj) {
    $.each($(obj + " li"), function (index, obj) {
        if ($(obj).children().attr("class") != $("#adType").val()) {
            $(obj).css("display", "none");
        }
    });
}

function doUpdate() {

	if($("#categoryCode").val() == ''){
		alert("請選擇網站類別!");
		return false;
	}
	
	$("body").block();
	
    var count = 0;
    var checkClostDate = true;
    if ($("#accountStatus").val() == '2'
        || $("#accountStatus").val() == '3') {
        if ($("#closeDate").val() == '') {
            checkClostDate = false;
        }
    }
    
    
    if($("#accountStatus").val() == '1'){
    	if($("#categoryCode").val() == ''){
    		checkClostDate = false;
    	}
    }
    if (checkClostDate) {
        $('input[name="adTypeCheck"]').each(function () {
            if (this.checked) {
                count = count + 1;
                if (this.id == 'searchAd') {
                    $("#adType").val("1");
                } else if (this.id == 'contentAd') {
                    $("#adType").val("2");
                }
            }
        });
        if (count == 2) {
            $("#adType").val("3");
        }

        if (count == 0) {
            $("#adType").val("4");
        }
        var xTypeArray2 = [];
        $.each($("#xTypeInfoTd li"), function (index, obj) {
            if ($(obj).children().prop("checked")) {
                xTypeArray2.push($(obj).children().attr("id"));
            }
        });
        var map = {
            "xTypeArray2": xTypeArray2
        }

        console.log(xTypeArray2)
        $.ajax({
            url: "doPfbAccountUpdate.html",
            data: {
                "adType": $("#adType").val(),
                "status": $("#accountStatus").val(),
                "statusNote": $("#statusNote").val(),
                "closeDate": $("#closeDate").val(),
                "customerInfoId": $("#customerInfoId").val(),
                "positionMenuIdArray": JSON.stringify(xTypeArray2),
                "rootDomain": $("#rootDomain").val(),
                "playType": $("#playType").val(),
                "categoryCode": $("#categoryCode").val(),
                "websiteChineseName": $("#websiteChineseName").val(),
                "websiteDisplayUrl": $("#websiteDisplayUrl").val()
            },
            type: "post",
            dataType: "json",
            success: function (respone) {
                console.log(">>>>>>>" + respone);
                location.href = "pfbAccountList.html";
            },
            error: function () {
                console.log(">>>>>>>error");
            }
        });
    } else {
    	if ($("#accountStatus").val() == '2'
            || $("#accountStatus").val() == '3'){
    		alert("帳戶狀態為關閉或封鎖時，必須有結束日期！");
    	} else if($("#accountStatus").val() == '1'){
    		alert("帳戶狀態為開通時，必須選擇網站類型！");
    	}
    }
    
    $("body").unblock();
//    document.updateForm.submit();
}

function backToList() {
    document.queryForm.submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("queryType").value = "";
	document.getElementById("queryText").value = "";
	document.getElementById("queryCategory").value = "";
	document.getElementById("queryStatus").value = "";
}

/*function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			
			}
	});
}*/