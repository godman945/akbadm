<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
	<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery-1.8.3.min.js" ></script>
	<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.treeview.js" ></script>
	<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.cookie.js" ></script>
	<script language="JavaScript" src="${request.contextPath}/html/js/treeView/treeView.js" ></script>
	<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery-ui-1.9.2.custom.min.js"></script>
	<script language="JavaScript" src="${request.contextPath}/html/js/jquery/jquery.datepick-zh-TW.js"></script>
	<script language="JavaScript" src="${request.contextPath}/html/js/common/common.js" ></script>

	<script language="JavaScript" src="${request.contextPath}/html/js/<@t.getAsString name="jsName" />" ></script>
	<script language="JavaScript" src="${request.contextPath}/html/js/<@t.getAsString name="dateJsName" />" ></script>
