/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import ch.skyguide.pvss.network.service.convertTable.EnumConverter;
import javax.swing.JTable;

/**
 *
 * @author caronyn
 */
public class ColumnDefinedConvertTableTableRenderer extends ConvertTableTableRenderer {

    String typeColumnName, convertTableColumnName;

    public ColumnDefinedConvertTableTableRenderer(String typeColumnName, String convertTableColumnName) {
        this.typeColumnName = typeColumnName;
        this.convertTableColumnName = convertTableColumnName;
    }

    @Override
    protected EnumConverter getEnumConverter(JTable table, int row, int column) {

        int typeColumn = table.getColumn(typeColumnName).getModelIndex();
        return (EnumConverter) table.getModel().getValueAt(row, typeColumn);
    }

    @Override
    protected ConvertTable getConvertTable(JTable table, int row, int column) {
        int valueListColumn = table.getColumn(convertTableColumnName).getModelIndex();
        return (ConvertTable) table.getModel().getValueAt(row, valueListColumn);
    }

    @Override
    protected void setConvertTable(JTable table, int row, int column, ConvertTable convertTable) {
        int valueListColumn = table.getColumn(convertTableColumnName).getModelIndex();
        table.getModel().setValueAt(convertTable, row, valueListColumn);
    }
}
