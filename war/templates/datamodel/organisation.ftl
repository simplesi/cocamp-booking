<#assign pagetitle = 'Organisation Edit'>
<#include "/templates/header.ftl">
 
  <h2>Organisation Edit</h2>
 <@s.actionerror/>
 <@s.form id="organisation" action="orgSave" method="post">
 <@s.push value="organisation">
	 <@s.hidden name="webKey"/>
	 <@s.textfield name="name" label="Name"/>
	 <@s.textfield name="email" label="Email"/>
	 <@s.textfield name="phone" label="Phone Number"/>
	 <@s.textarea name="address" label="Address"/>
	 <@s.textarea name="notes" label="Notes"/>
	 <@s.submit name="Save" value="Save"/>
	 <@s.submit value="Cancel" name="redirect-action:orgList"/>
 </@s.push>
 </@s.form>
 
 <#include "/templates/footer.ftl">