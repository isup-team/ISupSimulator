/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.io.parser;

/**
 *
 * @author CyaNn
 */
public class AliasParser extends LineParser {

    private static final char[] DELIMITERS = {SEPARATOR_CHAR};
    private String aliasId;
    private String aliasName;
    private String commentName;

    public String getAliasId() {
	return aliasId;
    }

    public String getAliasName() {
	return aliasName;
    }

    public String getCommentName() {
	return commentName;
    }

    @Override
    protected char[] getDelimiters() {
	return DELIMITERS;
    }

    @Override
    protected void assignValue(int number, String value) {
	switch (number) {
	    case 0 :
		aliasId = value;
		break;
	    case 1 :
		aliasName = value;
		break;
	    case 2 :
		commentName = subString(value, STRING_QUOTE, ALIAS_SPECIAL_CHAR);
		break;
	}
    }

    public AliasParser(String line) {
	super(line);
    }
}
