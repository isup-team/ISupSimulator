/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.socket;

/**
 *
 * @author caronyn
 */
public interface MessageProvider {

	String getHeartbeatMessage();
	String getStatusMessage();

	// event
	void messageReceived(String msg);

}
