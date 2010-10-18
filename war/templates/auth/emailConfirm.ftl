<#assign pagetitle = 'Confirm Email Address'>
<#include "/templates/header.ftl">

<p>An email has been sent to your given e-mail address. Please click the confirmation link in that email, or enter the confirmation code below:</p>

<@s.actionerror/>

<@s.form action="/signup/confirmEmail" method="post">
    <@s.textfield name="email" label="E-mail address"></@s.textfield>
    <@s.textfield name="hash" label="Confirmation code" value=""></@s.textfield>
    <@s.submit name="submit" value="Confirm Email"></@s.submit>
</@s.form>
 
<#include "/templates/footer.ftl">