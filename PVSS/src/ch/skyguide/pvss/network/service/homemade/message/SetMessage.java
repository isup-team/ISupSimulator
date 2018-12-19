/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.homemade.message;

/**
 *
 * @author caronyn
 */
public class SetMessage extends Message {

	private String name;
	private String value;

	@Override
	protected void appendMessageContent(StringBuilder sb) {
		sb.append("SET ");
		sb.append(name);
		sb.append(Message.SEPARATOR);
		sb.append(value.replace(Message.SEPARATOR, Message.SEPARATOR_REPLACE));
		// TODO à modifier une fois le message REGISTER traité
		//sb.append(" change");
		sb.append(Message.END);
	}

	public SetMessage(String name, String value) {
		this.name = name;
		this.value = value;
	}
}
