<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>PChome 關鍵字廣告</title>
	<script language="JavaScript" src="<@s.url value="/" />html/js/jquery/jquery-1.8.3.min.js"></script>
	<script language="JavaScript" src="<@s.url value="/" />html/js/jquery/jquery-common.js"></script>
	<script language="JavaScript" src="<@s.url value="/" />html/js/jquery/jquery.swfobject.1-1-1.min.js"></script>
	<script language="JavaScript" src="<@s.url value="/" />html/js/jquery/jquery-ui-1.9.2.custom.min.js"></script>
	<script language="JavaScript" src="<@s.url value="/" />html/js/jquery/jquery.datepick-zh-TW.js"></script>
</head>
<script language="JavaScript">
function initPage() {
	var msg = "${result}";
	if(msg == "saveOK") {
		//parent.document.body.scrollTop = 0;
		parent.location.reload();
	} else {
		alert(msg);
	}
}

</script>
<body onLoad="initPage();">
</body>
</html>