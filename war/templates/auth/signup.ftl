<#assign pagetitle = 'Sign Up'>
<#include "/templates/header.ftl">

 <@s.actionmessage/>
 <@s.actionerror/>
 
 <@s.form id="signup" action="signup/processSignup" method="post">
	 <@s.textfield name="name" label="Name"></@s.textfield>
	 <@s.textfield name="email" label="E-mail address"></@s.textfield>
	 <@s.password name="password" label="Password"></@s.password>
	 
	 <@s.doubleselect label="Organisation" name="OrganisationWebKey" list="allOrgs" listKey="webKey" listValue="Name" 
	 					headerValue="--- Please Select ---" headerKey=""
	 				  doubleLabel="Unit" doubleName="unitWebKey" doubleList="allUnits" doubleListKey="webKey" doubleListValue="Name"
	 				  	doubleHeaderValue="--- Please Select ---" DoubleHeaderKey=""
	 				  />
    <@s.url action="addOrg" id="addOrgURL"/><@s.a href="${addOrgURL}">Add an organisation</@s.a> | <@s.url action="addUnit" id="addUnitURL"/><@s.a href="${addUnitURL}">Add a unit</@s.a>

    <@s.submit name="signup" value="Sign Up"></@s.submit>
 </@s.form>
 
 

<#include "/templates/footer.ftl">