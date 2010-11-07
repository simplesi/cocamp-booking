<#assign pagetitle="Edit Unit">

<#include "/templates/header.ftl">

<h2>Unit Edit</h2>

<@s.actionmessage/>
<@s.actionerror/>

<@s.form id="unit" method="post">
    <@s.push value="model">
        <@s.hidden name="webKey"/>
        <@s.textfield name="name" label="Name"/>
        <@s.select name="organisationWebKey" label="Organisation" headerKey="" headerValue="-- Please Select --" 
        	list="allOrgs" listKey="webKey" listValue="name"/>
        <@s.textfield name="email" label="Email"/>
        <@s.textfield name="phone" label="Phone Number"/>
        <@s.textarea name="address" label="Address"/>
        <@s.textarea name="notesString" label="Notes"/>
        
        <@s.submit name="Save" value="Save" action="saveUnit"></@s.submit>
        <@s.submit value="Cancel" action="cancelEditUnit"/>
    </@s.push>
</@s.form>

<#include "/templates/footer.ftl">