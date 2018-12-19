/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isupsimulator.ch.skyguide.isupSimulator.ui;

import ch.skyguide.pvss.network.service.Service;
import javax.swing.ImageIcon;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author caronyn
 */
public enum ServiceStatusEnum {

		STARTED(Service.Status.STARTED, "Application.iconOn"),
		STOPPED(Service.Status.STOPPED, "Application.iconOff"),
		MITIGATED(Service.Status.MITIGATED, "Application.iconMitigate");
		Service.Status status;
		String resource;

		public static ServiceStatusEnum valueOf(Service.Status status) {
			for (ServiceStatusEnum st : values()) {
				if (st.status.equals(status)) {
					return st;
				}
			}
			throw new RuntimeException("Status does not exists !");
		}

		public ImageIcon getImageIcon() {
			ResourceMap resourceMap = Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(ISupSimulatorFrame.class);
			return resourceMap.getImageIcon(resource);
		}

		private ServiceStatusEnum(Service.Status status, String resource) {
			this.resource = resource;
			this.status = status;
		}

}
