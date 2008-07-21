<?xml version="1.0" encoding="utf-8"?>
<%@page import="org.yass.domain.SimplePlayList"%>
<%@page import="org.yass.YassConstants"%> <tracks><%
	SimplePlayList plst  = (SimplePlayList) session.getAttribute(YassConstants.CURRENT_PLAYLIST);
	for(Integer trackId : plst.trackIds){
%><track id="<%=trackId%>"/><%
	}
 %> </tracks>
  