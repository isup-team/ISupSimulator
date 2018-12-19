/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import isupsimulator.ch.skyguide.isupSimulator.util.GuiUtil;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @param <T> 
 * @author caronyn
 */
public abstract class TableColumnDefinition<T> {

    private String name;

    public String getName() {
        return name;
    }

    public abstract T getField(int rowIndex);

    public abstract void setField(int rowIndex, T value);

    public abstract boolean isEditable();

    public TableCellEditor getTableCellEditor() {
        return null;
    }

    public TableCellRenderer getTableCellRenderer() {
        return null;
    }

    public Class getColumnClass() {
        return Object.class;
    }

    public static void applyEditors(JTable table, TableColumnDefinition[] columnDefinitions) {
        for (TableColumnDefinition columnDefinition : columnDefinitions) {
            if (columnDefinition.getTableCellRenderer() != null) {
                table.getColumn(columnDefinition.getName()).setCellRenderer(
                        columnDefinition.getTableCellRenderer());
            }
            if (columnDefinition.getTableCellEditor() != null) {
                table.getColumn(columnDefinition.getName()).setCellEditor(
                        columnDefinition.getTableCellEditor());
            }
        }
    }

    public static void adjustColumnWidth(final JTable table) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                GuiUtil.adjustColumnPreferedWidth(table);
                table.revalidate();
                table.repaint();
            }
        });
    }

    public TableColumnDefinition(String name) {
        this.name = name;
    }
}
