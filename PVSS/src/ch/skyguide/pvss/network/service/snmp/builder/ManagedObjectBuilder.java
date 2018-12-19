/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp.builder;

import ch.skyguide.pvss.network.service.snmp.BuilderContainer;
import ch.skyguide.pvss.network.service.snmp.SMIAccess;
import org.snmp4j.PDU;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.smi.OID;

/**
 *
 * @author caronyn
 */
public abstract class ManagedObjectBuilder implements Builder<ManagedObject> {
    // attributes

    protected String name;
    protected String dpName;
    protected OID subOID;
    protected SMIAccess access;
    protected BuilderContainer parentMib;

    // properties
    public void setParentMib(BuilderContainer parentMib) {
        this.parentMib = parentMib;
    }

    public BuilderContainer getParentMib() {
        return parentMib;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDpName() {
        return dpName;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
    }

    public OID getOID() {
        return subOID;
    }

    public void setOID(OID subOID) {
        this.subOID = subOID;
    }

    public SMIAccess getAccess() {
        return access;
    }

    public void setAccess(SMIAccess access) {
        this.access = access;
    }

    public abstract void buildPDU(PDU pdu);

    // constructor
    public ManagedObjectBuilder(String name, OID subOID, SMIAccess access) {
        this.name = name;
        this.subOID = subOID;
        this.access = access;
    }
}
