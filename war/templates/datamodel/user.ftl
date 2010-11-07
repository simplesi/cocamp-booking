<#assign pagetitle = 'User edit'>
<#include "/templates/header.ftl">
 <@s.actionmessage/>
 <@s.actionerror/>
<@s.bean name="uk.org.woodcraft.bookings.BookingDataProvider" var="bookingData">
<@s.form id="user" method="post">
 	<@s.textfield name="name" label="Name"></@s.textfield>
    <@s.textfield name="email" label="E-mail address"></@s.textfield>
    <@s.password name="password" label="Password"></@s.password>
    <@s.doubleselect label="Organisation" name="OrganisationWebKey" list="allOrgs" listKey="webKey" listValue="Name" 
 					headerValue="--- Please Select ---" headerKey=""
 				    doubleLabel="Unit" doubleName="unitWebKey" doubleList="allUnits" doubleListKey="webKey" doubleListValue="Name"
 				  	doubleHeaderValue="--- Please Select ---" DoubleHeaderKey=""
 				  />
 	<#if Session.USER.accessLevel.canManageUsers>			  
 	 	<@s.select label="Access level" name="accessLevelString" list="accessLevels" listValue="DisplayName" />
 	 	<@s.checkbox label="Is approved" name="approved"/>
 	</#if>
    <@s.submit name="Save user" value="Save user" action="saveUser"/>
    <@s.submit value="Cancel" action="cancelEditUser"/>
</@s.form>
</@s.bean>
<#include "/templates/footer.ftl">