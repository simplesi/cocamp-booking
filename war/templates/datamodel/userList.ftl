<#assign pagetitle = 'Users'>

<#include "/templates/header.ftl">
<@s.actionmessage/>
<@s.actionerror/>

<table>
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Org</th>
        <th>Unit</th>
        <th>Status</th>
        <th>Edit</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as user>
        <tr>
            <td><@s.url id="editURL" action="editUser" email="${user.email}" />
            <@s.a href="${editURL}">${user.name}</@s.a></td>
            <td>${user.email}</td>
            <td>${user.organisationName}</td>
            <td>${user.unitName}</td>
            <td>${user.accessLevel.displayName}</td>
            <td>
                <@s.url id="deleteURL" action="deleteUser" email="${user.email}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
            </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">