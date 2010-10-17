 <@s.actionerror/>
 <@s.form id="signup" action="processSignup" method="post">
 <@s.textfield name="email" label="E-mail address"></@s.textfield>
 <@s.password name="password" label="Password"></@s.password>
 <@s.textfield name="name" label="Name"></@s.textfield>
 <@s.doubleselect label="Organisation" name="OrganisationWebKey" list="allOrgs" listKey="webKey" listValue="Name" 
 					headerValue="--- Please Select ---" headerKey=""
 				  doubleLabel="Unit" doubleName="unitWebKey" doubleList="allUnits" doubleListKey="webKey" doubleListValue="Name"
 				  	doubleHeaderValue="--- Please Select ---" DoubleHeaderKey=""
 				  />
 <@s.submit name="login" value="Log In"></@s.submit>
 </@s.form>
 