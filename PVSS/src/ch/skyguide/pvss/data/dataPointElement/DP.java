package ch.skyguide.pvss.data.dataPointElement;

import ch.skyguide.pvss.data.PVSSException;
import ch.skyguide.pvss.data.dataPointType.DPType;
import ch.skyguide.pvss.data.event.NameChangedListener;
import ch.skyguide.pvss.data.pattern.Path;
import ch.skyguide.pvss.data.pattern.item.DotItem;
import ch.skyguide.pvss.data.pattern.item.Item;
import ch.skyguide.pvss.data.pattern.item.LiteralItem;
import ch.skyguide.pvss.data.pattern.item.NumberItem;
import ch.skyguide.pvss.data.valueManager.DataType;

public abstract class DP implements NameChangedListener {

    protected DP parent = null;
    protected String name;
    protected ConfigCommon common = null;

    public Path getPath() {
        return new Path(getItem(null));
    }

    protected Item getItem(Item next) {
        Item item;

        if (hasParent() && !parent.getDataType().equals(DataType.STRUCT)) {
            item = new NumberItem(((DPNode) parent).getChildren().indexOf(this));
        } else {
            item = new LiteralItem(name);
        }

        if (next != null) {
            if (!(next instanceof NumberItem)) {
                Item dot = new DotItem();
                dot.setNext(next);
                item.setNext(dot);
            } else {
                item.setNext(next);
            }
        }

        if (hasParent()) {
            // recursive
            item = parent.getItem(item);
        }

        return item;

    }

    public Instance getInstance() {
        if (hasParent()) {
            return parent.getInstance();
        }
        throw new PVSSException(String.format("Datapoint [%s] has no instance", name));
    }

    public abstract DataType getDataType();

    public void cover(DPCoverVisitor visitor) {
        cover(visitor, 0);
    }

    protected void coverCommon(DPCoverVisitor visitor, int level) {
        if (common != null) {
            common.cover(visitor, level);
        }
    }

    protected abstract void cover(DPCoverVisitor visitor, int level);

    public String getName() {
        return name;
    }

    public boolean hasCommon() {
        return (common != null);
    }

    public ConfigCommon getCommonInstance() {
        if (!hasCommon()) {
            common = new ConfigCommon(this);
        }
        return common;
    }

    public Boolean hasParent() {
        return (parent != null);
    }

    void setParent(DP parent) {
        this.parent = parent;
    }

    public DP getParent() {
        return parent;
    }

    public DP(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
