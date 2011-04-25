<#assign pagetitle = 'Confirm Cancellation'>

<#include "/templates/header.ftl">
 
<h2>Confirm cancellation</h2>

<@s.actionmessage/>
<@s.actionerror/>

<@s.push value="model">
<h3>'${name}'</h3>
<p>Are you sure you wish to cancel this booking? Cancellations are subject to a cancellation fee, as per the booking fees.</p>
</p>
	<@s.form id="cancelItem" method="post">    
        <@s.hidden name="webKey"/>
   
        <@s.submit name="confirmCancelBooking" value="Cancel Booking"/>
        <@s.submit name="cancelCancelBooking" value="Return to edit" />
    
	</@s.form>
</@s.push>
<#include "/templates/footer.ftl">