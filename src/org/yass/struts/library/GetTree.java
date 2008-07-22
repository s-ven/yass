package org.yass.struts.library;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.yass.YassConstants;
import org.yass.struts.YassAction;

import com.opensymphony.xwork2.ActionContext;

public class GetTree extends YassAction implements YassConstants {

	private String UUID;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		try {
			final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			// initialize StreamResult with File object to save to file
			ServletActionContext.getResponse().setContentType("text/xml");
			ServletActionContext.getResponse().setBufferSize(5000 * 1000);
			final StreamResult result = new StreamResult(ServletActionContext.getResponse().getOutputStream());
			ServletActionContext.getResponse().setBufferSize(5000 * 1000);
			final DOMSource source = new DOMSource((Document) ActionContext.getContext().getApplication().get(LIB_XML_TREE));
			transformer.transform(source, result);
			ServletActionContext.getResponse().flushBuffer();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			return ERROR;
		} catch (final IOException e) {
			e.printStackTrace();
			return ERROR;
		} catch (final TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * @return the keywords
	 */
	public final String getUUID() {
		return UUID;
	}

	/**
	 * @param keywords
	 *          the keywords to set
	 */
	public final void setUUID(final String UUID) {
		this.UUID = UUID;
	}
}
