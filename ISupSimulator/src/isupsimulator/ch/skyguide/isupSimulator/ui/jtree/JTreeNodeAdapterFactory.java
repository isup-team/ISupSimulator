/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.Service;
import ch.skyguide.pvss.network.service.ServiceComposite;
import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import ch.skyguide.pvss.network.service.homemade.HomemadeService;
import ch.skyguide.pvss.network.service.literal.LiteralClient;
import ch.skyguide.pvss.network.service.modbus.WagoConvertTable;
import ch.skyguide.pvss.network.service.modbus.WagoService;
import ch.skyguide.pvss.network.service.snmp.SMIAccess;
import ch.skyguide.pvss.network.service.snmp.SNMPAgentService;
import ch.skyguide.pvss.network.service.snmp.SNMPConvertTable;
import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import ch.skyguide.pvss.network.service.snmp.SNMPTrapService;
import ch.skyguide.pvss.network.service.snmp.builder.ManagedObjectBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.ScalarBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.TableBuilder;
import ch.skyguide.pvss.network.service.socket.MessageDelimiter;
import ch.skyguide.pvss.network.service.xml.ISupSystem;
import ch.skyguide.pvss.network.service.xml.XmlClient;
import ch.skyguide.pvss.network.service.xml.XmlClientService;
import ch.skyguide.pvss.network.service.xml.XmlServerService;
import javax.swing.tree.DefaultMutableTreeNode;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

interface TreeAdapterFactory<T> {

    boolean canAdapt(Class type);

    T getNewInstance();

    JTreeNodeAdapterFactory[] getPossibleChildren();

    // TODO refactorer : mettre dans le treenode avec interface EditableTreeNode
    Object addChildTo(T object, JTreeNodeAdapterFactory factory);

    // TODO refactorer : mettre dans le treenode avec interface EditableTreeNode
    void removeChildTo(T object, Object target);

    DefaultMutableTreeNode getTreeNodeInstance(T object);
}

class CompositeTreeAdapterFactory implements TreeAdapterFactory<Service> {

    public boolean canAdapt(Class type) {
        return ServiceComposite.class.equals(type);
    }

    public Service getNewInstance() {
        return new ServiceComposite("New Node");
    }

    public DefaultMutableTreeNode getTreeNodeInstance(Service service) {
        DefaultMutableTreeNode adapter = new CompositeServiceAdapter(service);

        ServiceComposite<Service> cmp = (ServiceComposite<Service>) service;
        for (Service s : cmp) {
            // recursive composite
            adapter.add(JTreeNodeAdapterFactory.valueOf(s).getTreeNodeInstance(s));
        }

        return adapter;
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        JTreeNodeAdapterFactory[] factories = {
            JTreeNodeAdapterFactory.NODE,
            JTreeNodeAdapterFactory.LITERAL_CLIENT,
            JTreeNodeAdapterFactory.SNMP_AGENT,
            JTreeNodeAdapterFactory.SNMP_TRAP,
            JTreeNodeAdapterFactory.WAGO,
            JTreeNodeAdapterFactory.XML_CLIENT,
            JTreeNodeAdapterFactory.XML_SERVER,
            JTreeNodeAdapterFactory.HOMEMADE};
        return factories;
    }

    public Object addChildTo(Service service, JTreeNodeAdapterFactory factory) {
        ServiceComposite sc = (ServiceComposite) service;
        Object newInstance = factory.getNewInstance();
        sc.addService((Service) newInstance);
        return newInstance;
    }

    public void removeChildTo(Service service, Object target) {
        ServiceComposite sc = (ServiceComposite) service;
        sc.removeService((Service) target);
    }
}

class LiteralClientTreeAdapterFactory implements TreeAdapterFactory<Service> {

    public boolean canAdapt(Class type) {
        return LiteralClient.class.equals(type);
    }

    public Service getNewInstance() {
        return new LiteralClient("New literal client", MessageDelimiter.NONE);
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        return null;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(Service service) {
        return new LiteralClientAdapter(service);
    }

    public Object addChildTo(Service service, JTreeNodeAdapterFactory factory) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }

    public void removeChildTo(Service service, Object target) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }
}

class SNMPAgentTreeAdapterFactory implements TreeAdapterFactory<Service> {

    public boolean canAdapt(Class type) {
        return SNMPAgentService.class.equals(type);
    }

    public Service getNewInstance() {
        return new SNMPAgentService("New SNMP Agent", 161, SNMPAgentService.Version.v1, new OctetString("public"));
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        JTreeNodeAdapterFactory[] factories = {JTreeNodeAdapterFactory.SNMP_MIB};
        return factories;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(Service service) {
        DefaultMutableTreeNode node = new SNMPAgentAdapter(service);

        SNMPAgentService s = (SNMPAgentService) service;
        for (SNMPMIB m : s.getMibs()) {
            node.add(JTreeNodeAdapterFactory.valueOf(m).getTreeNodeInstance(m));
        }

        return node;
    }

    public Object addChildTo(Service service, JTreeNodeAdapterFactory factory) {
        SNMPAgentService sa = (SNMPAgentService) service;
        SNMPMIB mib = (SNMPMIB) factory.getNewInstance();
        sa.addMib(mib);
        return mib;
    }

    public void removeChildTo(Service service, Object target) {
        SNMPAgentService sa = (SNMPAgentService) service;
        sa.removeMib((SNMPMIB) target);
    }
}

class SNMPMibAdapterFactory implements TreeAdapterFactory<SNMPMIB> {

    public boolean canAdapt(Class type) {
        return SNMPMIB.class.equals(type);
    }

    public SNMPMIB getNewInstance() {
        return new SNMPMIB("New MIB", new OID("1.3.6.1.4.1"));
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        JTreeNodeAdapterFactory[] factories = {
            JTreeNodeAdapterFactory.SNMP_CONVERT_TABLE,
            JTreeNodeAdapterFactory.SNMP_TABLE
        };
        return factories;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(SNMPMIB mib) {
        DefaultMutableTreeNode node = new SNMPMibAdapter(mib);

        for (ConvertTable t : mib.getConvertTables()) {
            node.add(JTreeNodeAdapterFactory.valueOf(t).getTreeNodeInstance(t));
        }

        for (ManagedObjectBuilder b : mib.getBuilders()) {
            if (!(b instanceof ScalarBuilder)) {
                node.add(JTreeNodeAdapterFactory.valueOf(b).getTreeNodeInstance(b));
            }
        }

        return node;
    }

    public Object addChildTo(SNMPMIB mib, JTreeNodeAdapterFactory factory) {
        if (factory.equals(JTreeNodeAdapterFactory.SNMP_TABLE)) {
            TableBuilder table = (TableBuilder) factory.getNewInstance();
            mib.addBuilder(table);
            return table;
        } else if (factory.equals(JTreeNodeAdapterFactory.SNMP_CONVERT_TABLE)) {
            SNMPConvertTable table = (SNMPConvertTable) factory.getNewInstance();
            mib.addConvertTable(table);
            return table;
        } else {
            throw new RuntimeException("Cannot add this factory !");
        }
    }

    public void removeChildTo(SNMPMIB mib, Object target) {
        if (target instanceof ManagedObjectBuilder) {
            mib.removeBuilder((ManagedObjectBuilder) target);
        } else if (target instanceof ConvertTable) {
            mib.removeConvertTable((SNMPConvertTable) target);
        }

    }
}

class SNMPConvertTableAdapterFactory implements TreeAdapterFactory<ConvertTable> {

    public boolean canAdapt(Class type) {
        return SNMPConvertTable.class.equals(type);
    }

    public ConvertTable getNewInstance() {
        return new SNMPConvertTable<Integer32>("New ConvertTable");
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        return null;
    }

    public Object addChildTo(ConvertTable object, JTreeNodeAdapterFactory factory) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }

    public void removeChildTo(ConvertTable object, Object target) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }

    public DefaultMutableTreeNode getTreeNodeInstance(ConvertTable convertTable) {
        return new ConvertTableAdapter(convertTable);
    }
}

class SNMPTableAdapterFactory implements TreeAdapterFactory<TableBuilder> {

    public boolean canAdapt(Class type) {
        return TableBuilder.class.equals(type);
    }

    public TableBuilder getNewInstance() {
        return new TableBuilder("New Table", new OID("1"), SMIAccess.READ_ONLY);
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        return null;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(TableBuilder builder) {
        DefaultMutableTreeNode node = new SNMPTableBuilderAdapter((TableBuilder) builder);
        return node;
    }

    public Object addChildTo(TableBuilder mb, JTreeNodeAdapterFactory factory) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }

    public void removeChildTo(TableBuilder table, Object target) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }
}

class SNMPTrapTreeAdapterFactory implements TreeAdapterFactory<Service> {

    public boolean canAdapt(Class type) {
        return SNMPTrapService.class.equals(type);
    }

    public Service getNewInstance() {
        return new SNMPTrapService("New SNMP Trap", "127.0.0.1", "127.0.0.1", 162, 1000, new OID("1"), new OctetString("public"), 1, 1);
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        JTreeNodeAdapterFactory[] factories = {
            JTreeNodeAdapterFactory.SNMP_CONVERT_TABLE
        };
        return factories;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(Service service) {
        DefaultMutableTreeNode node = new SNMPTrapAdapter(service);
        SNMPTrapService st = (SNMPTrapService) service;

        for (ConvertTable t : st.getConvertTables()) {
            node.add(JTreeNodeAdapterFactory.valueOf(t).getTreeNodeInstance(t));
        }

        return node;
    }

    public Object addChildTo(Service service, JTreeNodeAdapterFactory factory) {
        SNMPTrapService st = (SNMPTrapService) service;
        SNMPConvertTable table = (SNMPConvertTable) factory.getNewInstance();
        st.addConvertTable(table);
        return table;
    }

    public void removeChildTo(Service service, Object target) {
        SNMPTrapService st = (SNMPTrapService) service;
        st.removeConvertTable((SNMPConvertTable) target);
    }
}

class WagoConvertTableAdapterFactory implements TreeAdapterFactory<ConvertTable> {

    public boolean canAdapt(Class type) {
        return WagoConvertTable.class.equals(type);
    }

    public ConvertTable getNewInstance() {
        return new WagoConvertTable<Object>("New ConvertTable");
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        return null;
    }

    public Object addChildTo(ConvertTable object, JTreeNodeAdapterFactory factory) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }

    public void removeChildTo(ConvertTable object, Object target) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }

    public DefaultMutableTreeNode getTreeNodeInstance(ConvertTable convertTable) {
        return new ConvertTableAdapter(convertTable);
    }
}

class WagoTreeAdapterFactory implements TreeAdapterFactory<WagoService> {

    public boolean canAdapt(Class type) {
        return WagoService.class.equals(type);
    }

    public WagoService getNewInstance() {
        return new WagoService("New Wago", "COM1", 1);
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        JTreeNodeAdapterFactory[] factories = {
            JTreeNodeAdapterFactory.WAGO_CONVERT_TABLE
        };
        return factories;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(WagoService service) {
        DefaultMutableTreeNode node = new WagoAdapter(service);

        for (WagoConvertTable t : service.getConvertTables()) {
            node.add(JTreeNodeAdapterFactory.valueOf(t).getTreeNodeInstance(t));
        }

        return node;
    }

    public Object addChildTo(WagoService service, JTreeNodeAdapterFactory factory) {
        WagoService ws = (WagoService) service;
        WagoConvertTable table = (WagoConvertTable) factory.getNewInstance();
        ws.addConvertTable(table);
        return table;
    }

    public void removeChildTo(WagoService service, Object target) {
        service.removeConvertTable((WagoConvertTable) target);
    }
}

class XmlClientTreeAdapterFactory implements TreeAdapterFactory<Service> {

    public boolean canAdapt(Class type) {
        return XmlClientService.class.equals(type);
    }

    public Service getNewInstance() {
        return new XmlClientService("New XML Client");
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        JTreeNodeAdapterFactory[] factories = {JTreeNodeAdapterFactory.XML_CLIENT_SIMULATOR};
        return factories;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(Service service) {
        return JTreeNodeAdapterFactory.NODE.getTreeNodeInstance(service);
    }

    public Object addChildTo(Service service, JTreeNodeAdapterFactory factory) {
        XmlClientService sc = (XmlClientService) service;

        XmlClient client = (XmlClient) factory.getNewInstance();
        sc.addService(client);
        return client;
    }

    public void removeChildTo(Service service, Object target) {
        XmlClientService sc = (XmlClientService) service;
        sc.removeService((XmlClient) target);
    }
}

class XmlClientSimulatorTreeAdapterFactory implements TreeAdapterFactory<Service> {

    public boolean canAdapt(Class type) {
        return XmlClient.class.equals(type);
    }

    public Service getNewInstance() {
        return new XmlClient("New XML Client Simulator", 1000, MessageDelimiter.ZERO, new ISupSystem("New_ISupSystem", ""));
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        return null;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(Service service) {
        return new XmlClientSimulatorAdapter((XmlClient) service);
    }

    public Object addChildTo(Service service, JTreeNodeAdapterFactory factory) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }

    public void removeChildTo(Service object, Object target) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }
}

class XmlServerTreeAdapterFactory implements TreeAdapterFactory<Service> {

    public boolean canAdapt(Class type) {
        return XmlServerService.class.equals(type);
    }

    public Service getNewInstance() {
        return new XmlServerService(new ISupSystem("New_ISupSystem", ""), 4000, 1000, 10000, MessageDelimiter.ZERO);
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        return null;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(Service service) {
        return new XMLServerAdapter(service);
    }

    public Object addChildTo(Service service, JTreeNodeAdapterFactory factory) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }

    public void removeChildTo(Service object, Object target) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }
}

class HomeMadeTreeAdapterFactory implements TreeAdapterFactory<Service> {

    public boolean canAdapt(Class type) {
        return HomemadeService.class.equals(type);
    }

    public Service getNewInstance() {
        return new HomemadeService("New Homemade", 4000, 1000, 10000);
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        return null;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(Service service) {
        return new HomemadeAdapter(service);
    }

    public Object addChildTo(Service service, JTreeNodeAdapterFactory factory) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }

    public void removeChildTo(Service object, Object target) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }
}

class DefaultTreeAdapterFactory implements TreeAdapterFactory<Object> {

    public boolean canAdapt(Class type) {
        return true;
    }

    public Object getNewInstance() {
        throw new UnsupportedOperationException("Unknown object to adapt !");
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        return null;
    }

    public DefaultMutableTreeNode getTreeNodeInstance(Object object) {
        return new DefaultMutableTreeNode(object) {
        };
    }

    public Object addChildTo(Object object, JTreeNodeAdapterFactory factory) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }

    public void removeChildTo(Object object, Object target) {
        throw new UnsupportedOperationException("No child is supported by leaf !");
    }
}

public enum JTreeNodeAdapterFactory {

    NODE("Node", new CompositeTreeAdapterFactory()),
    LITERAL_CLIENT("Literal Client", new LiteralClientTreeAdapterFactory()),
    SNMP_AGENT("SNMP Get", new SNMPAgentTreeAdapterFactory()),
    SNMP_MIB("Mib", new SNMPMibAdapterFactory()),
    SNMP_CONVERT_TABLE("Convert table", new SNMPConvertTableAdapterFactory()),
    SNMP_TABLE("Table", new SNMPTableAdapterFactory()),
    SNMP_TRAP("SNMP Traps", new SNMPTrapTreeAdapterFactory()),
    WAGO_CONVERT_TABLE("Convert table", new WagoConvertTableAdapterFactory()),
    WAGO("Wago", new WagoTreeAdapterFactory()),
    XML_CLIENT("Xml Client", new XmlClientTreeAdapterFactory()),
    XML_CLIENT_SIMULATOR("Simulator", new XmlClientSimulatorTreeAdapterFactory()),
    XML_SERVER("Xml Server", new XmlServerTreeAdapterFactory()),
    HOMEMADE("Home made", new HomeMadeTreeAdapterFactory()),
    DEFAULT("", new DefaultTreeAdapterFactory());
    // attribute
    private String name;
    private TreeAdapterFactory factory;

    // property
    public Object getNewInstance() {
        return factory.getNewInstance();
    }

    public DefaultMutableTreeNode getTreeNodeInstance(Object object) {
        return factory.getTreeNodeInstance(object);
    }

    public JTreeNodeAdapterFactory[] getPossibleChildren() {
        return factory.getPossibleChildren();
    }

    public Object addChildTo(Object object, JTreeNodeAdapterFactory factory) {
        return this.factory.addChildTo(object, factory);
    }

    public void removeChildTo(Object object, Object target) {
        this.factory.removeChildTo(object, target);
    }

    @Override
    public String toString() {
        return name;
    }

    // static methode
    public static JTreeNodeAdapterFactory valueOf(Object object) {
        for (JTreeNodeAdapterFactory sa : values()) {
            if (sa.factory.canAdapt(object.getClass())) {
                return sa;
            }
        }
        throw new RuntimeException(String.format("Adapter for %s cannot be found !", object.getClass()));
    }

    // constructor
    private JTreeNodeAdapterFactory(String name, TreeAdapterFactory factory) {
        this.name = name;
        this.factory = factory;
    }
}
