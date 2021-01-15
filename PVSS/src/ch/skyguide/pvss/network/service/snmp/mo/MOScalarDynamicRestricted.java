/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp.mo;

import ch.skyguide.pvss.network.service.snmp.OIDUtils;
import ch.skyguide.pvss.network.service.snmp.builder.ScalarBuilder;
import org.snmp4j.agent.DefaultMOScope;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.MOScope;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

/**
 *
 * @author caronyn
 */
public class MOScalarDynamicRestricted extends MOScalar {

    private ScalarBuilder builder;

    @Override
    public MOAccess getAccess() {
        return builder.getAccess().getMOAccess();
    }

    @Override
    public OID getOid() {
        OID oid = OIDUtils.concatOID(builder.getParentMib().getPrefixOID(), builder.getOID());
        return oid;
    }

    @Override
    public MOScope getScope() {
        OID oid = getOid();
        return new DefaultMOScope(oid, true, oid, true);
    }

    @Override
    public Variable getValue() {
        return builder.getValue();
    }

    public MOScalarDynamicRestricted(ScalarBuilder builder) {
        super(OIDUtils.concatOID(builder.getParentMib().getPrefixOID(), builder.getOID()), builder.getAccess().getMOAccess(), builder.getValue());
        this.builder = builder;
    }
}
