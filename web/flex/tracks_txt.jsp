<%@page import="org.yass.YassConstants"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.yass.domain.MediaFile"%>
<%@page import="org.yass.domain.PlayList"%>
<%
 	PlayList msr = (PlayList) session.getAttribute(YassConstants.CURRENT_PLAYLIST);
 	for (MediaFile mf : msr.getMediaFiles()) {
%>
<%=mf.getUuid() %><br>
<%}%>
