/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.dpl;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.dataPointElement.DPElement;
import ch.skyguide.pvss.data.dataPointType.DPType;
import ch.skyguide.pvss.data.dataPointType.ElementType;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import ch.skyguide.pvss.data.dataPointType.ReferenceType;
import ch.skyguide.pvss.data.io.RWConstants;
import ch.skyguide.pvss.data.pattern.Pattern;
import ch.skyguide.pvss.data.valueManager.DataType;
import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import ch.skyguide.pvss.network.service.snmp.builder.ManagedObjectBuilder;
import ch.skyguide.pvss.network.service.xml.ISupSubSystem;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.OID;

/**
 *
 * @author caronyn
 */
public class DplUtils {

    public static final int DATA_TYPE_XML_STATUS = 1501;
    public static final int DATA_TYPE_XML_TEXT = 1502;
    public static final int DATA_DIRECTION_READ = 4;
    public static final int DATA_DIRECTION_WRITE = 1;

    private interface TerminalNodeFactory {

        DPType create(String dpName);
    }

    public static NodeType createType(Database db, String pattern) {
        if (pattern == null || "".equals(pattern)) {
            return null;
        }

        String instanceName = "";
        NodeType type = null, currentType = null;
        String[] elements = pattern.split("\\.");

        if (elements.length > 0 && db.dpTypeExists(elements[0])) {
            return (NodeType) db.getDpType(elements[0]);
        }

        for (String e : elements) {
            if (!e.equals("")) {
                if (currentType == null) {
                    type = new NodeType("sg" + e, DataType.STRUCT);
                    instanceName = e;
                    currentType = type;
                    db.addDpType(type);
                } else {
                    NodeType subType = new NodeType(e, DataType.STRUCT);
                    currentType.addChild(subType);
                    currentType = subType;
                }
            }
        }

        if (type != null) {
            db.addInstance(type, instanceName);
        }

        return type;
    }

    private static void subTypeFactory(NodeType currentType, String pattern, TerminalNodeFactory factory) {

        if (pattern == null || "".equals(pattern)) {
            return;
        }

        String[] elements = pattern.split("\\.");
        for (int i = 0; i < elements.length; i++) {
            if (!elements[i].equals("")) {
                if (i < elements.length - 1) {
                    NodeType subType = (NodeType) currentType.find(elements[i]);
                    if (subType == null) {
                        subType = new NodeType(elements[i], DataType.STRUCT);
                        currentType.addChild(subType);
                    }
                    currentType = subType;
                } else {
                    DPType subType = factory.create(elements[i]);
                    currentType.addChild(subType);
                }
            }
        }
    }

    public static NodeType xmlGetSgFwSystemType() {
        // sgFwSystem
        NodeType sgFwSystem = new NodeType("sgFwSystem", DataType.STRUCT);
        sgFwSystem.addChild(new ElementType("PreStatus", DataType.STRING));
        sgFwSystem.addChild(new ElementType("Label1", DataType.STRING));
        sgFwSystem.addChild(new ElementType("Label2", DataType.STRING));
        sgFwSystem.addChild(new ElementType("Label3", DataType.STRING));
        sgFwSystem.addChild(new ElementType("Label4", DataType.STRING));
        sgFwSystem.addChild(new ElementType("Message", DataType.STRING));
        sgFwSystem.addChild(new ElementType("CommandOut", DataType.STRING));

        return sgFwSystem;

    }

    public static void xmlCreateComponents(Iterable<ISupSubSystem> subSystems, NodeType type, final NodeType sgFwSystem) {
        NodeType currentType = (NodeType) getFirstChild(type);

        for (ISupSubSystem subSystem : subSystems) {
            subTypeFactory(currentType, subSystem.getDpName(), new TerminalNodeFactory() {

                @Override
                public DPType create(String dpName) {
                    return new ReferenceType(dpName, sgFwSystem);
                }
            });
        }
    }

    private static DPType getFirstChild(DPType type) {
        if (type instanceof NodeType && ((NodeType) type).iterator().hasNext()) {
            NodeType nodeType = (NodeType) type;

            return getFirstChild(nodeType.iterator().next());
        } else {
            return type;
            //}
        }
    }
    

    public static void snmpCreateRawData(Iterable<ManagedObjectBuilder> mobs, NodeType type) {
        NodeType currentType = (NodeType) getFirstChild(type);

        for (ManagedObjectBuilder mob : mobs) {
            subTypeFactory(currentType, mob.getDpName(), new TerminalNodeFactory() {

                @Override
                public DPType create(String dpName) {
                    return new ElementType(dpName, DataType.UINT);
                }
            });
        }
    }

    public static String stringNullToEmpty(String string) {
        if (string == null) {
            return "";
        }
        return string;
    }

    public static void xmlBuildAddress(Database db, NodeType dpType, ISupSubSystem subSystem, String elementName, String addressName, int dataType, int direction, String prefix, String systemName, String pollGroup, int driverID, int agentID, String agentName) {
        StringBuilder address = new StringBuilder();
        if (dpType == null || subSystem.getDpName() == null || "".equals(subSystem.getDpName())) {
            return;
        }

        String subSystemName = prefix + RWConstants.PATH_SEPARATOR + subSystem.getDpName() + RWConstants.PATH_SEPARATOR;
        String path = subSystemName + elementName;

        if (systemName != null && !"".equals(systemName)) {
            address.append(systemName);
        }

        String name = subSystem.getName();
        if (name != null && !"".equals(name)) {
            if (address.length() > 0) {
                address.append('.');
            }
            address.append(name);
        }
        if (addressName != null && !"".equals(addressName)) {
            if (address.length() > 0) {
                address.append('.');
            }
            address.append(addressName);
        }

        Pattern pattern = new Pattern(path);
        DP dp = pattern.findFirstDP(db.getDPRoot());
        
        DPElement dpe = (DPElement) dp;

        dpe.setDistrib(dpType, driverID);
        dpe.setAddress(dpType, dataType, agentID, direction, address.toString(), stringNullToEmpty(pollGroup), stringNullToEmpty(agentName));
    }

    public static void xmlBuildAddress(Database db, NodeType dpType, Iterable<ISupSubSystem> subSystems, String prefix, String systemName, String pollGroup, int driverID, int agentID, String agentName) {

        for (ISupSubSystem subSystem : subSystems) {
            if (subSystem.getMode().isReadable()) {
                xmlBuildAddress(db, dpType, subSystem, "PreStatus", "Status", DATA_TYPE_XML_STATUS, DATA_DIRECTION_READ, prefix, systemName, pollGroup, driverID, agentID, agentName);
                xmlBuildAddress(db, dpType, subSystem, "Label1", "Label1", DATA_TYPE_XML_TEXT, DATA_DIRECTION_READ, prefix, systemName, pollGroup, driverID, agentID, agentName);
                xmlBuildAddress(db, dpType, subSystem, "Label2", "Label2", DATA_TYPE_XML_TEXT, DATA_DIRECTION_READ, prefix, systemName, pollGroup, driverID, agentID, agentName);
                xmlBuildAddress(db, dpType, subSystem, "Label3", "Label3", DATA_TYPE_XML_TEXT, DATA_DIRECTION_READ, prefix, systemName, pollGroup, driverID, agentID, agentName);
                xmlBuildAddress(db, dpType, subSystem, "Label4", "Label4", DATA_TYPE_XML_TEXT, DATA_DIRECTION_READ, prefix, systemName, pollGroup, driverID, agentID, agentName);
                xmlBuildAddress(db, dpType, subSystem, "Message", "Message", DATA_TYPE_XML_TEXT, DATA_DIRECTION_READ, prefix, systemName, pollGroup, driverID, agentID, agentName);
            }

            if (subSystem.getMode().isWritable()) {
                xmlBuildAddress(db, dpType, subSystem, "CommandOut", null, DATA_TYPE_XML_TEXT, DATA_DIRECTION_WRITE, prefix, systemName, pollGroup, driverID, agentID, agentName);
            }
        }
    }

    public static void snmpBuildAddress(Database db, NodeType dpType, OID oidPrefix, ManagedObjectBuilder mob, String prefix, String systemName, String pollGroup, int driverID, int agentID, String agentName) {
        String path = prefix + RWConstants.PATH_SEPARATOR + mob.getDpName();
        String address = ((OID) oidPrefix.clone()).append(mob.getOID()).toString();

        SMISyntax syn = SMISyntax.valueOf(((MOScalar) mob.getObject()).getValue().getSyntax());

        DP dp = db.getDataPoint(path);
        DPElement dpe = (DPElement) dp;

        dpe.setDistrib(dpType, driverID);
        dpe.setAddress(dpType, syn.getParamDataType(), agentID, DATA_DIRECTION_READ, address, stringNullToEmpty(pollGroup), stringNullToEmpty(agentName));
    }

    public static void snmpBuildAddress(Database db, NodeType dpType, Iterable<ManagedObjectBuilder> mobs, OID oidPrefix, String prefix, String systemName, String pollGroup, int driverID, int agentID, String agentName) {

        for (ManagedObjectBuilder mob : mobs) {
            if (mob.getDpName() != null) {
                snmpBuildAddress(db, dpType, oidPrefix, mob, prefix, systemName, pollGroup, driverID, agentID, agentName);
            }
        }
    }
}
