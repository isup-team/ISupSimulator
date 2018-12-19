/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp.builder;

import ch.skyguide.pvss.network.service.snmp.OIDUtils;
import ch.skyguide.pvss.network.service.snmp.SMIAccess;
import java.util.ArrayList;
import java.util.List;
import org.snmp4j.PDU;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.DefaultMOTable;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.smi.OID;

/**
 *
 * @author caronyn
 */
public class TableBuilder extends ManagedObjectBuilder {

	// attributes
	private List<ColumnBuilder> columns;
	private List<RowBuilder> rows;

	// properties
	public void addColumnBuilder(ColumnBuilder columnBuilder) {
		initializeColumns();
		columns.add(columnBuilder);
	}

	public void addRowBuilder(RowBuilder rowBuilder) {
		initializeRows();
		rows.add(rowBuilder);
	}

	public void removeRowBuilder(int index) {
		initializeRows();
		rows.remove(index);
	}

	private void initializeRows() {
		if (rows == null) {
			rows = new ArrayList<RowBuilder>();
		}
	}

	private void initializeColumns() {
		if (columns == null) {
			columns = new ArrayList<ColumnBuilder>();
		}
	}

	public List<ColumnBuilder> getColumns() {
		initializeColumns();
		return columns;
	}

	public List<RowBuilder> getRows() {
		initializeRows();
		return rows;
	}

	// methode
	@Override
	public ManagedObject getObject() {
		List<MOColumn> cols = new ArrayList<MOColumn>();
		for (ColumnBuilder colBuilder : columns) {
			MOColumn col = colBuilder.getObject();
			col.setAccess(this.getAccess().getMOAccess());
			cols.add(col);
		}

		MOTable table = new DefaultMOTable(
				OIDUtils.concatOID(getParentMib().getPrefixOID(), getOID()),
				null, cols.toArray(new MOColumn[0]));

		for (RowBuilder rowBuilder : rows) {
			table.addRow(rowBuilder.getObject());
		}

		return table;
	}

	@Override
	public void buildPDU(PDU pdu) {
		// TODO
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	// constructor
	private TableBuilder() {
		super("", new OID(), SMIAccess.READ_ONLY);
		initializeColumns();
		initializeRows();
	}
	
	public TableBuilder(String name, OID subOID, SMIAccess access) {
		super(name, subOID, access);
		initializeColumns();
		initializeRows();
	}
}
