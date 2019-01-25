$(document).ready(function(){
    page();
});

function doQuery() {
	document.forms[0].action = "contractList.html";
	document.forms[0].submit();
}

function add() {
	document.forms[0].action = "contractAdd.html";
	document.forms[0].submit();
}

function update(id) {
	document.forms[0].action = "contractUpdate.html?targetContractId=" + id;
	document.forms[0].submit();
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
	document.forms[0].action = "contractList.html";
	document.forms[0].submit();
}
