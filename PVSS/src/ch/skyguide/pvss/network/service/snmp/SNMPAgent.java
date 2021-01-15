/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.snmp4j.TransportMapping;
import org.snmp4j.TransportStateReference;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.smi.*;
import org.snmp4j.mp.*;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.transport.TransportListener;
import org.snmp4j.transport.TransportMappings;

/**
 *
 * @author caronyn
 */
public class SNMPAgent extends BaseAgent {

    private final static Logger LOGGER = Logger.getLogger(SNMPAgent.class.getName());
    private static final String CONF_FILE = "conf.agent";
    private static final String BOOT_COUNTER_FILE = "bootCounter.agent";
    private SNMPAgentService parent;

    public SNMPAgent(SNMPAgentService parent) throws IOException {
        // These files does not exist and are not used but has to be specified
        // Read snmp4j docs for more info
        super(new File(CONF_FILE), new File(BOOT_COUNTER_FILE),
                new CommandProcessor(
                new OctetString(MPv3.createLocalEngineID())));
        this.parent = parent;
    }

    /**
     * We let clients of this agent register the MO they need so this method does
     * nothing
     */
    @Override
    protected void registerManagedObjects() {
        // TODO : Placer autre part
        unregisterManagedObject(getSnmp4jLogMIB());
        unregisterManagedObject(getSnmp4jConfigMIB());
        unregisterManagedObject(getSnmpCommunityMIB());
        unregisterManagedObject(getSnmpFrameworkMIB());
        unregisterManagedObject(getSnmpNotificationMIB());
        unregisterManagedObject(getSnmpProxyMIB());
        unregisterManagedObject(getSnmpTargetMIB());
        unregisterManagedObject(getSnmpv2MIB());
        unregisterManagedObject(getUsmMIB());
        unregisterManagedObject(getVacmMIB());
    }

    /**
     * Clients can register the MO they need
     */
    public void registerManagedObject(ManagedObject mo) {
        try {
            server.register(mo, null);
        } catch (DuplicateRegistrationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void unregisterManagedObject(MOGroup moGroup) {
        moGroup.unregisterMOs(server, getContext(moGroup));
    }

    public boolean isRunning() {
        return (this.agentState == STATE_RUNNING);
    }

    /*
     * Empty implementation
     */
    @Override
    protected void addNotificationTargets(SnmpTargetMIB targetMIB,
            SnmpNotificationMIB notificationMIB) {
    }

    /**
     * Minimal View based Access Control
     *
     * http://www.faqs.org/rfcs/rfc2575.html
     */
    @Override
    protected void addViews(VacmMIB vacm) {

        if (parent.getVersion() != SNMPAgentService.Version.v3) {
            vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv1,
                    new OctetString("cpublic"),
                    new OctetString("v1v2group"),
                    StorageType.nonVolatile);

            vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c,
                    new OctetString("cpublic"),
                    new OctetString("v1v2group"),
                    StorageType.nonVolatile);

            LOGGER.info("Build SNMP community");
            for (OctetString ctx : getServer().getContexts()) {
                LOGGER.log(Level.INFO, "BUILD WITH {0}", ctx);
                vacm.addAccess(new OctetString("v1v2group"),
                        ctx,
                        SecurityModel.SECURITY_MODEL_ANY,
                        SecurityLevel.NOAUTH_NOPRIV,
                        MutableVACM.VACM_MATCH_EXACT,
                        new OctetString("fullReadView"),
                        new OctetString("fullWriteView"),
                        new OctetString("fullNotifyView"),
                        StorageType.nonVolatile);
            }
        } else {
            LOGGER.info("Build SNMPv3 USM");
            vacm.addGroup(SecurityModel.SECURITY_MODEL_USM,
                    new OctetString(parent.getUsmName()),
                    new OctetString("v3group"),
                    StorageType.nonVolatile);

            vacm.addAccess(
                    new OctetString("v3group"),
                    new OctetString(),
                    SecurityModel.SECURITY_MODEL_USM,
                    parent.getLevel().getSecurityLevel(),
                    MutableVACM.VACM_MATCH_EXACT,
                    new OctetString("fullReadView"),
                    new OctetString("fullWriteView"),
                    new OctetString("fullNotifyView"),
                    StorageType.nonVolatile);
        }

        vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3.6.1"),
                new OctetString(), VacmMIB.vacmViewIncluded,
                StorageType.nonVolatile);
        vacm.addViewTreeFamily(new OctetString("fullWriteView"), new OID("1.3.6.1"),
                new OctetString(), VacmMIB.vacmViewIncluded,
                StorageType.nonVolatile);
        vacm.addViewTreeFamily(new OctetString("fullNotifyView"), new OID("1.3.6.1"),
                new OctetString(), VacmMIB.vacmViewIncluded,
                StorageType.nonVolatile);
    }

    /**
     * User based Security Model, only applicable to SNMP v.3
     *
     */
    @Override
    protected void addUsmUser(USM usm) {
        if (parent.getVersion() == SNMPAgentService.Version.v3) {

            UsmUser user = null;

            if (parent.getLevel() == SNMPAgentService.SecurityLevel.noAuth_noPriv) {
                user = new UsmUser(new OctetString(parent.getUsmName()), null, null, null, null);
            } else if (parent.getLevel() == SNMPAgentService.SecurityLevel.auth_noPriv) {
                user = new UsmUser(new OctetString(parent.getUsmName()),
                        parent.getAuthAlgo().getOid(), parent.getAuthPassword(),
                        null, null);
            } else if (parent.getLevel() == SNMPAgentService.SecurityLevel.auth_priv) {
                user = new UsmUser(new OctetString(parent.getUsmName()),
                        parent.getAuthAlgo().getOid(), parent.getAuthPassword(),
                        parent.getPrivAlgo().getOid(), parent.getPrivPassword());
            }

            LOGGER.info(String.format("Add USM %s", user.toString()));

            usm.addUser(user.getSecurityName(), usm.getLocalEngineID(), user);
        }
    }

    @Override
    protected void initTransportMappings() throws IOException {
        transportMappings = new TransportMapping[1];

        String address = "0.0.0.0/" + String.valueOf(parent.getPort());

        Address addr = GenericAddress.parse(address);

        LOGGER.info(String.format("Server initialized with address: %s", addr.toString()));

        TransportMapping tm = TransportMappings.getInstance().createTransportMapping(addr);
        transportMappings[0] = tm;

        tm.addTransportListener(new TransportListener() {

            @Override
            public void processMessage(TransportMapping sourceTransport, Address incomingAddress, ByteBuffer wholeMessage, TransportStateReference tmStateReference) {
                StringBuilder sb = new StringBuilder();
                sb.append("Message sent to [");
                sb.append(incomingAddress.toString());
                sb.append("] : ");
                sb.append(new OctetString(wholeMessage.array()).toASCII('.'));
                LOGGER.log(Level.INFO, sb.toString());
            }
        });
    }

    public void addContext(OctetString context) {
        getServer().addContext(context);
    }

    /**
     * Start method invokes some initialization methods needed to start the agent
     *
     * @throws IOException
     */
    public void start() throws IOException {

        init();
        // This method reads some old config from a file and causes
        // unexpected behavior.
        // loadConfig(ImportModes.REPLACE_CREATE);
        addShutdownHook();
        //getSnmp4jConfigMIB();
        finishInit();
        run();
        sendColdStartNotification();
    }

    @Override
    protected void unregisterManagedObjects() {
        // here we should unregister those objects previously registered...
    }

    /**
     * The table of community strings configured in the SNMP engine's Local
     * Configuration Datastore (LCD).
     *
     * We only configure one, "public".
     */
    // TODO s'occuper des communitées
    private Variable[] buildCommunity(OctetString community) {
        Variable[] com = new Variable[]{
            community, // community name
            new OctetString("cpublic"), // security name
            getAgent().getContextEngineID(), // local engine ID
            new OctetString(community), // default context name
            new OctetString(), // transport tag
            new Integer32(StorageType.readOnly), // storage type
            new Integer32(RowStatus.active) // row status
        };

        return com;
    }

    @Override
    protected void addCommunities(SnmpCommunityMIB communityMIB) {
        for (OctetString ctx : getServer().getContexts()) {
            MOTableRow row2 = communityMIB.getSnmpCommunityEntry().createRow(
                    new OctetString("public2public").toSubIndex(true), buildCommunity(ctx));
            communityMIB.getSnmpCommunityEntry().addRow(row2);
        }

    }
}
