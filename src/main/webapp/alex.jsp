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
	
	<form id="searchForm" method="GET" action="adtestprd.jsp" >
                關鍵字:<input  type="text"  name="q"  value="<%=qv1%>"/><br>
               頁數: <input  type="text"  name="page" value="1" /><br> 
                比對:   
               <select name="precise">
  					<option value="true">精準</option>
  					<option value="false" selected >模糊</option>
				</select>
               
               <input type="submit"   value="send" />
                
    		</form>


<table border=1 >
<tr><td>
stg<p>


關鍵字=<%=qv1%><br>
頁數=<%= request.getParameter("page") %><br>
比對:= <%=request.getParameter("precise") %>
<br><br><br><br>


<script language="javascript">
			pad_width=300;
			pad_height=250;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201509250003C";
			adbackupTEST="alex";
</script>
<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>		    
		    

 


</body>
</html>