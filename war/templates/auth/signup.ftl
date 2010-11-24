<#assign pagetitle = 'Sign Up'>
<#include "/templates/header.ftl">
<h2>Sign up</h2>
<div class="helpcolumn">
<p>Please provide your details on the left to sign up.</p>
<p>A <strong>Woodcraft membership number</strong> is required for all Woodcraft Folk booking secretaries.</p>
<p>If your <strong>organisation</strong> is in the list on the left, please choose it. Otherwise, select 'Add an organisation'.</p>
<p>A <strong>unit</strong> is a group of people from the same organisation that will be camping together. For Woodcraft Folk this will be your District. If your unit is not already listed, please select 'Add a unit'.</p>
</div>

 <@s.actionmessage/>
 <@s.actionerror/>
 
 <@s.form id="signup" action="signup/processSignup" method="post">
	 <@s.textfield name="name" label="Name"></@s.textfield>
	 <@s.textfield name="email" label="E-mail address"></@s.textfield>
	 <@s.password name="password" label="Password"></@s.password>
	 <@s.textfield name="membershipNumber" label="Wcf Membership Number (if a member)"/> 
	 <@s.doubleselect label="Organisation / Unit" name="OrganisationWebKey" list="allOrgs" listKey="webKey" listValue="Name" 
	 					headerValue="--- Please Select ---" headerKey=""
	 				  doubleLabel="Unit" doubleName="unitWebKey" doubleList="allUnits" doubleListKey="webKey" doubleListValue="Name"
	 				  	doubleHeaderValue="--- Please Select ---" DoubleHeaderKey=""
	 				  />
    <@s.url action="addOrg" namespace="/signup/" id="addOrgURL"/>
    <@s.a href="${addOrgURL}">Add an organisation</@s.a> | 
    <@s.url action="addUnit" namespace="/signup/" id="addUnitURL"/>
    <@s.a href="${addUnitURL}">Add a unit</@s.a>

    <@s.submit name="signup" value="Sign Up"></@s.submit>
 </@s.form>
 
 

<#include "/templates/footer.ftl">