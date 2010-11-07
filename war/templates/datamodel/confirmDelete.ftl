<#assign pagetitle = 'Confirm Delete'>

<#include "/templates/header.ftl">
 
<h2>Confirm delete</h2>

<@s.actionmessage/>
<@s.actionerror/>

<@s.push value="model">
<h3>${name}</h3>
<p>Are you sure you wish to delete this item? Deleted items can not be recovered.</p>
</p>
	<@s.form id="deleteItem" method="post">    
        <@s.hidden name="webKey"/>
        <@s.hidden name="confirmedDelete" value="true"/>
   
        <@s.submit name="Delete" value="Delete"/>
        <@s.submit value="Cancel" id="Cancel" action="cancelDelete"/>
    
	</@s.form>
</@s.push>
<#include "/templates/footer.ftl">