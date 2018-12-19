/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service;

import ch.skyguide.common.event.*;

/**
 *
 * @author caronyn
 */
public abstract class ServiceLeaf implements Service {

    // attributes
    private Status status;
    private String name;
    private EventDispatcher<ServiceListener> serviceEventDispatcher;

    // serialization
    private Object readResolve() {
        initialize();
        return this;
    }

    // function
    private void initialize() {
        this.serviceEventDispatcher = new EventDispatcher<ServiceListener>();
        this.status = Status.STOPPED;
    }

    // properties
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public final synchronized Status getStatus() {
        return status;
    }

    public final synchronized void setStatus(Status status) {
        this.status = status;

        if (status == Status.STARTED) {
            fireServiceStarted();
        } else if (status == Status.STOPPED) {
            fireServiceStopped();
        }
        fireServiceChanged();

    }

    public Service getAgent(int i) {
        throw new UnsupportedOperationException("Not supported in leaf.");
    }

    // event
    public void addServiceListener(ServiceListener iListener) {
        serviceEventDispatcher.addListener(iListener);
    }

    public void removeServiceListener(ServiceListener iListener) {
        serviceEventDispatcher.removeListener(iListener);
    }

    public void fireServiceStarted() {
        serviceEventDispatcher.invoke("serviceStarted", new Event<ServiceLeaf>(this));
    }

    public void fireServiceStopped() {
        serviceEventDispatcher.invoke("serviceStopped", new Event<ServiceLeaf>(this));
    }

    public void fireServiceChanged() {
        serviceEventDispatcher.invoke("serviceChanged", new Event<ServiceLeaf>(this));
    }

    // abstract methodes
    protected abstract void startAgent();

    protected abstract void stopAgent();

    protected abstract void restartAgent();

    // internal methodes
    @Override
    public void start() {
        if (status != Status.STARTED) {
            startAgent();
        }
    }

    @Override
    public void stop() {
        if (status != Status.STOPPED) {
            stopAgent();
        }
    }

    @Override
    public void restart() {
        if (status != Status.STOPPED) {
            restartAgent();
        }
    }

    // constructor
    public ServiceLeaf(String name) {
        this.name = name;
        initialize();
    }
}
