<#assign pagetitle = 'Main menu'>
<#include "/templates/header.ftl">

<h2>Global administrator</h2>
<ul>
<li><@s.url id="eventListURL" action="event/list" /><@s.a href="${eventListURL}">List all events</@s.a></li>
<li><@s.url id="orgListURL" action="org/list" /><@s.a href="${orgListURL}">List all organisations</@s.a></li>
<li><@s.url id="unitListAllURL" action="unit/listAll" /><@s.a href="${unitListAllURL}">List all Units</@s.a></li>
<li><@s.url id="bookingListAllURL" action="booking/list"/><@s.a href="${bookingListAllURL}">List all bookings</@s.a></li>
</ul>

<h2>Organisation administrator</h2>
<ul>
<li><@s.url id="myOrgEditURL" action="org/edit" webKey="${Session.CURRENT_ORG.webKey}"/><@s.a href="${myOrgEditURL}">Edit My Organisation</@s.a></li>
<li><@s.url id="myUnitListURL" action="unit/list"/><@s.a href="${myUnitListURL}">List units for my organisation</@s.a></li>
<li><@s.url id="myOrgBookingListURL" action="booking/listForOrg"/><@s.a href="${myOrgBookingListURL}">List bookings for my organisation</@s.a></li>
</ul>

<h2>Unit admin</h2>
<ul>
<li><@s.url id="myUnitEditURL" action="unit/edit" webKey="${Session.CURRENT_UNIT.webKey}"/><@s.a href="${myUnitEditURL}">Edit My Unit</@s.a></li>
<li><@s.url id="myUnitBookingListURL" action="booking/listForUnit"/><@s.a href="${myUnitBookingListURL}">List bookings for my unit</@s.a></li>
</ul>

<#include "/templates/footer.ftl">