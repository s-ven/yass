<%@ page session="false" language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Yass player !</title>
<style>
body {
	margin: 0px;
	overflow: hidden;
}
</style>
</head>
<body scroll='no'>
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="main"
	width="100%" height="100%"
	codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
	<param name="movie" value="main.swf?userId=${user.id}" />
	<param name="quality" value="high" />
	<param name="bgcolor" value="#222222" />
	<param name="allowScriptAccess" value="sameDomain" />
	<param name="userId" value="${user.id}" />
	<embed src="main.swf?userId=${user.id}" quality="high"
		bgcolor="#222222" width="100%" height="100%" name="main"
		align="middle" play="true" loop="false" quality="high"
		allowScriptAccess="sameDomain" type="application/x-shockwave-flash"
		pluginspage="http://www.adobe.com/go/getflashplayer"
		userId="${user.id}">
	</embed> </object>
</noscript>
</body>
</html>
