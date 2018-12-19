package isupsimulator.ch.skyguide.isupSimulator.ui.event;

import ch.skyguide.common.event.Event;
import javax.swing.JComponent;

/**
 * Action event for MVP, the observer.
 * @author caronyn
 */
public class FileMVPEvent extends Event<JComponent> {

	// attributes
	private String fileName;

	// property
	/**
	 * File name getter.
	 * @return Return the file name to open.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Default constructor.
	 * @param iSource Source object.
	 * @param fileName File to open.
	 */
	public FileMVPEvent(JComponent iSource, String fileName) {
		super(iSource);
		this.fileName = fileName;
	}

}
