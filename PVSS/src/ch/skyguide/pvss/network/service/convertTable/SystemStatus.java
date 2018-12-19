/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.convertTable;

import java.awt.Color;

/**
 *
 * @author caronyn
 */
public enum SystemStatus {

	OPS(Color.GREEN),
	SBY(Color.WHITE),
	INI(Color.DARK_GRAY),
	DEG(Color.YELLOW),
	U_S(Color.RED),
	WIP(Color.BLUE),
	UKN(Color.CYAN),
	TEC(Color.PINK),
	ABS(Color.GRAY),
	STP(Color.BLUE);

	private Color color;

	public Color getColor() {
		return color;
	}

	private SystemStatus(Color color) {
		this.color = color;

	}
}
