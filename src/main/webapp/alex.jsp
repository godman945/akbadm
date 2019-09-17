<%@page contentType="text/html; charset=UTF-8"%>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
<script  id="pcadscript" language="javascript" async src="https://pacl.pchome.com.tw/js/ptag.js"></script>
<script>
   window.dataLayer = window.dataLayer || [];
   function ptag(){dataLayer.push(arguments);}
   ptag({"paid":"1543389867659"});
   ptag("event","convert",{
   "convert_id":"CAC20181210000000001",
   "convert_price":""},"click");
</script>






<table border=1 >
<tr>
<td>
stg<p>
<!-- 
<script language="javascript">
			pad_width=300;
			pad_height=250;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201509250003C";
</script>
<script id="pcadscript" language="javascript" src="html/js/kernel/pcadshowstg.js"></script>		    
	 -->	    


請點擊廣告啟用信箱
<br> <br> <br> 
<script language="javascript">
pad_width=300;
pad_height=250;
pad_customerId="PFBC20150519001";
pad_positionId="PFBP201801230005C";
</script>
<script id="pcadscript" language="javascript" src="html/js/kernel/pcadshowstg.js"></script>

<a href="http://yahoo.com.tw" target="_blank">非點擊廣告連結</a>

<div id="Layer"  style="opacity:0;pointer-events:none; position:absolute;top:102px; width:300px; height:250px;background-color: #CCCCCC; z-index:2;layer-background-color: #CCCCCC; border: 1px none #000000;" >
</div>




<div id="email_success" style="font-size:30px;color:red;">
	
<div>


</td>
</tr>
</table>

<script>


	

	
	document.addEventListener('visibilitychange', function(e) {
		if(e.srcElement.activeElement.id=='pchome8044_ad_frame1'){
			document.getElementById("email_success").innerHTML ='信箱已啟用';
				/*
				if(e.srcElement.activeElement.nextSibling.className != 'pchome_email_create_div'){
				var node = document.createElement("div"); 
				node.className = "pchome_email_create_div";
				var textnode = document.createTextNode("信箱已啟用");
				node.appendChild(textnode);                   
				e.srcElement.activeElement.insertAdjacentElement('afterend',node); 
				}
				*/
			
		}
	});
	
	
</script>



<!--  <script id="pcadscript" language="javascript" src="http://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>-->

</body>
</html>