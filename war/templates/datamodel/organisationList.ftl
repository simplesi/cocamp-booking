<#assign pagetitle = 'Organisations'>

<#include "/templates/header.ftl">

<table>
    <tr>
        <th>Name</th>
        <th>Approved</th>
        <th>Edit</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as org>
        <tr>
            <td>${org.name}</td>
            <td>${org.approved?string("yes", "no")}</td>
            <td>
                <@s.url id="editURL" action="edit" webKey="${org.webKey}" />
                <@s.a href="${editURL}">Edit</@s.a>
            </td>
            <td>
                <@s.url id="deleteURL" action="delete" webKey="${org.webKey}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
            </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">