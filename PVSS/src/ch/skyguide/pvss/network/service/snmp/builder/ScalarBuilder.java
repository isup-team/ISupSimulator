/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp.builder;

import ch.skyguide.pvss.network.service.snmp.OIDUtils;
import ch.skyguide.pvss.network.service.snmp.SMIAccess;
import ch.skyguide.pvss.network.service.snmp.SNMPConvertTable;
import ch.skyguide.pvss.network.service.snmp.mo.MOScalarDynamicRestricted;
import org.snmp4j.PDU;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

/**
 *
 * @author caronyn
 */
public class ScalarBuilder extends ManagedObjectBuilder {

    // attributes
    private SNMPConvertTable<? extends Variable> convertTable;
    private Variable value;

    // properties
    public Variable getValue() {
        return value;
    }

    public void setValue(Variable value) {
        this.value = value;
    }

    public void setConvertTable(SNMPConvertTable<? extends Variable> convertTable) {
        this.convertTable = convertTable;
    }

    public void removeConvertTable() {
        convertTable = null;
    }

    public SNMPConvertTable<? extends Variable> getConvertTable() {
        return convertTable;
    }

    // methode
    @Override
    public ManagedObject getObject() {
        return new MOScalarDynamicRestricted(this);
    }

    @Override
    public void buildPDU(PDU pdu) {
        pdu.add(new VariableBinding(OIDUtils.concatOID(getParentMib().getPrefixOID(), getOID()), this.getValue()));
    }

    // constructor
    private ScalarBuilder() {
        super("", new OID(), SMIAccess.READ_ONLY);
    }

    public ScalarBuilder(String name, OID subOID, SMIAccess access, Variable value) {
        super(name, subOID, access);
        this.value = value;
    }

    public ScalarBuilder(String name, OID subOID, SMIAccess access, Variable value, SNMPConvertTable convertTable) {
        super(name, subOID, access);
        this.value = value;
        this.convertTable = convertTable;
    }
}
