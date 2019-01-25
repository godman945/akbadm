<#--
    @param int pageNo       目前頁數
    @param int pageSize     每頁幾筆
    @param int pageCount    共幾頁
    @param int totalCount   共幾筆
    @javascript wantSearch(pageNo)  連結導向(到第幾頁)
-->
<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<input type="hidden" id="pageNo" name="pageNo" value="${pageNo!}" />
<input type="hidden" id="selectPageSize" name="selectPageSize" value="${pageSize!}" />
<input type="hidden" id="pageCount" name="pageCount" value="${pageCount!}" />
<input type="hidden" id="contentPath" name="contentPath" value="<@s.url value="/html/img/"/>" />

<span class="pages">
共 ${totalCount!} 筆&nbsp;&nbsp;
    <img id="fpage" src="<@s.url value="/html/img/"/>page_first_disable.gif" style="vertical-align:middle" />&nbsp;
    <img id="ppage" src="<@s.url value="/html/img/"/>page_pre_disable.gif" style="vertical-align:middle" />&nbsp;
    ${pageNo!}/${pageCount!}&nbsp;
    <img id="npage" src="<@s.url value="/html/img/"/>page_next_disable.gif" style="vertical-align:middle" />&nbsp;
    <img id="epage" src="<@s.url value="/html/img/"/>page_end_disable.gif" style="vertical-align:middle" />
    <#if pageSize?exists>
    &nbsp;&nbsp;每頁&nbsp;
    <select id="pageSize" name="pageSize" style="vertical-align:middle">
        <option>50</option>
        <option>100</option>
        <option>250</option>
        <option>500</option>
        <option>1000</option>
        <option>10000</option>
    </select>&nbsp;筆
    </#if>
</span>