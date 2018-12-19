/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.modbus;

/**
 *
 * @author caronyn
 */
public enum DataBits {

	B_5(5),
	B_6(6),
	B_7(7),
	B_8(8);
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

	public static DataBits valueOf(Integer i) {
		for (DataBits b : values()) {
			if (b.getBit().equals(i)) {
				return b;
			}
		}

		throw new RuntimeException ("Value not found");
	}

	private DataBits(Integer bit) {
		this.bit = bit;
	}
}
