/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.socket;

/**
 *
 * @author caronyn
 */
public enum MessageDelimiter {

	NONE (""),
	ZERO ("\0"),
	RETURN ("\n"),
	PIPE ("|");

	private String character;

	public String getCharacter() {
		return character;
	}

	private MessageDelimiter(String character) {
		this.character = character;
	}

}
