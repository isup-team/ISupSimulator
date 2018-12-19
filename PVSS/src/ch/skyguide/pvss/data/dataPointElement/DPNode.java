package ch.skyguide.pvss.data.dataPointElement;

import ch.skyguide.pvss.data.event.ChildChangedEvent;
import ch.skyguide.pvss.data.event.ChildChangedListener;
import ch.skyguide.pvss.data.event.NameChangedEvent;
import ch.skyguide.pvss.data.valueManager.DataType;
import java.util.Vector;

public class DPNode extends DP implements ChildChangedListener {

    // variable
    private DataType dataType;
    Vector<DP> children = new Vector<DP>();

    // accessors
    @Override
    public DataType getDataType() {
	return dataType;
    }

    public void addChild(DP child) {
        child.setParent(this);
        children.add(child);
    }

    public void removeChild(DP child) {
        child.setParent(null);
        children.remove(child);
    }

    public final Vector<DP> getChildren() {
	return children;
    }

    // constructor
    public DPNode(String name, DataType dataType) {
        super(name);
	this.dataType = dataType;
    }

    // methodes
    private DP find(String name) {
        for (DP child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    @Override
    protected void cover(DPCoverVisitor visitor, int level) {
        visitor.NodeCover(this, level);
        coverChildren(visitor, level);
        coverCommon(visitor, level);
    }

    protected void coverChildren(DPCoverVisitor visitor, int level) {
        for (DP child : children) {
            child.cover(visitor, level + 1);
        }
    }

    // event handling
    public void NameChanged(NameChangedEvent event) {
        name = event.getSource().getName();
    }

    public void ChildAdded(ChildChangedEvent event) {
        addChild(event.getChild().createDataPoint(event.getChild().getName()));
    }

    public void ChildRemoved(ChildChangedEvent event) {
        DP child = find(event.getChild().getName());
        removeChild(child);
    }

    
}
