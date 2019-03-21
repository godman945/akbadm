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
//String qv2=java.net.URLEncoder.encode(qv1);
}
%>
<body>

    	<script  id="pcadscript" language="javascript" async src="html/js/ptag/ptag.js"></script>
        <script>
	        window.dataLayer = window.dataLayer || []; 
	        function ptag(){dataLayer.push(arguments);}
	        ptag({"paid":"1543389867659"});
	        ptag("event","convert",{"convert_id":"CAC_TEST9","convert_price":"40","pa_em_value":"WEFWE","user_link_url":"","user_link_blank":true});
        </script>




		<input type="button" value="TEST" onclick="alex();"/>

		<a href="http://alex.pchome.com.tw:8080/akbadm/ptag.jsp" target= onclick="pchome_click();">點我轉址</a>
</body>
</html>


 


