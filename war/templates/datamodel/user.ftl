<#assign pagetitle = 'User edit'>
<#include "/templates/header.ftl">
 <@s.actionmessage/>
 <@s.actionerror/>
<@s.bean name="uk.org.woodcraft.bookings.BookingDataProvider" var="bookingData">
<@s.form id="user" method="post">
  <@s.push value="model">
 	<@s.textfield name="name" label="Name"></@s.textfield>
    <@s.textfield name="email" label="E-mail address" readonly="true"></@s.textfield>
    <@s.doubleselect label="Organisation" name="OrganisationWebKey" list="allOrgs" listKey="webKey" listValue="Name" 
 					headerValue="--- Please Select ---" headerKey=""
 				    doubleLabel="Unit" doubleName="unitWebKey" doubleList="allUnits" doubleListKey="webKey" doubleListValue="Name"
 				  	doubleHeaderValue="--- Please Select ---" DoubleHeaderKey=""
 				  />
 	<#if Session.USER.accessLevel.canManageUsers>			  
 	 	<@s.select label="Access level" name="accessLevelString" list="accessLevels" listValue="DisplayName" />
 	 	<@s.checkbox label="Is approved" name="approved"/>
 	</#if>
 	<#if Session.USER.accessLevel.canManageUsers || ( Session.USER.email = email )>	
 		<@s.url id="changePassword" action="changePasswordGeneric" namespace="/user" email="${email}" />
        <p><@s.a href="${changePassword}">Change password</@s.a></p>
 	</#if>
    <@s.submit name="Save user" value="Save user" action="saveUser"/>
    <@s.submit value="Cancel" action="cancelEditUser"/>
  </@s.push>
</@s.form>
</@s.bean>
<#include "/templates/footer.ftl">