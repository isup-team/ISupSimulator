/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml;

/**
 *
 * @author caronyn
 */
public class ISupStatus {

    private String version;
    private ISupSystem system;

    public ISupStatus(String version, ISupSystem system) {
        this.version = version;
        this.system = system;
    }

    public ISupStatus(ISupSystem system) {
        this.system = system;
        this.version = "1.0";
    }
}
