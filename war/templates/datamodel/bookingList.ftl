<#assign pagetitle = 'Bookings'>

<#include "/templates/header.ftl">

<table>
    <tr>
        <th>Name</th>
        <th>Age Group</th>
        <th>From</th>
        <th>To</th>
        <th>Price</th>
        <th>Edit</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as booking>
        <tr>
            <td>${booking.name}</td>
            <td>${booking.ageGroup}</td>
            <td>${booking.arrivalDate?date}</td>
            <td>${booking.departureDate?date}</td>
            <td>${booking.price}</td>
            <td>
                <@s.url id="editURL" action="edit" webKey="${booking.webKey}" />
                <@s.a href="${editURL}">Edit</@s.a>
            </td>
            <td>
                <@s.url id="deleteURL" action="delete" webKey="${booking.webKey}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
            </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">