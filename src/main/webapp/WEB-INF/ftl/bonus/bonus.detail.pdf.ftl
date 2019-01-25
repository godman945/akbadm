<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>請款單</title>
<style type="text/css">
/*    請款單    */
.sttm01{ position:relative; width:650px; height:650px; border:1px solid #000; padding:15px; font-size:13px; margin:0 auto 750px auto;}
.sttm01 h1{ font-size:32px; text-align:center;margin:5px 0 0 0;}
.sttm01 h1 img{vertical-align: top;}
.sttm01 .date{font-size: 16px;line-height: 24px;margin-bottom: 15px; text-align: center;}
.sttm01 .date_prn{ float:right; width:140px; line-height:20px;}
.sttm01 .info{ width:400px; font-size:16px; font-weight:bold; line-height:24px;}
.sttm01 .info u{ color:#FF0000; text-decoration:none;}
.sttm01 h3{ clear:both; font-size:16px;margin: 20px 0 5px 0;}
.sttm01 p{ font-size:13px; line-height:20px;margin-bottom: 15px;}
.sttm01 .stul{ width:80%; height:350px; list-style:none; margin-top: 15px;padding: 10px; background: #F9F9F9;}
.sttm01 .stul li{ width:100%; height:26px; line-height:26px;font-size:16px;}
.sttm01 .stul li.all{ border-top: 2px solid #000; font-weight:bold; margin-top:5px;height: 40px;}
.sttm01 .stul li .bold{ font-weight:bold;}
.sttm01 .stul li b u{font-size:10px; font-weight:normal;}
.sttm01 .stul li span{ display:block; float:left; width:40%}
.sttm01 .stul li b{ display:block; float:right; width:30%; text-align:right;}
.sttm01 .stul li .normal{ font-weight:normal;}
.sttm01 .stul li.all u{ font-size:13px; font-weight:normal; text-decoration:none; line-height:20px;}
.sttm01 .sign{ clear:both; width:230px; border-bottom:2px solid #000; height:40px; float:right; font-size:13px;}
.sttm01 .sign b{ display: block;font-size: 13px;  font-weight: normal;margin-top: 20px;}

/*    請款單2  */
.sttm02{ position:relative; width:650px; height:auto;border:1px solid #000; padding:15px; font-size:13px;margin:0 auto 20px auto;} 
.sttm02 h1{ font-size:32px; text-align:center;margin:5px 0 0 0;}
.sttm02 h1 img{vertical-align: top;}
.sttm02 .date{font-size: 16px;line-height: 24px;margin-bottom: 15px; text-align: center;}
.sttm02 h3{ clear:both; font-size:16px;margin: 20px 0 5px 0;}
.sttm02 p{ font-size:13px; line-height:20px;margin: 0;}
.sttm02 .date_prn{ float:right; width:140px; line-height:20px;}
.sttm02 .info{ width:400px; font-size:16px; font-weight:bold; line-height:24px;}
.sttm02 .info u{ color:#FF0000; text-decoration:none;}
.sttm02 .sttb{background:#bcbbb8;text-align:center;margin-bottom:10px;}
.sttm02 .sttb thead td{ background:#a7d8f7; font-weight:bold;}
.sttm02 .sttb th{background:url(../img/gthbg.gif) #eee top repeat-x; padding:5px;}
.sttm02 .sttb td{background:#fff;padding:5px;}
.sttm02 .sttb tr.all{ text-align:right; font-weight:bold;}
.sttm02 .sign{ clear:both; width:230px; height:100px; float:right; font-size:13px;}
.sttm02 .sign b{ display: block;font-size: 13px;margin-top: 80px;}
.sttm02 .sign2{ width:180px; border-bottom:2px solid #000; font-size: 9px; font-weight: normal;}
</style>
</head>

<body style="font-family:'Arial Unicode MS'">
<div class="sttm01">

<h1>
	<img src="http://show.pchome.com.tw/pfd/html/img/logo_pchome.png" border="0" />
	請款確認單
</h1>
<div class="date">請款區間：${checkRangDate!}</div>	
<div class="date_prn">列印日期：${printDate!}<br />
<!-- 請款人員：${checkPerson!} --></div>
		

<div class="info">經銷商:${pfdAccountTitle!}<br />
經銷商統一編號：${companyTaxId!}</p>
</div>

<div class="info">發票抬頭:${invoiceTitle!}<br />
統一編號：${invoiceTaxId!}<br />
</div>

<ul class="stul">

<li><span>本期廣告費用(+)</span><b class="normal" style="float:left;">$ ${totalAdClick?string("###,###")!}</b></li>

<li><span>本期佣金(-)</span><b class="normal">$ ${bonusMoney?string("###,###")!}</b></li>

<li><span>本期退款沖銷(+)</span><b class="normal"  style="float:left;">$0</b></li>

<li><span>本期應付獎金(-)</span><b class="normal">$ ${quarterAndYearBonusMoney?string("###,###")!}</b></li>

<li><span>前期未結廣告費用 (+)</span><b class="normal"  style="float:left;">$ ${nonCloseTotalAdClick?string("###,###")!}</b></li>

<li><span>前期未結佣(獎)金(-)</span><b class="normal">$ ${nonCloseBonusMoney?string("###,###")!}</b></li>

<li class="all"><span>應收廣告費用：</span><b class="normal" style="float:left;">$ ${totalAdNonCloseAdClick?string("###,###")!}</b></li>
<li><span>營業稅：</span><b class="normal" style="float:left;">$ ${totalAdClickTax?string("###,###")!}</b></li>
<li><span class="bold">總金額：</span><b style="float:left;">$ ${totalAdClickSum?string("###,###")!}<br /><u>(開立發票金額含稅)</u></b></li>
<li><span class="bold">應付佣金：</span><b class="normal">$ ${totalPayMoney?string("###,###")!}</b></li>
<li><span>營業稅：</span><b class="normal">$ ${totalPayTax?string("###,###")!}</b></li>
<li><span class="bold">總金額：</span><b>$ ${totalPayMoneySum?string("###,###")!}<br /><u>(開立發票金額含稅)</u></b></li>


</ul>
			
<h3>注意事項</h3>
每月10號前，本公司依請款確認單內所載金額開立廣告費發票寄予貴公司請款。</p>
</div>

<div class="sttm02">
<h1>
	<img src="http://show.pchome.com.tw/pfd/html/img/logo_pchome.png" border="0" />
	佣金請款明細
</h1>
<div class="date">帳款結帳區間：${checkRangDate!}</div>	
<div class="date_prn">列印日期：${printDate!}<br />
<!-- 請款人員：${checkPerson!}<br />
請款日期：${checkDate!} -->
</div>
		

<div class="info">經銷商:${pfdAccountTitle!}<br />
<!-- 發票抬頭:${invoiceTitle!}<br />
統一編號：${companyTaxId!}<br />
<u>付款方式：${payType!}</u> --></div>
<h3>應付佣金(+)	</h3>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="sttb">
<tbody>  
	<tr>
		<th style="width:20%" style="font-size:11px;">合約編號</th>
		<th style="width:13%" style="font-size:11px;">項目</th>
		<th style="width:42%" style="font-size:11px;">項目說明</th> 
		<th style="width:25%" style="font-size:11px;">佣金</th>
	</tr>
	
	<#if pfdBonusDetailVos?? >
		<#list pfdBonusDetailVos as vo>
			<tr>		 
				<td align="left" style="font-size:11px;">${vo.contractId!}</td>
				<td align="left" style="font-size:11px;">${vo.bonusItemName!}</td>
				<td align="left" style="font-size:11px;" class="tdHeight" >${vo.bonusDetailDesc!}</td>
				<td align="right" style="font-size:11px;">$ ${vo.nowBonus?string("###,###")!}</td>		
			</tr>
		</#list>
	</#if>
	
	<tr class="all">
		<td colspan="3" style="font-size:11px;">小計</td>		
		<td style="font-size:11px;">$ ${totalPayMoney?string("##,###")!}</td>
	</tr>
	<tr class="all">
		<td colspan="3" style="font-size:11px;">營業稅</td>		
		<td style="font-size:11px;">$ ${totalPayTax?string("##,###")!}</td>
	</tr>
	<tr class="all">
		<td colspan="3" style="font-size:11px;">總計(開立發票含稅金額)</td>		
		<td style="font-size:11px;">$ ${totalPayMoneySum?string("##,###")!}</td>
	</tr>
	
</tbody>
</table>
<!-- <h3>訂單應付明細(+)</h3> -->

<!-- 月獎金  -->
<#if monthBonusDetailVo?? && (monthBonusDetailVo?size > 0)>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="sttb">
<thead>
<tr>
<td colspan="7">月達成獎金</td>
</tr>
</thead>
<tbody>  
  	<tr>
	    <th>編號</th>
	    <th>帳戶編號</th>
	    <th>帳戶名稱</th>
	    <th>項目</th>
	    <th>廣告點擊費用</th>
	    <th>獎金比率 %</th>
	    <th>獎金</th>
   	</tr>
   	<#list monthBonusDetailVo as vo>
	<tr>
		<td>${(vo_index+1)!}</td>
		<td align="left">${vo.pfpId!}</td>
		<td align="left">${vo.pfpName!}</td>
		<td align="left">${vo.bonusItemName!}</td>
		<td align="right">$ ${vo.adClkPrice?string("###,###")!} </td>
		<td align="right">${(vo.bonusPercent)!} %</td>
		<td align="right">$ ${vo.bonusMoney?string("###,###")!}</td>
	</tr>
   	</#list>
	<tr class="all">
		<td colspan="6">總計</td>
		<td>$ ${totalMonthBonus?string("###,###")!}</td>
	</tr>
</tbody>
</table>
</#if>

<!-- 季獎金 -->
<#if quarterBonusDetailVo?? && (quarterBonusDetailVo?size > 0)>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="sttb">
<thead>
<tr>
<td colspan="7">季達成獎金</td>
</tr>
</thead>
<tbody>
  
	<tr>
		<th>編號</th>
		<th>帳戶編號</th>
	    <th>帳戶名稱</th>
		<th>項目</th>
		<th>廣告點擊費用</th>
		<th>獎金比率 %</th>
		<th>佣金</th>
	</tr>
	
	<#list quarterBonusDetailVo as vo>
	<tr>
		<td>${(vo_index+1)!}</td>
		<td align="left">${vo.pfpId!}</td>
		<td align="left">${vo.pfpName!}</td>
		<td align="left">${vo.bonusItemName!}</td>
		<td align="right">$ ${vo.adClkPrice?string("###,###")!} </td>
		<td align="right">${(vo.bonusPercent)!} %</td>
		<td align="right">$ ${vo.bonusMoney?string("###,###")!}</td>
	</tr>
	</#list>
	
    <tr class="all">
		<td colspan="6">總計</td>
		<td>$ ${totalQuarterBonus?string("###,###")!}</td>
	</tr>
</tbody>
</table>
</#if>

<!-- 年獎金 -->
<#if yearBonusDetailVo?? && (yearBonusDetailVo?size > 0)>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="sttb">
<thead>
<tr>
<td colspan="7">年達成獎金</td>
</tr>
</thead>
<tbody>
  
	<tr>
		<th>編號</th>
		<th>帳戶編號</th>
	    <th>帳戶名稱</th>
		<th>項目</th>
		<th>廣告點擊費用</th>
		<th>獎金比率 %</th>
		<th>佣金</th>
	</tr>
	
	<#list yearBonusDetailVo as vo>
	<tr>
		<td>${(vo_index+1)!}</td>
		<td align="left">${vo.pfpId!}</td>
		<td align="left">${vo.pfpName!}</td>
		<td align="left">${vo.bonusItemName!}</td>
		<td align="right">$ ${vo.adClkPrice?string("###,###")!} </td>
		<td align="right">${(vo.bonusPercent)!} %</td>
		<td align="right">$ ${vo.bonusMoney?string("###,###")!}</td>
	</tr>
	</#list>
	
    <tr class="all">
		<td colspan="6">總計</td>
		<td>$ ${totalYearBonus?string("###,###")!}</td>
	</tr>
</tbody>
</table>
</#if>

<!-- 專案獎金 -->
<#if caseBonusDetailVo?? && (caseBonusDetailVo?size > 0)>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="sttb">
<thead>
<tr>
<td colspan="7">專案獎金</td>
</tr>
</thead>
<tbody>
  
	<tr>
		<th>編號</th>
		<th>激勵時間</th>
		<th>活動名稱</th>
		<th>活動內容</th>
		<th>廣告點擊費用</th>
		<th>佣金率</th>
		<th>獎金</th>
	</tr>
	<#list caseBonusDetailVo as vo>
	<tr>
		<td>${(vo_index+1)!}</td>
		<td align="left">${vo.startDate?string("yyyy-MM-dd")!} ~ ${vo.endDate?string("yyyy-MM-dd")!}</td>
		<td align="left">${vo.bonusItemName!}</td>
		<td align="right">${vo.bonusNote!}</td>
		<td align="right">$ ${vo.adClkPrice?string("###,###")!} </td>
		<td align="right">$ ${(vo.bonusPercent)!} %</td>
		<td align="right">$ ${vo.bonusMoney?string("###,###")!}</td>
	</tr>
	</#list>  
	<tr class="all">
		<td colspan="6">總計</td>
		<td>$ ${totalCaseBonus?string("###,###")!}</td>
	</tr>
</tbody>
</table>
</#if>

<!-- 開發獎金 -->
<#if developBonusDetailVo?? && (developBonusDetailVo?size > 0)>
<table width="100%" border="0" cellspacing="1" cellpadding="0" class="sttb">
<thead>
<tr>
<td colspan="7">開發獎金</td>
</tr>
</thead>
<tbody>
  
	<tr>
		<th>編號</th>
		<th>開發時間</th>
		<th>活動名稱</th>
		<th>開發內容</th>
		<th>開發間數</th>
		<th>佣金率</th>
		<th>獎金</th>
	</tr>
	<#list developBonusDetailVo as vo>
	<tr>
		<td>${(vo_index+1)!}</td>
		<td align="left">${vo.startDate?string("yyyy-MM-dd")!} ~ ${vo.endDate?string("yyyy-MM-dd")!}</td>
		<td align="left">${vo.bonusItemName!}</td>
		<td align="right">${vo.bonusNote!}</td>
		<td align="right">${vo.developAmount!}</td>
		<td align="right">-</td>
		<td align="right">$ ${vo.bonusMoney?string("###,###")!}</td>
	</tr>
	</#list>  
	<tr class="all">
		<td colspan="6">總計</td>
		<td>$ ${totalDevelopBonus?string("###,###")!}</td>
	</tr>
</tbody>
</table>
</#if>
<div class="sign"><b>請款確認：<span class="sign2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;請在此蓋公司發票章&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></b></div>
<h3>注意事項</h3>
<p>佣金請款明細單請務必蓋上公司發票章，連同發票收據於每月15號前寄達本公司，逾期帳款將延至下期結帳月份結算。<br />
								
信封上請務必註明經銷商代號：${pfdCustomerInfoId!}以及Portal產品部-PChome聯播網經銷商小組收	<br />							
寄送地址：${sentAddress!}<br />								
聯絡人：${checkPerson!}<br />								
電話：${checkPersonTel!} 傳真：${fox!}</p>
</div>
</body>
</html>
