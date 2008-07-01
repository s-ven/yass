<?xml version="1.0" encoding="utf-8"?>
<%@page import="org.yass.YassConstants"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.yass.domain.MediaFile"%>
<%@page import="org.yass.domain.PlayList"%>
<tracks><%
 	PlayList msr = (PlayList) session.getAttribute(YassConstants.CURRENT_PLAYLIST);
 	for (MediaFile mf : msr.getMediaFiles()) {
%>
 <track UUID="<%=mf.getUuid() %>" trackNr="<%=mf.getTrack() %>" title="<%=StringEscapeUtils.escapeXml(mf.getTitle())%>" artist="<%=StringEscapeUtils.escapeXml(mf.getArtist())%>" album="<%=StringEscapeUtils.escapeXml(mf.getAlbum())%>" length="<%=mf.getLength() %>"/> <%
 	}
 %> </tracks>
