/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caronyn
 */
public class StringTransfert implements ClipboardOwner {

	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// do nothing
	}

	public String getClipboardContent() {
		String ret = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable content = clipboard.getContents(null);

		boolean isTransferable = (content != null) && content.isDataFlavorSupported(DataFlavor.stringFlavor);

		if (isTransferable) {
			try {
				ret = (String) content.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				// do nothing
			} catch (IOException ex) {
				// do nothing
			}
		}

		return ret;
	}

	public void setClipboardContent(String content) {
		StringSelection stringSelection = new StringSelection(content);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, this);
	}
}
