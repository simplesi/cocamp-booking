<#assign pagetitle = 'Early Bird Discounts'>

<#include "/templates/header.ftl">
<h2>Generate Early Bird Discounts</h2>
<p>Below are the proposed discounts for each unit, based on the bookings and payments registered as being in the system prior to the early bird discount deadline.</p>
<p><strong>To add these discounts</strong> (and replace any existing discounts), click 'apply discounts' below.</p>

<h3>Discounts</h3>
<#if ( discountLines.size() == 0)>
<p><em>There are no discounts in the system.</em></p>
<#else>
<table width="100%">
    <tr>
    	<th width="15%">Unit</th>
        <th width="10%">Discount</th>
        <th width="10%">Payments*</th>
        <th width="10%">Included Bookings*</th>
        <th width="10%">Unfunded Bookings*</th>
        <th width="30%">Details</th>
    </tr>
    
    <#list discountLines as unitdata>
        <tr class="divider">
            <td>${unitdata.unit.name}</td>
            <td>${unitdata.discount.amount?string.currency}</td>
            <td>${unitdata.totalQualifyingPayments?string.currency}</td>
            <td>${unitdata.paidBookings?size}</td>
            <td>${unitdata.unpaidBookings?size}</td>
            <td>${unitdata.discount.name}</td>         
        </tr>
        <tr>
        	<td></td>
       	 	<td></td>
       	 	<td colspan="4"><em>${unitdata.discount.comments}</em></td> 
       	</tr>
       	<#if (unitdata.previousDiscount?exists) >
       	<tr>
        	<td></td>
       	 	<td></td>
       	 	<td colspan="4">Existing discount: ${unitdata.previousDiscount.amount?string.currency} - ${unitdata.previousDiscount.name}</td> 
       	</tr>
       	</#if>
    </#list>
    <tr class="subtotal">
    	<td>Total</td>
    	<td>${totalDiscount?string.currency}</td>
    	<td></td>
    	<td></td>
    	<td></td>
    	<td></td>
    </tr> 	
</table>
<p>*at the time of the early booking deadline.</p>
</#if>
<br/><br/>
<@s.form id="discounts" method="post" action="confirmDiscounts">
    <@s.submit name="submit" value="Apply discounts"/>
</@s.form>

<#include "/templates/footer.ftl">