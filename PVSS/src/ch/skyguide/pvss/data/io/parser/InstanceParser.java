/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.io.parser;

/**
 *
 * @author CyaNn
 */
public class InstanceParser extends LineParser {

    private static final char[] DELIMITERS = {SEPARATOR_CHAR};
    private String dpName;
    private String typeName;
    private int id;

    public String getDpName() {
	return dpName;
    }

    public String getTypeName() {
	return typeName;
    }

    public int getId() {
	return id;
    }

    @Override
    protected char[] getDelimiters() {
	return DELIMITERS;
    }

    @Override
    protected void assignValue(int number, String value) {
	switch (number) {
	    case 0:
		dpName = value;
		break;
	    case 1:
		typeName = value;
		break;
	    case 2:
		id = Integer.valueOf(value);
		break;
	}
    }

    public InstanceParser(String line) {
	super(line);
    }
}
