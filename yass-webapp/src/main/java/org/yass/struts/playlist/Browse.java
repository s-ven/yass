/*
 Copyright (c) 2008 Sven Duzont sven.duzont@gmail.com> All rights reserved.

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"),
 to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is furnished
 to do so, subject to the following conditions: The above copyright notice
 and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS",
 WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.yass.struts.playlist;

import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.yass.YassConstants;
import org.yass.domain.PlayList;
import org.yass.domain.SmartPlayList;
import org.yass.struts.YassAction;

public class Browse extends YassAction implements YassConstants {

	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		try {
			final Map<Integer, PlayList> playLists = getUser().getPlayLists();
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final Node playListsNode = doc.appendChild(doc.createElement("playlists"));
			final Element libraryNode = (Element) playListsNode.appendChild(doc.createElement("library"));
			libraryNode.setAttribute("name", "LIBRARY");
			libraryNode.setAttribute("type", "void");
			Element plstNode = (Element) libraryNode.appendChild(doc.createElement("playlist"));
			plstNode.setAttribute("name", "Music");
			plstNode.setAttribute("type", "library");
			plstNode.setAttribute("id", getUser().getLibrary().getId() + "");
			final Element smartPlNode = (Element) playListsNode.appendChild(doc.createElement("smart"));
			smartPlNode.setAttribute("name", "SMART PLAYLISTS");
			smartPlNode.setAttribute("type", "void");
			final Element usrPlNode = (Element) playListsNode.appendChild(doc.createElement("user"));
			usrPlNode.setAttribute("name", "USER PLAYLISTS");
			usrPlNode.setAttribute("type", "void");
			plstNode = (Element) usrPlNode.appendChild(doc.createElement("playlist"));
			plstNode.setAttribute("name", "<New>");
			plstNode.setAttribute("type", "user");
			plstNode.setAttribute("id", "0");
			for (final PlayList plst : playLists.values()) {
				if (plst instanceof SmartPlayList) {
					plstNode = (Element) smartPlNode.appendChild(doc.createElement("playlist"));
					plstNode.setAttribute("type", "smart");
				} else {
					plstNode = (Element) usrPlNode.appendChild(doc.createElement("playlist"));
					plstNode.setAttribute("type", "user");
				}
				plstNode.setAttribute("name", plst.getName());
				plstNode.setAttribute("id", plst.getId() + "");
			}
			return outputDocument(doc);
		} catch (final ParserConfigurationException e) {
			LOG.error("", e);
		}
		return NONE;
	}

	public void setRefresh(final boolean refresh) {
	}
}
// <playlists>
// <library name="LIBRARY" type="void">
// <playlist name="Music" type="library" id="0"/>
// </library>
// <%
// final Map<Integer, PlayList> plsts = (Map<Integer,
// PlayList>)application.getAttribute(YassConstants.USER_PLAYLISTS);
// %>
// <smart name="SMART PLAYLISTS" type="void"><%
// for(PlayList plst : plsts.values()){
// if(plst instanceof SmartPlayList){ %>
// <playlist name="<%=plst.getName() %>" type="smart" id="<%=plst.getId() %>"/>
// <%
// }
// }
// %>
// </smart>
// <user name="USER PLAYLISTS" type="void"><%
// for(PlayList plst : plsts.values()){
// if(plst instanceof SimplePlayList){ %>
// <playlist name="<%=plst.getName() %>" type="user" id="<%=plst.getId() %>"/>
// <%
// }
// }
// %>
// <playlist name="&lt;New&gt;" type="user" id="0"/>
// </user>
// </playlists>