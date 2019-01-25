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






1: 160x240<p>
			
			<script language="javascript">
			pad_width=160;
			pad_height=240;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201510070007C";
			</script>
			<script id="pcadscript" language="javascript" src="http://alex.pchome.com.tw:8080/akbadm/html/js/kernel/pcadshowstg.js"></script>


<!-- 

	
2-1: 140x300<p> 
	<script language="javascript">
			pad_width=250;
			pad_height=80;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201512170007C";
			</script>
			<script id="pcadscript" language="javascript" src="http://alex.pchome.com.tw:8080/akbadm/html/js/kernel/pcadshowstg.js"></script>
<br><br><br>

 -->

<div id='alex99' style="height:50px;background-color:rebeccapurple;">
<!-- 
<script type="text/javascript">
(function() {
  if (window.CHITIKA === undefined) { window.CHITIKA = { 'units' : [] }; };
  var unit = {"calltype":"async[2]","publisher":"nnm11111","width":728,"height":90,"sid":"Chitika Default"};
  var placement_id = window.CHITIKA.units.length;
  window.CHITIKA.units.push(unit);
  document.write('<div id="chitikaAdBlock-' + placement_id + '"></div>');
}());
</script>
<script type="text/javascript" src="//cdn.chitika.net/getads.js" async></script>
 -->
</div>

<script>


/**/
window.onload=function (){
	var appendDom = document.getElementById('alex99');
	var script = document.createElement('div');
	var appendDiv = document.createElement('div');
	appendDiv.className = 'ad_backup_pchome';

	var  scriptText = document.getElementById("alex99").innerHTML;
	scriptText = scriptText.replace('<!--', '');
	scriptText = scriptText.replace('-->', '');
	script.innerHTML = scriptText;
	var elements = script.getElementsByTagName("*");
	for (var j = 0; j < elements.length; j++) {
		var tagName = elements[j].tagName;
		var attributes = elements[j].attributes;
		var text = elements[j].innerHTML;
		var content = document.createElement(tagName);
		for (var k = 0; k < attributes.length; k++) {
			var attrib = attributes[k];
			var name = attrib.name;
			var name = attrib.value;
			content.setAttribute(attrib.name, attrib.value);
		}
		content.text = text;
		appendDiv.appendChild(content,appendDiv.firstChild);
	}
	if(appendDiv.nextElementSibling == null){
		appendDom.appendChild(appendDiv,document.body);	
	}else{
		appendDom.insertBefore(appendDiv,document.body);	
	}
}


























/*
var appendDom = document.getElementById('alex');
var script = document.createElement('div');
var appendDiv = document.createElement('div');
appendDiv.className = 'ad_backup_pchome';

var  scriptText = document.getElementById("alex").innerHTML;
scriptText = scriptText.replace('<!--', '');
scriptText = scriptText.replace('-->', '');
script.innerHTML = scriptText;
var elements = script.getElementsByTagName("*");
for (var j = 0; j < elements.length; j++) {
	var tagName = elements[j].tagName;
	var attributes = elements[j].attributes;
	var text = elements[j].innerHTML;
	var content = document.createElement(tagName);
	for (var k = 0; k < attributes.length; k++) {
		var attrib = attributes[k];
		var name = attrib.name;
		var name = attrib.value;
		content.setAttribute(attrib.name, attrib.value);
	}
	content.text = text;
	appendDiv.appendChild(content,appendDiv.firstChild);
}
if(appendDiv.nextElementSibling == null){
	appendDom.appendChild(appendDiv,document.body);	
}else{
	appendDom.insertBefore(appendDiv,document.body);	
}
*/
</script>



</body>
</html>