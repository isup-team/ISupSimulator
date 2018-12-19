/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.snmp4j.agent.mo.DefaultMOTableRow;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

/**
 *
 * @author caronyn
 */
public class RowBuilder implements Builder<MOTableRow>, Iterable<Variable> {

    // attributes
    private OID index;
    private ArrayList<Variable> values;

    // properties
    public OID getOID() {
        return index;
    }

    public void setOID(OID index) {
        this.index = index;
    }

    public List<Variable> getValues() {
        return values;
    }

    public void addValue(int index, Variable element) {
        values.add(index, element);
    }

    public Variable removeValue(int index) {
        return values.remove(index);
    }

    @Override
    public Iterator<Variable> iterator() {
        return values.iterator();
    }

    // methode
    public void addValue(Variable value) {
        values.add(value);
    }

    @Override
    public MOTableRow getObject() {
        return new DefaultMOTableRow(index, values.toArray(new Variable[0]));
    }

    // constructor
    private RowBuilder() {
        values = new ArrayList<Variable>();
    }

    public RowBuilder(OID index) {
        this.index = index;
        values = new ArrayList<Variable>();
    }
}
