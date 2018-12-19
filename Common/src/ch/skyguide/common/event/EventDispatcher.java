package ch.skyguide.common.event;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

public class EventDispatcher<L extends Listener> {

	private List<L> _listeners = new ArrayList<L>();

	public void addListener(L iListener) {
		_listeners.add(iListener);
	}

	public void removeListener(L iListener) {
		_listeners.remove(iListener);
	}

	public void invoke(String iMethodeName, Object iEvent) {
		for (L listener : _listeners) {
			try {
				Method method = listener.getClass().getMethod(
						iMethodeName, iEvent.getClass());
				method.setAccessible(true);
				method.invoke(listener, iEvent);
			} catch (Exception ex) {
				System.err.println("Error in methode call : " + iMethodeName);
				ex.getCause().printStackTrace();
			}
		}
	}
}
