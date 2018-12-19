/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.xml.socket;

/**
 *
 * @author caronyn
 */
public enum XmlDelimiter {

	ZERO ('\0'),
	RETURN ('\n'),
	PIPE ('|');

	private char character;

	public char getCharacter() {
		return character;
	}

	private XmlDelimiter(char character) {
		this.character = character;
	}

}
