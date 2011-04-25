<#assign pagetitle = 'Confirm Unlock'>

<#include "/templates/header.ftl">
 
<h2>Confirm unlock</h2>

<@s.actionmessage/>
<@s.actionerror/>

<@s.push value="model">
<h3>'${name}'</h3>
<p>Are you sure you wish to unlock this booking for editing? 
This will incur the late booking fee (${25?string.currency}).</p>
</p>
	<@s.form id="unlockBooking" method="post">    
        <@s.hidden name="webKey"/>
   
        <@s.submit name="confirmUnlockBooking" value="Unlock Booking"/>
        <@s.submit name="cancelUnlockBooking" value="Back" />
    
	</@s.form>
</@s.push>
<#include "/templates/footer.ftl">