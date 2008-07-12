<?xml version="1.0" encoding="utf-8"?>
<%@page import="java.util.Iterator"%>
<%@page import="org.yass.domain.Track"%>
<%@page import="org.yass.domain.PlayList"%>
<%@page import="org.yass.YassConstants"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.yass.lucene.Constants"%>
<tracks><%
	Iterator<Track> it = ((PlayList) session.getAttribute(YassConstants.CURRENT_PLAYLIST)).getTracks().iterator();
	while(it.hasNext()){
		Track mf = it.next();
		if(mf.getTrackInfo(Constants.ARTIST) != null){
%>
 <track id="<%=mf.getId() %>" trackNr="<%=mf.getTrackNr() %>" title="<%=StringEscapeUtils.escapeXml(mf.getTitle())%>" artist="<%=mf.getTrackInfo(Constants.ARTIST).id%>" album="<%=mf.getTrackInfo(Constants.ALBUM).id%>" genre="<%=mf.getTrackInfo(Constants.GENRE).id%>" length="<%=mf.getLength() %>" rating="<%=mf.getRating() %>"/><%
		}
 	} 
 %> </tracks>
 