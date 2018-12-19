/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import ch.skyguide.pvss.network.service.convertTable.Entry;
import ch.skyguide.pvss.network.service.convertTable.EnumConverter;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author caronyn
 */
public abstract class ConvertTableTableRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // get converters
        EnumConverter syntax = getEnumConverter(table, row, column);
        ConvertTable convertTable = getConvertTable(table, row, column);

        // get colours
        Color color = table.getBackground();
        if (isSelected) {
            color = table.getSelectionBackground();
        }

        // set colors
        try {
            if (convertTable != null) {
                Entry entry = convertTable.find(syntax.toVariable(value));
                if (entry != null) {
                    color = entry.getStatus().getColor();

                    if (isSelected) {
                        color = color.darker();
                    }

                }
            }
            setBackground(color);
        } catch (ClassCastException ex) {
            setConvertTable(table, row, column, null);
        } catch (IllegalArgumentException ex) {
            setConvertTable(table, row, column, null);
        }

        return this;

    }

    protected abstract EnumConverter getEnumConverter(JTable table, int row, int column);

    protected abstract ConvertTable getConvertTable(JTable table, int row, int column);

    protected abstract void setConvertTable(JTable table, int row, int column, ConvertTable convertTable);
}
