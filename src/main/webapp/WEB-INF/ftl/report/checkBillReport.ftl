<#assign s=JspTaglibs["/struts-tags"]>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />當日當帳表</h2>

<table class="table01" width="800">
    <tr>
    	<td class="td01">對帳日期</td>
        <td class="td01">帳戶</td>
        <td class="td01">點擊花費(不含無效點擊)</td>
        <td class="td01">無效點擊花費</td>
        <td class="td01">超播金額</td>
        <td class="td01">帳戶花費(前台)</td>
        <td class="td01">帳戶餘額(前台)</td>
        <td class="td01">攤提花費(後台)</td>
        <td class="td01">攤提餘額(後台)</td>       
    </tr>
    <#if vos??>
	    <#list vos as vo>
	    	<tr>
		        <td class="td03">${vo.date!}</td>
		        <td class="td03">${vo.pfpId!}</td>
		        <td class="td04" <#if (vo.pvClkPrice-vo.transLoss) != (vo.transPrice-invalidClkPrice) || (vo.pvClkPrice-vo.transLoss) != vo.recognizePrice || (vo.transPrice-invalidClkPrice) != vo.recognizePrice >style="background-color: red;" </#if>>${vo.pvClkPrice?string('#,###')!}</td>
		        <td class="td04">${vo.invalidClkPrice?string('#,###')!}</td>
		        <td class="td04">${vo.transLoss?string('#,###')!}</td>
		        <td class="td04" <#if (vo.pvClkPrice-vo.transLoss) != (vo.transPrice-invalidClkPrice) || (vo.pvClkPrice-vo.transLoss) != vo.recognizePrice || (vo.transPrice-invalidClkPrice) != vo.recognizePrice >style="background-color: red;" </#if>>${vo.transPrice?string('#,###')!}</td>
		        <td class="td04" <#if vo.transRemain?string('#,###')! != vo.recognizeRemain?string('#,###')! >style="background-color: red;" </#if>>${vo.transRemain?string('#,###')!}</td>
		        <td class="td04" <#if (vo.pvClkPrice-vo.transLoss) != (vo.transPrice-invalidClkPrice) || (vo.pvClkPrice-vo.transLoss) != vo.recognizePrice || (vo.transPrice-invalidClkPrice) != vo.recognizePrice >style="background-color: red;" </#if>>${vo.recognizePrice?string('#,###')!}</td>
		        <td class="td04" <#if vo.transRemain?string('#,###')! != vo.recognizeRemain?string('#,###')! >style="background-color: red;" </#if>>${vo.recognizeRemain?string('#,###')!}</td>	        
		    </tr>
	    </#list>
    </#if>
</table>
