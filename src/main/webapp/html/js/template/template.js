var paramAdPoolSeq = null;
var paramDefineAdSeq = null;
var paramDefineAdId = null;
var paramDefineAdName = null;
var paramDefineAdType = null;
var paramDefineAdWidth = null;
var paramDefineAdHeight = null;
var paramDefineAdContent = null;
var paramTemplateAdProperties = null;
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
    
    paramAdPoolSeq = $('#paramAdPoolSeq');
    paramDefineAdSeq = $('#paramDefineAdSeq');
    paramDefineAdId = $('#paramDefineAdId');
    paramDefineAdName = $('#paramDefineAdName');
    paramDefineAdType = $('#paramDefineAdType');
    paramDefineAdWidth = $('#paramDefineAdWidth');
    paramDefineAdHeight = $('#paramDefineAdHeight');
    paramDefineAdContent = $('#paramDefineAdContent');
    
    
    
    if($("#defineAdWidth").val() != "" && $("#defineAdHeight").val()!= ""){
        $("#updateParamDefineAdSize option").each(function() {
        	var str = $.trim($(this).text());
        	var  paramDefineAdWidth = str.substring(0,str.search("x"));
        	var  paramDefineAdHeight= str.substring(str.search("x")+1,str.length);
        	if( $("#defineAdWidth").val() == $.trim(paramDefineAdWidth) && $("#defineAdHeight").val() == $.trim(paramDefineAdHeight)){
        		$(this).attr("selected","selected");
        		return;
        	}
        });
    }
    
    if($("#defineTemplateAdWidth").val() !="" && $("#defineTemplateAdHeight").val()!=""){
    	$("#paramTemplateAdSize option").each(function() {
        	var str = $.trim($(this).text());
        	var paramDefineAdWidth = str.substring(0,str.search("x"));
        	var paramDefineAdHeight = str.substring(str.search("x")+1,str.length);
        	if( $("#defineTemplateAdWidth").val() == $.trim(paramDefineAdWidth) && $("#defineTemplateAdHeight").val() == $.trim(paramDefineAdHeight)){
        		$(this).attr("selected","selected");
        		return;
        	}
        });
    }
    
    if($("#weight").val() !="" && $("#height").val()!=""){
        $("#iframeAdSize option").each(function() {
        	var str = $.trim($(this).text());
        	var  paramDefineAdWidth= str.substring(0,str.search("x"));
        	var  paramDefineAdHeight= str.substring(str.search("x")+1,str.length);
        	if( $("#weight").val() == $.trim(paramDefineAdWidth) && $("#height").val() == $.trim(paramDefineAdHeight)){
        		$(this).attr("selected","selected");
        		return;
        	}
        });
    }
    
    
    
    $("#paramTemplateAdSize option:selected").each(function() {
    	var str = $.trim($(this).text());
    	var  paramDefineAdWidth = str.substring(0,str.search("x"));
    	var  paramDefineAdHeight = str.substring(str.search("x")+1,str.length);
    	
    	$("#defineTemplateAdWidth").val($.trim(paramDefineAdWidth));
    	$("#defineTemplateAdHeight").val($.trim(paramDefineAdHeight));
    });
    
   
   if($("#defineTemplateAdType").val() == ""){
	   $("#paramTemplateAdType option:selected").each(function() {
		   $("#defineTemplateAdType").val(this.value);
	    });
   }

   if($("#defineAdProperties").val() == ""){
	   $("#paramTemplateAdProperties option:selected").each(function() {
		   $("#defineAdProperties").val(this.value);
	    });
   }
   
   
   if($("#defineAdType").val() == ""){
	   $("#paramAdType option:selected").each(function() {
	    	$("#defineAdType").val(this.value);
	    });
   }
  
   if($("#defineTemplateAdWidth").val() == "" && $("#defineTemplateAdHeight").val()== "" ){
	   $("#paramTemplateAdSize option:selected").each(function() {
       	var str = $.trim($(this).text());
       	var  paramDefineAdWidth = str.substring(0,str.search("x"));
       	var  paramDefineAdHeight = str.substring(str.search("x")+1,str.length);
       	$("#defineTemplateAdWidth").val($.trim(paramDefineAdWidth));
    	$("#defineTemplateAdHeight").val($.trim(paramDefineAdHeight));
       });
   }
   
   if($("#weight").val() == "" && $("#height").val()== "" ){
	   $("#iframeAdSize option:selected").each(function() {
       	var str = $.trim($(this).text());
       	
       	
       	var  paramDefineAdWidth = str.substring(0,str.search("x"));
       	var  paramDefineAdHeight = str.substring(str.search("x")+1,str.length);
       
       	
       	
       	$("#weight").val($.trim(paramDefineAdWidth));
    	$("#height").val($.trim(paramDefineAdHeight));
       });
   }
   
   $("#paramTemplateAdSize option").each(function() {
	   if($("#defaultHeight").val()!= "" && $("#defaultWidth").val()){
			var str = $.trim($(this).text());
			var  paramDefineAdWidth= str.substring(0,str.search("x"));
	    	var  paramDefineAdHeight  = str.substring(str.search("x")+1,str.length)
	       	if(iframeWidth == $("#defaultHeight").val() && iframeHeight == $("#defaultWidth").val()){
	       		$("#defineTemplateAdHeight").val($("#defaultWidth").val());
	       		$("#defineTemplateAdWidth").val($("#defaultHeight").val());
	       		$(this).attr("selected","selected");
	       	}
	   }
   });
   
   if($("#updateDefineTemplateType").val() != ""){
	   $("#paramTemplateUpdateAdProperties option").each(function() {
		   var className = $(this).attr('class');
		   var g = $("#updateDefineTemplateType").val();
		   if(className.indexOf(g) > 0 ){
			   $(this).css("display","");
		   }else{
			   $(this).css("display","none");
		   }
	   });
   }
});


function updateBtn(){
//	alert($("#paramTemplateAdSize option:selected").text());
	var str = $("#paramTemplateAdSize option:selected").text();
	var  paramDefineAdWidth = str.substring(0,str.search("x"));
	var  paramDefineAdHeight = str.substring(str.search("x")+1,str.length);
//	alert(paramDefineAdWidth);
	//alert(paramDefineAdHeight);
//	 $("#paramTemplateAdSize option:selected").each(function() {
//		 var str = $.trim($(this).text());
//		 var paramDefineAdWidth = str.substring(0,str.search("x"));
//		 var paramDefineAdHeight = str.substring(str.search("x")+1,str.length);
//		 $("#defineTemplateAdWidth").val($.trim(paramDefineAdWidth));
//		 $("#defineTemplateAdHeight").val($.trim(paramDefineAdHeight));
//	 });
}


//update 變更選項
function defineTemplateAdPropertiesUpdateSelect(){
	$("#paramTemplateUpdateAdProperties option:selected").each(function() {
    	$("#defineAdProperties").val(this.value);
    });
}


//點擊預覽
function preView(obj){
	var tdObj = $(obj).parent();
	var iframe = $(tdObj).children()[1];
	var width = $(iframe).css('width').replace("px","");
	var height = $(iframe).css('height').replace("px","");
	$.fancybox(
			$($(tdObj).children()[1]).html(),
			{
	        	'autoDimensions'	: false,
				'width'         		: 'auto',
				'height'        		: 'auto',
				'transitionIn'		: 'none',
				'transitionOut'		: 'none'
			}
		);
	
}
function selectSizeView(){
	var size = $.trim($("#sizeSelectId option:selected").text());
	if(size == ""){
		return false;
	}
	if(size == "全選"){
		$(location).attr( 'href' , 'templateProductQuery.html' );
	}
	for (var i = 0; i < size.length; i++) {
		if(size[i] == " "){
			size = size.replace(" ","");
		}
	}
	strLength = size.length;
	strWidthIndex = size.match("x").index;
	var width = size.substring(0,strWidthIndex);
	var height = size.substring(strWidthIndex+1,strLength);
	$("#paramTemplateProductWidth").val(width);
	$("#paramTemplateProductHeight").val(height);
	$("#templateProductSizeQueryId").submit();
	
}

function deleteTemplateAd(templateAdSeq) {
//	alert(templateAdSeq);
//	$.ajax({
//		url: "templateAdActionAjaxAction.html",
//		data:{
//			templateAdSeq : templateAdSeq
//		},
//		type:"post",
//		dataType:"html",
//		success:function(response, status){
//			 location.reload();
//		},
//		error: function(xtl) {
//			alert("系統繁忙，請稍後再試！");
//		}
//	});
//	alert("GGGGGG");
    if (confirm("確定刪除商品 [ " + templateAdSeq + " ] ?")) {
        document.getElementById("templateAdSeq").value = templateAdSeq;
        document.forms[1].action = "doTemplateAdDelete.html";
        document.forms[1].submit();
    }
	
	
}


function chkCount() {
    if($("#paramTemplateProductName").val() == ""){
        alert("請輸入商品樣板名稱!!!");
        return false;
    }else{
        $("#defineTemplateAdProductName").val($("#paramTemplateProductName").val());
    }
   if($("#pageTotalNum").val() == ""){
    alert("請輸入商品樣板每頁筆數!!!");
    return false;
   }
   if($("#startNum").val()== ""){
    alert("請輸入商品樣板開始筆數!!!");
    return false;
   }
    var content = document.getElementById("paramTemplateProductContent").value;
    var adCount = document.getElementById("paramTemplateAdCount").value;
    var part = content.split("{#");
    if((part.length - 1) != adCount ) {
        alert("商品樣板中的廣告樣板數量與選擇的不同，請確認!!!");
        return false;
    } 
    $("#form1").submit();
}


function changeTemplateSize(){
	 $("#paramTemplateAdSize option:selected").each(function() {
		 var str = $.trim($(this).text());
		 var  paramDefineAdWidth = str.substring(0,str.search("x"));
		 var  paramDefineAdHeight = str.substring(str.search("x")+1,str.length);
		 $("#defineTemplateAdWidth").val($.trim(paramDefineAdWidth));
		 $("#defineTemplateAdHeight").val($.trim(paramDefineAdHeight));
	 });
}

function changeIframeSize(){
	 $("#iframeAdSize option:selected").each(function() {
		 var str = $.trim($(this).text());
		 var paramDefineAdWidth = str.substring(0,str.search("x"));
		 var paramDefineAdHeight  = str.substring(str.search("x")+1,str.length);
		 $("#weight").val($.trim(paramDefineAdWidth));
		 $("#height").val($.trim(paramDefineAdHeight));
	 });
}

function defineTemplateAdTypeSelect(){
	$("#paramTemplateAdType option:selected").each(function() {
    	$("#defineTemplateAdType").val(this.value);
    });
}

function defineTemplateAdPropertiesSelect(){
	$("#paramTemplateAdProperties option:selected").each(function() {
    	$("#defineAdProperties").val(this.value);
    });
}

function defineAdTypeSelect(){
	 var changeFlag = false;
	 $("#paramAdType option:selected").each(function() {
	    	$("#defineAdType").val(this.value);
	 });
	 if($("#defineAdProperties").val() != ""){
		 $("#paramTemplateUpdateAdProperties option").each(function() {
		 if($(this).css("display") == "block"){
			 if($(this).attr("selected") == "selected"){
				 $(this).attr("selected", false);
			 }
			 $(this).css("display","none");
		 }else{
			 $(this).css("display","");
			 if(!changeFlag){
				 $(this).attr("selected","true");
				 $("#defineAdProperties").val(this.value)
				 changeFlag = true;
			 }
		 }
		 });
	 }
}

function addDad() {
    var paramAdPoolSeq = document.getElementById("paramAdPoolSeq").value;
    window.open("defineAdAdd.html?paramAdPoolSeq=" + paramAdPoolSeq, "addDad", 'height=480, width=480, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
}

function updateDad(paramDefineAdSeq) {
    window.open("defineAdUpdate.html?paramDefineAdSeq=" + paramDefineAdSeq, "updateDad", 'height=480, width=480, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
}

function doAdd() {
	var str = $.trim($("#paramDefineAdSize option:selected").text());
	var paramDefineAdWidth = str.substring(0,str.search("x"));
	var paramDefineAdHeight  = str.substring(str.search("x")+1,str.length);
    
	$.ajax({
        url: 'doDefineAdAddAJAX.html',
        data : {'paramAdPoolSeq' : paramAdPoolSeq.val(),
                'paramDefineAdId' : paramDefineAdId.val(),
                'paramDefineAdName' : paramDefineAdName.val(),
                'paramDefineAdType' : paramDefineAdType.val(),
                'paramDefineAdWidth' : paramDefineAdWidth,
                'paramDefineAdHeight' : paramDefineAdHeight,
                'paramDefineAdContent' : paramDefineAdContent.val(),
                'defineAdProperties' : paramTemplateAdProperties.val()
               },
        type: 'post',
        beforeSend: function() {
        	if (!checkParameter()) {
        		return false;
        	}
        },
        dataType: 'json',
        success: function(response, status) {
            if ((response.message == null) || (response.message == "")) {
                alert("新增成功");
                location.reload();
                window.opener.location.reload();
                window.close();
            }
            else {
                alert(response.message);
            }
        },
        error: function(xtl) {
            alert(xtl);
        }
    });
}

function doUpdate() {
	var str = $.trim($("#updateParamDefineAdSize option:selected").text());
	var  paramDefineAdWidth = str.substring(0,str.search("x"));
	var  paramDefineAdHeight = str.substring(str.search("x")+1,str.length);
	
	
	
    $.ajax({
        url: 'doDefineAdUpdateAJAX.html',
        data : {'paramAdPoolSeq' : paramAdPoolSeq.val(),
            	'paramDefineAdSeq' : paramDefineAdSeq.val(),
	            'paramDefineAdId' : paramDefineAdId.val(),
	            'paramDefineAdName' : paramDefineAdName.val(),
	            'paramDefineAdType' : paramDefineAdType.val(),
	            'paramDefineAdWidth' : paramDefineAdWidth,
	            'paramDefineAdHeight' : paramDefineAdHeight,
	            'paramDefineAdContent' : paramDefineAdContent.val() },
        type: 'post',
        beforeSend: function() {
            if (paramDefineAdSeq.val() == "") {
                alert("廣告定義序號錯誤！");
                return false;
            }
        	if (!checkParameter()) {
        		return false;
        	}
        },
        dataType: 'json',
        success: function(response, status) {
        	alert("GGG");
            if ((response.message == null) || (response.message == "")) {
                alert("修改成功");
                location.reload();
                window.opener.location.reload();
                window.close();
            }
            else {
                alert(response.message);
            }
        },
        error: function(xtl) {
            alert("系統繁忙，請稍後再試！");
        }
    });
}

function doDelete(paramDefineAdSeq) {
    $.ajax({
        url: 'doDefineAdDeleteAJAX.html',
        data : {'paramDefineAdSeq' : paramDefineAdSeq},
        type: 'post',
        beforeSend: function() {
            if (paramDefineAdSeq == "") {
                alert("廣告定義序號錯誤！");
                return false;
            }
        },
        dataType: 'json',
        success: function(response, status) {
            if ((response.message == null) || (response.message == "")) {
                alert("刪除成功");
                location.reload();
            }
            else {
                alert(response.message);
            }
        },
        error: function(xtl) {
            alert("系統繁忙，請稍後再試！");
        }
    });
}

function checkParameter() {
    if (paramAdPoolSeq.val() == "") {
        alert("請輸入廣告來源序號！");
        return false;
    }
    if (paramDefineAdId.val() == "") {
        alert("請輸入廣告定義代碼！");
        return false;
    }
    if (paramDefineAdName.val() == "") {
        alert("請輸入廣告定義名稱！");
        return false;
    }
    if (paramDefineAdType.val() == "0") {
        alert("請輸入廣告定義Type！");
        return false;
    }
    if (paramDefineAdWidth.val() == "") {
        alert("請輸入廣告定義寬度！");
        return false;
    }
    if (paramDefineAdHeight.val() == "") {
        alert("請輸入廣告定義高度！");
        return false;
    }
    if (paramDefineAdContent.val() == "") {
        alert("請輸入廣告定義內容！");
        return false;
    }
    return true;
}