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


<table border="1" valign="top">
	
	<tr valign="top" align='center'>
		<td colspan="3">
		    11: 640x390<p>
		    
		    <script language="javascript">
			pad_width=640;
			pad_height=390;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201801160002C";
			</script>
			<script id="pcadscript" language="javascript" src="http://showstg.pchome.com.tw/adm/html/js/kernel/asynpcadshowstg3.js"></script>
		    
		   
		    	
		</td>
	</tr>
	<tr valign="top" align='center'>
		<td colspan="3">
		    12: 950x390<p>
		    
		    <script language="javascript">
			pad_width=950;
			pad_height=390;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201801170004C";
			</script>
			<script id="pcadscript" language="javascript" src="http://showstg.pchome.com.tw/adm/html/js/kernel/asynpcadshowstg3.js"></script>
		    	
		</td>
	</tr>
	<tr valign="top">
		<td colspan="3">
		    13: 970x250<p>
		    
		    <script language="javascript">
			pad_width=970;
			pad_height=250;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201801170005C";
			</script>
			<script id="pcadscript" language="javascript" src="http://showstg.pchome.com.tw/adm/html/js/kernel/asynpcadshowstg3.js"></script>
		    	
		</td>
	</tr>
	<tr valign="top" align='center'>
		<td>
			14: 120x600<p>
			
			<script language="javascript">
			pad_width=120;
			pad_height=600;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201508250005C";
			</script>
			<script id="pcadscript" language="javascript" src="http://showstg.pchome.com.tw/adm/html/js/kernel/asynpcadshowstg3.js"></script>
			
		</td>
		<td>
			15: 160x600<p>
			
			<script language="javascript">
			pad_width=160;
			pad_height=600;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201510070001C";
			</script>
			<script id="pcadscript" language="javascript" src="http://showstg.pchome.com.tw/adm/html/js/kernel/asynpcadshowstg3.js"></script>
		</td>
		<td>
		    
		    
		</td>
	</tr>
</table>

</body>
</html>