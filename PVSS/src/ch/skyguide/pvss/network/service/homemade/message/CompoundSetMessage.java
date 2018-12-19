/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.homemade.message;

import ch.skyguide.pvss.network.service.homemade.HMSubSystem;
import java.util.List;

/**
 *
 * @author caronyn
 */
public class CompoundSetMessage extends Message {

	private List<HMSubSystem> subSystems;

	@Override
	protected void appendMessageContent(StringBuilder sb) {

		for (HMSubSystem subSystem : subSystems) {
			Message msg = new SetMessage(subSystem.getName(), subSystem.getValue().toString());
			msg.appendMessageContent(sb);
		}

	}

	public CompoundSetMessage(List<HMSubSystem> subSystems) {
		this.subSystems = subSystems;
	}

}
