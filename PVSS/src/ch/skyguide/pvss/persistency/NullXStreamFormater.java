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
public class NullXStreamFormater extends XStreamFormater {

	@Override
	protected void configure(XStream xstream) {
		// do nothing
	}
}
