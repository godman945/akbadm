<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<input type="hidden" id="paramAdPoolSeq" name="paramAdPoolSeq" value="${paramAdPoolSeq!}" />
<input type="button" id="btn_newDad" name="newDad" value="新增" onclick="addDad()">
<table class="table01">
    <tr>
        <td width="150" class="td01">廣告定義序號</td>
        <td width="150" class="td01">pool 序號</td>
        <td width="150" class="td01">廣告定義代碼</td>
        <td width="150" class="td01">廣告定義名稱</td>
        <td width="150" class="td01">廣告定義類型</td>
        <td width="150" class="td01">操作</td>
    </tr>
    <#list defineAdList as defineAd>
    <tr>
        <td class="td02">${defineAd.defineAdSeq!}</td>
        <td class="td02">${defineAd.adPoolSeq!}</td>
        <td class="td03">${defineAd.defineAdId!}</td>
        <td class="td03">${defineAd.defineAdName!}</td>
        <td class="td03">${defineAd.admDefineAdType.defineAdTypeName!}</td>
        <td class="td03">
            <#if (defineAd.admRelateTadDads?size == 0)>
            <input type="button" id="updateBtn" value="修改" onclick="updateDad('${defineAd.defineAdSeq!}')">
            <input type="button" id="deleteBtn" value="刪除" onclick="doDelete('${defineAd.defineAdSeq!}')">
            </#if>
        </td>
    </tr>
    </#list>
</table>
<input type="hidden" id="messageId" value="${message!}" />