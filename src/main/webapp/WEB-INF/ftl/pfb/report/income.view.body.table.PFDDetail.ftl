<#assign s=JspTaglibs["/struts-tags"]>
		
			<table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <th class="text-center">PFD ID</th>
                    <th class="text-center">儲值金花費</th>
                    <th class="text-center">禮金花費</th>
                    <th class="text-center">後付花費</th>
                    <th class="text-center">小計</th>
                </tr>
            </thead>
            <tbody compile-template ng-bind-html="AjaxDialogDetail">
			<#if pfpPriceUsedDetail??>
			<#list pfpPriceUsedDetail as vo>
				<tr>
		            <td class="text-center">${(vo[0])!}</td>
		            <td class="text-right">$ ${vo[1]?string("#,###,###")!}</td>
		            <td class="text-right">$ ${vo[2]?string("#,###,###")!}</td>
		            <td class="text-right">$ ${vo[3]?string("#,###,###")!}</td>
		            <td class="text-right">$ ${vo[4]?string("#,###,###")!}</td>
	          	</tr>
		    </#list>
		    </#if>
	    	</tbody>
	    
	    </table>