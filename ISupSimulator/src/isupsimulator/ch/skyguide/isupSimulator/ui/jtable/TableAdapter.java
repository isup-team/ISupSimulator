/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import ch.skyguide.pvss.network.service.snmp.builder.ColumnBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.RowBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.TableBuilder;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

/**
 *
 * @author caronyn
 */
public class TableAdapter extends AbstractTableModel implements EditableTableModel {

	public static final String OID_COLUMN_NAME = "OID";
	TableBuilder tableBuilder;

	@Override
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	@Override
	public int getRowCount() {
		return tableBuilder.getRows().size();
	}

	@Override
	public int getColumnCount() {
		return tableBuilder.getColumns().size() + 1;
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0) {
			return OID_COLUMN_NAME;
		} else {
			return tableBuilder.getColumns().get(column - 1).getName();
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == 0) {
			return tableBuilder.getRows().get(row).getOID();
		} else {
			List<Variable> values = tableBuilder.getRows().get(row).getValues();
			if (values.isEmpty()) {
				// lazy initialization
				for (ColumnBuilder cb : tableBuilder.getColumns()) {
					values.add(cb.getSyntax().getDefaultVariable());
				}
			}
			SMISyntax syntax = tableBuilder.getColumns().get(column - 1).getSyntax();
			Variable value = values.get(column - 1);
			return syntax.toJava(value);
		}
	}

	private void initializeValues() {
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == 0) {
			SMISyntax syntax = SMISyntax.OBJECT_IDENTIFIER;
			tableBuilder.getRows().get(row).setOID((OID) syntax.toVariable(aValue));
		} else {
			RowBuilder builder = tableBuilder.getRows().get(row);
			SMISyntax syntax = tableBuilder.getColumns().get(column - 1).getSyntax();

			try {
				builder.getValues().set(column - 1, syntax.toVariable(aValue));
			} catch (Exception e) {
				builder.getValues().set(column - 1, syntax.getDefaultVariable());
			}
		}
		fireTableCellUpdated(row, column);
	}

	public TableAdapter(TableBuilder tableBuilder) {
		this.tableBuilder = tableBuilder;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return Object.class;
		} else {
			return String.class;
		}
	}

	public void insertNewRow(int row) {
		tableBuilder.addRowBuilder(new RowBuilder(new OID("0")));
		this.fireTableRowsInserted(row, row);
	}

	public void removeRow(int row) {
		tableBuilder.removeRowBuilder(row);
		this.fireTableRowsDeleted(row, row);
	}

	public void moveRow(int start, int stop, int to) {
		this.fireTableChanged(new TableModelEvent(this));
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
