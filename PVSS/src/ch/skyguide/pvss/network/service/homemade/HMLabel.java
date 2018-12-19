/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.homemade;

/**
 *
 * @author caronyn
 */
public class HMLabel extends HMVariant<String> {

	@Override
	public HMTypeEnum getHMType() {
		return HMTypeEnum.LABEL;
	}

	HMLabel() {
		super("");
	}

	public HMLabel(String value) {
		super(value);
	}
}
