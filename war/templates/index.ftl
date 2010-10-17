<#assign pagetitle = 'Main menu'>
<#include "/templates/header.ftl">

<h2>Global administrator</h2>
<p>
List all events<br/>
<@s.url id="orgListURL" action="org/list" />
<@s.a href="${orgListURL}">List all organisations</@s.a><br/>
<@s.url id="unitListAllURL" action="unit/listAll" />
<@s.a href="${unitListAllURL}">List all Units</@s.a><br/>

<@s.url id="bookingListAllURL" action="booking/list"/>
<@s.a href="${bookingListAllURL}">List all bookings</@s.a>


<h2>Organisation administrator</h2>
<p>
<@s.url id="myOrgEditURL" action="org/edit" webKey="${Session.CURRENT_ORG.webKey}"/>
<@s.a href="${myOrgEditURL}">Edit My Organisation</@s.a><br/>

<@s.url id="myUnitListURL" action="unit/list"/>
<@s.a href="${myUnitListURL}">List units for my organisation</@s.a><br/>

<@s.url id="myOrgBookingListURL" action="booking/listForOrg"/>
<@s.a href="${myOrgBookingListURL}">List bookings for my organisation</@s.a><br/>
</p>

<h2>Unit admin</h2>
<p>
<@s.url id="myUnitEditURL" action="unit/edit" webKey="${Session.CURRENT_UNIT.webKey}"/>
<@s.a href="${myUnitEditURL}">Edit My Unit</@s.a><br/>

<@s.url id="myUnitBookingListURL" action="booking/listForUnit"/>
<@s.a href="${myUnitBookingListURL}">List bookings for my unit</@s.a><br/>
</p>

<#include "/templates/footer.ftl">