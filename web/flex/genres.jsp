<?xml version="1.0" encoding="utf-8"?><%@page import="org.yass.YassConstants"%><%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.yass.domain.LibraryPlayList"%>
<genres>
<%
LibraryPlayList msr = (LibraryPlayList) session.getAttribute(YassConstants.CURRENT_PLAYLIST);
 	for (String genre : msr.getGenres()) {
%> <genre name="<%=StringEscapeUtils.escapeXml(genre)%>"/>
 <%
 	}
 %>
 </genres>
