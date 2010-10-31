<#assign pagetitle = 'Events'>

<#include "/templates/header.ftl">
<@s.actionmessage/>
<@s.actionerror/>

<table>
    <tr>
        <th>Name</th>
        <th>Open for bookings?</th>
        <th>Edit</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as event>
        <tr>
            <td>${event.name}</td>
            <td>${event.isCurrentlyOpen?string("yes", "no")}</td>
            <td>
                <@s.url id="editURL" action="edit" webKey="${event.webKey}" />
                <@s.a href="${editURL}">Edit</@s.a>
            </td>
            <td>
                <@s.url id="deleteURL" action="delete" webKey="${event.webKey}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
            </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">