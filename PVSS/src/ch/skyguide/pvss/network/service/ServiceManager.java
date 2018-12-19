/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service;

/**
 *
 * @author caronyn
 */
public class ServiceManager {

    // attributes
    static private ServiceManager singleton;
    private ServiceComposite root;

    // properties
    public ServiceComposite getRoot() {
	return root;
    }

    public void setRoot(ServiceComposite root) {
	this.root = root;
    }

    // constructor
    private ServiceManager() {
	root = new ServiceComposite("root");
    }

    static public ServiceManager getInstance() {
	if (singleton == null) {
	    singleton = new ServiceManager();
	}
	return singleton;
    }

}
