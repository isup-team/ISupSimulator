/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui.documentStyleManager.update;

import ch.skyguide.common.ui.documentStyleManager.token.AbstractTokenizer;
import java.awt.Component;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;

/**
 *
 * @author CyaNn
 */
public class AsynchronousUpdater extends UpdateStrategy {

    private DocumentEvent docEvent;
    private UpdateThread thread = new UpdateThread();
    private boolean isUpdated;
    private long lastUpdate = 0;

    private class UpdateThread extends Thread {

	@Override
	public void run() {

	    while (!this.isInterrupted()) {
		try {

		    while (isUpdated) {
			Thread.sleep(100);
		    }

		    tokenize(docEvent);
		    repaintComponent();
		    isUpdated = true;

		    while (Calendar.getInstance().getTimeInMillis() < lastUpdate + 500) {
			Thread.sleep(100);
		    }

		} catch (BadLocationException ex) {
		    Logger.getLogger(AsynchronousUpdater.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
		    Logger.getLogger(AsynchronousUpdater.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }

	}
    }

    @Override
    public void update(DocumentEvent e) throws BadLocationException {
	docEvent = e;

	if (!thread.isAlive()) {
	    thread.start();
	}

	lastUpdate = Calendar.getInstance().getTimeInMillis();
	isUpdated = false;
    }

    public AsynchronousUpdater(AbstractTokenizer tokenizer) {
	super(tokenizer);
	isUpdated = false;
    }
}
