<#assign pagetitle = 'Event Edit'>

<#include "/templates/header.ftl">
 
<script>
  $(function() {
  	$( "#event_publicEventStart" ).datepicker({ dateFormat: 'dd/mm/y' });
  	$( "#event_publicEventEnd" ).datepicker({ dateFormat: 'dd/mm/y' });
  	$( "#event_internalEventStart" ).datepicker({ dateFormat: 'dd/mm/y' });
  	$( "#event_internalEventEnd" ).datepicker({ dateFormat: 'dd/mm/y' });
  	$( "#event_earlyBookingDeadline" ).datepicker({ dateFormat: 'dd/mm/y' });
  	$( "#event_bookingDeadline" ).datepicker({ dateFormat: 'dd/mm/y' });
  	$( "#event_bookingSystemLocked" ).datepicker({ dateFormat: 'dd/mm/y' });
  });
</script>

<h2>Event Edit</h2>
<@s.actionmessage/>
<@s.actionerror/>

<p>Internal Start and End are the earliest and latest event booking dates, for working weeks, etc.</p>
<@s.form id="event" method="post">
    <@s.push value="model">
        <@s.hidden name="webKey"/>
        <@s.textfield name="name" label="Name"/>
        <@s.textfield name="publicEventStart" value="%{getText('format.date',{publicEventStart})}" label="Public start"/>
        <@s.textfield name="publicEventEnd" value="%{getText('format.date',{publicEventEnd})}" label="Public end"/>
        <@s.textfield name="internalEventStart" value="%{getText('format.date',{internalEventStart})}" label="Internal start"/>
        <@s.textfield name="internalEventEnd" value="%{getText('format.date',{internalEventEnd})}" label="Internal end"/>
        
        <@s.textfield name="earlyBookingDeadline" value="%{getText('format.date',{earlyBookingDeadline})}" label="Early booking deadline"/>
        <@s.textfield name="bookingDeadline" value="%{getText('format.date',{bookingDeadline})}" label="Booking deadline"/>
        <@s.textfield name="bookingSystemLocked" value="%{getText('format.date',{bookingSystemLocked})}" label="Booking system locked"/>
        
        
        
        <@s.checkbox name="isCurrentlyOpen" label="Is open for bookings"/>
        <@s.submit name="Save" value="Save" action="saveEvent"/>
        <@s.submit value="Cancel" action="cancelEditEvent"/>
    </@s.push>
</@s.form>


  

<#include "/templates/footer.ftl">