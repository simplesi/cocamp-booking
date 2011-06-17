<#assign pagetitle="Units">
<#include "/templates/header.ftl">
<h2>Units</h2>
<p>Please click the name of a unit below to see its full details.</p>
<table>
    <tr>
        <th>Name</th>
        <th>Village</th>
        <th>Approved</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as unit>
        <tr>
            <td> <@s.url id="editURL" action="editUnit" webKey="${unit.webKey}" />
            <@s.a href="${editURL}">${unit.name!"unnamed"}</@s.a></td>
            <td><#if unit.villageKey?exists>${unit.village.name!""}</#if></td>
            <td>${unit.approved?string("yes", "no")}</td>
            <td>
                <@s.url id="deleteURL" action="deleteUnit" webKey="${unit.webKey}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
            </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">