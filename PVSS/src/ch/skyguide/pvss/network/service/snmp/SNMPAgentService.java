/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import ch.skyguide.pvss.network.persistency.dpl.DplUtils;
import ch.skyguide.pvss.network.service.ServiceLeaf;
import ch.skyguide.pvss.network.service.snmp.builder.ManagedObjectBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.security.*;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

/**
 *
 * @author caronyn
 */
public class SNMPAgentService extends ServiceLeaf implements MOGroup {

    // inner class
    public enum Version {

        v1, v2c, v3
    }

    public enum SecurityLevel {

        noAuth_noPriv(org.snmp4j.security.SecurityLevel.NOAUTH_NOPRIV),
        auth_noPriv(org.snmp4j.security.SecurityLevel.AUTH_NOPRIV),
        auth_priv(org.snmp4j.security.SecurityLevel.AUTH_PRIV);
        private final int securityLevel;

        public int getSecurityLevel() {
            return securityLevel;
        }

        private SecurityLevel(int securityLevel) {
            this.securityLevel = securityLevel;
        }
    }

    public enum AuthAlgorithm {

        SHA(AuthSHA.ID), MD5(AuthMD5.ID);
        private final OID oid;

        public OID getOid() {
            return oid;
        }

        private AuthAlgorithm(OID oid) {
            this.oid = oid;
        }
    }

    public enum PrivAlgorithm {

        AES128(PrivAES128.ID), AES192(PrivAES192.ID), AES256(PrivAES256.ID), DES(PrivDES.ID), DES3(Priv3DES.ID);
        private OID oid;

        public final OID getOid() {
            return oid;
        }

        private PrivAlgorithm(OID oid) {
            this.oid = oid;
        }
    }
    // logger
    private final static Logger LOGGER = Logger.getLogger(SNMPAgentService.class.getName());
    // attributes
    private int port;
    private SNMPAgentService.Version version;
    private OctetString context;
    private SNMPAgent snmpAgent;
    private List<SNMPMIB> mibs;
    // snmp v3   
    private OctetString usmName;
    private SNMPAgentService.SecurityLevel level;
    private SNMPAgentService.AuthAlgorithm authAlgo;
    private OctetString authPassword;
    private SNMPAgentService.PrivAlgorithm privAlgo;
    private OctetString privPassword;

    // properties
    public final int getPort() {
        return port;
    }

    public final void setPort(int port) {
        this.port = port;
    }

    public final Version getVersion() {
        return version;
    }

    public final void setVersion(Version version) {
        this.version = version;
    }

    public OctetString getContext() {
        return context;
    }

    public void setContext(OctetString context) {
        this.context = context;
    }

    public OctetString getUsmName() {
        return usmName;
    }

    public void setUsmName(OctetString usmName) {
        this.usmName = usmName;
    }

    public AuthAlgorithm getAuthAlgo() {
        return authAlgo;
    }

    public void setAuthAlgo(AuthAlgorithm authAlgo) {
        this.authAlgo = authAlgo;
    }

    public OctetString getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(OctetString authPassword) {
        this.authPassword = authPassword;
    }

    public SecurityLevel getLevel() {
        return level;
    }

    public void setLevel(SecurityLevel level) {
        this.level = level;
    }

    public PrivAlgorithm getPrivAlgo() {
        return privAlgo;
    }

    public void setPrivAlgo(PrivAlgorithm privAlgo) {
        this.privAlgo = privAlgo;
    }

    public OctetString getPrivPassword() {
        return privPassword;
    }

    public void setPrivPassword(OctetString privPassword) {
        this.privPassword = privPassword;
    }

    private void initializeMib() {
        if (mibs == null) {
            mibs = new ArrayList<SNMPMIB>();
        }
    }

    public SNMPMIB getMib(int i) {
        initializeMib();
        return mibs.get(i);
    }

    public final List<SNMPMIB> getMibs() {
        initializeMib();
        return mibs;
    }

    public boolean addMib(SNMPMIB e) {
        initializeMib();
        return mibs.add(e);
    }

    public boolean removeMib(SNMPMIB e) {
        initializeMib();
        return mibs.remove(e);
    }

    // methode
    @Override
    public void registerMOs(MOServer server, OctetString context) throws DuplicateRegistrationException {

        for (SNMPMIB mib : mibs) {
            mib.registerMOs(server, context);
        }
    }

    @Override
    public void unregisterMOs(MOServer server, OctetString context) {
        for (SNMPMIB mib : mibs) {
            mib.unregisterMOs(server, context);


        }
    }

    // methodes
    @Override
    protected synchronized void startAgent() {
        try {
            SNMPAgentPool snmpAgentPool = SNMPAgentPool.getInstance();
            snmpAgent = snmpAgentPool.getAgentInstance(this);

            System.out.println("CONTEXT " + context.toString());
            
            if (version != Version.v3) {
                this.registerMOs(snmpAgent.getServer(), context);
            } else {
                this.registerMOs(snmpAgent.getServer(), null);
            }

            snmpAgentPool.tryStart(port);

            LOGGER.log(Level.INFO, "Agent [{0}] Started !", this.getName());
            this.setStatus(Status.STARTED);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "Agent [{0}] start failed !", this.getName());
        }

    }

    @Override
    protected synchronized void stopAgent() {
        try {
            if (snmpAgent != null) {
                this.unregisterMOs(snmpAgent.getServer(), context);
            }

            SNMPAgentPool snmpAgentPool = SNMPAgentPool.getInstance();
            snmpAgentPool.tryStop(port);

            this.setStatus(Status.STOPPED);
            LOGGER.log(Level.INFO, "Agent [{0}] Stopped !", this.getName());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "Agent [{0}] stop failed !", this.getName());
        }
    }

    @Override
    public void restartAgent() {
        try {
            this.unregisterMOs(snmpAgent.getServer(), context);
            this.registerMOs(snmpAgent.getServer(), context);
            LOGGER.log(Level.INFO, "Agent [{0}] Refreshed ! (be carefull port, version and context are not considered, please stop and restart manually).", this.getName());
        } catch (DuplicateRegistrationException ex) {
            LOGGER.log(Level.SEVERE, "Agent [{0}] refresh failed !", this.getName());
        }
    }

    @Override
    public void buildDpl(Database db) {
        for (SNMPMIB mib : mibs) {
            if (mib.getDpPrefix() != null) {
                NodeType type = DplUtils.createType(db, mib.getDpPrefix());

                DplUtils.snmpCreateRawData(mib.getBuilders(), type);
                DplUtils.snmpBuildAddress(db, type, mib.getBuilders(), mib.getPrefixOID(), mib.getDpPrefix(), this.getName(), mib.getPollGroup(), mib.getDriverID(), mib.getAgentID(), mib.getDriverName());
            }
        }

    }

    // constructor
    private SNMPAgentService() {
        super("");
        initializeMib();
    }

    public SNMPAgentService(String name, int port, Version version, OctetString context) {
        super(name);
        initializeMib();
        this.port = port;
        this.version = version;
        this.context = context;

    }
}
