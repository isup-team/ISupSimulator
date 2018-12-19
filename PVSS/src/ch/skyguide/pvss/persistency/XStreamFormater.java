/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.persistency;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author caronyn
 */
public abstract class XStreamFormater {

	// decorator pattern
	private XStreamFormater formater;

	public void configureXml(XStream xstream) {
		configure(xstream);
		if (formater != null) {
			formater.configure(xstream);
		}
	}

	protected abstract void configure(XStream xstream);

	public XStreamFormater(XStreamFormater formater) {
		this.formater = formater;
	}

	public XStreamFormater() {
	}

}
