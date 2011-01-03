<#assign pagetitle = 'Bookings'>

<#include "/templates/header.ftl">
<h2>Bookings</h2>
<p>Please click a person's name below to see the full details of their booking. 
<@s.url id="addURL" action="addBooking" namespace="/manageUnit" />
<@s.a href="${addURL}">Add a booking</@s.a> to your current unit.</p>

<table>
    <tr>
        <th>Name</th>
        <th>Age Group</th>
        <th>From</th>
        <th>To</th>
        <th>Price</th>
        <th>Booking date</th>
    </tr>
    
    <#list ModelList as booking>
    <@s.url id="editURL" action="editBooking" webKey="${booking.webKey}" />
        <tr>
            <td> <@s.a href="${editURL}">${booking.name!"unnamed"}</@s.a></td>
            <td>${booking.ageGroup}</td>
            <td>${(booking.arrivalDate?date)!}</td>
            <td>${(booking.departureDate?date)!}</td>
            <td>${booking.fee?string.currency}</td>
            <td>${booking.bookingCreationDate?date}</td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">