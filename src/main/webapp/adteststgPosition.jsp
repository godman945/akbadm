<%@page contentType="text/html; charset=UTF-8"%>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="favicon.ico?v=1.0" rel="shortcut icon">
</head>
<%
String qv="";
String qv1="";

if(request.getParameter("q")!=null){


qv=request.getParameter("q");

qv1=new String( qv.getBytes("ISO-8859-1"),"UTF-8");
String qv2=java.net.URLEncoder.encode(qv1);
}
%>
<body>
<!--  <script async src="http://showstg.pchome.com.tw/adm/html/js/ptag/ptag.js?id=PA_TRACKING_ID"></script>-->
<script async src="http://alex.pchome.com.tw:8080/akbadm/html/js/ptag/ptag.js?id=PA_TRACKING_ID"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function ptag(){dataLayer.push(arguments);}
  ptag('js', new Date());
  ptag('config', 'PA_TRACKING_ID');
  ptag('event', 'page_view');
  
</script>



<button id="go" type="button" style="background-color:aquamarine;width:80px;">購物鈕</button>


<script>

	document.getElementById("go").addEventListener('click', function(event) {
		console.log("使用者點擊購物車")
		//取消事件的預設行為
		event.preventDefault();
		ptag('event', 'shopping', {'event_label': 'shopping_cart', 'event_category': 'buy'});
		console.log("傳送完畢")
	});
</script>

</body>
</html>