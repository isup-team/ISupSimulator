/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.pattern;

import java.util.Vector;

/**
 *
 * @author CyaNn
 */
public enum Delimiter {

    DOT("."),
    WILDCARD("_"),
    TWICESTAR("**"),
    STAR("*"),
    BRACKET("{"),
    ENDBRACKET("}"),
    COMA(","),
    SQUAREBRACKET("["),
    ENDSQUAREBRACKET("]");
    
    private String code;

    private Delimiter(String code) {
	this.code = code;
    }

    public String getCode() {
	return code;
    }

    public static String[] getCodes() {
	String[] ret = new String[values().length];
	int i = 0;

	for (Delimiter delimiter : values()) {
	    ret[i++] = delimiter.code;
	}

	return ret;
    }

    public static Delimiter valueOfCode(String string) {
	for (Delimiter delimiter : values()) {
	    if (delimiter.getCode().equals(string)) {
		return delimiter;
	    }
	}

	return null;
    }
}

