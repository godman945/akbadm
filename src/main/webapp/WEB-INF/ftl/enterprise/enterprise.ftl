<center><div class="titel_t">PChome百大客戶匯入</div></center>
<br>

<form method="post" action="uploadEnterprise.html" enctype="multipart/form-data">
請選擇檔案：<input type="file" name="fileUpload" />
<br>
<br>
<input type="submit" value="上傳">
</form>

<#if message?exists && message!="">
<br>
<div class="t15_red">
    ${message}
</div>
</#if>

<br><br>新增清單：<br>
<#list addMap?keys as skey>
    <font color="#0000CC">${addMap[skey]} ( ${skey} )</font><br>
</#list>

<br><br>移除清單：<br>
<#list removeMap?keys as skey>
    <font color="#0000CC">${removeMap[skey]} ( ${skey} )</font><br> 
</#list>
