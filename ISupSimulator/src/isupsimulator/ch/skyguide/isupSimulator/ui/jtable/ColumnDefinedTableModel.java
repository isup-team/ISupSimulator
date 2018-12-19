/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author caronyn
 */
public abstract class ColumnDefinedTableModel extends AbstractTableModel implements EditableTableModel {

	protected TableColumnDefinition[] columnDefinitions;

	@Override
	public boolean isCellEditable(int row, int column) {
		return columnDefinitions[column].isEditable();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnDefinitions[columnIndex].getColumnClass();
	}

	public int getColumnCount() {
		return columnDefinitions.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnDefinitions[column].getName();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return columnDefinitions[columnIndex].getField(rowIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		columnDefinitions[columnIndex].setField(rowIndex, aValue);
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public ColumnDefinedTableModel(TableColumnDefinition[] columnDefinitions) {
		this.columnDefinitions = columnDefinitions;
	}
}
