/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.snmp;

import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOAccessImpl;

/**
 *
 * @author caronyn
 */
public enum SMIAccess {
	READ_ONLY (MOAccessImpl.ACCESS_READ_CREATE),
	READ_CREATE (MOAccessImpl.ACCESS_READ_CREATE),
	NOTIFY (MOAccessImpl.ACCESS_FOR_NOTIFY),
	READ_WRITE (MOAccessImpl.ACCESS_READ_WRITE),
	WRITE_ONLY (MOAccessImpl.ACCESS_WRITE_ONLY);

	private final MOAccess impl;

	// property
	public MOAccess getMOAccess() {
	    return impl;
	}

	public static SMIAccess valueOf(MOAccess moAccess) {
	    for (SMIAccess a : SMIAccess.values()) {
		if (a.getMOAccess().equals(moAccess)) {
		    return a;
		}
	    }
	    return NOTIFY;
	}

	// constructor
	SMIAccess (MOAccess impl) {
	    this.impl = impl;
	}

    }
