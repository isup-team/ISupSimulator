package ch.skyguide.pvss.data;

import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.dataPointElement.Root;
import ch.skyguide.pvss.data.dataPointType.DPType;
import ch.skyguide.pvss.data.dataPointType.DPTypeMap;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import ch.skyguide.pvss.data.pattern.Pattern;
import java.util.Collection;

public class Database {

    private DPTypeMap dpTypes = new DPTypeMap();
    private Root root = new Root();

    public DPType getDpType(String name) {
	return dpTypes.get(name);
    }
    
    public boolean dpTypeExists(String name) {
        return dpTypes.containsKey(name);
    }

    public Collection<DPType> getDpTypes() {
	return dpTypes.values();
    }

    public Root getDPRoot() {
	return root;
    }

    public void addDpType(DPType dpType) {
	dpTypes.put(dpType.getName(), dpType);
    }

    public void addInstance(NodeType dpNode, String name) {
	root.addChild(dpNode.createInstance(name));
    }

    public DP getDataPoint(Pattern pattern) {
	return pattern.findDP(root);
    }

    public DP getDataPoint(String path) {
	return getDataPoint(new Pattern(path));
    }

}
