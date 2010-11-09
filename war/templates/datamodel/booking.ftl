<#assign pagetitle = 'Booking Edit'>

<#include "/templates/header.ftl">

<script>
  $(function() {
  	$( "#booking_dob" ).datepicker({ dateFormat: 'dd/mm/yy', changeYear: true });
  	$( "#booking_arrivalDate" ).datepicker({ dateFormat: 'dd/mm/yy', 
  											 minDate: new Date( ${earliestDate?string("yyyy,M-1,d")}), 
  											 maxDate: new Date( ${latestDate?string("yyyy,M-1,d-1")}) });
  	$( "#booking_departureDate" ).datepicker({ dateFormat: 'dd/mm/yy', 
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
        <@s.textfield name="email" label="Email"/>
        <!-- todo: pickers -->
        <@s.textfield name="dob" label="Date Of Birth"/>
        Age group for event: ${model.ageGroup}<br/>
        <@s.textfield name="arrivalDate" label="Arrival Date"/>
        <@s.textfield name="departureDate" label="Departure Date"/>

        <@s.submit name="Save" value="Save" action="saveBooking"/>
        <@s.submit value="Cancel Edit" action="cancelEditBooking"/>
    </@s.push>
</@s.form>

<#include "/templates/footer.ftl">