<#assign pagetitle="Unit-Village Assignment">
<#include "/templates/header.ftl">
<h2>Village Assignments</h2>
<@s.actionmessage/>
<@s.actionerror/>

<#if ( ProblemBookings.size() != 0)>
<p>Not all bookings were switched to new villages, as some already had a custom village set. If you need to change these bookings, they should be altered directly</p>
<h3>Unchanged bookings</h3>
<table>
   <tr>
   		<th>Unit</th>
        <th>Booking Name</th>
        <th>Current village</th>
    </tr>
    
    <#list ProblemBookings as booking>
    	<@s.url id="editURL" action="editBooking" webKey="${booking.webKey}" />
        <tr>
        	<td>${booking.unit.name}</td>
            <td> <@s.a href="${editURL}">${booking.name!"unnamed"}</@s.a><#if booking.isCancelled> (Cancelled)</#if></td>
       		<td>${booking.village.name!}</td>
        </tr>
    </#list>
</table>
</#if>

<p>To change the village for a given unit, please alter it in the dropdown below, and press apply. 
Bookings which were already manually assigned to a different village will not be affected. You can manually change the village for a given booking by editing the booking directly.</p>


<@s.form action="applyUnitVillageMappings" method="post">
<table>
    <tr>
        <th>Name</th>
        <th>Village</th>
    </tr>
    
    <#list Units as unit>
        <tr>
            <td>${unit.name!"unnamed unit"}</td>
            <td>
				<@s.select name="map['${unit.webKey}']" list="villages" label="" listKey="webKey" listValue="name" labelposition="left" labelSeparator=""/>		
			</td>
        </tr>
    </#list>
</table>
<@s.submit name="submit" value="Apply"/>
</@s.form>

<#include "/templates/footer.ftl">