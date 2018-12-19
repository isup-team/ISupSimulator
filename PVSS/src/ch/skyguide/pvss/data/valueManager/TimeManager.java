/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.valueManager;

import ch.skyguide.pvss.data.PVSSException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author caronyn
 */
public class TimeManager extends AbstractManager<Date> {

    public static final String DPL_FORMAT = "dd.MM.yyyy HH:mm:ss.SSS";

    @Override
    public Date fromDpl(String string) {
	try {
	    DateFormat format = new SimpleDateFormat(DPL_FORMAT);
	    return format.parse(string);
	} catch (Exception ex) {
	    throw new PVSSException(String.format("Dpl String [%s] is not a valid PVSS Time", string));
	}
    }

    @Override
    public String toDpl(Date value) {
	DateFormat format = new SimpleDateFormat(DPL_FORMAT);
	return format.format(value);
    }

}
