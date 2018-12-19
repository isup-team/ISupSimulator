/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.dataPointType;

import ch.skyguide.pvss.data.valueManager.DataType;
import java.util.HashMap;

/**
 *
 * @author caronyn
 */
public class DPTypeMap extends HashMap<String, DPType> {

    // TODO : Reflechir à une autre solution
    // Flyweight pattern
    @Override
    public DPType get(Object key) {
	String name = (String) key;
	if (!super.containsKey(name)) {
	    super.put(name, new NodeType(name, DataType.STRUCT));
	}
	return super.get(name);
    }

}
