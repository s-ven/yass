package org.yass.struts;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.yass.YassConstants;
import org.yass.domain.Library;
import org.yass.domain.PlayList;
import org.yass.domain.TrackStat;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class YassAction extends ActionSupport implements YassConstants {

	public static final Log LOG = LogFactory.getLog(YassAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -4592155945099982999L;

	protected PlayList getPlayList(final String plId) {
		return (PlayList) ActionContext.getContext().getSession().get(plId);
	}

	protected Library getLibrary() {
		return (Library) ActionContext.getContext().getApplication().get(ALL_LIBRARY);
	}

	protected Map<Integer, TrackStat> getTrackStats() {
		return (Map<Integer, TrackStat>) ActionContext.getContext().getApplication().get(USER_TRACK_STATS);
	}

	protected void setPlayList(final String playListId, final PlayList search) {
		ActionContext.getContext().getSession().put(playListId, search);
	}

	protected Map<Integer, PlayList> getPlayLists() {
		return (Map<Integer, PlayList>) ActionContext.getContext().getApplication().get(USER_PLAYLISTS);
	}

	/**
	 * @param document
	 * @throws TransformerFactoryConfigurationError
	 * @throws IllegalArgumentException
	 */
	public String outputDocument(final Document document) {
		try {
			final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			ServletActionContext.getResponse().setContentType("text/xml");
			ServletActionContext.getResponse().setHeader("Content-Encoding", "gzip");
			ByteArrayOutputStream bout;
			final GZIPOutputStream gzoz = new GZIPOutputStream(bout = new ByteArrayOutputStream(), 2048000);
			final StreamResult result = new StreamResult(gzoz);
			final DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			gzoz.finish();
			ServletActionContext.getResponse().setContentLength(bout.size());
			bout.writeTo(ServletActionContext.getResponse().getOutputStream());
		} catch (final FileNotFoundException e) {
			LOG.error("", e);
			return ERROR;
		} catch (final IOException e) {
			LOG.error("", e);
			return ERROR;
		} catch (final TransformerException e) {
			LOG.error("", e);
			return ERROR;
		}
		return NONE;
	}
}
