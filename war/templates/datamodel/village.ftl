<#assign pagetitle = 'Edit Village'>

<#include "/templates/header.ftl">
 

<h2>Village Edit</h2>
<@s.actionmessage/>
<@s.actionerror/>

<@s.form id="event" method="post">
    <@s.push value="model">
        <@s.hidden name="webKey"/>
        <@s.textfield name="name" label="Name"/>
        <@s.submit name="Save" value="Save" action="saveVillage"/>
        <@s.submit value="Cancel" action="cancelEditVillage"/>
    </@s.push>
</@s.form>

<#include "/templates/footer.ftl">