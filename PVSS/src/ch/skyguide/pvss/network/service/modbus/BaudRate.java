/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.modbus;

/**
 *
 * @author caronyn
 */
public enum BaudRate {

	BD_50(50),
	BD_75(75),
	BD_110(110),
	BD_134(134),
	BR_150(150),
	BR_300(300),
	BR_600(600),
	BR_1200(1200),
	BR_1800(1800),
	BR_2400(2400),
	BR_4800(4800),
	BR_7200(7200),
	BR_9600(9600),
	BR_19200(19200),
	BR_38400(38400),
	BR_57600(57600),
	BR_115200(115200),
	BR_230400(230400),
	BR_460800(460800),
	BR_921600(921600);
	private int rate;

	public Integer getRate() {
		return rate;
	}

	public static Integer[] rateValues() {
		Integer[] rates = new Integer[values().length];

		for (int i = 0; i < values().length; i++) {
			rates[i] = values()[i].rate;
		}

		return rates;
	}

	public static BaudRate valueOf(Integer i) {
		for (BaudRate b : values()) {
			if (b.getRate().equals(i)) {
				return b;
			}
		}
		System.out.println("Not found");
		throw new RuntimeException("Value not found");
	}

	private BaudRate(Integer rate) {
		this.rate = rate;
	}
}
