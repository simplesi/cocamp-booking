<#if Session?exists && Session.USER?exists>
<div id="userbar">
	<@s.bean name="uk.org.woodcraft.bookings.BookingDataProvider" var="bookingData">
	<@s.form action="switch">
	
<#-- Utter mess trying to set the value for these selects. Can't work out how to do it, so this hack will have to do for now.
	 It should be value=Session.CURRENT_EVENT.webKey but OGNL then converts that to null having looked the correct value up, 
	 and hence nameValue property isn't set
-->
	<@s.submit name="Go" value="Go"></@s.submit>
	  
	  <#if Session.USER.accessLevel.canChangeUnit>   	
	    	<@s.select value="%{'${Session.CURRENT_UNIT.webKey}'}" headerValue="--- Please Select ---" headerKey=""
	    	name="navUnitKey" list="myUnits" listKey="webKey" listValue="name" />
	  <#else>
	    	Unit: ${Session.CURRENT_UNIT.name}<br/>
	  </#if>
	  
	  <#if Session.USER.accessLevel.canChangeOrg>
	    	<@s.select name="navOrgKey" headerValue="--- Please Select ---" headerKey=""
	        	list=allOrgs listKey="webKey" listValue="name" value="%{'${Session.CURRENT_ORG.webKey}'}" />
	  <#else>
	    	Organization: ${Session.CURRENT_ORG.name}<br/>
	  </#if>
	      
	  <#if ( allOpenEvents.size() > 1) >
          <@s.select name="navEventKey" headerValue="--- Please Select ---" headerKey=""
	        	list="allOpenEvents" listKey="webKey" listValue="name" value="%{'${Session.CURRENT_EVENT.webKey}'}" />
	  <#else>
	    	Event: ${Session.CURRENT_EVENT.name}<br/>
	  </#if>
	
	  
	  </@s.form>
	 </@s.bean>

	<a href="#" title="Currently logged in as ${Session.USER.name}">${Session.USER.name}</a>
</div>
</#if>


