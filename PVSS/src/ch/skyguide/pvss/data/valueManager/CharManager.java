/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.valueManager;

/**
 *
 * @author caronyn
 */
public class CharManager extends AbstractManager<Character> {

    private static final String FORMAT = "\\%d";

    @Override
    public Character fromDpl(String string) {
	String stringCode = string;
	if (string.length() >= 1) {
	    stringCode = string.substring(1);
	}
	int code = Integer.valueOf(stringCode);

	return new Character((char) code);

    }

    @Override
    public String toDpl(Character value) {
	return String.format(FORMAT, (int)value);
    }

}
