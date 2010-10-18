<#assign pagetitle="Units">
<#include "/templates/header.ftl">

<table>
    <tr>
        <th>Name</th>
        <th>Approved</th>
        <th>Edit</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as unit>
        <tr>
            <td>${unit.name}</td>
            <td>${unit.approved?string("yes", "no")}</td>
            <td>
                <@s.url id="editURL" action="edit" webKey="${unit.webKey}" />
                <@s.a href="${editURL}">Edit</@s.a>
            </td>
            <td>
                <@s.url id="deleteURL" action="delete" webKey="${unit.webKey}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
            </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">