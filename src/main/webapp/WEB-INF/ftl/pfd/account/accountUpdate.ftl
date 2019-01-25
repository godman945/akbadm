<#assign s=JspTaglibs["/struts-tags"]>

<script type="text/javascript">

function doUpdate() {
    document.forms[0].submit();
}

$(document).ready(function(){

	var selectValue = $("#pfpAdtypeSelect").val();
	if(selectValue != ""){
		var selectArray = selectValue.split("");
		for(var i=1;i<=12;i++){
			if(selectArray[i - 1] == "1"){
				if(i==1 || i==5 || i==9){
					$("#deviceTr" + i).show();
				}
				$("#pfpAdtypeBox" + i).attr("checked","checked");
			}
		}
	}

	$("[id ^= 'pfpAdtypeBox']").change(function(){
		var number = $(this).attr("id").replace("pfpAdtypeBox","");
		if(number=="1" || number=="5" || number =="9"){
			if($(this).attr('checked')){
				$("#deviceTr" + number).show();
				for(var j=1;j<=3;j++){
					$("#pfpAdtypeBox" + (parseInt(number)+j)).attr("checked","true");
				}
			} else {
				$("#deviceTr" + number).hide();
				for(var j=1;j<=3;j++){
					$("#pfpAdtypeBox" + (parseInt(number)+j)).removeAttr("checked");
				}
			}
		}
		
		var adtypeSelect = "";
		for(var i=1;i<=12;i++){
			if($("#pfpAdtypeBox" + i).attr('checked')){
				adtypeSelect = adtypeSelect + "1";
			} else {
				adtypeSelect = adtypeSelect + "0";
			}
		}
		$("#pfpAdtypeSelect").val(adtypeSelect);
	});

	var reSendBtn = $('#reSendBtn');
    reSendBtn.bind('click', function() {
        $.ajax({
            url: 'reSendPfdAccountEmail.html',
            data : {'targetId' : $('#customerInfoId').val()},
            type: 'post',
            beforeSend: function() {
            },
            dataType: 'json',
            success: function(response, status) {
                var message = response.message;
                if ( message != "" || message.length > 0) {
                    alert(message);
                }
            },
            error: function(xtl) {
                alert("系統繁忙，請稍後再試！");
            }
        });        
    });

});

</script>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />經銷商修改</h2>

<form action="doPfdAccountUpdate.html" method="post">

經銷商資料：<br>
<table width="600" class="table01">
    <tr>
        <td width="100" class="td01">帳戶編號</td>
        <td class="td02">${customerInfoId!}</td>
        <input type="hidden" name="customerInfoId" id="customerInfoId" value="${customerInfoId!}">
    </tr>
    <tr>
        <td width="100" class="td01">狀態</td>
        <td class="td02">
            <#if customerInfoStatus == "0">
                <input type="hidden" name="customerInfoStatus" id="customerInfoStatus" value="${customerInfoStatus!}">申請中
            <#else>
                <select name="customerInfoStatus" id="customerInfoStatus">
                <#list accountStatusMap?keys as key>
                    <#if key != "0" && key != "4">
		  		        <option value="${key}" <#if customerInfoStatus?exists && customerInfoStatus = key>selected="selected" </#if>>${accountStatusMap[key]}</option>
		  		    </#if>
		  	    </#list>
		  	    </select>
            </#if>
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">公司名稱 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="companyName" value="${companyName!}" size="20" maxlength="30" /> ( 限 30 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">統一編號 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="companyTaxId" value="${companyTaxId!}" size="8" maxlength="8" /> ( 限 8 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">公司負責人 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="companyOwner" value="${companyOwner!}" size="10" maxlength="20" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">公司電話 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="companyTel" value="${companyTel!}" size="20" maxlength="20" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">公司傳真 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="companyFax" value="${companyFax!}" size="20" maxlength="20" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">公司郵遞區號 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="companyZip" value="${companyZip!}" size="5" maxlength="5" /> ( 限 5 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">公司地址 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="companyAddr" value="${companyAddr!}" size="30" maxlength="50" /> ( 限 50 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">公司網址 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="companyWebsite" value="${companyWebsite!}" size="30" maxlength="255" /> ( 限 255 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">公司類型</td>
        <td class="td02">
            <input type="radio" name="companyCategory" value="1" <#if companyCategory?exists && companyCategory == "1">checked</#if>>經銷商 &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="radio" name="companyCategory" value="2" <#if companyCategory?exists && companyCategory == "2">checked</#if>>代理商
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">其他設定</td>
        <td class="td02">
            <input type="checkbox" id="mixFlag" name="mixFlag" value="y" <#if mixFlag?exists && mixFlag == "y">checked</#if>>綜合型經銷商(合約到期後，已建立的廣告要繼續播完且能拆帳)
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">廣告播放類型</td>
        <td class="td02">
            <input type="checkbox" id="pfpAdtypeBox1" name="pfpAdtypeBox1" value="1" />搜尋廣告+聯播網廣告
            <input type="checkbox" id="pfpAdtypeBox5" name="pfpAdtypeBox2" value="1" />搜尋廣告
            <input type="checkbox" id="pfpAdtypeBox9" name="pfpAdtypeBox3" value="1" />聯播網廣告
            <input type="hidden" id="pfpAdtypeSelect" name = "pfpAdtypeSelect" value="${pfpAdtypeSelect!}" />
        </td>
    </tr>
    <tr id="deviceTr1" style="display:none;">
        <td width="100" class="td01">搜尋廣告+聯播網廣告</td>
        <td class="td02">
            <input type="checkbox" id="pfpAdtypeBox2" name="pfpAdtypeBox2" value="1" />電腦+行動裝置
            <input type="checkbox" id="pfpAdtypeBox3" name="pfpAdtypeBox3" value="1" />電腦
            <input type="checkbox" id="pfpAdtypeBox4" name="pfpAdtypeBox4" value="1" />行動裝置
        </td>
    </tr>
    <tr id="deviceTr5" style="display:none;">
        <td width="100" class="td01">搜尋廣告</td>
        <td class="td02">
            <input type="checkbox" id="pfpAdtypeBox6" name="pfpAdtypeBox6" value="1" />電腦+行動裝置
            <input type="checkbox" id="pfpAdtypeBox7" name="pfpAdtypeBox7" value="1" />電腦
            <input type="checkbox" id="pfpAdtypeBox8" name="pfpAdtypeBox8" value="1" />行動裝置
        </td>
    </tr>
    <tr id="deviceTr9" style="display:none;">
        <td width="100" class="td01">聯播網廣告</td>
        <td class="td02">
            <input type="checkbox" id="pfpAdtypeBox10" name="pfpAdtypeBox10" value="1" />電腦+行動裝置
            <input type="checkbox" id="pfpAdtypeBox11" name="pfpAdtypeBox11" value="1" />電腦
            <input type="checkbox" id="pfpAdtypeBox12" name="pfpAdtypeBox12" value="1" />行動裝置
        </td>
    </tr>
    <tr>
        <td width="100" class="td01">備註</td>
        <td class="td02">
            <textarea name="note" cols=40 rows=5>${note!}</textarea> ( 限 200 字元 )
        </td>
    </tr>
</table>
<br>

經銷商聯絡人資料：<br>
<table width="600" class="table01">
    <tr>
        <td width="100" class="td01">聯絡人 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="contactPerson" value="${contactPerson!}" size="10" maxlength="20" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">聯絡電話 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="contactTel" value="${contactTel!}" size="20" maxlength="20" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">行動電話 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="contactMobile" value="${contactMobile!}" size="10" maxlength="10" /> ( 限 10 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">Email <font color="red">*</font></td>
        <td class="td02"><input type="text" name="contactEmail" value="${contactEmail!}" size="30" maxlength="50" /> ( 限 50 字元 )&nbsp;&nbsp; <#if customerInfoStatus == "0"><input type="button" id="reSendBtn" value="補寄認證信"></#if></td>
    </tr>
</table>
<br>

財務聯絡人資料：<br>
<table width="600" class="table01">
    <tr>
        <td width="100" class="td01">聯絡人 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="financialOfficer" value="${financialOfficer!}" size="10" maxlength="20" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">聯絡電話 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="financialTel" value="${financialTel!}" size="20" maxlength="20" /> ( 限 20 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">行動電話 <font color="red">*</font></td>
        <td class="td02"><input type="text" name="financialMobile" value="${financialMobile!}" size="10" maxlength="10" /> ( 限 10 字元 )</td>
    </tr>
    <tr>
        <td width="100" class="td01">Email <font color="red">*</font></td>
        <td class="td02"><input type="text" name="financialEmail" value="${financialEmail!}" size="30" maxlength="50" /> ( 限 50 字元 )</td>
    </tr>
</table>
<br>

P幣設定：<br>
<table width="600" class="table01">
    <tr>
        <td width="100" class="td01">可使用總額度 <font color="red">*</font></td>
        <td class="td02">${totalQuota!}</td>
    </tr>
</table>
<br>

<table width="600">
    <tr>
        <td align="center">
            <input type="button" value="確定" onclick="doUpdate()">
            <input type="button" value="返回" onclick="javascript:window.location='pfdAccountMaintain.html'">
        </td>
    </tr>
</table>

</form>

<input type="hidden" id="messageId" value="${message!}">
