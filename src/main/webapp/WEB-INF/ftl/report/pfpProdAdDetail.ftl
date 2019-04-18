<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="http://showstg.pchome.com.tw/pfp/html/css/style.css" rel="stylesheet" type="text/css" /> 
</head>

<body>
<h2><img src="http://showstg.pchome.com.tw/pfp/html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle">商品成效</h2>
<div class="grtba"> 
	<div style="line-height:30px;">
		日期：  >> 2019-01-22 ~ 2019-01-29<br> 
		廣告客戶名稱：pc8044 <br>
		商品廣告名稱：玩起來0129
	</div>
		<div id="reportTableOut" class="scrollwrap" style="position: static; zoom: 1;">
			<table id="excerptTable" class="tablesorter tablesorter-default tablesorter76e219d240aca" border="0" cellpadding="3" cellspacing="1" role="grid" style="max-width:100%;min-width:100%;"> 
			  	<thead>
			    	<tr height="35"> 
							<th style="background-image: url();width:30%;">商品明細</th>
							<th style="background-image: url();width:25%;"><div class="tablesorter-header-inner">陳列次數</div></th>
							<th style="background-image: url();width:25%;"><div class="tablesorter-header-inner">商品點選數</div></th>
							<th style="background-image: url();width:20%;"><div class="tablesorter-header-inner">商品點選率</div></th>
			    	</tr> 
				</thead>
			     
			     <tbody aria-live="polite" aria-relevant="all">
			     	<#list prodAdVoList as voList>
					<tr height="30" role="row" class="even">
							<td align="left">
								<img src="/${voList.prodImg!}" width="45%" align="left">
			    				<span style="text-align: center; vertical-align:middle;">${voList.prodName!}</span>
							</td>
							<td align="center">${voList.pv!}</td>
							<td align="center">${voList.clk!}</td>
							<td align="center">${voList.clkRate!}</td>
			 	    </tr>
			 	    </#list>  
			 	</tbody>
			
			 	<tfoot>
			 	<tr height="35" role="row">
							<th height="30" align="center" data-column="0">總計：${sumProdAdVo.rowTotal!}</th>
							<th height="30" align="center" data-column="1">${sumProdAdVo.pv!}</th>
							<th height="30" align="center" data-column="2">${sumProdAdVo.clk!}</th>
							<th height="30" align="center" data-column="3">${sumProdAdVo.clkRate!}</th>
			
			   	</tr> 
			   	</tfoot>
			</table>
		</div>
</div>
</body>
</html>

