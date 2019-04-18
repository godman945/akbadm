$(document).ready(function(){
	$("#dialogDiv").dialog({
		autoOpen : false,
		show : "blind",
		hide : "explode",
		width : 1000,
		resizable : false,
		modal : true,
		closeOnEscape : true,
		draggable : false,
		title : ""
	});
});


function previewProdAdDetail(pfpCustomerInfoId,startDate,endDate,adSeq) {
	window.open ('prodAdDetailReport.html?pfpCustomerInfoId='+pfpCustomerInfoId+'&adSeq='+adSeq+'&startDate='+startDate+'&endDate='+endDate ,'_blank');
}


