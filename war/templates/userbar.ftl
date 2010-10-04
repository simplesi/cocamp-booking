<#if Session.USER?exists>
<div style="border:1px black solid; float:right;">
    You are logged in as: ${Session.USER.name}<br/>
    Organisation: ${Session.CURRENT_ORG.name}<br/>
    Unit: ${Session.CURRENT_UNIT.name}<br/>
</div>
</#if>