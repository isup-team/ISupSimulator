/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.homemade;

/**
 *
 * @author caronyn
 */
public class HMChanel extends HMVariant<Integer> {

	@Override
	public HMTypeEnum getHMType() {
		return HMTypeEnum.CHANEL;
	}

	public HMChanel() {
		super(1);
	}

	public HMChanel(Integer value) {
		super(value);
	}


}
