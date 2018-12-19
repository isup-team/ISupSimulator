package isupsimulator.ch.skyguide.isupSimulator.ui.event;

import ch.skyguide.common.event.Event;
import javax.swing.JComponent;

/**
 * Action event for MVP, the observer.
 * @author caronyn
 */
public class DataMVPEvent<T> extends Event<JComponent> {

	// attributes
	private T data;

	// property
	/**
	 * The local data getter.
	 * @return Returh the data.
	 */
	public T getData() {
		return data;
	}

	/**
	 * Default Constructor.
	 * @param iSource Source object.
	 * @param data The data changed.
	 */
	public DataMVPEvent(JComponent iSource, T data) {
		super(iSource);
		this.data = data;
	}
}
