$(document).ready(function(){

    //開始日期 
    $("#startDate").datepicker({
	    showOn: "button",
	    buttonImage: "html/img/icon_cal.gif",
	    buttonImageOnly: true,
	    changeMonth: false,
	    changeYear: false,
        dateFormat: "yy-mm-dd"
    });

    //結束日期    
    $("#endDate").datepicker({
	    showOn: "button",
	    buttonImage: "html/img/icon_cal.gif",
	    buttonImageOnly: true,
	    changeMonth: false,
	    changeYear: false,
        dateFormat: "yy-mm-dd"
    });

    page();
    tableSorter();
});

function doQuery() {
	document.forms[0].action = "adReport.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("adType").options[0].selected = true;
	document.getElementById("pfdCustomerInfoId").options[0].selected = true;
	document.getElementById("searchType").options[0].selected = true;
	document.getElementById("searchText").value = "";
}

function downloadReport() {
	document.forms[0].action = "downloadAd.html";
	document.forms[0].submit();
}

//開啟明細
function fundDetalView(startDate,endDate,adSeq,adTitle){

	var adType = $("#adType").val();
	var pfdCustomerInfoId = $("#pfdCustomerInfoId").val();
	var searchType = $("#searchType").val();
	var searchText = $("#searchText").val();
	
	$.ajax({
		url: "adDetailReport.html",
		data:{
			 "startDate": startDate,
			 "endDate": endDate,
			 "searchAdSeq": adSeq,
			 "adTitle": adTitle,
			 "adType": adType,
			 "pfdCustomerInfoId": pfdCustomerInfoId,
			 "searchType": searchType,
			 "searchText": searchText
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$('#adDetalDiv').html(response);
			$("#adDetalDiv").dialog(
					{
						autoOpen : false,
						show : "blind",
						hide : "explode",
						width : 950,
						height : 800,
						resizable : false,
						modal : true,
						closeOnEscape : true,
						draggable : false,
						title : "廣告客戶明細",
						open : function(event, ui) {
							// 隱藏「x」關閉按鈕
							$(this).parent().children().children('.ui-dialog-titlebar-close').hide();
						},
						buttons : {
							"Ok" : function() {
								$(this).dialog("close");
							}
						}
					});
			$("#adDetalDiv").dialog("open");
				
			detalTableSorter();

		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    document.forms[0].action = "adReport.html";
	document.forms[0].submit();
}

function preview(img) {
    $.fancybox({
        'href':img,
        'showCloseButton' :true,
        'autoSize':true,
        'autoHeight':true,
        'autoScale':true,
        'transitionIn':'none',
        'transitionOut':'none',
        'padding':0,
        'overlayOpacity':.75,
        'overlayColor':'#fff',
        'scrolling':'no'
    });
}

function preViewHtml5(width,height,imgSrc,realUrl){
	
	 $.fancybox(
			 '<div style="position:absolute;z-index:10;border:0px;background:none;width:' + width + 'px;height:' + height + 'px;">' + 
			 '<a href="' + realUrl + '" target="_blank" style="display:block;width:' + width + 'px;height:' + height + 'px;background-color:transparent;"><img src="html/img/blank.gif" style="width:' + width + 'px;height:' + height + 'px;border:0px;"></a>'
			 + '</div>'
			 + '<iframe src="' + imgSrc + '" width="' + width + '" height="' + height + '"  allowtransparency="true" frameborder="0" scrolling="no" ></iframe>',
	    		{
	    			'autoDimensions'	: false,
	    			'width'         	: width,
	    			'height'        	: height,
	    			'autoSize'			: true,
	    			'autoHeight'		: true,
	    			'autoScale'			: false,
	    			'transitionIn'		: 'none',
	    			'transitionOut'		: 'none',
	    			'padding'			: 0,
	    			'overlayOpacity'    : .75,
	    			'overlayColor'      : '#fff',
	    			'scrolling'			: 'no'
	    		}
	    );
}

function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			5 : { sorter: 'fancyNumber' },
			6 : { sorter: 'fancyNumber' },
			7 : { sorter: 'fancyNumber' },
			8 : { sorter: 'rangesort' },
			9 : { sorter: 'rangesort' },
			10 : { sorter: 'rangesort' },
			11 : { sorter: false }
			}
	});
}

function detalTableSorter(){
	$(".tableDetalView").tablesorter({
		headers:{
			1 : { sorter: 'fancyNumber' },
			2 : { sorter: 'fancyNumber' },
			3 : { sorter: 'fancyNumber' },
			4 : { sorter: 'rangesort' },
			5 : { sorter: 'rangesort' },
			6 : { sorter: 'rangesort' }
			}
	});
}