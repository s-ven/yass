<?xml version="1.0" encoding="utf-8"?>
<%@ page contentType="text/xml"%>
<%@page import="org.yass.domain.PlayList"%>
<%@page import="org.yass.YassConstants"%> <tracks><%
PlayList plst  = (PlayList) session.getAttribute(YassConstants.CURRENT_PLAYLIST);
	for(Integer trackId : plst.trackIds){
%><track id="<%=trackId%>"/><%
	}
 %> </tracks>
  