<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Yass Player Authentication Page</title>
<style>
body {
	margin: 0px;
	overflow: hidden;
	background-color: #222222;
	vertical-align: center;
}
</style>
</head>
<body>
<form method="POST" action="j_security_check">
<p>Username: <input type="text" name="j_username" /></p>
<p>Password: <input type="password" name="j_password" /></p>
<input type="submit" value="Login" /></form>
</body>
</html>