<#assign pagetitle="Canned Reports">

<#include "/templates/header.ftl">

<h2>Canned Reports</h2>
<p>Please choose the report to view from the list below and click GO.</p>
<@s.actionmessage/>
<@s.actionerror/>

<@s.form id="report">
        <@s.select name="selectedReport" label="Report" headerKey="" headerValue="-- Please Select --" 
        	list="availableReports" listKey="label.tag" listValue="label.displayName"/>     
        <@s.submit name="Go" value="Go"/>

</@s.form>

<#include "/templates/footer.ftl">