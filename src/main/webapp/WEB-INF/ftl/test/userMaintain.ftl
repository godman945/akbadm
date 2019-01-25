<#assign s=JspTaglibs["/struts-tags"]>
<#assign t=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<script>
    $(function() {
		
		$("#pfp2pfd").click(function(){
           $.post("testpfp2pfd.html" , function(rs){
               alert(rs);
           }) 
        });
	  
    });
</script>

<input type="button" name="" id="pfp2pfd" class=""  value="pfp bind 2 pfd"/>
