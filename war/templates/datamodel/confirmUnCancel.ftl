<#assign pagetitle = 'Confirm UnCancellation'>

<#include "/templates/header.ftl">
 
<h2>Confirm booking uncancellation</h2>

<@s.actionmessage/>
<@s.actionerror/>

<@s.push value="model">
<h3>'${name}'</h3>
<p>Are you sure you wish to reinstate this cancelled booking?</p>
</p>
	<@s.form id="cancelItem" method="post">    
        <@s.hidden name="webKey"/>
   
        <@s.submit name="confirmUnCancelBooking" value="Reinstate Booking"/>
        <@s.submit name="cancelUnCancelBooking" value="Return to edit" />
    
	</@s.form>
</@s.push>
<#include "/templates/footer.ftl">