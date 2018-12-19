/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.io.parser;

import ch.skyguide.pvss.data.pattern.PatternParser;
import ch.skyguide.pvss.data.pattern.Pattern;


/**
 *
 * @author caronyn
 */
public class TypeParser extends LineParser {

    private static final char[] DELIMITERS = {SEPARATOR_CHAR, ID_SEPARATOR_CHAR, REF_SEPARATOR_CHAR};
    private String typeName;
    private int pvssTypeId;
    private int id;
    private String referenceName;

    @Override
    protected char[] getDelimiters() {
	return DELIMITERS;
    }

    public int getId() {
	return id;
    }

    public String getTypeName() {
	return typeName;
    }

    public int getDataTypeId() {
	return pvssTypeId;
    }

    public String getReferenceName() {
	return referenceName;
    }

    @Override
    protected void assignValue(int number, String value) {
	switch (number) {
	    case 0 :
		this.typeName = subStringTo(value, PATH_SEPARATOR);
		break;
	    case 1 :
		this.pvssTypeId = Integer.valueOf(value);
		break;
	    case 2 :
		this.id = Integer.valueOf(value);
		break;
	    case 3 :
		this.referenceName = value;
		break;
	}
    }

    public TypeParser(String line) {
	super(line);
    }

}
