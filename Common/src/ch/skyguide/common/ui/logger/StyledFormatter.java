/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author caronyn
 */
public abstract class StyledFormatter {

	// constants
	public static final int DEFAULT_BUFFER_SIZE = 1000000;
	// attributes
	private StyledDocument document;
	private Formatter formatter;
	private String lineSeparator;
	private int bufferSize;

	// property
	public StyledDocument getDocument() {
		return document;
	}

	public Formatter getFormatter() {
		return formatter;
	}

	public String getLineSeparator() {
		return lineSeparator;
	}

	// function
	private void slideDocument() {
		if (document.getLength() > bufferSize) {
			try {
				//int nextLine = getNextTextPosition(lineSeparator, document.getLength() - bufferSize) + lineSeparator.length();
				int nextLine = document.getLength() - bufferSize;
				System.out.println(nextLine);
				document.remove(0, nextLine);
			} catch (BadLocationException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	// methode
	protected void appendToDocument(String text, AttributeSet set) {
		try {
			document.insertString(document.getLength(), text, set);
			slideDocument();
		} catch (BadLocationException ex) {
			throw new RuntimeException(ex.getMessage(), ex.getCause());
		}
	}

	public abstract void format(LogRecord record);

	// constructor
	public StyledFormatter(JTextPane textPane, Formatter formatter) {
		this(textPane, formatter, DEFAULT_BUFFER_SIZE);
	}

	public StyledFormatter(JTextPane textPane, Formatter formatter, int bufferSize) {
		this.document = textPane.getStyledDocument();
		this.formatter = formatter;
		this.bufferSize = bufferSize;
		this.lineSeparator = (String) java.security.AccessController.doPrivileged(
				new sun.security.action.GetPropertyAction("line.separator"));
	}
}
