<#assign pagetitle="Edit Unit">

<#include "/templates/header.ftl">

<h2>Unit Edit</h2>
<div class="helpcolumn">
<p>A <strong>unit</strong> is a group of people from the same organisation that will be camping together. For Woodcraft Folk this will be your District.</p>
<p>As well as providing contact details, please provide provisional numbers and requirements below.</p>
</div>
<@s.actionmessage/>
<@s.actionerror/>

<@s.form id="unit" method="post">
    <@s.push value="model">
        <@s.hidden name="webKey"/>
        <@s.textfield name="name" label="Unit Name"/>
        <@s.select name="organisationWebKey" label="Organisation" headerKey="" headerValue="-- Please Select --" 
        	list="allOrgs" listKey="webKey" listValue="name"/>
        
		<#if model.village?exists>
			Village: ${model.village.name!}<br/>
		</#if>
        	
        <@s.textfield name="email" label="Email"/>
        <@s.textfield name="phone" label="Phone Number"/>
        <@s.textarea name="address" label="Address"/>
        <@s.textarea name="notesString" label="Any other notes"/>
        
        <h3>Estimate of numbers</h3>
        <p>How may of each of the following will you bring to camp? These numbers are not binding, but please try to be as accurate as possible to help our planning.</p>       
        <!-- @s.textfield name="estimateWoodchip" 	label="Woodchips"/ -->
        <!-- @s.textfield name="estimateElfin" 		label="Elfins"/ -->
        <!-- @s.textfield name="estimatePioneer" 	label="Pioneers"/ -->
        <@s.textfield name="estimateVenturer" 	label="Venturers"/>
        <@s.textfield name="estimateDF" 		label="DFs"/>
        <@s.textfield name="estimateAdult" 		label="Adults"/>
    
    	<h3>Equipment</h3>
    	<p>What equipment will your unit provide?</p>
    	<@s.checkbox name="equipmentKitchen" 	label="Kitchen"/>
    	<@s.checkbox name="equipmentTables" 	label="Tables"/>
    	<@s.checkbox name="equipmentBenches" 	label="Benches / chairs"/>
    	<@s.checkbox name="equipmentLighting" 	label="Lighting"/>
    	<@s.textarea name="equipmentOther" 	label="Other details on equipment"/>
    	
    	<h3>Village canvas</h3>
    	<p>What canvas will your unit provide (for use in your village)?</p>
    	<@s.checkbox name="canvasMarquee" 	label="Village marquee (for approx 90 campers)"/>
    	<@s.checkbox name="canvasKitchen" 	label="Kitchen tent"/>
    	<@s.checkbox name="canvasStore" 	label="Store tent"/>
    	<@s.textarea name="canvasOther" 	label="Other canvas offers for village"/>
 
 		<h3>Shared large canvas</h3>
 		<p>What large canvas can your unit offer (for use centrally)?</p>
 		<@s.checkbox name="largeCanvasTown" 		label="Main marquee (for approx 700 people)"/>
 		<@s.checkbox name="largeCanvasActivity" 	label="Activity marquee"/>
 		<@s.checkbox name="largeCanvasCafe" 		label="Cafe marquee"/>
 		<@s.textarea name="largeCanvasOther" 		label="Other large canvas offers for event"/>
	
		<h3>Partners and hosting</h3>
		<@s.textfield name="villagePartner" 	label="Is there another district you would like to be in a village with - if so, who?"/>
		<@s.textarea name="delegation" 			label="Do you have a delegation in mind that you wish to host? This could be international or from another organisation in this country."/>	
	
        <@s.submit name="Save" value="Save" action="saveUnit"></@s.submit>
        <@s.submit value="Cancel" action="cancelEditUnit"/>
    </@s.push>
</@s.form>

<#include "/templates/footer.ftl">