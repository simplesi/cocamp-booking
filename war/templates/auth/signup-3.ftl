<#assign pagetitle = 'Sign Up'>
<#include "/templates/header.ftl">
<h2>Sign up - Step 3</h2>
<div class="helpcolumn">
<p>Signing up for CoCamp bookings is a five step process.</p>
<ol><li>1)Select/add your organisation></li>
<li>2)Select/add your unit</li>
<li><strong>3)Add your personal contact details</strong></li>
<li>4)Confirm your email address</li>
<li>5)Have your account approved by an administrator</li>
</ol>
</div>

 <@s.actionmessage/>
 <@s.actionerror/>
 <p>Please provide your personal contact details.</p>
 <p>A <strong>Woodcraft membership number</strong> is required for all Woodcraft Folk booking secretaries.</p>
 
 <@s.form id="signup" action="signup-3" method="post">
	 <@s.textfield name="name" label="Name"></@s.textfield>
	 <@s.textfield name="email" label="E-mail address"></@s.textfield>
	 <@s.password name="password" label="Password"></@s.password>
	 
	 <@s.textfield name="membershipNumber" label="Wcf Membership Number (if a member)"/> 
	 <@s.textfield name="" label="Organisation" value="${organisation.name}"/>
	 <@s.textfield name="" label="Unit" value="${unit.name}"/>

    <@s.submit value="<- Previous" action="signup-2-pre"/>
    <@s.submit value="Sign up"/>
 </@s.form>
 
 

<#include "/templates/footer.ftl">