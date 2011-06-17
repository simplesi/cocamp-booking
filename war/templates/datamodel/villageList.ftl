<#assign pagetitle = 'Villages'>

<#include "/templates/header.ftl">
<@s.actionmessage/>
<@s.actionerror/>

<@s.url id="addURL" action="addVillage" namespace="/manageAll" />
<@s.a href="${addURL}">Add a village</@s.a> to the current event.</p>

<table>
    <tr>
        <th>Name</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as village>
    <@s.url id="editURL" action="editVillage" webKey="${village.webKey}" />
        <tr>
            <td><@s.a href="${editURL}">${village.name!"unnamed"}</@s.a></td>
            <td>
                <@s.url id="deleteURL" action="deleteVillage" webKey="${village.webKey}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
            </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">