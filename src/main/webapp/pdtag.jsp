<%@page contentType="text/html; charset=UTF-8"%>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- 需全站埋code -->
<script>
(function(window, document, script, js, ptag, a, m) {
	window['pchome'] = ptag;
	window[ptag] = window[ptag] || function() {
	(window[ptag].q = window[ptag].q || []).push(arguments)
	}, window[ptag].l = 1 * new Date();
	a = document.createElement(script); 
	m = document.getElementsByTagName(script)[0];
	a.async = 1;
	a.src = js;
	m.parentNode.insertBefore(a, m);
})(window, document, 'script', 'html/js/ptag/pdtag.js', 'ptag');
	ptag({"paid":"alex_1234567"});
	//ptag("paid":"55555555555","event","convert",{"convert_id":"CAC_TEST3","convert_price":"40","pa_em_value":"WEFWE","user_link_url":"","user_link_blank":true},"clck");
</script>


<!-- 事件(轉換) -->
<script>
ptag({"event":{"type":"convert","convert_id":"CAC_TEST001","convert_price":"40","pa_em_value":"123@yahoo.com.tw","user_link_url":"24h.pchome.com.tw","user_link_blank":true,"click":false,"op1":"alex_op1"}});
</script>

<!-- 事件(轉換) 點擊才可觸發-->
<script>
ptag({"event":{"type":"convert","convert_id":"CAC_TEST002","convert_price":"40","pa_em_value":"123@yahoo.com.tw","user_link_url":"24h.pchome.com.tw","user_link_blank":true,"click":true,"op1":"alex_op1"}});
</script>


<!-- 事件(page_view) -->
<script>
ptag({"event":{"type":"page_view","op1":"page_view_op1","op2":"page_view_op2","pa_em_value":"page_view_123@yahoo.com.tw"}});
</script>


<!-- 事件(追蹤) -->
<script>
ptag({"event":{"type":"tracking","tracking_id":"TAC_TEST001","prod_id":"prod_12345","prod_price":"199","prod_dis":"99","op1":"tracking_op1","op2":"tracking_op2","ec_stock_status":"y","pa_em_value":"tracking_123@yahoo.com.tw"}});
</script>

<!-- 事件(MARK) -->
<script>
ptag({"event":{"type":"mark","mark_id":"mark_TEST001","mark_layer1":"2FDH"}});
</script>

<input type="button" value="點擊觸發事件" onclick="pchome_click();"/>




</body>
</html>