<#assign pagetitle = 'Booking Edit'>

<#include "/templates/header.ftl">

<script>
  $(function() {
  	$( "#booking_dob" ).datepicker({ dateFormat: 'dd/mm/y', changeYear: true });
  	$( "#booking_arrivalDate" ).datepicker({ dateFormat: 'dd/mm/y', 
  											 minDate: new Date( ${earliestDate?string("yyyy,M-1,d")}), 
  											 maxDate: new Date( ${latestDate?string("yyyy,M-1,d-1")}) });
  	$( "#booking_departureDate" ).datepicker({ dateFormat: 'dd/mm/y', 
  											 minDate: new Date( ${earliestDate?string("yyyy,M-1,d+1")}), 
  											 maxDate: new Date( ${latestDate?string("yyyy,M-1,d")}) });
  });
</script>

<h2>Booking Edit</h2>

<@s.actionerror/>

<@s.form id="booking" method="post">
    <@s.push value="model">
        <@s.hidden name="webKey"/>
        <@s.textfield name="name" label="Name"/>
        <@s.textfield name="dob" label="Date Of Birth" value="%{getText('format.date',{dob})}"/>
        <#if dob?exists >Age group for event: ${model.ageGroup}<br/></#if>
        
        <@s.textfield name="email" label="Email"/>
        <@s.textfield name="phoneNumber" label="Phone Number"/>
      	<@s.textfield name="membershipNumber" label="Wcf Membership Number (if a member)"/> 
      	
        <@s.select name="diet" list="diets" label="Diet"/>
        <@s.textfield name="dietNotes" label="Additional diet notes"/>
        <@s.textarea name="otherNeeds" label="Other needs (disability, allergies, health conditions)"/>
        
        <@s.checkbox label="Wants to become a CoCamp member" name="becomeMember"/>
        
        <@s.textfield name="arrivalDate" label="Arrival Date" value="%{getText('format.date',{arrivalDate})}"/>
        <@s.textfield name="departureDate" label="Departure Date" value="%{getText('format.date',{departureDate})}"/>

        <@s.submit name="Save" value="Save" action="saveBooking"/>
        <@s.submit value="Cancel Edit" action="cancelEditBooking"/>
    </@s.push>
</@s.form>

<#include "/templates/footer.ftl">