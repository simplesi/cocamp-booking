<#assign pagetitle = 'User edit'>
<#include "/templates/header.ftl">

<@s.actionerror/>

<@s.form id="user" action="save" method="post">
    <@s.textfield name="email" label="E-mail address"></@s.textfield>
    <@s.password name="password" label="Password"></@s.password>
    <@s.textfield name="name" label="Name"></@s.textfield>
     <@s.doubleselect label="Organisation" name="OrganisationWebKey" list="allOrgs" listKey="webKey" listValue="Name" 
 					headerValue="--- Please Select ---" headerKey=""
 				    doubleLabel="Unit" doubleName="unitWebKey" doubleList="allUnits" doubleListKey="webKey" doubleListValue="Name"
 				  	doubleHeaderValue="--- Please Select ---" DoubleHeaderKey=""
 				  />
    <@s.submit name="Add user" value="Add user"></@s.submit>
</@s.form>
<#include "/templates/footer.ftl">