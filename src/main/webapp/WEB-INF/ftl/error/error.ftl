<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<table width="100%" cellpadding="0" cellspacing="10" bgcolor="#EEEEEE">
    <tr>
        <td width="665" valign="top">
        <table width="100%"  border="0" align="center" cellpadding="8" cellspacing="0" bgcolor="#FFF6E5" style="border:1px #DCDCDC solid">
            <tr>
                <td class="t20 bold"><div align="left">OOPS!</div></td>
            </tr>
            <tr>
                <td height="360" valign="top" bgcolor="#FFFFFF">
                <br/>
                <table width="80%"  border="0" align="center" cellpadding="0" cellspacing="3">
                    <tr>
                        <td height="40" class="t13lg"><div align="left"></div></td>
                    </tr>
                    <tr>
                        <td class="t16b">
                            <div>很抱歉！系統忙碌, 請稍候再試，謝謝！</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="t13lg"><div align="center"><img src="<@s.url value="/html/images/"/>bg_div3.gif" width="500" height="1"/></div></td>
                    </tr>
                </table>
                </td>
            </tr>
        </table>
        </td>
    </tr>
</table>
<div style="display:none;">
    <BR/>以下印出exception: 
    <BR/>----------------------------------------------------------------------------------------------------------------<br>
    <BR/>
    <div>Exception Stack : </div>
    <div style="font-size: 14px; ">${exceptionStack!}</div> 
</div>
