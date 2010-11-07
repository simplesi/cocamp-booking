<#assign pagetitle = 'Booking Edit'>

<#include "/templates/header.ftl">
 
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