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
		<td>
			1: 250x80<p>
			
		<script language="javascript">
			pad_width=250;
			pad_height=80;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201512170007C";
			</script>
			<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>
		
		</td>
		<td>
			2: 140x300<p>
			
	<script language="javascript">
			pad_width=140;
			pad_height=300;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201601210010C";
			</script>
			<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>
			
		</td>
		<td>
		    3: 160x240<p>
		    
		    <script language="javascript">
			pad_width=160;
			pad_height=240;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201510070007C";
			</script>
			<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>
		    
		</td>
	</tr>
	<tr valign="top" align='center'>
		<td>
			4: 180x150 <p>
			
			<script id="asynpchomeadjs" async src="https://kdpic.pchome.com.tw/img/js/asynpcadshowstg.js"></script> 
			<ins class="asynpchomead" 
			pad_width=180;
			pad_height=150;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201605040001C";
			></ins>
			<script>
			(asynpchomead = window.asynpchomead || []).push({});
			</script>
		</td>
		<td>
			5: 300x100<p>
			
			<script language="javascript">
			pad_width=300;
			pad_height=100;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201801230001C";
			</script>
			<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>
		</td>
		<td>
		    6: 300x250<p>
		    
			<script language="javascript">
			pad_width=300;
			pad_height=250;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201509250003C";
			</script>
			<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>		    
		    
		</td>
	</tr>
	<tr valign="top" align='center'>
		<td>
			7: 300x600<p>
			
			<script language="javascript">
			pad_width=300;
			pad_height=600;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201711040015C";
			</script>
			<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>
			
		</td>
		<td>
			8: 320x480 M版原頁開<p>
			
			<script language="javascript">
			pad_width=320;
			pad_height=480;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201605050001C";
			</script>
			<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>
						
		</td>
		<td>
		    9: 336x280<p>
		    
		    <script language="javascript">
			pad_width=336;
			pad_height=280;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201601190002C";
			</script>
			<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>
		    
		</td>
	</tr>
	<tr valign="top" align='center'>
		<td colspan="3">
		    10: 728x90<p>
		    
	 
		    <script language="javascript">
			pad_width=728;
			pad_height=90;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201608010001C";
			</script>
			<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>
		    	
		</td>
	</tr>
	
</table>


</body>
</html>