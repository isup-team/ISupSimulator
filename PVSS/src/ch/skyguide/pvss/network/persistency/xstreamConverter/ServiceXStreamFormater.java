/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.xstreamConverter;

import ch.skyguide.pvss.persistency.XStreamFormater;
import ch.skyguide.pvss.network.service.ServiceComposite;
import ch.skyguide.pvss.network.service.ServiceLeaf;
import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import ch.skyguide.pvss.network.service.snmp.SNMPAgentService;
import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import ch.skyguide.pvss.network.service.snmp.SNMPTrapService;
import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import ch.skyguide.pvss.network.service.snmp.builder.ColumnBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.ManagedObjectBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.RowBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.ScalarBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.TableBuilder;
import ch.skyguide.pvss.network.service.convertTable.Entry;
import ch.skyguide.pvss.network.service.homemade.HomemadeService;
import ch.skyguide.pvss.network.service.homemade.HMSubSystem;
import ch.skyguide.pvss.network.service.homemade.HMTypeEnum;
import ch.skyguide.pvss.network.service.literal.LiteralClient;
import ch.skyguide.pvss.network.service.modbus.Coil;
import ch.skyguide.pvss.network.service.modbus.Register;
import ch.skyguide.pvss.network.service.modbus.WagoService;
import ch.skyguide.pvss.network.service.snmp.SNMPConvertTable;
import ch.skyguide.pvss.network.service.xml.ISupLabel;
import ch.skyguide.pvss.network.service.xml.ISupSubSystem;
import ch.skyguide.pvss.network.service.xml.ISupSystem;
import ch.skyguide.pvss.network.service.xml.XmlClient;
import ch.skyguide.pvss.network.service.xml.TcpHost;
import ch.skyguide.pvss.network.service.xml.XmlClientService;
import ch.skyguide.pvss.network.service.xml.XmlServerService;
import com.thoughtworks.xstream.XStream;
import org.snmp4j.smi.VariableBinding;

/**
 *
 * @author caronyn
 */
public class ServiceXStreamFormater extends XStreamFormater {

    @Override
    protected void configure(XStream xstream) {

        // global
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
        xstream.aliasPackage("smi", "org.snmp4j.smi");
        xstream.aliasPackage("service", "ch.skyguide.pvss.network.service");
        xstream.aliasPackage("xml", "ch.skyguide.pvss.network.service.xml");
        xstream.aliasPackage("homemade", "ch.skyguide.pvss.network.service.homemade");
        xstream.aliasPackage("modbus", "ch.skyguide.pvss.network.service.modbus");
        xstream.aliasSystemAttribute("type", "class");

        // services
        xstream.useAttributeFor(ServiceComposite.class, "name");
        xstream.addImplicitCollection(ServiceComposite.class, "children");

        // service leaf
        xstream.useAttributeFor(ServiceLeaf.class, "name");
        xstream.omitField(ServiceLeaf.class, "status");
        xstream.omitField(ServiceLeaf.class, "serviceEventDispatcher");

        // convert table
        xstream.aliasType("SNMPConvertTable", SNMPConvertTable.class);
        xstream.useAttributeFor(ConvertTable.class, "name");
        xstream.addImplicitCollection(ConvertTable.class, "list");

        // entry
        xstream.aliasType("entry", Entry.class);
        xstream.useAttributeFor(Entry.class, "status");
        xstream.useAttributeFor(Entry.class, "value");

        // SNMP
        // snmp agent service
        xstream.aliasType("snmp", SNMPAgentService.class);
        xstream.addImplicitCollection(SNMPAgentService.class, "mibs");
        xstream.useAttributeFor(SNMPAgentService.class, "context");
        xstream.useAttributeFor(SNMPAgentService.class, "port");
        xstream.useAttributeFor(SNMPAgentService.class, "version");
        // snmpv3
        xstream.useAttributeFor(SNMPAgentService.class, "usmName");
        xstream.useAttributeFor(SNMPAgentService.class, "level");
        xstream.useAttributeFor(SNMPAgentService.class, "authAlgo");
        xstream.useAttributeFor(SNMPAgentService.class, "authPassword");
        xstream.useAttributeFor(SNMPAgentService.class, "privAlgo");
        xstream.useAttributeFor(SNMPAgentService.class, "privPassword");

        xstream.omitField(SNMPAgentService.class, "snmpAgent");

        // snmp traps
        xstream.aliasType("traps", SNMPTrapService.class);
        xstream.useAttributeFor(SNMPTrapService.class, "address");
        xstream.useAttributeFor(SNMPTrapService.class, "sourceAddress");
        xstream.useAttributeFor(SNMPTrapService.class, "port");
        xstream.useAttributeFor(SNMPTrapService.class, "interval");
        xstream.useAttributeFor(SNMPTrapService.class, "enterpriseOID");
        xstream.useAttributeFor(SNMPTrapService.class, "community");
        xstream.useAttributeFor(SNMPTrapService.class, "genericTrap");
        xstream.useAttributeFor(SNMPTrapService.class, "specificTrap");
        xstream.omitField(SNMPTrapService.class, "running");
        xstream.omitField(SNMPTrapService.class, "thread");
        xstream.addImplicitCollection(SNMPTrapService.class, "builders");

        // variable binding
        xstream.aliasField("value", VariableBinding.class, "variable");
        xstream.useAttributeFor(VariableBinding.class, "oid");

        // Literal
        xstream.aliasType("literal", LiteralClient.class);
        xstream.useAttributeFor(LiteralClient.class, "keepAlive");
        xstream.useAttributeFor(LiteralClient.class, "delimiter");
        xstream.omitField(LiteralClient.class, "thread");

        // SNMPMIB
        xstream.aliasType("mib", SNMPMIB.class);
        xstream.useAttributeFor(SNMPMIB.class, "name");
        xstream.useAttributeFor(SNMPMIB.class, "prefixOID");
        xstream.useAttributeFor(SNMPMIB.class, "driverID");
        xstream.useAttributeFor(SNMPMIB.class, "driverName");
        xstream.useAttributeFor(SNMPMIB.class, "pollGroup");
        xstream.useAttributeFor(SNMPMIB.class, "agentID");
        xstream.useAttributeFor(SNMPMIB.class, "dpPrefix");
        xstream.addImplicitCollection(SNMPMIB.class, "builders");

        // scalar
        xstream.aliasType("scalar", ScalarBuilder.class);
        xstream.useAttributeFor(ManagedObjectBuilder.class, "name");
        xstream.useAttributeFor(ManagedObjectBuilder.class, "dpName");
        xstream.useAttributeFor(ManagedObjectBuilder.class, "subOID");
        xstream.useAttributeFor(ManagedObjectBuilder.class, "access");

        // table
        xstream.aliasType("table", TableBuilder.class);

        // column
        xstream.aliasType("column", ColumnBuilder.class);
        xstream.useAttributeFor(ColumnBuilder.class, "name");
        xstream.useAttributeFor(ColumnBuilder.class, "index");
        xstream.useAttributeFor(ColumnBuilder.class, "syntax");

        // row
        xstream.aliasType("row", RowBuilder.class);
        xstream.useAttributeFor(RowBuilder.class, "index");
        xstream.addImplicitCollection(RowBuilder.class, "values");

        // converters
        for (SMISyntax syntax : SMISyntax.values()) {
            xstream.registerConverter(new SmiTypeConverter(syntax));
        }

        // TODO voire si nécéssaire
        xstream.registerConverter(new TcpAddressConverter());
        xstream.registerConverter(new UdpAddressConverter());

        // XML
        // XML Client Service

        // tcp host
        xstream.aliasType("tcpHost", TcpHost.class);
        xstream.useAttributeFor(TcpHost.class, "address");
        xstream.useAttributeFor(TcpHost.class, "port");

        // XML Client Service
        xstream.useAttributeFor(XmlClientService.class, "keepAlive");
        
        // ISup system simulator
        xstream.aliasType("client", XmlClient.class);
        xstream.omitField(XmlClient.class, "threads");
        xstream.omitField(XmlClient.class, "activeChain");
        xstream.useAttributeFor(XmlClient.class, "period");
        xstream.useAttributeFor(XmlClient.class, "delimiter");

        // ISup system
        xstream.aliasType("system", ISupSystem.class);
        xstream.useAttributeFor(ISupSystem.class, "name");
        xstream.useAttributeFor(ISupSystem.class, "source");
        xstream.addImplicitCollection(ISupSystem.class, "subSystems");

        // ISup sub system
        xstream.aliasType("subSystem", ISupSubSystem.class);
        xstream.useAttributeFor(ISupSubSystem.class, "mode");
        xstream.useAttributeFor(ISupSubSystem.class, "dpName");
        xstream.useAttributeFor(ISupSubSystem.class, "name");
        xstream.useAttributeFor(ISupSubSystem.class, "status");
        xstream.addImplicitCollection(ISupSubSystem.class, "labels");

        // Label
        xstream.aliasType("label", ISupLabel.class);
        xstream.useAttributeFor(ISupLabel.class, "name");
        xstream.useAttributeFor(ISupLabel.class, "value");

        // XML Server Service
        xstream.omitField(XmlServerService.class, "thread");
        xstream.useAttributeFor(XmlServerService.class, "port");
        xstream.useAttributeFor(XmlServerService.class, "heartbeatPeriod");
        xstream.useAttributeFor(XmlServerService.class, "statusPeriod");
        xstream.useAttributeFor(XmlServerService.class, "delimiter");
        xstream.useAttributeFor(XmlServerService.class, "driverID");
        xstream.useAttributeFor(XmlServerService.class, "driverName");
        xstream.useAttributeFor(XmlServerService.class, "pollGroup");
        xstream.useAttributeFor(XmlServerService.class, "agentID");
        xstream.useAttributeFor(XmlServerService.class, "dpPrefix");

        // Homemade
        //xstream.aliasType("homemade", HomemadeService.class);
        xstream.omitField(HomemadeService.class, "thread");
        xstream.useAttributeFor(HomemadeService.class, "port");
        xstream.useAttributeFor(HomemadeService.class, "heartbeatPeriod");
        xstream.useAttributeFor(HomemadeService.class, "statusPeriod");
        xstream.addImplicitCollection(HomemadeService.class, "subSystems");

        // Homemade sub system
        xstream.aliasType("hmsystem", HMSubSystem.class);
        xstream.useAttributeFor(HMSubSystem.class, "name");

        // converters
        for (HMTypeEnum types : HMTypeEnum.values()) {
            xstream.registerConverter(new HomemadeTypeConverter(types));
        }

        // Modbus
        xstream.omitField(WagoService.class, "thread");
        xstream.useAttributeFor(WagoService.class, "port");
        xstream.useAttributeFor(WagoService.class, "id");
        xstream.useAttributeFor(WagoService.class, "baudRate");
        xstream.useAttributeFor(WagoService.class, "dataBits");
        xstream.useAttributeFor(WagoService.class, "parity");
        xstream.useAttributeFor(WagoService.class, "stopBits");
        xstream.useAttributeFor(WagoService.class, "echo");
        xstream.useAttributeFor(WagoService.class, "encoding");

        // Coil
        xstream.aliasType("coil", Coil.class);
        xstream.useAttributeFor(Coil.class, "id");
        xstream.useAttributeFor(Coil.class, "name");
        xstream.useAttributeFor(Coil.class, "value");

        // Register
        xstream.aliasType("register", Register.class);
        xstream.useAttributeFor(Register.class, "id");
        xstream.useAttributeFor(Register.class, "name");
        xstream.useAttributeFor(Register.class, "value");

        // TODO : problème référence parent
        //xstream.setMarshallingStrategy(new TreeMarshallingStrategy());

    }

    public ServiceXStreamFormater() {
        super();
    }

    public ServiceXStreamFormater(XStreamFormater formater) {
        super(formater);
    }
}
