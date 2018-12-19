/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.homemade;

/**
 *
 * @author caronyn
 */
public class HMSwitch extends HMVariant<HMSwitchEnum> {

	@Override
	public HMTypeEnum getHMType() {
		return HMTypeEnum.SWITCH;
	}

	public HMSwitch() {
		super(HMSwitchEnum.A);
	}

	public HMSwitch(HMSwitchEnum value) {
		super(value);
	}


}
