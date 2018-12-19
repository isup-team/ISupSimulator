/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml.socket;

import java.util.Date;

/**
 *
 * @author caronyn
 */
public class SocketUtil {

	public static void waitRest(Object object, Long period, Long time) throws InterruptedException {
		synchronized (object) {
			long timeelapsed = new Date().getTime() - time;
			if (timeelapsed < 0L) {
				timeelapsed = 0L;
			}
			object.wait(period - timeelapsed);
		}
	}
}
