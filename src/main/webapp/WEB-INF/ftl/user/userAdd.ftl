<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />新增帳號</h2>
<form action="doUserAdd.html" method="post">
<table width="450" class="table01">
    <tr>
        <td width="100" class="td01">帳號(E-mail)<font color="red">*</font></td>
        <td class="td02"> <input type="text" name="paramUserEmail" value="${paramUserEmail!}" /> ( 限 50 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">密碼<font color="red">*</font></td>
        <td class="td02"> <input type="text" name="paramUserPassword" value="${paramUserPassword!}" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">姓名<font color="red">*</font></td>
        <td class="td02"> <input type="text" name="paramUserName" value="${paramUserName!}" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">部門組織<font color="red">*</font></td>
        <td class="td02">
            <table>
                <tr>
                    <td>
                        <select name="paramParentDeptId" id="paramParentDeptId">
                            <option value="0">-- 請選擇部門 --</option>
                            <#if parentDeptList?exists>
                            <#list parentDeptList as dept1>
                                <#assign selectedFlag="">
                                <#if paramParentDeptId?exists>
                                    <#if dept1.depId?string == paramModelId>
                                        <#assign selectedFlag="selected">
                                    </#if>
                                </#if>
                                <option value="${dept1.depId}" ${selectedFlag}>${dept1.depName}</option>
                            </#list>
                            </#if>
                        </select>
                    </td>
                    <td>
                        <div id="childDeptOptionId">
                        <select name="paramChildDeptId" id="paramChildDeptId">
                            <option value="0">-- 請選擇小組 --</option>
                            <#if childDeptList?exists>
                            <#list childDeptList as dept2>
                                <#assign selectedFlag="">
                                <#if paramChildDeptId?exists>
                                    <#if dept2.depId?string == paramModelId>
                                        <#assign selectedFlag="selected">
                                    </#if>
                                </#if>
                                <option value="${dept2.depId}" ${selectedFlag}>${dept2.depName}</option>
                            </#list>
                            </#if>
                        </select>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">權限範本 <font color="red">*</font></td>
        <td class="td02">
            <select name="paramModelId" id="paramModelId">
                <option value="0">-- 請選擇 --</option>
                <#list privilegeModelMap?keys as key>
                    <#assign selectedFlag="">
                    <#if paramModelId?exists>
                        <#if key == paramModelId>
                            <#assign selectedFlag="selected">
                        </#if>
                    </#if>
                    <option value="${key}" ${selectedFlag}>${privilegeModelMap[key]}</option>
                </#list>
            </select>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">狀態 <font color="red">*</font></td>
        <td class="td02">
            <select name="paramStatus">
                <#list userStatusMap?keys as key>
                    <#assign selectedFlag="">
                    <#if paramStatus?exists>
                        <#if key == paramStatus>
                            <#assign selectedFlag="selected">
                        </#if>
                    </#if>
                    <option value="${key}" ${selectedFlag}>${userStatusMap[key]}</option>
                </#list>
            <select>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">帳號備註 </td>
        <td class="td02"> <textarea name="paramNote" cols=25 rows=2>${pNote!}</textarea> ( 限 50 字元 )</td>
    </tr>
</table>
<br>
<table width="450">
    <tr>
        <td align="center">
            <input type="submit" id="addBtn" value="儲存">
            <input type="button" value="返回" onclick="javascript:window.location='userMaintain.html'">
        </td>
    </tr>
</table>
</form>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />權限預覽</h2>
<div id="showPrivilege"></div>

<input type="hidden" id="messageId" value="${message!}">

<input type="hidden" id="outputHtmlId" value="${outputHtml!}">
