<#assign pagetitle = 'Actions pending approval'>

<#include "/templates/header.ftl">
<@s.actionmessage/>
<@s.actionerror/>

<h3>Users pending approval</h3>
<#if (PendingUsers.size() > 0)>
<@s.form id="user" method="post">
<table>
    <tr>
    	<th>Select</th>
        <th>Name</th>
        <th>Email</th>
        <th>Org</th>
        <th>Unit</th>
        <th>Status</th>
    </tr>
   
    <#list PendingUsers as user>    
        <tr>
        	<td><@s.checkbox name="selectedUser" fieldValue="${user.email}"></@s.checkbox></td>
            <td><@s.url id="editURL" action="editUser" email="${user.email}" />
            <@s.a href="${editURL}">${user.name}</@s.a></td>
            <td>${user.email!""}</td>
            <td>${user.organisationName!""}</td>
            <td>${user.unitName!""}</td>
            <td>${user.accessLevel.displayName!""}</td>
        </tr>
    </#list>
</table>

<@s.submit value="Approve selected users" action="bulkApproveUsers"/>
<@s.submit value="Delete selected users" action="bulkDeleteUsers"/>
</@s.form>

<#else>
<p>There are currently no pending users.</p>
</#if>


<h3>Organisations pending approval</h3>
<#if (PendingOrgs.size() > 0)>
<@s.form id="org" method="post">
<table>
    <tr>
    	<th>Select</th>
        <th>Name</th>
        <th>Address</th>
    </tr>
   
    <#list PendingOrgs as org>    
        <tr>
        	<td><@s.checkbox name="selectedOrg" fieldValue="${org.webKey}"/></td>
            <td><@s.url id="editURL" action="editOrg" webKey="${org.webKey}" />
            <@s.a href="${editURL}">${org.name!""}</@s.a></td>
            <td>${org.address!""}</td>
        </tr>
    </#list>
</table>

<@s.submit value="Approve selected organisations" action="bulkApproveOrgs"/>
<@s.submit value="Delete selected organisations" action="bulkDeleteOrgs"/>
</@s.form>

<#else>
<p>There are currently no pending organisations.</p>
</#if>

<h3>Units pending approval</h3>
<#if (PendingUnits.size() > 0)>
<@s.form id="unit" method="post">
<table>
    <tr>
    	<th>Select</th>
        <th>Name</th>
        <th>Organisation</th>
    </tr>
   
    <#list PendingUnits as unit>    
        <tr>
        	<td><@s.checkbox name="selectedUnit" fieldValue="${unit.webKey}"/></td>
            <td><@s.url id="editURL" action="editUnit" webKey="${unit.webKey}" />
            <@s.a href="${editURL}">${unit.name!""}</@s.a></td>
            <td>${unit.organisation.name!""}</td>
        </tr>
    </#list>
</table>

<@s.submit value="Approve selected units" action="bulkApproveUnits"/>
<@s.submit value="Delete selected units" action="bulkDeleteUnits"/>
</@s.form>

<#else>
<p>There are currently no pending units.</p>
</#if>


<#include "/templates/footer.ftl">