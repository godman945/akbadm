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
<a href="http://showstg.pchome.com.tw/adm/ptag.jsp">點我連到ptag.jsp(不新開)</a>
<br><br><br><br>
<a href="http://showstg.pchome.com.tw/adm/ptag.jsp" target="_blank">點我連到ptag.jsp(新開)</a>


</body>
</html>


 


