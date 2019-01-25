<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" ng-app="admApp">
<head>
<@t.insertAttribute name="title" />
</head>
<body>
<table class="table01" width="100%" height="100%">
    <tr>
        <td colspan="2" class="td07"><@t.insertAttribute name="header" /></td>
    </tr>
    <tr>
        <td width="180" valign="top" class="td12"><@t.insertAttribute name="left" /></td>
        <td width="85%" height="600" valign="top" class="td07 rightbody">
            <table width="100%" >
                <tr>
                    <td colspan="3" height="10"></td>
                </tr>
                <tr>
                    <td width="10"></td>
                    <td>
                        <@t.insertAttribute name="body" />
                    </td>
                    <td width="10"></td>
                </tr>
                <tr>
                    <td colspan="3" height="10"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
