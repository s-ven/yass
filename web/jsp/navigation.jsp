<?xml version="1.0" encoding="utf-8"?>
<%@page contentType="text/xml"%>
<%@page import="java.util.Map"%>
<%@page import="org.yass.domain.PlayList"%>
<%@page import="org.yass.YassConstants"%>
<%@page import="org.yass.domain.SimplePlayList"%>
<%@page import="org.yass.domain.SmartPlayList"%><playlists>
  <library name="LIBRARY" type="void">
  <playlist name="Music" type="library" id="0"/>
</library>
 <%
 	final Map<Integer, PlayList> plsts = (Map<Integer, PlayList>)application.getAttribute(YassConstants.USER_PLAYLISTS);
%>
 	<smart name="SMART PLAYLISTS" type="void"><%
 	for(PlayList plst : plsts.values()){
 		if(plst instanceof SmartPlayList){ %> 
 <playlist name="<%=plst.getName() %>" type="smart" id="<%=plst.getId() %>"/>
 <%
 		}
 	}
 %>
 	</smart>
 	<user name="USER PLAYLISTS" type="void"><%
 	for(PlayList plst : plsts.values()){
 		if(plst instanceof SimplePlayList){ %> 
 <playlist name="<%=plst.getName() %>" type="user" id="<%=plst.getId() %>"/>
 <%
 		}
 	}
 %>
 	 <playlist name="&lt;New&gt;" type="user" id="0"/>
 	</user>
</playlists>
