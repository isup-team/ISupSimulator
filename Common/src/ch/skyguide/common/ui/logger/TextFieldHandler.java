/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui.logger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 *
 * @author caronyn
 */
public class TextFieldHandler extends Handler {

	private StyledFormatter formatter;

	// methodes
	@Override
	public synchronized void publish(LogRecord record) {
		//System.out.println(this.getLevel() + " " + isLoggable(record));
		if (!isLoggable(record)) {
			return;
		}
		formatter.format(record);
	}

	@Override
	public void flush() {
		// do nothing
	}

	@Override
	public void close() throws SecurityException {
		// do nothing
	}

	// constructor
	public TextFieldHandler(StyledFormatter formatter) {
		super();
		this.formatter = formatter;
		setFormatter(formatter.getFormatter());
	}
}
