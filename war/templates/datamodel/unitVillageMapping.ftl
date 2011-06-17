<#assign pagetitle="Unit-Village Assignment">
<#include "/templates/header.ftl">
<h2>Village Assignments</h2>
<p>To change the village for a given unit, please alter it in the dropdown below, and press apply. 
Bookings which were already manually changed to a different unit will not be affected.</p>
<@s.actionmessage/>
<@s.actionerror/>

<@s.form action="applyUnitVillageMappings" method="post">
<table>
    <tr>
        <th>Name</th>
        <th>Village</th>
    </tr>
    
    <#list Mappings as mapping>
        <tr>
            <td>${mapping.unitName!"unnamed unit"}</td>
            <td>
				<@s.select name="map['${mapping.unitWebKey}']" list="villages" label="" listKey="webKey" listValue="name" value=mapping.VillageWebKey labelposition="left" labelSeparator=""/>		
			</td>
        </tr>
    </#list>
</table>
<@s.submit name="submit" value="Apply"/>
</@s.form>

<#include "/templates/footer.ftl">