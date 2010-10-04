<#assign pagetitle = 'Login'>
<#include "/templates/header.ftl">

<p>Please log in to continue</p>

 <@s.actionerror/>
 <@s.form action="index" method="post">
 	<@s.hidden name="LOGIN_ATTEMPT" value="1"/>
 	<@s.textfield name="LOGIN_EMAIL" label="E-mail address" value="globaladmin@example.com"></@s.textfield>
 	<@s.textfield name="LOGIN_PASSWORD" label="Password" value="password"></@s.textfield>
 	<@s.submit name="login" value="Log In"></@s.submit>
 </@s.form>
 
<#include "/templates/footer.ftl">