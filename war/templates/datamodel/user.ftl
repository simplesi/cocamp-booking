<#assign pagetitle = 'User edit'>
<#include "/templates/header.ftl">
<div class="helpcolumn">
<p>Please provide your details on the left to sign up.</p>
<p>A <strong>Woodcraft membership number</strong> is required for all Woodcraft Folk booking secretaries.</p>
<#if Session.USER.accessLevel.canChangeUnit || Session.USER.accessLevel.canChangeOrg>
	<#if Session.USER.accessLevel.canChangeOrg>
	<p>Your <strong>organisation</strong> is the default organisation the booking process will start on, but you can change organisations when navigating from the dropdown above as you are an administrator.</p>
	<p>A <strong>unit</strong> is a group of people from the same organisation that will be camping together. For Woodcraft Folk this will be your District.</p>
	<p>Your <strong>unit</strong> is the default unit the process will start on, but you can change units using the dropdown above.</p>
	<#else>
	<p>Your <strong>organisation</strong> is the organisation you are responsible for, and as an organisational booking secretary, you can change details for everyone in this organisation.</p>
	<p>A <strong>unit</strong> is a group of people from the same organisation that will be camping together. For Woodcraft Folk this will be your District.</p>
	
	<p>Your <strong>unit</strong> is the default unit the process will start on, but you can change units using the dropdown above as you are an organisational booking secretary.</p>
	</#if>
<#else>
<p>Your <strong>organisation</strong> is the organisation you are part of. <p>A <strong>unit</strong> is a group of people from the same organisation that will be camping together. For Woodcraft Folk this will be your District.
Your unit is the unit you are responsible for making bookings for.</p>
</#if>

</div>
 <@s.actionmessage/>
 <@s.actionerror/>
<@s.bean name="uk.org.woodcraft.bookings.BookingDataProvider" var="bookingData">
<@s.form id="user" method="post">
  <@s.push value="model">
 	<@s.textfield name="name" label="Name"></@s.textfield>
    <@s.textfield name="email" label="E-mail address" readonly="true"></@s.textfield>
    <@s.textfield name="membershipNumber" label="Wcf membership number (if a member)"></@s.textfield>

 	<#if Session.USER.accessLevel.canManageUsers>		
 	    <@s.doubleselect label="Organisation / Unit" name="OrganisationWebKey" list="allOrgs" listKey="webKey" listValue="Name" 
 					headerValue="--- Please Select ---" headerKey=""
 				    doubleLabel="Unit" doubleName="unitWebKey" doubleList="allUnits" doubleListKey="webKey" doubleListValue="Name"
 				  	doubleHeaderValue="--- Please Select ---" DoubleHeaderKey=""
 				  />	  
 	 	<@s.select label="Access level" name="accessLevelString" list="accessLevels" listValue="DisplayName" />
 	 	<@s.checkbox label="Is approved" name="approved"/>
 	<#else>
 		<@s.textfield label="Organisation" name="Organisation" listValue="Name" readonly="true" />
 		<@s.textfield label="Unit" name="Unit" listValue="Name" readonly="true" />

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