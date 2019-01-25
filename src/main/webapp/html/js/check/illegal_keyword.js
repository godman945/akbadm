$(document).ready(function(){
    page();
});

function addIllegalKeyword() {
	document.forms[0].action = "doIllegalKeywordAdd.html";
	document.forms[0].submit();
}

function updateIllegalKeyword(seq, contentId) {
    document.getElementById("targetSeq").value = seq;
    document.getElementById("modifyContent").value = document.getElementById(contentId).value;
    document.forms[0].action = "doIllegalKeywordUpdate.html";
    document.forms[0].submit();
}

function deleteIllegalKeyword(seq, content) {
    if (confirm("確定刪除禁用字 [ " + content + " ] ?")) {
        document.getElementById("targetSeq").value = seq;
        document.forms[0].action = "doIllegalKeywordDelete.html";
        document.forms[0].submit();
    }
}

//分頁功能
function wantSearch(pageNo) {

    if (pageNo != null) {
        $("#pageNo").val(pageNo);
    }
	document.forms[0].action = "illegalKeywordList.html";
	document.forms[0].submit();
}
