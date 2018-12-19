/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui;

/**
 * Inspired from pattern Model / View / Presenter for macro visual components.
 * All panels controlers must implements this interface.
 * @author caronyn
 */
public interface Presenter {

	/**
	 * Getter of the presented view.
	 * @return Return the view.
	 */
	public View getView();
}
