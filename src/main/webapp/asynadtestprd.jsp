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
String qv2="";




if(request.getParameter("q")!=null){


qv=request.getParameter("q");

qv1=new String( qv.getBytes("ISO-8859-1"),"UTF-8");
 //qv2=java.net.URLEncoder.encode(qv1);
}
%>
<body>
	
	<form id="searchForm" method="GET" action="asynadtestprd.jsp" >
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

<p>

<h1>非同步廣告</h1>
<hr>
關鍵字廣告<br>
<p>


<script id="asynpchomeadjs" async src="http://kdpic.pchome.com.tw/img/js/xasynpcadshow.js"></script>
<ins class="asynpchomead"
    pad_width=478
    pad_height=115;
    pad_customerId="PFBC20150519001";
    pad_positionId="PFBP201506290003S";
    pad_keyword="<%=qv1 %>"; 
    pad_page="<%= request.getParameter("page") %>"; 
    pad_precise="<%=request.getParameter("precise") %>"></ins>
<script>
(asynpchomead = window.asynpchomead || []).push({});
</script>




<hr>
async ad 
<p>
<script id="asynpchomeadjs" async src="http://kdpic.pchome.com.tw/img/js/xasynpcadshow.js"></script>
<ins class="asynpchomead"
     pad_width="160"
     pad_height="240"
     pad_customerId="PFBC20150519001"
     pad_positionId="PFBP201507200005C"
     pad_ssl="false"></ins>
<script>
(asynpchomead = window.asynpchomead || []).push({});
</script>

<hr>


500x500 <br>
<p>


<script id="asynpchomeadjs" async src="http://kdpic.pchome.com.tw/img/js/xasynpcadshow.js"></script>
<ins class="asynpchomead"
     pad_width="500"
     pad_height="500"
     pad_customerId="PFBC20160804001"
     pad_positionId="PFBP201608310008C"
     pad_ssl="false"></ins>
     
<script>
(asynpchomead = window.asynpchomead || []).push({});
</script>

<p>





<p>


</td></tr>




</table> 

<hr>



</body>
</html>