<#assign pagetitle = 'Confirm Delete'>

<#include "/templates/header.ftl">
 
<h2>Confirm delete</h2>

<@s.actionmessage/>
<@s.actionerror/>

<@s.push value="model">
<h3>'${name}'</h3>
<p>Are you sure you wish to delete this item? Deleted items can not be recovered.</p>
</p>
	<@s.form id="deleteItem" method="post">    
        <@s.hidden name="webKey"/>
        <@s.hidden name="email"/>
   
        <@s.submit name="Delete" name="confirmDelete" value="Delete"/>
        <@s.submit value="Cancel" name="cancelDelete" id="Cancel" />
    
	</@s.form>
</@s.push>
<#include "/templates/footer.ftl">