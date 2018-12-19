/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import ch.skyguide.pvss.network.service.Service.Status;
import ch.skyguide.pvss.network.service.ServiceLeaf;
import ch.skyguide.pvss.network.service.convertTable.ConvertTableListable;
import ch.skyguide.pvss.network.service.snmp.builder.ScalarBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;

/**
 *
 * @author caronyn
 */
public class SNMPTrapService extends ServiceLeaf implements BuilderContainer<ScalarBuilder>, ConvertTableListable<SNMPConvertTable>, Runnable {

    // attribute
    private String address;
    private String sourceAddress;
    private int port;
    private int interval;
    private OID enterpriseOID;
    private OctetString community;
    private int genericTrap;
    private int specificTrap;
    private List<SNMPConvertTable> convertTables;
    private List<ScalarBuilder> builders;
    private Thread thread;
    // logger
    private final static Logger LOGGER = Logger.getLogger(SNMPTrapService.class.getName());

    // property
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
        for (ScalarBuilder b : builders) {
            if (e.equals(b.getConvertTable())) {
                b.removeConvertTable();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.sourceAddress = agentAddress;
    }

    public OID getEnterpriseOID() {
        return enterpriseOID;
    }

    public void setEnterpriseOID(OID enterpriseOID) {
        this.enterpriseOID = enterpriseOID;
    }

    public OctetString getCommunity() {
        return community;
    }

    public void setCommunity(OctetString community) {
        this.community = community;
    }

    public int getGenericTrap() {
        return genericTrap;
    }

    public void setGenericTrap(int genericTrap) {
        this.genericTrap = genericTrap;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSpecificTrap() {
        return specificTrap;
    }

    public void setSpecificTrap(int specificTrap) {
        this.specificTrap = specificTrap;
    }

    @Override
    public OID getPrefixOID() {
        return enterpriseOID;
    }

    @Override
    public ScalarBuilder getBuilder(int i) {
        return builders.get(i);
    }

    // methodes
    @Override
    public void addBuilder(ScalarBuilder builder) {
        builder.setParentMib(this);
        builders.add(builder);
    }

    @Override
    public void removeBuilder(ScalarBuilder builder) {
        builder.setParentMib(null);
        builders.remove(builder);
    }

    @Override
    public List<ScalarBuilder> getBuilders() {
        return builders;
    }

    // function
    private CommunityTarget buildCommunityTarget() {

        UdpAddress addr = new UdpAddress(address + "/" + port);
        CommunityTarget target = new CommunityTarget(addr, community);
        return target;
    }

    private PDU buildPDU() {
        PDUv1 pdu = (PDUv1) DefaultPDUFactory.createPDU(SnmpConstants.version1);
        pdu.setType(PDU.V1TRAP);
        pdu.setEnterprise(enterpriseOID);
        pdu.setGenericTrap(genericTrap);
        pdu.setSpecificTrap(specificTrap);
        pdu.setAgentAddress(new IpAddress(sourceAddress));

        for (ScalarBuilder builder : builders) {
            builder.buildPDU(pdu);
        }

        return pdu;
    }

    @Override
    public void run() {

        synchronized (this) {
            try {
                CommunityTarget target = buildCommunityTarget();
                Snmp snmp = new Snmp(new DefaultUdpTransportMapping());

                while (this.thread.isAlive()) {
                    PDU pdu = buildPDU();
                    snmp.send(pdu, target);
                    LOGGER.log(Level.INFO, "Trap sent : {0}", pdu.toString());

                    this.wait(interval);
                }
            } catch (IOException ex) {
                Logger.getLogger(SNMPTrapService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                // do nothing
            }
        }
    }

    // methode
    @Override
    protected void startAgent() {
        setStatus(Status.STARTED);
        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void stopAgent() {
        setStatus(Status.STOPPED);
        this.thread.interrupt();
    }

    @Override
    public void restartAgent() {
        stopAgent();
        startAgent();
    }
        
    @Override
    public void buildDpl(Database db) {
        // do nothing yet
    }

    // constructor
    private SNMPTrapService() {
        super("");
        builders = new LinkedList<ScalarBuilder>();
    }

    public SNMPTrapService(String name, String address, String agentAddress, int port, int interval, OID enterpriseOID, OctetString community, int genericTrap, int specificTrap) {
        super(name);
        builders = new LinkedList<ScalarBuilder>();
        this.address = address;
        this.sourceAddress = agentAddress;
        this.port = port;
        this.interval = interval;
        this.enterpriseOID = enterpriseOID;
        this.community = community;
        this.genericTrap = genericTrap;
        this.specificTrap = specificTrap;
    }
}
