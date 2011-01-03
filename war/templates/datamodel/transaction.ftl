<#assign pagetitle = 'Transaction Edit'>

<#include "/templates/header.ftl">
 <script>
  $(function() {
  	$( "#transaction_timestamp" ).datepicker({ dateFormat: 'dd/mm/yy', changeYear: true });
  });
</script>
<h2>Transaction Edit</h2>
<div class="helpcolumn">
<p>These are the details of this transaction which will be shown in the accounts screen.</p>
<p>
<h3>Credits</h3>
<strong>Payment</strong> - payment received from the unit.<br/>
<strong>Discount</strong> - discount applied to the unit.<br/>
<strong>Adjustment</strong> - adjustment based on discussion with the unit, eg additional discount/refund or correction.<br/>
<br/><strong>Amount</strong> is the size of the transaction. A positive value is a credit to the account (payment, discount, etc). A negative value is a debit from the account.</p>
</p>

<h3>Debits</h3>
<strong>Bill</strong> - an additional cost to the unit, eg further fees for food.<br/>
<br/><strong>Amount</strong> is the size of the transaction. A positive value is a debit from the account (bill). Negative values should not be used for bills.</p>
</p>


</div>
<@s.actionmessage/>
<@s.actionerror/>

<@s.form id="transaction" method="post">
    <@s.push value="model">
        <@s.hidden name="webKey"/>
        <@s.textfield name="timestamp" label="Date (dd/mm/yyyy)" value="%{getText('format.date',{timestamp})}"/>
        <@s.select name="type" list="transactionTypes" label="Type"/>
        <@s.textfield name="name" label="Label"/>
        <@s.textfield name="amount" label="Amount" value="${amount?string('0.00')}"/>
        <@s.textarea name="comments" label="Comments"/>  
        <@s.submit name="Save" value="Save" action="saveTransaction"/>
        <@s.submit value="Cancel" action="cancelEditTransaction"/>
    </@s.push>
</@s.form>

<#include "/templates/footer.ftl">