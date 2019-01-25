<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<div class="cont">
    <h2>
        <img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" />黑名單管理
    </h2>
    <div class="grtba borderbox" style="height: 80px;">
        <div style="clear:both;background:#e4e3e0" >
            <form method="post" id="searchForm" name="searchForm">
                <table width="75%">
                    <tr>
                        <td>網址</td>
                        <td>
                            <input type="text" style="width:300px;" id="searchUrl">
                            <input type="button" value="搜尋" onClick="searchBtn()">
                        </td>
                    </tr>
                    <tr>
                        <td>拒絕原因:</td>
                        <td>
                            <input type="text" id="rejectReason" style="width:300px;" placeholder="請輸入拒絕理由">
                            <input type="button" value="送出" onClick="rejectSubmit();">
                            <input type="button" value="取消封鎖" id="cancelBtn" onClick="cancelReject();">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        
       <span class="pages" style="float: right;">
                        共 ${totalColume!}筆
    <img id="firstPage" src="./html/img/page_first_disable.gif" style="vertical-align:middle" onClick="searchByPage(this);">
    <img id="pageUp" src="./html/img/page_pre_disable.gif" style="vertical-align:middle" onClick="searchByPage(this);">
    ${page!}/${totalPage!}
    <img id="nextPage" src="./html/img/page_next_disable.gif" style="vertical-align: middle; cursor: pointer;"  onClick="searchByPage(this);">
    <img id="lastPage" src="./html/img/page_end_disable.gif" style="vertical-align: middle; cursor: pointer;" onClick="searchByPage(this);">
    &nbsp;&nbsp;每頁&nbsp;
    <select id="pageSize"  style="vertical-align:middle">
        <option value="10">10</option>
        <option value="20" selected="selected">20</option>
        <option value="50">50</option>
        <option value="100">100</option>
    </select>&nbsp;筆
</span>
    </div>
</div>  
    <#if pfbxPermissionList?exists>
    <table id="tableView" class="tablesorter" width="100%" border="0" cellpadding="0" cellspacing="1">
<thead>
    <tr>
        <th></th>
        <th>新增日期</th>
        <th>網址</th>
        <th>會員帳戶</th>
        <th>網址狀態</th>
        <th>拒絕原因</th>
    </tr>   
</thead>
<tbody>
        <#list pfbxPermissionList as pfbxPermissionVO>
            <tr>
                <td class="td01"><input type="checkbox" name="rejectCheckbox" id=${pfbxPermissionVO.id!}></td>
                <td class="td01">${pfbxPermissionVO.creatDate!}</td>
                <td class="td01" id="${pfbxPermissionVO.pfbxApplyId!}">${pfbxPermissionVO.content!}</td>
                <td class="td01">${pfbxPermissionVO.pfbxCustomerInfo.contactName!}</td>
                <td class="td01">
                    <#if '${pfbxPermissionVO.status!}' == '2'>
                                                                  封鎖
                        <#elseif '${pfbxPermissionVO.status!}' == '1'>
                                                                  生效
                        <#elseif '${pfbxPermissionVO.status!}' == '3'>
                                                                使用者已刪除
                    </#if>
                </td>
                <td class="td01">${pfbxPermissionVO.remark!}</td>
            <tr>
        </#list>
    </#if>
</tbody>
</table>

<input id="page" type="hidden" value=${page!}>
<input id="totalPage" type="hidden" value=${totalPage!}>
<input id="totalPage" type="hidden" value=${totalPage!}>
<input id="page_size" type="hidden" value=${pageSize!}>
<input id="search_url" type="hidden" value=${searchUrl!}>













