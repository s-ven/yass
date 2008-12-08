<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
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
}

#global {
	position: absolute;
	width: 300px;
	height: 110px;
	left: 50%;
	top: 50%;
	margin-top: -55px; /* moitié de la hauteur */
	margin-left: -150px; /* moitié de la largeur */
	border: 1px solid #000;
	text-align: center;
	color: #ffffff;
	font-family: Arial;
	font-size: 12px;
}
</style>
</head>
<body>
<div id="global">
<form method="POST" action="j_security_check">
<p>Username: <input type="text" name="j_username" /></p>
<p>Password: <input type="password" name="j_password" /></p>
<input type="submit" value="Login" /></form>
</div>
</body>
</html>