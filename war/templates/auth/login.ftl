<#assign pagetitle = 'Login'>
<#include "/templates/header.ftl">

 <@s.actionerror/>
 <@s.form action="processLogin" method="post">
 	<@s.textfield name="email" label="E-mail address" value="foo@cocamp"></@s.textfield>
 	<@s.textfield name="password" label="Password" value="cocamp"></@s.textfield>
 	<@s.submit name="login" value="Log In"></@s.submit>
 </@s.form>
 
<#include "/templates/footer.ftl">