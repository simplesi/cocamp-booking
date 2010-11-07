<#assign pagetitle="Units">
<#include "/templates/header.ftl">

<table>
    <tr>
        <th>Name</th>
        <th>Approved</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as unit>
        <tr>
            <td> <@s.url id="editURL" action="editUnit" webKey="${unit.webKey}" />
            <@s.a href="${editURL}">${unit.name}</@s.a></td>
            <td>${unit.approved?string("yes", "no")}</td>
            <td>
                <@s.url id="deleteURL" action="deleteUnit" webKey="${unit.webKey}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
            </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">