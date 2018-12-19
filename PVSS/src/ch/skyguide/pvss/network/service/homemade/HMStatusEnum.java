/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.homemade;

import ch.skyguide.pvss.network.service.convertTable.SystemStatus;

/**
 *
 * @author caronyn
 */
public enum HMStatusEnum {

	OPS(SystemStatus.OPS),
	STDB(SystemStatus.SBY),
	INIT(SystemStatus.INI),
	STOPPED(SystemStatus.STP),
	DEGRADED(SystemStatus.DEG),
	FAILED(SystemStatus.U_S),
	ABSENT(SystemStatus.ABS),
	unknown(SystemStatus.UKN);
	private SystemStatus iSupStatus;

	public SystemStatus getISupStatus() {
		return iSupStatus;
	}

	private HMStatusEnum(SystemStatus iSupStatus) {
		this.iSupStatus = iSupStatus;
	}
}
