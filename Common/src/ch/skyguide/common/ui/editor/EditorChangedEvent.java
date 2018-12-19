package ch.skyguide.common.ui.editor;

import ch.skyguide.common.event.Event;

/**
 * Action event for MVP, the observer.
 * @author caronyn
 */
public class EditorChangedEvent<T> extends Event<Editor> {

	private T value;

	/**
	 * Value getter.
	 * @return Return the value changed.
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Default constructor.
	 * @param iSource Source object.
	 * @param fileName File to open.
	 */
	public EditorChangedEvent(Editor iSource, T value) {
		super(iSource);
		this.value = value;
	}


}
