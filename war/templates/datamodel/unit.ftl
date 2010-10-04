<#assign pagetitle="Edit Unit">
<#include "/templates/header.ftl">

 <h2>Unit Edit</h2>
 <@s.actionerror/>
 <@s.form id="unit" action="save" method="post">
 <@s.push value="unit">
	 <@s.hidden name="webKey"/>
	 <@s.textfield name="name" label="Name"/>
	 <@s.select name="unit" label="Organisation" headerKey="" headerValue="-- Please Select --" 
	 	list="allOrgs" listKey="key" listValue="name"/>
	 <@s.textfield name="email" label="Email"/>
	 <@s.textfield name="phone" label="Phone Number"/>
	 <@s.textarea name="address" label="Address"/>
	 <@s.textarea name="notes" label="Notes"/>
	 
	 <@s.submit name="Save" value="Save"></@s.submit>
 </@s.push>
 </@s.form>
 
 <#include "/templates/footer.ftl">