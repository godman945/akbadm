<#assign s=JspTaglibs["/struts-tags"]>

<script type="text/javascript">

function addAccount() {
    window.location = "pfdAccountAdd.html";
}

function updateAccount(targetId) {
    document.getElementById("targetId").value = targetId;
    document.forms[0].action = "pfdAccountUpdate.html";
    document.forms[0].submit();
}

</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />經銷商維護</h2>
<table width="95%">
    <tr>
        <td><input type="button" id="addBtn" value="新增" onclick="addAccount()"></td>
    </tr>
</table>
<br>
<table class="table01" width="800">
    <tr>
        <td class="td01" width="120">帳戶編號</td>
        <td class="td01" width="200">公司名稱</td>
        <td class="td01" width="80">統一編號</td>
        <td class="td01" width="50">狀態</td>
        <td class="td01" width="120">建立日期</td>
        <td class="td01" width="120">啟用日期</td>
    </tr>
<#if accountList?exists>
    <#list accountList as account>
    <tr>
        <td class="td03"><a href="javascript:updateAccount('${account.customerInfoId!}');">${account.customerInfoId}</a></td>
        <td class="td02">${account.companyName}</td>
        <td class="td03">${account.companyTaxId}</td>
        <td class="td03">${accountStatusMap[account.status]}</td>
        <td class="td03">${account.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
        <td class="td03"><#if account.activateDate?exists>${account.activateDate?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
    </tr>
    </#list>
</#if>
</table>

<input type="hidden" id="messageId" value="${message!}">

<form action="" method="post">
    <input type="hidden" id="targetId" name="targetId" value="">
</form>
