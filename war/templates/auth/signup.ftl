<#assign pagetitle = 'Signup'>
<#include "/templates/header.ftl">

 <@s.actionmessage/>

 <@s.actionerror/>
 <@s.form id="signup" action="signup/processSignup" method="post">
 <@s.textfield name="email" label="E-mail address"></@s.textfield>
 <@s.password name="password" label="Password"></@s.password>
 <@s.textfield name="name" label="Name"></@s.textfield>
 <@s.doubleselect label="Organisation" doubleLabel="Unit" name="organisationWebKey" list="allOrgs" listKey="webKey" listValue="name" headerValue="--- Please Select ---" headerKey=""
 				  doubleName="unitWebKey" doubleList="allUnits" doubleListKey="webKey" doubleListValue="Name"/>
 <@s.submit name="signup" value="Sign up"></@s.submit>
 </@s.form>
 
 
 <a href="#">Add new organisation</a>
 <a href="#">Add new unit</a>

<#include "/templates/footer.ftl">