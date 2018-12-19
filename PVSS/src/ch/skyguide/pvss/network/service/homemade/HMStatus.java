/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.homemade;

/**
 *
 * @author caronyn
 */
public class HMStatus extends HMVariant<HMStatusEnum> {

	// methode implementation
	@Override
	public HMTypeEnum getHMType() {
		return HMTypeEnum.STATUS;
	}

	// constructor
	public HMStatus() {
		super(HMStatusEnum.OPS);
	}

	public HMStatus(HMStatusEnum value) {
		super(value);
	}
}
