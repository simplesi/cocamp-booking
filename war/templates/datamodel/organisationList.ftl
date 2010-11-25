<#assign pagetitle = 'Organisations'>

<#include "/templates/header.ftl">

<h2>Organisations</h2>
<p>Please click the name of an organisation below to see its full details.</p>

<@s.actionmessage/>
<@s.actionerror/>

<table>
    <tr>
        <th>Name</th>
        <th>Approved</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as org>
        <tr>
            <td><@s.url id="editURL" action="editOrg" webKey="${org.webKey}" />
                <@s.a href="${editURL}">${org.name}</@s.a></td>
            <td>${org.approved?string("yes", "no")}</td>
            <td>
                <@s.url id="deleteURL" action="deleteOrg" webKey="${org.webKey}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
            </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">