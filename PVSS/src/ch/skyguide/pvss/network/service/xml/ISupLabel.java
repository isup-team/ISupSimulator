/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.xml;

/**
 *
 * @author caronyn
 */
public class ISupLabel {

	private String name;
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private ISupLabel() {
	}
	
	public ISupLabel(String name, String value) {
		this.name = name;
		this.value = value;
	}

}
