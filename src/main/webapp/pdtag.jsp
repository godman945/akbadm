<%@page contentType="text/html; charset=UTF-8"%>  
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<!-- Google Tag Manager 
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-PR7VQ9H');
-->
</script>

<!-- End Google Tag Manager -->

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PTAG-STG</title>
<!-- Google Tag Manager (noscript) 
<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-PR7VQ9H"
height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
-->
<!-- End Google Tag Manager (noscript) -->


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
})(window, document, 'script', 'http://showstg.pchome.com.tw/adm/html/js/ptag/pdtag.js', 'ptag');
	ptag({"paid":"alex_1234567"});
	ptag({"event":{"type":"tracking","tracking_id":"TAC_TEST001"}});
</script>
<!-- 
	pchome廣告事件代碼需緊貼在全域代碼下
-->
<!-- 事件(轉換) 載入觸發 -->
<script type="text/javascript">
	ptag({"event":{"type":"convert","convert_id":"CAC_TEST001","convert_price":"40"}});
</script>

<!-- 事件(page_view)-->
<script>
	ptag({"event":{"type":"page_view","op1":"page_view_op1","op2":"page_view_op2","pa_em_value":"page_view_123@yahoo.com.tw"}});	
</script>

<!-- 事件(MARK) -->
<script>
	ptag({"event":{"type":"mark","mark_id":"MARK_TEST001","prod_id":"prod_12345","prod_price":"199","prod_dis":"99","op1":"mark_op1","op2":"mark_op2","ec_stock_status":"y","pa_em_value":"mark_123@yahoo.com.tw"}});
</script>

<!-- 事件(轉換) 點擊才可觸發 START-->
<script>
  function pchome_click(url,open_flag){
	  var callback = function(){
		if (url == null || url.length == 0 || url == '') {
		        alert('url 是空值，url is null');
		        return false
		}
		var blank = false;
		if (typeof open_flag === "boolean") {
			blank = open_flag
		}
		
		if (blank) {
		    window.open(url, '_0')
		} else {
		    location.href = url
		}
      }
	  ptag({"event":{"type":"convert","convert_id":"CAC_TEST002","convert_price":"50"}},callback());
   }
</script>
<!-- 事件(轉換) 點擊才可觸發 END-->



</head>
<body>
<!-- 參數1:連結網址 參數2:是否開啟新頁 參數3:轉換op1 value -->
<input type="button" value="點擊觸發轉換事件" onclick="test();pchome_click('http://yahoo.com.tw',true);"/>

<input type="button" value="MARK_ID觸發事件" onclick="test();mark_click('http://google.com.tw',true,'i_am_mark_layer1');"/>


<input type="button" value="MARK_ID觸發事件(不同mark_value)" onclick="test();mark_click('http://google.com.tw',true,'i_am_mark_layer2');"/>

<script>
function test(){
	console.log("CCCCCCCCCCCCCCCCCCCCCcc")	
}
</script>


</body>
</html>