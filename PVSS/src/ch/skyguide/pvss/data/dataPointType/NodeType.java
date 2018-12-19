/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.dataPointType;

import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.dataPointElement.DPNode;
import ch.skyguide.pvss.data.dataPointElement.Instance;
import ch.skyguide.pvss.data.event.ChildChangedEvent;
import ch.skyguide.pvss.data.valueManager.DataType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author CyaNn
 */
public class NodeType extends DPType implements Iterable<DPType> {

    private DataType dataType;

    private Set<DPType> children = new HashSet<DPType>();

    public DataType getDataType() {
	return dataType;
    }

    public void setDataType(DataType dataType) {
	this.dataType = dataType;
    }

    public void addChild(DPType child) {
	children.add(child);
	invokeChildAdded(new ChildChangedEvent(this, child));
    }

    public void addAll(Collection<DPType> dpTypes) {
	for (DPType dpType : dpTypes) {
	    addChild(dpType);
	}
    }

    public void removeChild(DPType child) {
	children.remove(child);
	invokeChildRemoved(new ChildChangedEvent(this, child));
    }

    public boolean contains(DPType child) {
	return children.contains(child);
    }
    
    public DPType find(String name) {
        for (DPType child : children) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    public DP createInstance(String name) {
	DPNode node = new Instance(name, this);
	addNameChangedListener(node);
	createChildrenDataPoint(node);
	return node;
    }

    @Override
    public DP createDataPoint(String name) {
	DPNode node = new DPNode(name, dataType);
	addNameChangedListener(node);
	createChildrenDataPoint(node);
	return node;
    }

    private void createChildrenDataPoint(DPNode node) {
	for (DPType child : children) {
	    node.addChild(child.createDataPoint(child.getName()));
	}
	addChildChangedListener(node);
    }

    public NodeType(String name, DataType dataType) {
	super(name);
	this.dataType = dataType;
    }

    @Override
    public void cover(DPTypeCoverVisitor visitor, int level) {
	visitor.NodeTypeCover(this, level);

	for (DPType child : children) {
	    child.cover(visitor, level + 1);
	}
    }

    @Override
    public Iterator<DPType> iterator() {
        return children.iterator();
    }

}
