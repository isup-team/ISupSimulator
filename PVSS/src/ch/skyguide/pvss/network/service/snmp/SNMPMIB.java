/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp;

import ch.skyguide.pvss.network.persistency.dpl.DPExportable;
import ch.skyguide.pvss.network.service.snmp.builder.ManagedObjectBuilder;
import ch.skyguide.pvss.network.service.convertTable.ConvertTableListable;
import ch.skyguide.pvss.network.service.snmp.builder.ColumnBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.ScalarBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.TableBuilder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

/**
 *
 * @author caronyn
 */
public class SNMPMIB implements MOGroup, BuilderContainer, ConvertTableListable<SNMPConvertTable>, DPExportable {

    // attributes
    private String name;
    private OID prefixOID;
    private int driverID;
    private String driverName;
    private String pollGroup;
    private int agentID;
    private String dpPrefix;
    private List<SNMPConvertTable> convertTables;
    private List<ManagedObjectBuilder> builders;

    // properties
    @Override
    public void addConvertTable(SNMPConvertTable e) {
        if (convertTables == null) {
            convertTables = new ArrayList<SNMPConvertTable>();
        }
        convertTables.add(e);
    }

    @Override
    public void removeConvertTable(SNMPConvertTable e) {
        // integrity check
        for (ManagedObjectBuilder b : builders) {
            if (b instanceof ScalarBuilder) {
                ScalarBuilder sb = (ScalarBuilder) b;
                if (e.equals(sb.getConvertTable())) {
                    sb.removeConvertTable();
                }
            }
            if (b instanceof TableBuilder) {
                TableBuilder tb = (TableBuilder) b;
                for (ColumnBuilder cb : tb.getColumns()) {
                    if (e.equals(cb.getConvertTable())) {
                        cb.removeConvertTable();
                    }
                }
            }
        }

        convertTables.remove(e);
    }

    @Override
    public List<SNMPConvertTable> getConvertTables() {
        if (convertTables == null) {
            convertTables = new ArrayList<SNMPConvertTable>();
        }
        return convertTables;
    }

    @Override
    public SNMPConvertTable getConvertTable(String name) {
        for (SNMPConvertTable table : convertTables) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    // serialization
    private Object readResolve() {
        initialize();
        return this;
    }

    // function
    private void initialize() {
        if (builders == null) {
            builders = new LinkedList<ManagedObjectBuilder>();
        }
    }

    // properties
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public OID getPrefixOID() {
        return prefixOID;
    }

    public void setPrefixOID(OID value) {
        prefixOID = value;
    }

    @Override
    public int getAgentID() {
        return agentID;
    }

    @Override
    public void setAgentID(int agentID) {
        this.agentID = agentID;
    }

    @Override
    public String getDpPrefix() {
        return dpPrefix;
    }

    @Override
    public void setDpPrefix(String dpPrefix) {
        this.dpPrefix = dpPrefix;
    }

    @Override
    public int getDriverID() {
        return driverID;
    }

    @Override
    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }
    
    @Override
    public String getDriverName() {
        return driverName;
    }

    @Override
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    
    @Override
    public String getPollGroup() {
        return pollGroup;
    }

    @Override
    public void setPollGroup(String pollGroup) {
        this.pollGroup = pollGroup;
    }
    
    @Override
    public ManagedObjectBuilder getBuilder(int i) {
        return builders.get(i);
    }

    // methodes
    @Override
    public void addBuilder(ManagedObjectBuilder builder) {
        builder.setParentMib(this);
        builders.add(builder);
    }

    @Override
    public void removeBuilder(ManagedObjectBuilder builder) {
        builder.setParentMib(null);
        builders.remove(builder);
    }

    @Override
    public List<ManagedObjectBuilder> getBuilders() {
        return builders;
    }

    @Override
    public void registerMOs(MOServer server, OctetString context) throws DuplicateRegistrationException {

        for (ManagedObjectBuilder builder : builders) {
            server.register(builder.getObject(), context);
        }
    }

    @Override
    public void unregisterMOs(MOServer server, OctetString context) {
        for (ManagedObjectBuilder builder : builders) {
            server.unregister(builder.getObject(), context);
        }
    }

    // constructor
    private SNMPMIB() {
        initialize();
    }

    public SNMPMIB(String name, OID enterpriseOID) {
        this.name = name;
        this.prefixOID = enterpriseOID;
        initialize();
    }

}
