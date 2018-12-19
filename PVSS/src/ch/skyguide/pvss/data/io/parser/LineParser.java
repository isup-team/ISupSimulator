/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.io.parser;

import ch.skyguide.pvss.data.io.RWConstants;
import java.util.StringTokenizer;

/**
 *
 * @author caronyn
 */
public abstract class LineParser implements RWConstants {

    protected abstract char[] getDelimiters();

    private static final int getBeginPos(String string, char delimCharFrom) {
	int from = string.indexOf(delimCharFrom);
	if (from < -1) {
	    return 0;
	} else {
	    return from + 1;
	}
    }

    private static final int getEndPos(String string, char delimCharTo, int from) {
	int to = string.indexOf(delimCharTo, from);
	if (to == -1) {
	    return string.length();
	} else {
	    return to;
	}
    }

    public static final String subString(String string, char delimCharFrom) {
	return string.substring(getBeginPos(string, delimCharFrom));
    }

    public static final String subString(String string, char delimCharFrom, char delimCharTo) {
	int from = getBeginPos(string, delimCharFrom);
	return string.substring(from, getEndPos(string, delimCharTo, from));
    }

    public static final String subStringTo(String string, char delimCharTo) {
	return string.substring(0, getEndPos(string, delimCharTo, 0));
    }

    public LineParser(String line) {
	StringTokenizer tokenizer = new StringTokenizer(line, new String(getDelimiters()));
	int i = 0;
	while (tokenizer.hasMoreTokens()) {
	    String token = tokenizer.nextToken();
	    assignValue(i, token);
	    i++;
	}
    }

    protected abstract void assignValue(int number, String value);
}
