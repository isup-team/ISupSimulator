/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.modbus;

import net.wimpi.modbus.procimg.DigitalOut;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.IllegalAddressException;

/**
 *
 * @author CARONYN
 */
public interface ProcessImageContainer {

	public DigitalOut getDigitalOut(int ref) throws IllegalAddressException;
	public Register getRegister(int ref) throws IllegalAddressException;

}
