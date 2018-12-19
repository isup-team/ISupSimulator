/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.modbus;

/**
 *
 * @author caronyn
 */
public enum StopBits {

	B_1(1),
	B_2(2);
	Integer bit;

	public Integer getBit() {
		return bit;
	}

	public static Integer[] bitsValues() {
		Integer[] bits = new Integer[values().length];

		for (int i = 0; i < values().length; i++) {
			bits[i] = values()[i].bit;
		}

		return bits;
	}

	public static StopBits valueOf(Integer i) {
		for (StopBits b : values()) {
			if (b.getBit().equals(i)) {
				return b;
			}
		}

		throw new RuntimeException("Value not found");
	}

	private StopBits(Integer bit) {
		this.bit = bit;
	}
}
