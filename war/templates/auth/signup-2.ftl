<#assign pagetitle = 'Sign Up'>
<#include "/templates/header.ftl">
<h2>Sign up - Step 2</h2>
<div class="helpcolumn">
<p>Signing up for Woodcraft Folk bookings is a five step process.</p>
<ol><li>1) Select/add your organisation></li>
<li><strong>2) Select/add your unit</strong></li>
<li>3)Add your personal contact details</li>
<li>4)Confirm your email address</li>
<li>5)Have your account approved by an administrator</li>
</ol>
</div>

 <@s.actionmessage/>
 <@s.actionerror/>
 <@s.url action="addUnit" namespace="/signup/" id="addUnitURL"/>
 
 <p>Please choose your unit from the list below. A unit is a group of people from the same organisation that will be camping together. 
 For Woodcraft Folk this will be your District. If your unit is not listed, you can <@s.a href="${addUnitURL}">add it</@s.a>.
 <@s.form id="signup" action="signup-2" method="post"> 
 	<@s.textfield name="" label="Organisation" value="${organisation.name}" readonly="true"/>
	<@s.select label="Unit" name="UnitWebKey" list="unitsForCurrentOrg" listKey="webKey" listValue="Name"/>
    <@s.a href="${addUnitURL}">Add a unit</@s.a>
    <@s.submit value="<- Previous" action="signup-1-pre"/>
    <@s.submit value="Next ->"/>
 </@s.form>
 
<#include "/templates/footer.ftl">