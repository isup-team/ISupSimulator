/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp;

import ch.skyguide.pvss.network.service.convertTable.EnumConverter;
import ch.skyguide.pvss.network.service.convertTable.TypeConverter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.snmp4j.smi.*;

// null
class SMINull implements TypeConverter<Variable> {

    @Override
    public Class getVariableType() {
        return Null.class;
    }

    @Override
    public Variable toVariable(Object value) {
        return getDefaultVariable();
    }

    @Override
    public Object toJava(Variable value) {
        return getDefaultJava();
    }

    @Override
    public Variable getDefaultVariable() {
        return new Null();
    }

    @Override
    public Object getDefaultJava() {
        return new Null().toString();
    }

}

// integer
class SMIInteger32 implements TypeConverter<Variable> {

    @Override
    public Class getVariableType() {
        return Integer32.class;
    }

    @Override
    public Variable toVariable(Object value) {
        if (value instanceof String) {
            value = Integer.parseInt((String) value);
        }
        return new Integer32((Integer) value);
    }

    @Override
    public Object toJava(Variable value) {
        return ((Integer32) value).toInt();
    }

    @Override
    public Variable getDefaultVariable() {
        return new Integer32(0);
    }

    @Override
    public Object getDefaultJava() {
        return new Integer(0);
    }

}

class SMIUnsignedInteger32 implements TypeConverter<Variable> {

    @Override
    public Class getVariableType() {
        return UnsignedInteger32.class;
    }

    @Override
    public Variable toVariable(Object value) {
        if (value instanceof String) {
            value = Integer.parseInt((String) value);
        }
        return new UnsignedInteger32((Integer) value);
    }

    @Override
    public Object toJava(Variable value) {
        return ((UnsignedInteger32) value).toInt();
    }

    @Override
    public Variable getDefaultVariable() {
        return new UnsignedInteger32(0);
    }

    @Override
    public Object getDefaultJava() {
        return new Integer(0);
    }

}

class SMIGauge32 implements TypeConverter<Variable> {

    @Override
    public Class getVariableType() {
        return Gauge32.class;
    }

    @Override
    public Variable toVariable(Object value) {
        if (value instanceof String) {
            value = Long.parseLong((String) value);
        } else if (value instanceof Integer) {
            value = new Long((Integer) value);
        }
        return new Gauge32((Long) value);
    }

    @Override
    public Object toJava(Variable value) {
        return ((Gauge32) value).toLong();
    }

    @Override
    public Variable getDefaultVariable() {
        return new Gauge32(0);
    }

    @Override
    public Object getDefaultJava() {
        return new Long(0);
    }
}

class SMICounter32 implements TypeConverter<Variable> {

    @Override
    public Class getVariableType() {
        return Counter32.class;
    }

    @Override
    public Variable toVariable(Object value) {
        if (value instanceof String) {
            value = Long.parseLong((String) value);
        } else if (value instanceof Integer) {
            value = new Long((Integer) value);
        }
        return new Counter32((Long) value);
    }

    @Override
    public Object toJava(Variable value) {
        return ((Counter32) value).toLong();
    }

    @Override
    public Variable getDefaultVariable() {
        return new Counter32(0);
    }

    @Override
    public Object getDefaultJava() {
        return new Long(0);
    }
}

class SMICounter64 implements TypeConverter<Variable> {

    @Override
    public Class getVariableType() {
        return Counter64.class;
    }

    @Override
    public Variable toVariable(Object value) {
        if (value instanceof String) {
            value = Long.parseLong((String) value);
        } else if (value instanceof Integer) {
            value = new Long((Integer) value);
        }
        return new Counter64((Long) value);
    }

    @Override
    public Object toJava(Variable value) {
        return ((Counter64) value).toLong();
    }

    @Override
    public Variable getDefaultVariable() {
        return new Counter64(0);
    }

    @Override
    public Object getDefaultJava() {
        return new Long(0);
    }
}

// time
class SMITimeTicks implements TypeConverter<Variable> {

    @Override
    public Class getVariableType() {
        return TimeTicks.class;
    }
    public static final String FORMAT = "HH:mm:ss.SS";

    @Override
    public Variable toVariable(Object value) {
        if (value instanceof String) {
            DateFormat timeF = new SimpleDateFormat(FORMAT);
            try {
                return new TimeTicks(timeF.parse((String) value).getTime());
            } catch (ParseException ex) {
                // do nothing
            }
        } else if (value instanceof Long) {
            return new TimeTicks((Long) value);
        }
        return getDefaultVariable();
    }

    @Override
    public Object toJava(Variable value) {
        DateFormat timeF = new SimpleDateFormat(FORMAT);
        TimeTicks t = ((TimeTicks) value);
        return timeF.format(new Date(t.getValue()));
    }

    @Override
    public Variable getDefaultVariable() {
        return new TimeTicks(0);
    }

    @Override
    public Object getDefaultJava() {
        DateFormat timeF = new SimpleDateFormat(FORMAT);
        ;
        return timeF.format(new Date(0));
    }
}

// string
class SMIOctetString implements TypeConverter<Variable> {

    @Override
    public Class getVariableType() {
        return OctetString.class;
    }

    @Override
    public Variable toVariable(Object value) {
        return new OctetString((String) value);
    }

    @Override
    public Object toJava(Variable value) {
        return ((OctetString) value).toString();
    }

    @Override
    public Variable getDefaultVariable() {
        return new OctetString("");
    }

    @Override
    public Object getDefaultJava() {
        return "";
    }
}

class SMIObjectIdentifier implements TypeConverter<Variable> {

    @Override
    public Class getVariableType() {
        return OID.class;
    }

    @Override
    public Variable toVariable(Object value) {
        return new OID((String) value);
    }

    @Override
    public Object toJava(Variable value) {
        return ((OID) value).toString();
    }

    @Override
    public Variable getDefaultVariable() {
        return new OID();
    }

    @Override
    public Object getDefaultJava() {
        return "";
    }
}

class SMIIPAddress implements TypeConverter<Variable> {

    @Override
    public Class getVariableType() {
        return IpAddress.class;
    }

    @Override
    public Variable toVariable(Object value) {
        return new IpAddress((String) value);
    }

    @Override
    public Object toJava(Variable value) {
        return ((IpAddress) value).toString();
    }

    @Override
    public Variable getDefaultVariable() {
        return new IpAddress();
    }

    @Override
    public Object getDefaultJava() {
        return new IpAddress().toString();
    }
}

// binary
class SMIOpaque implements TypeConverter<Variable> {

    public static final char DELIM = ':';
    public static final byte[] NULL = {0x00, 0x00, 0x00, 0x00};

    @Override
    public Class getVariableType() {
        return Opaque.class;
    }

    @Override
    public Variable toVariable(Object value) {
        String v = (String) value;
        return new Opaque(Opaque.fromHexString(v, DELIM).getValue());
    }

    @Override
    public Object toJava(Variable value) {
        return ((Opaque) value).toString();
    }

    @Override
    public Variable getDefaultVariable() {
        return new Opaque(NULL);
    }

    @Override
    public Object getDefaultJava() {
        return toJava(getDefaultVariable());
    }
}

public enum SMISyntax implements EnumConverter<Variable> {

    NULL(SMIConstants.SYNTAX_NULL, 660, new SMINull()),
    INTEGER(SMIConstants.SYNTAX_INTEGER, 661, new SMIInteger32()),
    UNSIGNED_INTEGER32(SMIConstants.SYNTAX_UNSIGNED_INTEGER32, 662, new SMIUnsignedInteger32()),
    GAUGE32(SMIConstants.SYNTAX_GAUGE32, 662, new SMIGauge32()),
    COUNTER32(SMIConstants.SYNTAX_COUNTER32, 662, new SMICounter32()),
    COUNTER64(SMIConstants.SYNTAX_COUNTER64, 663, new SMICounter64()),
    OCTET_STRING(SMIConstants.SYNTAX_OCTET_STRING, 666, new SMIOctetString()),
    OBJECT_IDENTIFIER(SMIConstants.SYNTAX_OBJECT_IDENTIFIER, 664, new SMIObjectIdentifier()),
    IPADDRESS(SMIConstants.SYNTAX_IPADDRESS, 669, new SMIIPAddress()),
    TIMETICKS(SMIConstants.SYNTAX_TIMETICKS, 662, new SMITimeTicks()),
    OPAQUE(SMIConstants.SYNTAX_OPAQUE, 670, new SMIOpaque()),
    BITS(SMIConstants.SYNTAX_BITS, 666, new SMIOctetString());
    private int smi;
    private int paramDataType;
    private TypeConverter<Variable> type;

    // property
    public int getSmi() {
        return smi;
    }

    public int getParamDataType() {
        return paramDataType;
    }
    
    @Override
    public Class getVariableType() {
        return type.getVariableType();
    }

    @Override
    public Variable toVariable(Object value) {
        return type.toVariable(value);
    }

    @Override
    public Object toJava(Variable value) {
        return type.toJava(value);
    }

    @Override
    public Variable getDefaultVariable() {
        return type.getDefaultVariable();
    }

    @Override
    public Object getDefaultJava() {
        return type.getDefaultJava();
    }

    public static SMISyntax valueOf(int smi) {
        for (SMISyntax s : SMISyntax.values()) {
            if (s.getSmi() == smi) {
                return s;
            }
        }
        return null;
    }

    // constructor
    SMISyntax(int smi, int paramDataType, TypeConverter<Variable> type) {
        this.smi = smi;
        this.paramDataType = paramDataType;
        this.type = type;
    }

    @Override
    public EnumConverter[] allValues() {
        return values();
    }

    @Override
    public EnumConverter find(Variable value) {
        return valueOf(value.getSyntax());
    }
}
