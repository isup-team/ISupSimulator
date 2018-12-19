/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.homemade.message;

/**
 *
 * @author caronyn
 */
public abstract class Message {

	public static final char END = '\n';
	public static final char SEPARATOR = ' ';
	public static final char SEPARATOR_REPLACE = '_';

	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		return getMessage(sb);
	}

	public String getMessage(StringBuilder sb) {
		appendMessageContent(sb);
		return sb.toString();
	}

	protected abstract void appendMessageContent(StringBuilder sb);
}
