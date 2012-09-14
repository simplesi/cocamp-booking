<#assign pagetitle = 'Booking Edit'>

<#include "/templates/header.ftl">

<#if isEditable>
<script>
  $(function() {
  	$( "#booking_dob" ).datepicker({ dateFormat: 'dd/mm/yy', changeYear: true, yearRange: '1900:2011' });
  	$( "#booking_arrivalDate" ).datepicker({ dateFormat: 'dd/mm/yy', 
  											 minDate: new Date( ${earliestDate?string("yyyy,M-1,d")}), 
  											 maxDate: new Date( ${latestDate?string("yyyy,M-1,d-1")}) });
  	$( "#booking_departureDate" ).datepicker({ dateFormat: 'dd/mm/yy', 
  											 minDate: new Date( ${earliestDate?string("yyyy,M-1,d+1")}), 
  											 maxDate: new Date( ${latestDate?string("yyyy,M-1,d")}) });
  });
</script>
</#if>

<h2>Booking Edit</h2>
<div class="helpcolumn">
<p>Booking information for this individual should be entered on the left. 
This information should be as accurate as possible, details cannot be changed after the bookings deadline.</p>
<p>A Woodcraft Folk <strong>membership number</strong> should be provided for all adults in Woodcraft Folk units. If you are a venturer and not a member already, check the box if you want to become one.</p>
</div>
<@s.actionerror/>


<@s.form id="booking" method="post">

	<#if !isEditable>
		<p><em>This booking is locked for editing - you can only change certain fields. See below for more details.</em></p>
	<#else>
		<p><em>This booking can be edited until ${editCutoffDate?date}.</em></p>
	</#if>  
	<@s.push value="model">
        <@s.hidden name="webKey"/>
        <@s.textfield name="name" label="Name" readonly=!isEditable/>
        <@s.textfield name="dob" label="Date Of Birth (dd/mm/yyyy)" value="%{getText('format.date',{dob})}" readonly=!isEditable/>
        <#if model.dob?exists && model.ageGroup!="" >Age group for event: ${model.ageGroup}<br/></#if>
        
        <@s.textfield name="email" label="Email"/>
        <@s.textfield name="phoneNumber" label="Phone Number" />
      	<@s.textfield name="membershipNumber" label="Wcf Membership Number (if a member)"/> 
      	
      	<#if isEditable>
        	<@s.select name="diet" list="diets" label="Diet"/>
        <#else>
        	<@s.textfield name="diet" label="Diet" readonly="true"/>
        </#if>
        
        <@s.textfield name="dietNotes" label="Additional diet notes" readonly=!isEditable/>
        <@s.textarea name="otherNeeds" label="Other needs (disability, allergies, health conditions)" />
        
        <@s.checkbox label="Become a Woodcraft Folk member?" name="becomeMember"/>
        
        <@s.textfield name="arrivalDate" label="Arrival Date" value="%{getText('format.date',{arrivalDate})}" readonly=!isEditable/>
        <@s.textfield name="departureDate" label="Departure Date" value="%{getText('format.date',{departureDate})}" readonly=!isEditable/>

		<#if canAssignVillage>
			<@s.select name="villageWebKey" list="villages" label="Village" listKey="webKey" listValue="name"/>
		<#else>
			<#if model.village?exists>
			Village: ${model.village.name!}<br/><br/>
			</#if>
		</#if>  

		<#if bookingCreationDate?exists && webKey?exists >
		Booking created : ${model.bookingCreationDate?date}<br/>
		</#if>
		
		<#if model.bookingUnlockDate?exists >
		Booking was unlocked on : ${model.bookingUnlockDate?date}<br/>
		</#if>
		
		<#if model.cancellationDate?exists >
		Booking was cancelled on : ${model.cancellationDate?date}<br/>
		</#if>
        <@s.submit name="Save" value="Save" action="saveBooking"/>
        <@s.submit value="Back" action="cancelEditBooking"/>
        <#if model.webKey?exists >
	        <#if model.cancellationDate?exists >
				<@s.submit value="Uncancel Booking" action="unCancelBooking"/>				
			<#else>
				<@s.submit value="Cancel Booking" action="cancelBooking"/>
			</#if>
		</#if>
		
		<#if !isEditable>
		<p>
		<h3>Booking locked</h3>
		The booking amendment deadline for this event has passed, and so bookings cannot be edited.
		If you need to change this booking, you can unlock it for editing for 24 hours - which will incur the amendment fee for this booking, as per the event's booking policy.</p>
		<@s.submit value="Unlock Booking" action="unlockBooking"/>
		</#if>
	</@s.push>
</@s.form>

<#include "/templates/footer.ftl">