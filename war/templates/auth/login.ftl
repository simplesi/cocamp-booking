<#assign pagetitle = 'Log In'>
<#include "/templates/header.ftl">

<p>
    To access the Booking System, you must log in.
    <br/>
    If this is your first visit, please <@s.url id="signupURL" action="signup/"/><@s.a href="${signupURL}">Sign Up</@s.a> for an account.
</p>

<@s.actionmessage/>
<@s.actionerror/>
<@s.form action="index" method="post">
	<@s.hidden name="LOGIN_ATTEMPT" value="1"/>
	<@s.textfield name="LOGIN_EMAIL" label="E-mail address" value="globaladmin@example.com"></@s.textfield>
	<@s.textfield name="LOGIN_PASSWORD" label="Password" value="password"></@s.textfield>
	<@s.submit name="login" value="Log In"></@s.submit>
</@s.form>
 
<#include "/templates/footer.ftl">