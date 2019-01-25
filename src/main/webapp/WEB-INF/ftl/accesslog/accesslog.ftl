<#assign s=JspTaglibs["/struts-tags"]>

<script type="text/javascript">

$.datepicker.regional = {
    showOn: "both",
    buttonImage: "<@s.url value="/" />html/img/icon_cal.gif",
    buttonImageOnly: true,
    buttonText: ""
};

function clean() {
    document.getElementById("paramStatus").options[0].selected = true;
    document.getElementById("paramChannel").options[0].selected = true;
    document.getElementById("paramChannelOrdId").value = "";
    document.getElementById("paramUserId").value = "";
    document.getElementById("paramBillingOrdId").value = "";
    document.getElementById("paramIp").value = "";
    document.getElementById("paramStartDate").value = "";
    document.getElementById("paramEndDate").value = "";
}

</script>

        <center><div class="t15_red">Access Log 查詢</div></center>
        <h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢條件</h2>
<form method="post" action="accesslogQueryList.html">
        <div class="grtba">
            <table class="srchtb" width="100%">
                <tr>
                    <td align="right" width="100">狀態</td>
                    <td width="400">
                        <select name="paramStatus" >
                            <option value="">全部</option>
		    	            <#list statusMap?keys as skey>
		    		            <option value="${skey}" <#if paramStatus?exists && paramStatus = skey>selected="selected" </#if>>${statusMap[skey]}</option>
		    	            </#list>
		                </select>
                    </td>
                    <td align="right" width="100">頻道</td>
                    <td width="400">
                        <select name="paramChannel" >
                            <option value="">全部</option>
		    	            <#list channelMap?keys as skey>
		    		            <option value="${skey}" <#if paramChannel?exists && paramChannel = skey>selected="selected" </#if>>${channelMap[skey]}</option>
		    	            </#list>
		                </select>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="100">頻道單號</td>
                    <td width="400">
                        <input type="text" id="paramChannelOrdId" name="paramChannelOrdId" value="${paramChannelOrdId!}" />
                    </td>
                    <td align="right" width="100">會員帳號</td>
                    <td width="400">
                        <input type="text" id="paramUserId" name="paramUserId" value="${paramUserId!}" />
                    </td>
                </tr>
                <tr>
                    <td align="right" width="100">金流單號</td>
                    <td width="400">
                        <input type="text" id="paramBillingOrdId" name="paramBillingOrdId" value="${paramBillingOrdId!}" />
                    </td>
                    <td align="right" width="100">IP</td>
                    <td width="400">
                        <input type="text" id="paramIp" name="paramIp" value="${paramIp!}" />
                    </td>
                </tr>
                <tr>
                    <td align="right" width="100">日期</td>
                    <td width="400">
                        <div class="cal">
        	            <input type="text" id="paramStartDate" name="paramStartDate" value="${paramStartDate!}" style="width:100px;" readonly/> 至 
        	            <input type="text" id="paramEndDate" name="paramEndDate" value="${paramEndDate!}" style="width:100px;" readonly/>
                        </div>
                    </td>
                    <td align="right" width="100"></td>
                    <td width="400"></td>
                </tr>
                <tr>
                    <td colspan="4" align="center">
                        <input type="submit" />
                        <input type="button" value="清空條件" onclick="clean()" />
                    </td>
                </tr>
            </table>
        </div>
</form>
        <h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>
        <div class="grtba">
<#if (accessLogList?exists) && (accessLogList?size>0)>
        <div style="height:300px;overflow:auto;">
            <table class="table01" width="140%">
                <tr>
                    <td class="td01" width="30">NO.</td>
                    <td class="td01" width="60">狀態</td>
                    <td class="td01" width="60">頻道</td>
                    <td class="td01" width="300">訊息</td>
                    <td class="td01" width="100">頻道單號</td>
                    <td class="td01" width="100">金流單號</td>
                    <td class="td01" width="60">郵寄通知</td>
                    <td class="td01" width="100">帳號</td>
                    <td class="td01" width="100">IP</td>
                    <td class="td01" width="80">日期</td>
                </tr>
                <#assign index=0>
                <#list accessLogList as accessLog>
                <#assign index = index+1>
                <tr>
                    <td class="td03" width="30">${index!}</td>
                    <td class="td03" width="60">
                        <#if accessLog.status?exists>
                            ${statusMap[accessLog.status]!}
                        </#if>
                    </td>
                    <td class="td03" width="60">
                        <#if accessLog.ch?exists>
                            ${channelMap[accessLog.ch]!}
                        </#if>
                    </td>
                    <td class="td02" width="300">${accessLog.message!}</td>
                    <td class="td03" width="100">${accessLog.chOrdId!}</td>
                    <td class="td03" width="100">${accessLog.billOrdId!}</td>
                    <td class="td03" width="60">
                        <#if accessLog.mailSend?exists>
                            ${mailStatusMap[accessLog.mailSend]!}
                        </#if>
                    </td>
                    <td class="td03" width="100">${accessLog.memberId!}</td>
                    <td class="td03" width="100">${accessLog.clientIp!}</td>
                    <td class="td03" width="80">${accessLog.accsTime?string("yyyy-MM-dd")}</td>
                </tr>
                </#list>
            </table>
        </div>
<#else>
<br>
<center><div class="t15_red"><#if !message?exists || message=="">請選擇參數後查詢！</#if></div></center>
</#if>
        </div>

<input type="hidden" id="messageId" value="${message!}">
