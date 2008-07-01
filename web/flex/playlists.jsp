 <?xml version="1.0" encoding="utf-8"?>
<%@page import="java.util.Map"%>
<%@page import="org.yass.domain.PlayList"%>
<%@page import="org.yass.YassConstants"%>
<playlists>

<library name="LIBRARY" type="void">
 <playlist name="Music" type="library" id="0"/>
	</library><!-- 
	<smart name="SMART PLAYLISTS" type="void">
	 <playlist name="Most played" type="smart" id="1"/>
	 <playlist name="Top rated" type="smart" id="1"/>
	 <playlist name="&lt;New&gt;" type="smart" id="-1"/>
	</smart> -->
	<user name="USER PLAYLISTS" type="void">
<%
	final Map<String, PlayList> plsts = (Map<String, PlayList>)application.getAttribute(YassConstants.USER_PLAYLISTS);
	for(PlayList plst : plsts.values()){
%>


<playlist name="<%=plst.getName() %>" type="user" id="<%=plst.getId() %>"/>
<%
	}%>
	 <playlist name="&lt;New&gt;" type="user" id="-1"/>
	</user>
</playlists> 