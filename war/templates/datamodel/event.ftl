<#assign pagetitle = 'Event Edit'>

<#include "/templates/header.ftl">
 
<h2>Event Edit</h2>
<@s.actionmessage/>
<@s.actionerror/>

<p>Internal Start and End are the earliest and latest event booking dates, for working weeks, etc.</p>

<@s.form id="event" method="post">
    <@s.push value="model">
        <@s.hidden name="webKey"/>
        <@s.textfield name="name" label="Name"/>
        <@s.textfield name="publicEventStart" label="Public start"/>
        <@s.textfield name="publicEventEnd" label="Public end"/>
        <@s.textfield name="internalEventStart" label="Internal start"/>
        <@s.textfield name="internalEventEnd" label="Internal end"/>
        <@s.checkbox name="isCurrentlyOpen" label="Is open for bookings"/>
        <@s.submit name="Save" value="Save" action="saveEvent"/>
        <@s.submit value="Cancel" action="cancelEditEvent"/>
    </@s.push>
</@s.form>

<#include "/templates/footer.ftl">