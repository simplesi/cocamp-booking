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
 <s:form action="processLogin" method="post">
 <s:textfield name="email" label="E-mail address" value="foo@cocamp"></s:textfield>
 <s:textfield name="password" label="Password" value="cocamp"></s:textfield>
 <s:submit name="login" value="Log In"></s:submit>
 </s:form>
</body>
</html>