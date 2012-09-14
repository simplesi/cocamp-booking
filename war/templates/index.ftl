<#assign pagetitle = 'Main menu'>
<#include "/templates/header.ftl">

<@s.actionmessage/>
<@s.actionerror/>

<h2>Welcome to Woodcraft Folk bookings!</h2>
<p>You are logged into the Woodcraft Folk Booking system - please use the menus above to navigate around.</p>
<#if Session.USER.accessLevel.canChangeUnit>
<p>As an <strong>organisational booking secretary</strong>, you can use the menu in the top-right to change the unit being displayed.</p>
</#if>
<#if Session.USER.accessLevel.canChangeOrg>
<p>As an <strong>administrator</strong>, you can also use the menu to change which organisation being displayed.</p>
</#if>

<#include "/templates/footer.ftl">