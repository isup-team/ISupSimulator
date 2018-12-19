package isupsimulator.ch.skyguide.isupSimulator.ui.event;

import ch.skyguide.common.event.Event;
import ch.skyguide.pvss.network.service.Service;
import javax.swing.JComponent;

/**
 * Action event for MVP, the observer.
 * @author caronyn
 */
public class ServiceMVPEvent extends Event<JComponent> {

	// attributes
	private Service service;

	// property
	/**
	 * The local service getter.
	 * @return Returh the service.
	 */
	public Service getService() {
		return service;
	}

	/**
	 * Default constructor.
	 * @param iSource Source object.
	 * @param fileName File to open.
	 */
	public ServiceMVPEvent(JComponent iSource, Service service) {
		super(iSource);
		this.service = service;
	}

}
