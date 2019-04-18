<%@page contentType="text/html; charset=UTF-8"%>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="favicon.ico?v=1.0" rel="shortcut icon">

<!-- Google Tag Manager 
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-PR7VQ9H');</script>
-->
<!-- End Google Tag Manager -->



<!-- Google Tag Manager -->
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-NL9DNPR');</script>
<!-- End Google Tag Manager -->


</head>
<%
String qv="";
String qv1="";

if(request.getParameter("q")!=null){
qv=request.getParameter("q");
qv1=new String( qv.getBytes("ISO-8859-1"),"UTF-8");
//String qv2=java.net.URLEncoder.encode(qv1);
}
%>


<body>

<!-- Google Tag Manager (noscript) -->
<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-NL9DNPR"
height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<!-- End Google Tag Manager (noscript) -->





<!-- Google Tag Manager (noscript)  
<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-PR7VQ9H"
height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
-->
 
 <!-- pchome tag start 
  <script type="text/javascript">
	  (function(i,s,o,g,ptag,a,m){i['pchome'] = ptag;i[ptag]=i[ptag]
	  ||function(){(i[ptag].q=i[ptag].q||[]).push(arguments)},i[ptag].l=1*new Date();
	  a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})
	  (window,document,'script','https://pacl.pchome.com.tw/js/pdtag.js','ptag');
	  ptag({"paid":"1554864217116"});
	  ptag("event","convert",{"convert_id":"CAC20190410000000002","convert_price":""});
	  ptag("event","convert",{"convert_id":"CAC20190410000000003","convert_price":""},"click");
  </script>
  -->
  <!-- pchome tag end -->
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
		<input type="button" value="TEST" onclick="alex();"/>
		<a href="http://alex.pchome.com.tw:8080/akbadm/ptag.jsp" target= onclick="pchome_click();">點我轉址</a>
		<a ga-btn="btn_store_91app" onclick="obApi('track'),pchome_click();" href="http://www.gokirei.com/SalePage/Index/54VkU7HbjA9hSdLQlUT8xg==" target="_blank"><img class="e-img" src="img/e-1.png"></a>
		<a href="http://alex.pchome.com.tw:8080/akbadm/ptag.jsp" target="" >點我開新頁</a>
</body>


	<script>
		
		function obApi(obj){
			console.log(obj);
		}
	</script>
</html>


 


