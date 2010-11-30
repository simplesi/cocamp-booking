<#assign pagetitle = 'Confirm Email Address'>
<#include "/templates/header.ftl">
<h2>Sign up - Step 4</h2>
<div class="helpcolumn">
<p>Signing up for CoCamp bookings is a five step process.</p>
<ol><li>1)Select/add your organisation></li>
<li>2)Select/add your unit</li>
<li>3)Add your personal contact details</li>
<li><strong>4)Confirm your email address</strong></li>
<li>5)Have your account approved by an administrator</li>
</ol>
</div>
<p>An email has been sent to your given e-mail address. Please click the confirmation link in that email, or enter the confirmation code below:</p>

<@s.actionmessage/>
<@s.actionerror/>
<@s.form action="/signup/confirmEmail" method="post">
    <@s.textfield name="email" label="E-mail address"></@s.textfield>
    <@s.textfield name="hash" label="Confirmation code" value=""></@s.textfield>
    <@s.submit name="submit" value="Confirm Email"></@s.submit>
</@s.form>
 
<#include "/templates/footer.ftl">