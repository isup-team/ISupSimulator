/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml;

import ch.skyguide.pvss.network.service.socket.MessageFormater;
import ch.skyguide.pvss.network.service.socket.MessageDelimiter;

/**
 *
 * @author caronyn
 */
public class XmlMessageFormater implements MessageFormater {

	// attribute
	private String version;
	private String encoding;
	private MessageDelimiter delimiter;

	private String getHeader() {
		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version=\"");
		sb.append(version);
		sb.append("\" encoding=\"");
		sb.append(encoding);
		sb.append("\"?>");

		return sb.toString();
	}

	// methode implementation
	@Override
	public MessageDelimiter getDelimiter() {
		return delimiter;
	}

	@Override
	public String removeSpecialChar(String xml) {
		String ret = xml;
		ret = ret.replace("\n", "");
		ret = ret.replace("  ", "");
		return ret;
	}

	@Override
	public String format(String msg) {
		StringBuilder sb = new StringBuilder();

		sb.append(getHeader());
		sb.append(removeSpecialChar(msg));
		sb.append(delimiter.getCharacter());

		return sb.toString();
	}

	@Override
	public String unFormat(String msg) {
		String result = msg;

		result = result.replace(getHeader(), "");
		result = removeSpecialChar(result);

		return result;
	}

	// constructor
	public XmlMessageFormater(String version, String encoding, MessageDelimiter delimiter) {
		this.version = version;
		this.encoding = encoding;
		this.delimiter = delimiter;
	}
}
