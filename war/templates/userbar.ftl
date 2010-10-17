<#if Session?exists && Session.USER?exists>
<div style="border:1px black solid; float:right;">
    You are logged in as: ${Session.USER.name}<br/>
    Event: ${Session.CURRENT_EVENT.name}<br/>
    Organisation: ${Session.CURRENT_ORG.name}<br/>
    Unit: ${Session.CURRENT_UNIT.name}<br/>
    <@s.url id="logoutURL" action="logout"/>
    <@s.a href="${logoutURL}">Logout</@s.a>
</div>
</#if>