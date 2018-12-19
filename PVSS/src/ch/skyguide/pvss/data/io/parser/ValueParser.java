/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.io.parser;

import ch.skyguide.pvss.data.PVSSException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author CyaNn
 */
public class ValueParser extends LineParser {

    private static final char[] DELIMITERS = {SEPARATOR_CHAR};
    private String elementName;
    private String typeName;
    private String original_value;
    private int original_status;
    private Date original_stime;

    public String getElementName() {
	return elementName;
    }

    public String getTypeName() {
	return typeName;
    }

    public int getOriginal_status() {
	return original_status;
    }

    public Date getOriginal_stime() {
	return original_stime;
    }

    public String getOriginal_value() {
	return original_value;
    }

    @Override
    protected char[] getDelimiters() {
	return DELIMITERS;
    }

    @Override
    protected void assignValue(int number, String value) {
	switch (number) {
	    case 0:
		elementName = value;
		break;
	    case 1:
		typeName = value;
		break;
	    case 2:
		original_value = value;
		break;
	    case 3:
		original_status = Integer.valueOf(value.replace(HEXA_PREFIX, ""));
		break;
	    case 4:
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		try {
		    original_stime = format.parse(value);
		} catch (ParseException ex) {
		    throw new PVSSException(String.format("Bad date format, [%s] does not parse [%s]", DATE_FORMAT, value));
		}
		break;
	}
    }

    public ValueParser(String line) {
	super(line);
    }
}
