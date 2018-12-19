/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.dataPointElement;

import ch.skyguide.pvss.data.pattern.Path;
import ch.skyguide.pvss.data.pattern.item.Item;
import ch.skyguide.pvss.data.valueManager.DataType;

/**
 *
 * @author CyaNn
 */
public class Root extends DPNode {

    private static final String DEFAULT_NAME = "root";

    @Override
    public Path getPath() {
	return null;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Root() {
	super(DEFAULT_NAME, DataType.STRUCT);
    }

    @Override
    protected Item getItem(Item next) {
	return next;
    }

    @Override
    protected void cover(DPCoverVisitor visitor, int level) {
	visitor.RootCover(this, level);
	coverChildren(visitor, level);
    }

}
