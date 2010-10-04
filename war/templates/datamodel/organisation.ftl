 <@s.actionerror/>
 <@s.form id="organisation" action="processOrganisation" method="post">
 <@s.textfield name="name" label="Name"/>
 <@s.textfield name="email" label="Email"/>
 <@s.textfield name="phone" label="Phone Number"/>
 <@s.textarea name="postalAddress" label="Address"/>
 
 <@s.doubleselect label="Organisation" doubleLabel="Unit" name="organisationKey" list="allOrgs" listKey="key" listValue="name"headerValue="--- Please Select ---" headerKey=""
 				  doubleName="unitKey" doubleList="allUnits" doubleListKey="key" doubleListValue="Name"/>
 <@s.submit name="login" value="Log In"></@s.submit>
 </@s.form>