/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.dataPointType.NodeType;

/**
 *
 * @author caronyn
 */
public interface Service {

    // inner class
    public enum Status {

        STARTED, STOPPED, MITIGATED;
    }
    
    // properties
    Status getStatus();

    String getName();

    void setName(String name);

    // methode
    void start();

    void stop();

    void restart();

    void buildDpl(Database db);

}
