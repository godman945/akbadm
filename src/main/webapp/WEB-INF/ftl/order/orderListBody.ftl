<#assign s=JspTaglibs["/struts-tags"]>

<div class="cont">
	<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />訂單搜尋</h2>
	
	<div  class="grtba">
		
<table width="95%" class="srchtb">
    <tr>
    	<td align="right">訂單編號</td>
		<td> 
        	<input type="text" id="ordNo" name="ordNo" />      
		</td>
    	<td align="right">帳戶編號</td>
		<td align="left">
        	<input type="text" id="account" name="account" />
		</td>
	</tr>

    <tr>
    	<td align="right">開始日期</td>
    	<td align="left">
    		<input type="text" id="ordStrDate" name="ordStrDate" value="${ordStrDate!}" readonly="readonly" />
    	</td>  
    	<td align="right">結束日期</td>
		<td align="left">
    		<input type="text" id="ordEndDate" name="ordEndDate" value="${ordEndDate!}" readonly="readonly" />
    	</td>  	
	</tr>
	
	<tr>
		<td align="right">訂單狀態</td>
		<td align="left">
            <select id="ordStatus" name="ordStatus" >
            	<option value="" selected="selected">---請選擇---</option>
		    	<option value="B1" >未付款</option>
		    	<option value="B2" >付款中</option>
		    	<option value="B3" >付款成功</option>
		    	<option value="B4" >付款失敗</option>
		    	<option value="R1" >退款審核中</option>
		    	<option value="R2" >退款中</option>
		    	<option value="R3" >退款成功</option>
		    	<option value="R4" >退款失敗</option>
		    </select>
		</td>
	</tr>
	
    <tr>
    	<td colspan="4" align="center"></td>
	</tr>
	
	<tr>
		<td colspan="4" align="center">
		    <input type="button" id="search" value="查詢訂單" />
		    <#-- <input type="button" id="reset" value="清除重填" /> -->
		</td>
	</tr>
</table>
</from>
</div>

<br>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />訂單列表</h2>
 <div  class="grtba">
<input type="hidden" id="billingUrl" value="${billingUrl!}" /> 
<div id="transTable"></div>

	</div>

</div>

