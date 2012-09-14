<#assign pagetitle = 'Accounts'>

<#include "/templates/header.ftl">
<h2>Accounts - ${Session.CURRENT_UNIT.name}</h2>
<p>This is your current statement of accounts, listing costs for the bookings you have made, and any payments received for your ${viewLevel}.</p>
<p>Please ensure that any outstanding balance is paid before the end of Camp. You can do this by visiting the office.</p>
<br/>
<br/>
<h3>Costs</h3>
<#if ( costs.size() == 0)>
<p><em>You currently have nobody booked into this event.</em></p>
<#else>
<table width="100%">
    <tr>
    	<th width="15%">Date</th>
        <th width="15%">Type</th>
        <th width="30%">Details</th>
        <th width="10%">Quantity</th>
        <th width="15%">Price</th>
        <th width="15%">Total Price</th>
    </tr>
    
    <#list costs as cost>
        <tr>
            <td>${(cost.date?date)!}</td>
            <td>${cost.type}</td>
            <td>${cost.name}</td>
            <td>${(cost.quantity)!""}</td>
            <td>${cost.price?string.currency}</td>
            <td>${cost.linePrice?string.currency}</td>         
        </tr>
    </#list>
    <tr class="subtotal">
    	<td>Total</td>
    	<td></td>
    	<td></td>
    	<td>${totalBookingsCount}</td>
    	<td></td>
    	<td>${totalCost?string.currency}</td>
    </tr> 	
</table>
</#if>
<br/><br/>
<h3>Payments</h3>
<#if ( payments.size() == 0)>
<p><em>No payments have been logged on the system so far for your unit.</em></p>
<#else>
<table width="100%">
    <tr>
    	<th width="15%">Date</th>
        <th width="15%">Type</th>
        <th width="55%">Details</th>
        <th width="15%">Amount</th>
    </tr>
    
    <#list payments as payment>
        <tr>
            <td>${payment.timestamp?date}</td>
            <td>${payment.type}</td>
            <td>${payment.name}</td>
            <td>${payment.amount?string.currency}</td>    
        </tr>
        <#if payment.comments != "">
        	<tr><td></td><td></td><td><em>${payment.comments}</em></td></td></tr>
        </#if>
    </#list>
    <tr class="subtotal">
    	<td></td>
    	<td>Total</td>
    	<td></td>
    	<td>${paymentTotal?string.currency}</td>
    </tr> 	
</table>
</#if>
<br/><br/>
<h3>Balance Outstanding</h3>
<table>
    <tr>
    	<td>Total Costs</th>
    	<td>${totalCost?string.currency}</td>
    </tr>
    <tr>
        <td>- Total Payments</th>
        <td>${paymentTotal?string.currency}</td>
    </tr>
    <tr class="subtotal">
        <td>Balance</th>
        <th>${balance?string.currency}</th>
    </tr>
</table>

<#if IsBeforeEarlyBookingDeadline>
	<#if ( costs.size() == 0)>
	<p><em>You need to book people into the event and have paid the deposit for them before ${earlyBookingsDate?date} in order to qualify for the early bookings discount.</em></p>
	<#else>
	<p><em>To qualify all of your bookings so far for the discount on early bookings, you need to have paid the deposit for each before ${earlyBookingsDate?date}. Note that this discount only applies to bookings for the full duration of camp.</em></p>   	
	</#if>
</#if>

<#include "/templates/footer.ftl">