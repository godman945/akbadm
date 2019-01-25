<#assign s=JspTaglibs["/struts-tags"]>

<table class="table01" width="90%">
    <tr>
        <td class="td01" width="50">排名</td>
        <td class="td01" width="100">帳戶名稱</td>
        <td class="td01" width="100">廣告活動</td>
        <td class="td01" width="50">每日花費上線</td>
        <td class="td01" width="50">曝光數</td>
        <td class="td01" width="50">點選率</td>
        <td class="td01" width="50">點選次數</td>
        <td class="td01" width="50">未達費用</td>
        <td class="td01" width="50">費用</td>
    </tr>
    <#if adPvclkVOs?exists && (adPvclkVOs?size>0)>
    <#list adPvclkVOs as vos>
    <tr>
        <td class="td03">${vos_index+1!}</td>
        <td class="td03">${vos.pfpAdPvclk[5]!}</td>
        <td class="td03">${vos.pfpAdPvclk[6]!}</td>
        <td class="td03">${vos.pfpAdPvclk[7]!}</td>
        <td class="td03">${vos.pfpAdPvclk[0]!}</td>
        <td class="td03">${vos.adClickRate?string("0.##")!}%</td>
        <td class="td03">${vos.pfpAdPvclk[1]!}</td>
        <td class="td03">${vos.shortCost?string("0.##")!}</td>
        <td class="td03">${vos.pfpAdPvclk[2]!}</td>
    </tr>
    </#list>
    </#if>
</table>


