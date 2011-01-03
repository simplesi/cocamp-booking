<#assign pagetitle = 'Transactions'>

<#include "/templates/header.ftl">
<h2>Transactions</h2>
<p>Please click a transaction below to see the full details. 
<@s.url id="addURL" action="addTransaction" namespace="/manageAll" />
<@s.a href="${addURL}">Add a transaction</@s.a> to the currently selected unit.</p>

<table>
    <tr>
        <th>Unit</th>
        <th>Date</th>
        <th>Type</th>
        <th>Details</th>
        <th>Amount</th>
        <th>Delete</th>
    </tr>
    
    <#list ModelList as transaction>
    <@s.url id="editURL" action="editTransaction" webKey="${transaction.webKey}" />
        <tr>
        	<td>${transaction.unit.name!"unnamed"}</td>
        	<td>${transaction.timestamp?date}</td>
        	<td>${transaction.type}</td>
            <td> <@s.a href="${editURL}">${transaction.name!"no details"}</@s.a></td>
            <td>${transaction.amount?string.currency}</td>
            <td><@s.url id="deleteURL" action="deleteTransaction" webKey="${transaction.webKey}" />
                <@s.a href="${deleteURL}">Delete</@s.a>
                </td>
        </tr>
    </#list>
</table>

<#include "/templates/footer.ftl">