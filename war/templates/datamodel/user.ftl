<#assign pagetitle = 'User edit'>
<#include "/templates/header.ftl">
<div class="helpcolumn">
<p>Please provide your details on the left to sign up.</p>
<p>A <strong>Woodcraft membership number</strong> is required for all Woodcraft Folk booking secretaries.</p>
<p>If your <strong>organisation</strong> is in the list on the left, please choose it. Otherwise, select 'Add an organisation'.</p>
<p>A <strong>unit</strong> is a group of people from the same organisation that will be camping together. For Woodcraft Folk this will be your District. If your unit is not already listed, please select 'Add a unit'.</p>
</div>
 <@s.actionmessage/>
 <@s.actionerror/>
<@s.bean name="uk.org.woodcraft.bookings.BookingDataProvider" var="bookingData">
<@s.form id="user" method="post">
  <@s.push value="model">
 	<@s.textfield name="name" label="Name"></@s.textfield>
    <@s.textfield name="email" label="E-mail address" readonly="true"></@s.textfield>
    <@s.textfield name="membershipNumber" label="Wcf membership number (if a member)"></@s.textfield>
    <@s.doubleselect label="Organisation / Unit" name="OrganisationWebKey" list="allOrgs" listKey="webKey" listValue="Name" 
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