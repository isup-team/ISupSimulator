/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml.socket;

import ch.skyguide.pvss.network.persistency.xstreamConverter.ServiceXStreamFormater;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 *
 * @author caronyn
 */
public class XmlMessageFormater {

	// attribute
	private String version;
	private String encoding;
	private ServiceXStreamFormater xstreamFormater;
	private XStream xstream;
	private XmlDelimiter delimiter;

	public XStream getXstream() {
		return xstream;
	}

	public XmlDelimiter getDelimiter() {
		return delimiter;
	}

	// function
	public String removeSpecialChar(String xml) {
		String ret = xml;
		ret = ret.replace("\n", "");
		ret = ret.replace("  ", "");
		return ret;
	}

	private String getHeader() {
		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version=\"");
		sb.append(version);
		sb.append("\" encoding=\"");
		sb.append(encoding);
		sb.append("\"?>");

		return sb.toString();
	}

	// methodes
	public String format(String msg) {
		StringBuilder sb = new StringBuilder();

		sb.append(getHeader());
		sb.append(removeSpecialChar(msg));
		sb.append(delimiter.getCharacter());

		return sb.toString();
	}

	public String unFormat(String msg) {
		String result = msg;

		result = result.replace(getHeader(), "");
		result = removeSpecialChar(result);

		return result;
	}

	// constructor
	public XmlMessageFormater(String version, String encoding, XmlDelimiter delimiter, ServiceXStreamFormater formater) {
		this.version = version;
		this.encoding = encoding;
		this.xstreamFormater = formater;
		this.delimiter = delimiter;

		xstream = new XStream(new DomDriver(encoding));
		formater.configureXml(xstream);
	}
}
