<#assign pagetitle = 'Signup'>
<#include "/templates/header.ftl">

 <@s.actionmessage/>

 <@s.actionerror/>
 <@s.form id="signup" action="signup/processSignup" method="post">
 <@s.textfield name="email" label="E-mail address"></@s.textfield>
 <@s.password name="password" label="Password"></@s.password>
 <@s.textfield name="name" label="Name"></@s.textfield>
 <@s.doubleselect label="Organisation" doubleLabel="Unit" name="organisationWebKey" list="allOrgs" listKey="webKey" listValue="name" 
 					headerValue="--- Please Select ---" headerKey="" value ="${defaultOrgWebKey!}"
 				  doubleName="unitWebKey" doubleList="allUnits" doubleListKey="webKey" doubleListValue="Name"
 				  	doubleHeaderValue="--- Please Select ---" DoubleHeaderKey="" value ="${defaultUnitWebKey!}"
 				  />
 				  <@s.url action="addOrg" id="addOrgURL"/>
 				  <@s.a href="${addOrgURL}">Add an organisation</@s.a>
 				  <@s.url action="addUnit" id="addUnitURL"/>
 				  <@s.a href="${addUnitURL}">Add a unit</@s.a>
 				  
 <@s.submit name="signup" value="Sign up"></@s.submit>
 </@s.form>
 
<#include "/templates/footer.ftl">