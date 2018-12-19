/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.socket;

/**
 *
 * @author caronyn
 */
public interface MessageFormater {

	MessageDelimiter getDelimiter();
	String removeSpecialChar(String msg);
	String format(String msg);
	String unFormat(String msg);

}
