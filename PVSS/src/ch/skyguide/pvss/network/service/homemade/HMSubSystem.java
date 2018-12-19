/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.homemade;

/**
 *
 * @author caronyn
 */
public class HMSubSystem {

	// attributes
	private String name;
	private HMVariant value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HMVariant getValue() {
		return value;
	}

	public void setValue(HMVariant value) {
		this.value = value;
	}

	// constructor

	private HMSubSystem() {
	}
	
	public HMSubSystem(String name, HMVariant value) {
		this.name = name;
		this.value = value;
	}

}
