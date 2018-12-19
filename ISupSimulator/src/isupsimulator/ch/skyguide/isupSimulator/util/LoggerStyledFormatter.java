/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.util;

import ch.skyguide.common.ui.logger.StyledFormatter;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Catch log for styled JTextPane.
 * @author caronyn
 */
public class LoggerStyledFormatter extends StyledFormatter {

	// constant
	private enum LevelColor {

		OFF(Level.OFF, Color.RED),
		SEVERE(Level.SEVERE, Color.RED),
		WARNING(Level.WARNING, Color.ORANGE),
		INFO(Level.INFO, Color.BLACK),
		CONFIG(Level.CONFIG, Color.BLACK),
		FINE(Level.FINE, Color.GRAY),
		FINER(Level.FINER, Color.GRAY),
		FINEST(Level.FINEST, Color.GRAY);
		// attribute
		private Color color;
		private Level level;

		// property
		public Color getColor() {
			return color;
		}

		public Level getLevel() {
			return level;
		}

		// methode
		public static LevelColor valueOf(Level level) {
			for (LevelColor vc : LevelColor.values()) {
				if (vc.level.equals(level)) {
					return vc;
				}
			}
			return null;
		}

		// constructor
		private LevelColor(Level level, Color color) {
			this.level = level;
			this.color = color;
		}
	}
	private static final String DATE_FORMAT = "HH:MM:ss.sss";

	private AttributeSet getHeadAttributeSet(Level level) {
		MutableAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setBold(set, true);
		StyleConstants.setForeground(set, LevelColor.valueOf(level).getColor());

		return set;
	}

	private AttributeSet getMessageAttributeSet(Level level) {
		MutableAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setForeground(set, LevelColor.valueOf(level).getColor());

		return set;
	}

	@Override
	public void format(LogRecord record) {
		StringBuilder sb = new StringBuilder();
		Date date = new Date();
		date.setTime(record.getMillis());
		String strDate = new SimpleDateFormat(DATE_FORMAT).format(date);

		if (record.getMessage() != null) {
			sb.append(getFormatter().formatMessage(record));
		}

		if (record.getThrown() != null) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(record.getThrown().getClass().getName());
			sb.append(": ");
			sb.append(record.getThrown().getMessage());
		}

		appendToDocument(record.getLevel().getName(), getHeadAttributeSet(record.getLevel()));
		appendToDocument(" [", getHeadAttributeSet(record.getLevel()));
		appendToDocument(strDate, getHeadAttributeSet(record.getLevel()));
		appendToDocument("] : ", getHeadAttributeSet(record.getLevel()));
		appendToDocument(sb.toString(), getMessageAttributeSet(record.getLevel()));
		appendToDocument(getLineSeparator(), null);

	}

	public LoggerStyledFormatter(JTextPane textPane, Formatter formatter) {
		super(textPane, formatter);
	}
}
