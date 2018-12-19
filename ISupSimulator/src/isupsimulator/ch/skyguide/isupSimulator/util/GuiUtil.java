/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author caronyn
 */
public class GuiUtil {

	public static void buildRecentMenu(List list, JMenu menu, ActionListener listener) {
		for (Object object : list) {
			File file = new File(object.toString());

			JMenuItem item = new JMenuItem(file.getName());
			item.addActionListener(listener);
			item.setActionCommand(object.toString());
			menu.insert(item, 0);
		}
	}

	public static void adjustColumnPreferedWidth(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();

		for (int col = 0; col < table.getColumnCount(); col++) {
			int maxWidth = 0;

			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer rend = table.getCellRenderer(row, col);
				Object value = table.getValueAt(row, col);
				Component comp = rend.getTableCellRendererComponent(table, value, false, false, row, col);

				maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);
			}

			columnModel.getColumn(col).setPreferredWidth(maxWidth);
		}
	}

	public static void centerWindow(Window w) {
		// After packing a Frame or Dialog, centre it on the screen.
		Dimension us = w.getSize(), them = Toolkit.getDefaultToolkit().getScreenSize();
		int newX = (them.width - us.width) / 2;
		int newY = (them.height - us.height) / 2;
		w.setLocation(newX, newY);
	}

	public static void maximizeWindow(Window w) {
		Dimension us = w.getSize(), them = Toolkit.getDefaultToolkit().getScreenSize();
		w.setBounds(0, 0, them.width, them.height);
	}
}
