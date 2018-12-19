/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.modbus;

import net.wimpi.modbus.procimg.DigitalIn;
import net.wimpi.modbus.procimg.DigitalOut;
import net.wimpi.modbus.procimg.IllegalAddressException;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.procimg.ProcessImage;
import net.wimpi.modbus.procimg.Register;

/**
 *
 * @author CARONYN
 */
public class DynamicProcessImage implements ProcessImage {

	// constant
	public static final int COIL_NUMBER = 512;
	public static final int REGISTER_NUMBER = 32;

	// attribute
	private ProcessImageContainer container;

	@Override
	public DigitalOut[] getDigitalOutRange(int offset, int count) throws IllegalAddressException {
		DigitalOut[] outs = new DigitalOut[count];

		for (int i = 0; i < count; i++) {
			outs[i] = getDigitalOut(i + offset);
		}

		return outs;
	}

	@Override
	public DigitalOut getDigitalOut(int ref) throws IllegalAddressException {
		return container.getDigitalOut(ref);
	}

	@Override
	public int getDigitalOutCount() {
		return COIL_NUMBER;
	}

	@Override
	public Register[] getRegisterRange(int offset, int count) throws IllegalAddressException {
		Register[] regs = new Register[count];

		for (int i = 0; i < count; i++) {
			regs[i] = getRegister(i + offset);
		}

		return regs;	}

	@Override
	public Register getRegister(int ref) throws IllegalAddressException {
		return container.getRegister(ref);
	}

	@Override
	public int getRegisterCount() {
		return REGISTER_NUMBER;
	}

	@Override
	public DigitalIn[] getDigitalInRange(int offset, int count) throws IllegalAddressException {
		// TODO accepter les inputs
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public DigitalIn getDigitalIn(int ref) throws IllegalAddressException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getDigitalInCount() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public InputRegister[] getInputRegisterRange(int offset, int count) throws IllegalAddressException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public InputRegister getInputRegister(int ref) throws IllegalAddressException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getInputRegisterCount() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	// contructor
	public DynamicProcessImage(ProcessImageContainer container) {
		this.container = container;
	}

}
