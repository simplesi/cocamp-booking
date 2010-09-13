<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Please login</title>
</head>
<body>
 <s:actionerror/>
 <s:form action="processSignup" method="post">
 <s:textfield name="email" label="E-mail address"></s:textfield>
 <s:textfield name="password" label="Password"></s:textfield>
 <s:textfield name="name" label="Name"></s:textfield>
 <s:select label="Organisation" name="organisation" headerKey="1" headerValue="-- Please Select --" list="#{'01':'Org1','02':'Org2','03':'Org3'}" />
 <s:select label="Unit" name="unit" headerKey="1" headerValue="-- Please Select --" list="#{'01':'Unit1','02':'Unit2','03':'Unit3'}" /> 
 <s:submit name="login" value="Log In"></s:submit>
 </s:form>
 <a href="#">Add new organisation</a>
 <a href="#">Add new unit</a>
</body>
</html>