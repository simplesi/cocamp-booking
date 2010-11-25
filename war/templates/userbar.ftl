<#if Session?exists && Session.USER?exists>
<div id="userbar">
	<@s.bean name="uk.org.woodcraft.bookings.BookingDataProvider" var="bookingData">
	<@s.form action="switch">
	
<#-- Utter mess trying to set the value for these selects. Can't work out how to do it, so this hack will have to do for now.
	 It should be value=Session.CURRENT_EVENT.webKey but OGNL then converts that to null having looked the correct value up, 
	 and hence nameValue property isn't set
-->
	<#if Session.USER.accessLevel.canChangeUnit || Session.USER.accessLevel.canChangeOrg || ( allOpenEvents.size() > 1) >
	<@s.submit name="Go" value="Go"></@s.submit>
	</#if>
	  
	  <#if Session.USER.accessLevel.canChangeUnit>   	
	    	<@s.select value="%{'${Session.CURRENT_UNIT.webKey}'}"
	    	name="navUnitKey" list="myUnits" listKey="webKey" listValue="name" />
	  <#else>
	    	<span id="static_text">Unit: ${Session.CURRENT_UNIT.name}</span>
	  </#if>
	  
	  <#if Session.USER.accessLevel.canChangeOrg>
	    	<@s.select name="navOrgKey"
	        	list=allOrgs listKey="webKey" listValue="name" value="%{'${Session.CURRENT_ORG.webKey}'}" />
	  <#else>
	    	<span id="static_text">Organisation: ${Session.CURRENT_ORG.name} | </span>
	  </#if>
	      
	  <#if ( allOpenEvents.size() > 1) >
          <@s.select name="navEventKey"
	        	list="allOpenEvents" listKey="webKey" listValue="name" value="%{'${Session.CURRENT_EVENT.webKey}'}" />
	  <#else>
	    	<span id="static_text">Event: ${Session.CURRENT_EVENT.name} | </span>
	  </#if>
	
	  
	  </@s.form>
	 </@s.bean>

 	<@s.url action="profile" namespace="/user" id="profileURL" />
	<a href="${profileURL}" title="Currently logged in as ${Session.USER.name}">${Session.USER.name}</a>
</div>
</#if>


