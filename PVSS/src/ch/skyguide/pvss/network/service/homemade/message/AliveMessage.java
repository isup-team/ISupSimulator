/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.homemade.message;

/**
 *
 * @author caronyn
 */
public class AliveMessage extends Message{

	@Override
	protected void appendMessageContent(StringBuilder sb) {
		sb.append("ALIVE");
		sb.append(Message.END);
	}

}
