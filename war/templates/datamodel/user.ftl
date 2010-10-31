<#assign pagetitle = 'User edit'>
<#include "/templates/header.ftl">

<@s.actionerror/>
<@s.bean name="uk.org.woodcraft.bookings.BookingDataProvider" var="bookingData">
<@s.form id="user" action="save" method="post">
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
 	</#if>
    <@s.submit name="Save user" value="Save user"></@s.submit>
</@s.form>
</@s.bean>
<#include "/templates/footer.ftl">