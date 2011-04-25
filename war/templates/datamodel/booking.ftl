<#assign pagetitle = 'Booking Edit'>

<#include "/templates/header.ftl">

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

<h2>Booking Edit</h2>
<div class="helpcolumn">
<p>Booking information for this individual should be entered on the left. 
This information should be as accurate as possible, details cannot be changed after the bookings deadline.</p>
<p>If an <strong>email address</strong> is supplied, this can be used by the bookings secretary to contact all people they have booked in, and it will be used later to invite people to 'My Village', the online pre-CoCamp community.</p>
<p>A Woodcraft Folk <strong>membership number</strong> should be provided for all adults in Woodcraft Folk units.</p>
</div>
<@s.actionerror/>


<@s.form id="booking" method="post">
    <@s.push value="model">
        <@s.hidden name="webKey"/>
        <@s.textfield name="name" label="Name"/>
        <@s.textfield name="dob" label="Date Of Birth (dd/mm/yyyy)" value="%{getText('format.date',{dob})}"/>
        <#if dob?exists && model.ageGroup!="" >Age group for event: ${model.ageGroup}<br/></#if>
        
        <@s.textfield name="email" label="Email"/>
        <@s.textfield name="phoneNumber" label="Phone Number"/>
      	<@s.textfield name="membershipNumber" label="Wcf Membership Number (if a member)"/> 
      	
        <@s.select name="diet" list="diets" label="Diet"/>
        <@s.textfield name="dietNotes" label="Additional diet notes"/>
        <@s.textarea name="otherNeeds" label="Other needs (disability, allergies, health conditions)"/>
        
        <@s.checkbox label="Wants to become a CoCamp member" name="becomeMember"/>
        
        <@s.textfield name="arrivalDate" label="Arrival Date" value="%{getText('format.date',{arrivalDate})}"/>
        <@s.textfield name="departureDate" label="Departure Date" value="%{getText('format.date',{departureDate})}"/>

		<#if model.bookingCreationDate?exists && model.webKey?exists >
		Booking created : ${model.bookingCreationDate?date}<br/>
		</#if>
		<#if model.cancellationDate?exists >
		Booking was cancelled on : ${model.cancellationDate?date}<br/>
		</#if>
        <@s.submit name="Save" value="Save" action="saveBooking"/>
        <@s.submit value="Cancel Edit" action="cancelEditBooking"/>
        <#if model.webKey?exists >
	        <#if model.cancellationDate?exists >
				<@s.submit value="Uncancel Booking" action="unCancelBooking"/>				
			<#else>
				<@s.submit value="Cancel Booking" action="cancelBooking"/>
			</#if>
		</#if>
    </@s.push>
</@s.form>

<#include "/templates/footer.ftl">