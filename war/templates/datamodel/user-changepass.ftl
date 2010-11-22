<#assign pagetitle = 'Change password'>
<#include "/templates/header.ftl">
 <@s.actionmessage/>
 <@s.actionerror/>
<h2>Change password</h2>
<@s.form id="user" method="post">
	
 	<@s.textfield name="name" label="Name" readonly="true"></@s.textfield>
 	<@s.hidden name="email"/>
 	<#if !Session.USER.accessLevel.canManageUsers || ( Session.USER.email = email )>		
    <@s.password name="oldPassword" label="Current Password"/>
    </#if>
    <@s.password name="newPassword" label="New Password"/>
    <@s.password name="newPasswordConfirm" label="Confirm New Password"/>
    
    <@s.submit value="Change Password"/>
    <@s.submit value="Cancel" action="cancelChangePassword"/>
</@s.form>
<#include "/templates/footer.ftl">