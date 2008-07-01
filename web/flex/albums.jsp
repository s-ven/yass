<?xml version="1.0" encoding="utf-8"?><%@page import="org.yass.YassConstants"%><%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.yass.domain.LibraryPlayList"%>
<albums>
<%
	LibraryPlayList msr = (LibraryPlayList) session.getAttribute(YassConstants.CURRENT_PLAYLIST);
 	for (String album : msr.getAlbums()) {
%> <album name="<%=StringEscapeUtils.escapeXml(album)%>"/>
 <%
 	}
 %></albums>
