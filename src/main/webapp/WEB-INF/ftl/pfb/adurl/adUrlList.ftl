<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<div class="container-fluid" ng-controller="searchCtrl">
            <h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>網址查詢</strong></h3>

            <div class="container-fluid">
                <form class="form-horizontal" id="searchForm" action="searchAdUrlList.html" method="post">

                    <div class="form-group">
                        <div class="control-label col-lg-2 col-md-2">
                            <label class="control-label">關鍵字查詢</label>
                        </div>
                        <div class="control-label col-lg-4 col-md-5">
                            <input type="text" class="form-control" style="width:500px;" 
                                   placeholder="PFB帳戶編號 / 會員帳號 / 公司名稱 / 統一編號 / 申請網址" name="keyword" value="${keyword!}" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-lg-2 col-md-2">帳戶類別</label>
                        <label class="control-label col-lg-4 col-md-5">
                            <select class="form-control" name="category" style="width:200px;">
                                <option value="">全部</option>
                                <#list enumcategory as e>
                                <option value="${e.category}" <#if category?exists && category==e.category>selected</#if> >${e.chName}</option>
                                </#list>
                            </select>
                        </label>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-lg-2 col-md-2">帳戶狀態</label>
                        <label class="control-label col-lg-4 col-md-5">
                            <select class="form-control" name="accStatus" style="width:200px;">
                            	<option value="">全部</option>
				                <#list queryStatusOptionsMap?keys as key>
				                <option value="${key}"<#if accStatus?exists && accStatus = key> selected</#if>>${queryStatusOptionsMap[key]}</option>
				                </#list>
				            </select>
                        </label>
                    </div>

                    <div class="form-group ">
                        <div class="col-lg-offset-4 col-md-offset-1">
                            <input type="button" class="btn btn-default" id="searchForm_SubBT" style="margin-left:-105px;" value="查詢" />
                        </div>
                    </div>
                </form>
            </div>

            <#if urlVOS?exists>
            <h3><img src="<@s.url value="/" />html/img/iconcr.gif" hspace="2" vspace="12" align="absmiddle" /><strong>PFB列表</strong></h3>
            <div class="container-fluid">
                <table id="tableView" class="table table-striped table-bordered tablesorter">
                    <thead>
                        <tr>
                            <th class="text-center">帳戶狀態</th>
                            <th class="text-center">帳戶編號</th>
                            <th class="text-center">會員帳號</th>
                            <th class="text-center">帳戶類別</th>
                            <th class="text-center">公司名稱</th>
                            <th class="text-center">統一編號</th>
                            <th class="text-center">申請網址</th>
                            <th class="text-center">廣告網址不符</th>
                            <th class="text-center">已封鎖網址</th>
                        </tr>
                    </thead>
					
					<tbody compile-template ng-bind-html="bonusSetUpData">
                    <#list urlVOS as e>
                        <tr>
                            <td class="text-center">${queryStatusOptionsMap[e.status]}</td>
                            <td class="text-center">${e.pfbid!}</td>
                            <td class="text-center">${e.memberid!}</td>
                            <td class="text-center">${e.category!}</td>
                            <td class="text-center">${e.companyname!}</td>
                            <td class="text-center">${e.taxid!}</td>
                            <td class="text-center">${e.url!}</td>
                            <td class="text-center">
                            <#if e.notincount?exists && e.notincount == 'error'>
                            	${e.notincount!}
							<#else>
							    <a href="pfbErrUrlDetail.html?id=${e.pfbid!}&keyword=${keyword!}&category=${category!}" >${e.notincount!}</a>                        	
                            </#if>
                            </td>
                            <td class="text-center">
                            <#if e.notincount?exists && e.notincount == 'error'>
                            	${e.blockcount!}
							<#else>
							    <a href="pfbBlockUrlDetail.html?id=${e.pfbid!}&keyword=${keyword!}&category=${category!}" >${e.blockcount!}</a>                        	
                            </#if>
                            </td>
                        </tr>
					</#list>	          	
                    </tbody>
                    
                </table>
            </div>
            </#if>

        </div>