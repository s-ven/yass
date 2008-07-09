<?xml version="1.0" encoding="utf-8"?>
<%@page import="org.yass.YassConstants"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.yass.domain.Track"%>
<%@page import="org.yass.domain.PlayList"%>
<%@page import="org.yass.lucene.Constants"%>
<tracks><%
 	PlayList msr = (PlayList) session.getAttribute(YassConstants.CURRENT_PLAYLIST);
 	for (Track mf : msr.getMediaFiles()) {
 		
 		
%>
 <track UUID="<%=mf.getUuid() %>" trackNr="<%=mf.getTrack() %>" title="<%=StringEscapeUtils.escapeXml(mf.getTitle())%>" artist="<%=mf.getProperty(Constants.ARTIST).id%>" album="<%=mf.getProperty(Constants.ALBUM).id%>" genre="<%=mf.getProperty(Constants.GENRE).id%>" length="<%=mf.getLength() %>"/> <%
 	}
 %> </tracks>
 