<#assign s=JspTaglibs["/struts-tags"]>

<script type="text/javascript" language="javascript">
$.datepicker.regional = {
    showOn: "both",
    buttonImage: "<@s.url value="/" />html/img/icon_cal.gif",
    buttonImageOnly: true,
    buttonText: "",
    dateFormat: "yy-mm-dd"
};

$(function() {
    $.datepicker.setDefaults($.datepicker.regional);
    $("#start").datepicker({});
    $("#end").datepicker({});
});

function clean() {
    document.getElementById("channel").options[0].selected = true;
    document.getElementById("motion").options[0].selected = true;
    document.getElementById("memberId").value = "";
    document.getElementById("orderId").value = "";
    document.getElementById("customerInfoId").value = "";
    document.getElementById("userId").value = "";
    document.getElementById("clientIp").value = "";
    document.getElementById("start").value = "";
    document.getElementById("end").value = "";
    document.getElementById("message").value = "";
//    document.getElementById("pageSize").options[0].selected = true;
//    document.getElementById("pageNo").options[0].selected = true;
}
</script>

<center><div class="t15_red">Access Log 查詢</div></center>
<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢條件</h2>
<form name="form1">
<div class="grtba">
    <table class="srchtb">
        <tr>
            <td align="right">會員帳號</td>
            <td><input type="text" id="memberId" name="memberId" value="${memberId!}" /></td>
            <td align="right">頻道名稱</td>
            <td>
                <select id="channel" name="channel">
                    <option value="">全部</option>
                <#list enumAccesslogChannels as enumAccesslogChannel>
                    <option value="${enumAccesslogChannel.channel}" <#if channel?exists && (enumAccesslogChannel.channel == channel)>selected="selected"</#if>>${enumAccesslogChannel.channel}</option>
                </#list>
                </select>
            </td>
        </tr>
        <tr>
            <td align="right">訂單編號</td>
            <td><input type="text" id="orderId" name="orderId" value="${orderId!}" /></td>
            <td align="right">頻道動作</td>
            <td>
                <select id="motion" name="motion">
                    <option value="">全部</option>
                <#list enumAccesslogActions as enumAccesslogAction>
                    <option value="${enumAccesslogAction.action}" <#if motion?exists && (enumAccesslogAction.action == motion)>selected="selected"</#if>>${enumAccesslogAction.message}</option>
                </#list>
                </select>
            </td>
        </tr>
        <tr>
            <td align="right">帳號編號</td>
            <td><input type="text" id="customerInfoId" name="customerInfoId" value="${customerInfoId!}" /></td>
            <td align="right">開始日期</td>
            <td><input type="text" id="start" name="start" value="${start!}" readonly="readonly" /></td>
        </tr>
        <tr>
            <td align="right">使用者編號</td>
            <td><input type="text" id="userId" name="userId" value="${userId!}" /></td>
            <td align="right">結束日期</td>
            <td><input type="text" id="end" name="end" value="${end!}" readonly="readonly" /></td>
        </tr>
        <tr>
            <td align="right">連線 IP</td>
            <td><input type="text" id="clientIp" name="clientIp" value="${clientIp!}" /></td>
            <td align="right">訊息</td>
            <td><input type="text" id="message" name="message" value="${message!}" /></td>
        </tr>
        <tr>
            <td colspan="4" align="center">
                <input type="submit" />
                <input type="button" value="清空條件" onclick="clean()" />
            </td>
        </tr>
    </table>
</div>

<h2><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />查詢結果</h2>
<div align="right">
    <span>每頁
        <select id="pageSize" name="pageSize" onchange="javascript:document.getElementById('pageNo').options[0].selected = true;document.form1.submit();">
            <option value="20" <#if pageSize == 20>selected="selected"</#if>>20</option>
            <option value="50" <#if pageSize == 50>selected="selected"</#if>>50</option>
            <option value="100" <#if pageSize == 100>selected="selected"</#if>>100</option>
        </select>筆
    </span>
    <span>第
        <select id="pageNo" name="pageNo" onchange="javascript:document.form1.submit()">
    <#if totalPage gt 0>
        <#list 1..totalPage as i>
            <option value="${i}" <#if pageNo == i>selected="selected"</#if>>${i}</option>
        </#list>
    </#if>
        </select>頁
    </span>
    <span>共${totalCount}筆</span>
    <span>共${totalPage}頁</span>
</div>
<div class="grtba">
<#if accesslogs?exists>
<div>
    <table class="table01" style="min-width:1210px">
        <tr>
            <td class="td01" width="50px">序號</td>
            <td class="td01" width="80px">頻道名稱</td>
            <td class="td01" width="100px">頻道動作</td>
            <td class="td01" width="300px">訊息</td>
            <td class="td01" width="100px">會員帳號</td>
            <td class="td01" width="110px">訂單編號</td>
            <td class="td01" width="110px">帳戶編號</td>
            <td class="td01" width="110px">使用者編號</td>
            <td class="td01" width="100px">連線 IP</td>
            <td class="td01" width="80px">信件通知</td>
            <td class="td01" width="70px">建立日期</td>
        </tr>
        <#list accesslogs as accesslog>
        <tr height="35px">
            <td class="td03">${accesslog.accesslogId}</td>
            <td class="td03">${accesslog.channel}</td>
            <td class="td03">
                <#list enumAccesslogActions as enumAccesslogAction>
                    <#if enumAccesslogAction.action == accesslog.action>
                        ${enumAccesslogAction.message}
                    </#if>
                </#list>
            </td>
            <td class="td02">${accesslog.message}</td>
            <td class="td03">${accesslog.memberId!}</td>
            <td class="td03">${accesslog.orderId!}</td>
            <td class="td03">${accesslog.customerInfoId!}</td>
            <td class="td03">${accesslog.userId!}</td>
            <td class="td03">${accesslog.clientIp!}</td>
            <td class="td03">
                <#list enumAccesslogEmailStatuses as enumAccesslogEmailStatus>
                    <#if enumAccesslogEmailStatus.status == accesslog.mailSend>
                        ${enumAccesslogEmailStatus}
                    </#if>
                </#list>
            </td>
            <td class="td03">${accesslog.createDate?string("yyyy-MM-dd")}<br/>${accesslog.createDate?string("HH:mm:ss")}</td>
        </tr>
        </#list>
    </table>
</div>
<#else>
<br>
<center><div class="t15_red">請選擇參數後查詢！</div></center>
</#if>
</div>
</form>