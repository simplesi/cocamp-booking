<#assign pagetitle = 'Actions pending approval'>

<#include "/templates/header.ftl">
<@s.actionmessage/>
<@s.actionerror/>

<h3>Users pending approval</h3>
<#if (PendingUsers.size() > 0)>
<p>The below are users who have signed up for bookings. Clicking approve will mean that they can log in, view, add and remove bookings for their unit. 
Please verify that they are permitted to organise bookings by verifying membership number or with IFM, and only approve once this is done. This must be carefully done to protect confidential information in the booking system.</p>
<@s.form id="user" method="post">
<table>
    <tr>
    	<th>Select</th>
        <th>Name</th>
        <th>Email</th>
        <th>Org</th>
        <th>Unit</th>
        <th>Membership No</th>
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
            <td>${user.membershipNumber!""}</td>
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
<p>These are organisations entered by people as part of the signup process. Since people have not been verified at this stage, these are not necessarily valid, and so are hidden from all other users until they are approved. 
Please ensure that these are valid and not duplicates of existing organisations, and then click approve.</p>
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
<p>These are units entered by people as part of the signup process. Since people have not been verified at this stage, these are not necessarily valid, and so are hidden from all other users until they are approved. 
Please ensure that these are valid and not duplicates of existing units, and then click approve.
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