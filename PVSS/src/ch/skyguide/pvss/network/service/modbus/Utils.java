/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.modbus;

import com.sun.comm.Win32Driver;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.comm.CommPortIdentifier;

/**
 *
 * @author caronyn
 */
public class Utils {

	public static String[] getComPorts() {
		Win32Driver w32Driver = new Win32Driver();
		w32Driver.initialize();
		//récupération de l'énumération
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();

		List<String> ports = new ArrayList<String>();

		while (portList.hasMoreElements()) {
			String name = ((CommPortIdentifier)portList.nextElement()).getName();
			if (!ports.contains(name) &&  !name.substring(0, 3).equals("LPT")) {
				ports.add(name);
			}
			
		}

		return ports.toArray(new String[ports.size()]);
	}
}
