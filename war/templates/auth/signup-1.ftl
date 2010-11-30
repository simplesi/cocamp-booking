<#assign pagetitle = 'Sign Up'>
<#include "/templates/header.ftl">
<h2>Sign up - Step 1</h2>
<div class="helpcolumn">
<p>Signing up for CoCamp bookings is a five step process. It will take 10 minutes to complete.</p>
<ol><li><strong>1) Select/add your organisation</strong></li>
<li>2) Select/add your unit</li>
<li>3) Add your personal contact details</li>
<li>4) Confirm your email address</li>
<li>5) Have your account approved by an administrator</li>
</ol>
</div>

 <@s.actionmessage/>
 <@s.actionerror/>
 <@s.url action="addOrg" namespace="/signup/" id="addOrgURL"/>
 
 <p>Please choose your organisation from the list below. If your organisation is not listed, you can <@s.a href="${addOrgURL}">add it</@s.a>.
 <@s.form id="signup" action="signup-1" method="post"> 
	<@s.select label="Organisation" name="OrganisationWebKey" list="allOrgs" listKey="webKey" listValue="Name"/>
    <@s.a href="${addOrgURL}">Add an organisation</@s.a>
    <@s.submit value="Next ->"></@s.submit>
 </@s.form>
 
<#include "/templates/footer.ftl">