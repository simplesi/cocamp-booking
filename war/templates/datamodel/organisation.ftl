<#assign pagetitle = 'Organisation Edit'>

<#include "/templates/header.ftl">
 
<h2>Organisation Edit</h2>
<@s.actionmessage/>
<@s.actionerror/>

<@s.form id="organisation" method="post">
    <@s.push value="model">
        <@s.hidden name="webKey"/>
        <@s.textfield name="name" label="Name"/>
        <@s.textfield name="email" label="Email"/>
        <@s.textfield name="phone" label="Phone Number"/>
        <@s.textarea name="address" label="Address"/>
        <@s.textarea name="notesString" label="Notes"/>
        <@s.submit name="Save" value="Save" action="saveOrg"/>
        <@s.submit value="Cancel" action="cancelEditOrg"/>
    </@s.push>
</@s.form>

<#include "/templates/footer.ftl">