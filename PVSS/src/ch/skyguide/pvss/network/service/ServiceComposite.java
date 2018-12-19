/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service;

import ch.skyguide.pvss.data.Database;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @param <T>
 * @author caronyn
 */
public class ServiceComposite<T extends Service> implements Service, Iterable<T> {

    // attributes
    private String name;
    private ArrayList<T> children;

    // methode
    @Override
    public final Status getStatus() {
        // composite recursive
        boolean allStarted = true, allStopped = true;
        
        if (children.isEmpty()) {
            allStarted = false;
            allStopped = true;
        }
        
        for (Service child : children) {
            if (child.getStatus() != Status.STARTED) {
                allStarted = false;
            }
            if (child.getStatus() != Status.STOPPED) {
                allStopped = false;
            }
        }
        
        if (allStarted) {
            return Status.STARTED;
        } else if (allStopped) {
            return Status.STOPPED;
        } else {
            return Status.MITIGATED;
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    public void addService(T service) {
        children.add(service);
    }
    
    public void removeService(T service) {
        children.remove(service);
    }
    
    public T getService(int i) {
        return children.get(i);
    }
    
    public T getService(String name) {
        for (T service : children) {
            if (service.getName().equals(name)) {
                return service;
            }
        }
        throw new IndexOutOfBoundsException("Service with name : [" + name + "]] does not exists !");
    }
    
    @Override
    public Iterator<T> iterator() {
        if (children == null) {
            children = new ArrayList<T>();
        }
        return children.iterator();
    }
    
    @Override
    public void start() {
        // composite recursive
        for (Service agent : children) {
            agent.start();
        }
    }
    
    @Override
    public void stop() {
        // composite recursive
        for (Service agent : children) {
            agent.stop();
        }
    }
    
    @Override
    public void restart() {
        // composite recursive
        for (Service agent : children) {
            agent.restart();
        }
    }
    
    @Override
    public void buildDpl(Database db) {
        for (T child : children) {
            child.buildDpl(db);
        }
    }

    // constructor
    private ServiceComposite() {
    }
    
    public ServiceComposite(String name) {
        children = new ArrayList<T>();
        this.name = name;
    }
}
