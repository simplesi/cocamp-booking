<#assign pagetitle = 'Login'>
<#include "/templates/header.ftl">
<table>
<tr><th>Name</th><th>Approved</th><th>Edit</th><th>Delete</th></tr>
<#list OrganisationList as org>
<tr><td>${org.name}</td>
<td>${org.approved?string("yes", "no")}</td>
<td>
<@s.url id="editURL" action="orgEdit" webKey="${org.webKey}" />

<@s.a href="${editURL}">Edit</@s.a>
</td>
<td>
<@s.url id="deleteURL" action="orgDelete" webKey="${org.webKey}" />
<@s.a href="${deleteURL}">Delete</@s.a>
</td>
</tr>
</#list>
</table>
<#include "/templates/footer.ftl">