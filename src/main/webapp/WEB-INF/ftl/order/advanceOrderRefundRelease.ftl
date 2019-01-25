<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />預付退款審核</h2>

<div  class="grtba">

<form action="advanceOrderRefundRelease.html" method="post">

<table width="95%" class="srchtb">
    <tr>
    	<td align="right">訂單編號</td>
		<td> 
            <input type="text" id="billingOrderId" name="billingOrderId" value="${billingOrderId!}" />
        </td>
       	<td align="right">帳戶編號</td>
		<td align="left">
        	<input type="text" id="pfpCustomerInfoId" name="pfpCustomerInfoId" value="${pfpCustomerInfoId!}" />
        </td>
    </tr>
    <tr>
        <td align="right">申請日期</td>
    	<td align="left">
        	<input type="text" id="startDate" name="startDate" value="${startDate!}"  readonly />
        </td>
        <td align="right">~</td>
		<td align="left">
            <input type="text" id="endDate" name="endDate" value="${endDate!}"  readonly />
		</td>
	</tr>
	<tr>
        <td align="right">退款狀態 </td>
        <td align="left">
            <select id="refundStatus" name="refundStatus" >
            	<option value="y" selected="selected">退款審核中</option>
		    	<option value="p" >退款完成</option>		    	
		    </select>
        </td>
    <tr>
        <td colspan="5" align="center">
            <br>
            <input type="button" value="查詢" onclick="doQuery()" />
        </td>
    </tr>
</table>
</div>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>

<#if dataList?exists && (dataList?size>0)>

<table>
    <tr>
        <td>
            <input type="button" value="核准" onclick="doApprove()">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="拒絕" onclick="doReject()">
        </td>
    </tr>
</table>
<@t.insertAttribute name="page" />
<table class="table01">
    <tr>
        <td class="td01" width="50">
        	勾選
        	<!--勾選<input type="checkbox" onclick="selectAll_new(this, 'releaseSeq', 'rejectReason')" />-->
        </td>
        <td class="td01" width="280">帳戶編號</td>
        <td class="td01" width="200">帳戶名稱</td>
        <td class="td01" width="250">訂單編號</td>
        <td class="td01" width="200">訂單日期</td>
        <td class="td01" width="200">訂單金額</td>
        <td class="td01" width="200">可退款金額</td>
        <td class="td01" width="200">付款方式</td>
        <td class="td01" width="150">退款狀態</td>
        <td class="td01" width="200">金流狀態</td>
        <td class="td01" width="200">申請日期</td>
          <td class="td01" width="100">拒絕原因</td>
    </tr>

    <#list dataList as data>
    <tr>
    	<#if lastQueryStatus == '' || lastQueryStatus == 'y' >
    		<#if data.overOneDay == 'Y' >
       			<td class="td03"><input type="checkbox" name="releaseSeqs" value="${data.releaseSeq!}" onclick="controlAdUI(this, 'rejectReason_${data.releaseSeq!}')" ></td>
       		<#elseif data.overOneDay == 'N' >	
       			<td class="td03"><input type="checkbox" name="releaseSeqs" value="${data.releaseSeq!}" onclick="controlAdUI(this, 'rejectReason_${data.releaseSeq!}')" disabled ></td>
       		</#if>
        <#elseif lastQueryStatus == 'p' >
        	<td class="td03"></td>     	
        </#if>
        
        <td class="td02">${data.customerInfoId!}</td>
        <td class="td02">${data.customerInfoTitle!}</td>
        <td class="td03">${data.billingId!}</td>
        <td class="td02">${data.notifyDate!}</td>
        <td class="td02">${data.orderPriceTax!}</td>
        
        <!--可退款金額-->
        <#if data.refundStatus == 'Y' >
        	<td class="td02">${data.refundPriceTax!}</td>
        <#else>
        	<td class="td02">${data.orderRemainTax!}</td>
        </#if>
        
        <!--付款方式-->
        <#if data.billingPayStatus == 'B301' || data.billingPayStatus == 'R101' || data.billingPayStatus == 'R103' || data.billingPayStatus == 'R201' || data.billingPayStatus == 'R203' || data.billingPayStatus == 'R301' || data.billingPayStatus == 'R303' || data.billingPayStatus == 'R401' || data.billingPayStatus == 'R403'>
        	<td class="td02">信用卡</td>
        <#elseif data.billingPayStatus == 'B302' || data.billingPayStatus == 'R102' || data.billingPayStatus == 'R202' || data.billingPayStatus == 'R302' || data.billingPayStatus == 'R402'>
       	    <td class="td02">ATM</td>
        <#else>
         	<td class="td02">處理中</td>
        </#if>
        
        <!--退款狀態-->
        <#if data.refundStatus == 'F' >
        	<td class="td02">退款審核中</td>
        <#elseif data.refundStatus == 'Y' >
        	<td class="td02">退款完成</td>
        <#elseif data.refundStatus == 'N' >	
        	<td class="td02">退款失敗</td>
        </#if>
        
        <!--金流狀態-->        
        <#if data.billingStatus == 'B301' || data.billingStatus == 'B302'>
        	<td class="td02">付款成功</td>
        <#elseif data.billingStatus == 'R101' || data.billingStatus == 'R102' || data.billingStatus == 'R103'>
        	<td class="td02">退款審核中</td>
        <#elseif data.billingStatus == 'R201' || data.billingStatus == 'R202' || data.billingStatus == 'R203'>
        	<td class="td02">退款中</td>
        <#elseif data.billingStatus == 'R301' || data.billingStatus == 'R302' || data.billingStatus == 'R303'>
        	<td class="td02">退款完成</td>
        <#elseif data.billingStatus == 'R401' || data.billingStatus == 'R402' || data.billingStatus == 'R403'>
        	<td class="td02">退款失敗</td>
        <#elseif data.billingStatus == 'R501' >
        	<td class="td02">退款審核成功</td>
        <#elseif data.billingStatus == 'R601' >
        	<td class="td02">退款審核失敗</td>
        <#else >	
        	<td class="td02">處理中</td>
        </#if>
        
        <!--申請日期--> 
        <td class="td02">${data.applyTime!}</td>  
 		<!--拒絕原因-->      
        <td class="td02">
            <input type="text" name="rejectReason" id="rejectReason_${data.releaseSeq!}" disabled>            
        </td>
        
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>
	<input type="hidden" id="lastQueryStatus" name="lastQueryStatus" value="${lastQueryStatus!}" />
</form>


