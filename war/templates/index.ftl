<#assign pagetitle = 'Main menu'>
<#include "/templates/header.ftl">

<h2>Global administrator</h2>
<ul>
<li><@s.url id="userListURL" namespace="/manageAll" action="listUsers" /><@s.a href="${userListURL}">List all users</@s.a></li>
<li><@s.url id="eventListURL" namespace="/manageAll" action="listEvents" /><@s.a href="${eventListURL}">List all events</@s.a></li>
<li><@s.url id="orgListURL" namespace="/manageAll" action="listOrgs" /><@s.a href="${orgListURL}">List all organisations</@s.a></li>
<li><@s.url id="unitListAllURL" namespace="/manageAll" action="listUnits" /><@s.a href="${unitListAllURL}">List all units</@s.a></li>
<li><@s.url id="bookingListAllURL" namespace="/manageAll" action="listBookings"/><@s.a href="${bookingListAllURL}">List all bookings</@s.a></li>
</ul>

<h2>Manage '${Session.CURRENT_ORG.name}'</h2>
<ul>
<li><@s.url id="myOrgEditURL" namespace="/manageOrg" action="editOrg"/><@s.a href="${myOrgEditURL}">Edit organisation details</@s.a></li>
<li><@s.url id="myUnitListURL" namespace="/manageOrg" action="listUnits"/><@s.a href="${myUnitListURL}">List units</@s.a></li>
<li><@s.url id="myOrgBookingListURL" namespace="/manageOrg" action="listBookings"/><@s.a href="${myOrgBookingListURL}">List all bookings for my organisation</@s.a></li>
</ul>

<h2>Manage '${Session.CURRENT_UNIT.name}'</h2>
<ul>
<li><@s.url id="myUnitEditURL" namespace="/manageUnit" action="editUnit" webKey="${Session.CURRENT_UNIT.webKey}"/><@s.a href="${myUnitEditURL}">Edit unit details</@s.a></li>
<li><@s.url id="myUnitBookingListURL" namespace="/manageUnit" action="listBookings"/><@s.a href="${myUnitBookingListURL}">List bookings</@s.a></li>
</ul>

<#include "/templates/footer.ftl">