/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.socket;

/**
 *
 * @author caronyn
 */
public class SocketUtil {

	public static void waitRest(Object object, Long period, Long time) throws InterruptedException {
		synchronized (object) {
			long timeelapsed = Math.max(0, System.currentTimeMillis() - time);
			long wait = (Math.max(1, period - timeelapsed));

			object.wait(wait);
		}
	}
}
