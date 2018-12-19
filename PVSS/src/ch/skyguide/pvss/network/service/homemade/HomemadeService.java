/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.homemade;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import ch.skyguide.pvss.network.service.ListContainer;
import ch.skyguide.pvss.network.service.Service.Status;
import ch.skyguide.pvss.network.service.ServiceLeaf;
import ch.skyguide.pvss.network.service.homemade.message.AliveMessage;
import ch.skyguide.pvss.network.service.homemade.message.CompoundSetMessage;
import ch.skyguide.pvss.network.service.homemade.message.Message;
import ch.skyguide.pvss.network.service.socket.MessageDelimiter;
import ch.skyguide.pvss.network.service.socket.MessageFormater;
import ch.skyguide.pvss.network.service.socket.ServerThread;
import java.util.LinkedList;

/**
 *
 * @author caronyn
 */
public class HomemadeService extends ServiceLeaf implements ListContainer<HMSubSystem> {

    // inner class
    private class HomemadeFormater implements MessageFormater {

        @Override
        public MessageDelimiter getDelimiter() {
            return MessageDelimiter.RETURN;
        }

        @Override
        public String removeSpecialChar(String msg) {
            return msg;
        }

        @Override
        public String format(String msg) {
            return msg;
        }

        @Override
        public String unFormat(String msg) {
            return msg;
        }
    }

    private class HomemadeServerThread extends ServerThread {

        MessageFormater formater;

        @Override
        public MessageFormater getFormater() {
            return formater;
        }

        @Override
        public String getHeartbeatMessage() {
            Message msg = new AliveMessage();
            return msg.getMessage();
        }

        @Override
        public String getStatusMessage() {
            Message msg = new CompoundSetMessage(HomemadeService.this.subSystems);
            return msg.getMessage();
        }

        @Override
        public void messageReceived(String msg) {
            // Do nothing
            // TODO interpreter le message reçu.
        }

        @Override
        public void statusChanged(boolean isRunning) {
            if (isRunning) {
                HomemadeService.this.setStatus(Status.STARTED);
            } else {
                HomemadeService.this.setStatus(Status.STOPPED);
            }
        }

        public HomemadeServerThread(String name, int port, long heartbeatPeriod, long statusPeriod) {
            super(name, port, heartbeatPeriod, statusPeriod);
            this.formater = new HomemadeFormater();
        }
    }
    //attributes
    private HomemadeServerThread thread;
    // serializable
    private int port;
    private long heartbeatPeriod;
    private long statusPeriod;
    private LinkedList<HMSubSystem> subSystems;

    // property
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getHeartbeatPeriod() {
        return heartbeatPeriod;
    }

    public void setHeartbeatPeriod(long heartbeatPeriod) {
        this.heartbeatPeriod = heartbeatPeriod;
    }

    public long getStatusPeriod() {
        return statusPeriod;
    }

    public void setStatusPeriod(long statusPeriod) {
        this.statusPeriod = statusPeriod;
    }

    // delegate
    @Override
    public void lazyInitializeSubSystems() {
        if (subSystems == null) {
            subSystems = new LinkedList<HMSubSystem>();
        }
    }

    @Override
    public boolean addSubSystem(HMSubSystem e) {
        lazyInitializeSubSystems();
        return subSystems.add(e);
    }

    @Override
    public void addSubSystem(int index, HMSubSystem e) {
        lazyInitializeSubSystems();
        subSystems.add(index, e);
    }

    @Override
    public boolean removeSubSystem(HMSubSystem o) {
        lazyInitializeSubSystems();
        return subSystems.remove(o);
    }

    @Override
    public HMSubSystem removeSubSystem(int i) {
        lazyInitializeSubSystems();
        return subSystems.remove(i);
    }

    @Override
    public HMSubSystem getSubSystem(int index) {
        lazyInitializeSubSystems();
        return subSystems.get(index);
    }

    @Override
    public Iterable<HMSubSystem> getSubSystems() {
        lazyInitializeSubSystems();
        return subSystems;
    }

    @Override
    protected void startAgent() {
        this.thread = new HomemadeServerThread(this.getName(), port, heartbeatPeriod, statusPeriod);
        this.thread.start();
    }

    @Override
    protected void stopAgent() {
        this.thread.forceInterrupt();
    }

    @Override
    protected void restartAgent() {
        this.thread.restartSender();
    }
        
    @Override
    public void buildDpl(Database db) {
        // do nothing yet
    }
    
    // constructor
    private HomemadeService() {
        super("");
    }

    public HomemadeService(String name, int port, long heartbeatPeriod, long statusPeriod) {
        super(name);
        this.port = port;
        this.heartbeatPeriod = heartbeatPeriod;
        this.statusPeriod = statusPeriod;
    }
}
