<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />行動裝置成效</h2>

<#if dataList?exists && (dataList?size>0)>

<table class="table01" width="800">
    <tr>
        <td class="td01">廣告內容</td>
        <td class="td01" width="80">廣告活動</td>
        <td class="td01" width="80">廣告群組</td>
        <td class="td01" width="80">曝光數</td>
        <td class="td01" width="80">點選次數</td>
        <td class="td01" width="80">點選率</td>
        <td class="td01" width="80">平均點選出價</td>
        <td class="td01" width="80">費用</td>
    </tr>
    <#assign index=0>
    <#list dataList as data>
    <tr>
        <td class="td02" height="35">
	    	<iframe height="120" width="350" src="adModel.html?adNo=${data.adSeq!}&tproNo=tpro_201306280001" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" align="ceneter" class="akb_iframe"></iframe>
			</iframe>    	
        </td>
        <td class="td02">${data.adActionName!}</td>
        <td class="td04">${data.adGroupName!}</td>
        <td class="td04">${data.pvSum!}</td>
        <td class="td04">${data.clkSum!}</td>
        <td class="td04">${data.clkRate!}</td>
        <td class="td04">${data.clkPriceAvg!}</td>
        <td class="td04">${data.priceSum!}</td>
    </tr>
    </#list>

</table>

<#else>

<#if message?exists && message!="">

${message!}

</#if>

</#if>

</form>
