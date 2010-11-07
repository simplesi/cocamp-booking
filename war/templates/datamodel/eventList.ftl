<#assign pagetitle = 'Events'>

<#include "/templates/header.ftl">
<@s.actionmessage/>
<@s.actionerror/>

<table>
    <tr>
        <th>Name</th>
        <th>Open for bookings?</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as event>
    <@s.url id="editURL" action="editEvent" webKey="${event.webKey}" />
        <tr>
            <td><@s.a href="${editURL}">${event.name}</@s.a></td>
            <td>${event.isCurrentlyOpen?string("yes", "no")}</td>
            <td>
                <@s.url id="deleteURL" action="deleteEvent" webKey="${event.webKey}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
            </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">