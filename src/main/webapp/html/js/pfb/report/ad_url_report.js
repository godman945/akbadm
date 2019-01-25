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
    
    var searchCategory = $("#category").val();
    $("#searchCategory option").each(function(){
    	var value = $(this).val();
    	if(searchCategory == value){
    		$(this).attr("selected","selected");
    	}
    });
    
    var searchOption = $("#option").val();
    $("#searchOption option").each(function(){
    	var value = $(this).val();
    	if(searchOption == value){
    		$(this).attr("selected","selected");
    	}
    });
    
    page();
    tableSorter();
});

function doQuery() {
	$('#downloadFlag').val("");
	document.forms[0].action = "pfbUrlReport.html";
	document.forms[0].submit();
}

function doClear() {
	document.getElementById("startDate").value = "";
	document.getElementById("endDate").value = "";
	document.getElementById("searchText").value = "";
	document.getElementById("searchCategory").value = "";
	document.getElementById("searchOption").value = "";
	document.getElementById("category").value = "";
	document.getElementById("option").value = "";
	
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
    $('#downloadFlag').val("");
    document.forms[0].action = "pfbUrlReport.html";
	document.forms[0].submit();
}

function tableSorter(){
	$("#tableView").tablesorter({
		headers:{
			0 : { sorter: 'text' },
			5 : { sorter: false },
			6 : { sorter: 'fancyNumber' },
			7 : { sorter: 'fancyNumber' },
			8 : { sorter: 'fancyNumber' },
			9 : { sorter: 'rangesort' },
			10 : { sorter: 'rangesort' },
			11 : { sorter: 'rangesort' }
			}
	});
}

//下載報表
function doDownlaod() {
	$('#downloadFlag').val("yes");
    document.forms[0].action = "pfbUrlReportDownload.html";
	document.forms[0].submit();
}

//開啟明細
function fundDetalView(customerInfoId,adPvclkDate){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var searchCategory = $("#searchCategory").val();
	var searchOption = $("#searchOption").val();
	var searchText = $("#searchText").val();
	
	$.ajax({
		url: "pfbUrlDetalReport.html",
		data:{
			 "startDate": startDate,
			 "endDate": endDate,
			 "searchCategory": searchCategory,
			 "searchOption": searchOption,
			 "searchText": searchText,
			 "customerInfoId": customerInfoId,
			 "adPvclkDate": adPvclkDate
		},
		type:"post",
		dataType:"html",
		success:function(response, status){
			$('#adUrlDetalDiv').html(response);
			$("#adUrlDetalDiv").dialog(
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
						title : "貼code網址明細",
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
			$("#adUrlDetalDiv").dialog("open");
			/*$.fancybox(
					$('#adUrlDetalDiv').html(),
					{
						'modal'             : true,
		    			'autoDimensions'	: false,
		    			'width'         	: 900,
		    			'height'        	: 400,
		    			'autoSize'			: true,
		    			'autoHeight'		: true,
		    			'autoScale'			: true,
		    			'transitionIn'		: 'none',
		    			'transitionOut'		: 'none',
		    			'padding'			: 0,
		    			'overlayOpacity'    : .75,
		    			'overlayColor'      : '#fff',
		    			'scrolling'			: 'yes'		                    
					}
			);*/
				
			detalTableSorter();

		},
		error: function(xtl) {
			alert("系統繁忙，請稍後再試！");
		}
	});
}

function detalTableSorter(){
	$(".tableDetalView").tablesorter({
		headers:{
			0 : { sorter: 'text' },
			1 : { sorter: 'fancyNumber' },
			2 : { sorter: 'fancyNumber' },
			3 : { sorter: 'fancyNumber' },
			4 : { sorter: 'rangesort' },
			5 : { sorter: 'rangesort' },
			6 : { sorter: 'rangesort' }
			}
	});
}