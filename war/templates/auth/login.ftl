<#assign pagetitle = 'Log In'>
<#include "/templates/header.ftl">

    <@s.actionmessage/>
    <@s.actionerror/>

<div style="float: right;">
        
    <@s.form action="index" namespace="/" method="post">
    	<@s.hidden name="LOGIN_ATTEMPT" value="1"/>
    	<@s.textfield name="LOGIN_EMAIL" label="E-mail address"/>
    	<@s.password name="LOGIN_PASSWORD" label="Password"/>
    	<@s.submit name="login" value="Log In"></@s.submit>
    
    </@s.form>
</div>
<p><strong>Welcome to the CoCamp booking system!</strong></p>
<p>To access the Booking System, you must log in.</p>
<p>If this is your first visit, please <@s.url id="signupURL" namespace="/" action="signup/"/><@s.a href="${signupURL}">Sign Up</@s.a> for an account.</p>

<#include "/templates/footer.ftl">