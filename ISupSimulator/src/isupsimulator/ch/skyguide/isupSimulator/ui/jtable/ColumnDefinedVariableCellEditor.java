/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author caronyn
 */
public class ColumnDefinedVariableCellEditor extends VariableCellEditor {

	private String convertTableColumnName;

	@Override
	public ConvertTable getConvertTable(JTable table, int row, int column) {
		TableColumn valueListCol = table.getColumn(convertTableColumnName);
		return (ConvertTable) table.getModel().getValueAt(row, valueListCol.getModelIndex());
	}

	public ColumnDefinedVariableCellEditor(String convertTableColumnName) {
		super();
		this.convertTableColumnName = convertTableColumnName;
	}

}
