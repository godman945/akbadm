<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>


<!--
<script  id="pcadscript" language="javascript" async src="html/js/ptag/ptag.js"></script>
<script>
	  window.dataLayer = window.dataLayer || [];
	  function ptag(){dataLayer.push(arguments);}
	  ptag({'paid':'pa_20180001'});
	  ptag('event','page_view',{'op1':'page_view_op1','op2':'page_view_op2'});
	  ptag('event','convert',{'convert_id':'convert001','convert_price':'199','op1':'convert_op1'});
	  ptag('event','tracking',{'tracking_id':'tracking001','prod_id':'prod_001','prod_price':'150','prod_dis':'145','op2':'tracking_op2'});
</script>
-->

 		  <script language="javascript">
			pad_width=300;
			pad_height=250;
			pad_customerId="PFBC20150519001";
			pad_positionId="PFBP201801230004C";
			</script>
			<script id="pcadscript" language="javascript" src="https://kdpic.pchome.com.tw/img/js/pcadshowstg.js"></script>
<br>
請輸入tracking_id : <input id="tracking_id" type="text" value="traceId002">
<br><br>
請輸入prod_id :&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input id="prod_id" type="text" value="P400058940704">
<br><br>
請輸入prod_price :&nbsp<input id="prod_price" type="text" value="150">
<br><br>
請輸入prod_dis :&nbsp&nbsp&nbsp&nbsp<input id="prod_dis" type="text" value="141">
<br><br>
請輸入ec_stock_status :&nbsp&nbsp&nbsp&nbsp<input id="ec_stock_status" type="text" value="0">

<br><br><br>
<input type="button" value="送出" onclick="submit()">

<script  id="pcadscript" language="javascript" async src="html/js/ptag/ptag.js"></script>

<br><br><br><br>
送至PACL資訊<br>
<textarea id="paclArea" rows="15" cols="100" disabled>
</textarea>

<script>
	window.dataLayer = window.dataLayer || [];
	function submit(){
		var tracking_id = $("#tracking_id").val();
		var prod_id = $("#prod_id").val();
		var prod_price = $("#prod_price").val();
		var prod_dis = $("#prod_dis").val();
		var ec_stock_status = $("#ec_stock_status").val();
		
		if(tracking_id == '' || prod_id == ''){
			alert("資料不足");
			return false;
		}
	
		window.dataLayer = window.dataLayer || [];
		function ptag(){dataLayer.push(arguments);}
		ptag({'paid':1541043693437});
		ptag('event','tracking',{'tracking_id':tracking_id,'prod_id':prod_id,'prod_price':prod_price,'prod_dis':prod_dis,'op2':'tracking_op2','ec_stock_status':ec_stock_status});
		alex();
	}
</script>
