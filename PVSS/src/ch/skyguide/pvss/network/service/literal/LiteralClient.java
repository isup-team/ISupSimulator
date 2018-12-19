/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.literal;

import ch.skyguide.pvss.network.service.socket.MessageDelimiter;
import ch.skyguide.pvss.network.service.xml.*;
import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.network.service.ServiceLeaf;
import ch.skyguide.pvss.network.service.socket.MessageFormater;
import ch.skyguide.pvss.network.service.socket.SocketThread;
import ch.skyguide.pvss.network.service.socket.SocketUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caronyn
 */
public class LiteralClient extends ServiceLeaf {

    private class LiteralFormater implements MessageFormater {
        
        @Override
        public MessageDelimiter getDelimiter() {
            return LiteralClient.this.getDelimiter();
        }

        @Override
        public String removeSpecialChar(String msg) {
            return msg;
        }

        @Override
        public String format(String msg) {
		StringBuilder sb = new StringBuilder();

		sb.append(removeSpecialChar(msg));
		sb.append(delimiter.getCharacter());

		return sb.toString();
        }

        @Override
        public String unFormat(String msg) {
		String result = msg;

		result = removeSpecialChar(result);

		return result;
        }
    }

    // inner class
    private class LiteralClientThread extends SocketThread {

        // constant
        private String message;
        public static final int TIMEOUT = 2000;

        // constructor
        public LiteralClientThread() {
            this.formater = new LiteralFormater();
        }
        // attributes
        private MessageFormater formater;

        private Socket connect() throws UnknownHostException, IOException {
            Socket client = new Socket();

            InetAddress inetAddress = InetAddress.getByName(LiteralClient.this.getAddress());
            InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, LiteralClient.this.getPort());

            client.connect(socketAddress, TIMEOUT);
            client.setSoTimeout(TIMEOUT);

            return client;
        }

        @Override
        public MessageFormater getFormater() {
            return formater;
        }

        @Override
        public void statusChanged(boolean isRunning) {
            setStatus();
        }

        public synchronized void sendMessage(String message) {
            this.message = message;
            this.notify();
        }

        @Override
        public synchronized void run() {
            String name = LiteralClient.this.getName();
            String address = LiteralClient.this.getAddress();
            int port = LiteralClient.this.getPort();
            log("Thread Started for %s on %s:%s.", name, address, port);
            setRunning(true);
            Socket client = null;

            while (!this.isInterrupted()) {
                try {
                    if (client == null) {
                        client = connect();
                    }

                    this.wait();

                    send(client, getFormater().format(message));
                    log("Status sent\n%s", getFormater().removeSpecialChar(LiteralClient.this.getMessage()));

                    String msg = receive(client);
                    log("Ack received\n%s", getFormater().removeSpecialChar(msg));

                    if (!LiteralClient.this.isKeptAlive()) {
                        disconnect(client);
                        client = null;
                    }
                } catch (InterruptedException ex) {
                    if (forceInterupt) {
                        this.interrupt();
                    }
                } catch (IOException ex) {
                    log(Level.WARNING, ex.getMessage() + " for %s on %s:%s.", name, address, port);
                }

            }
            try {
                disconnect(client);
            } catch (IOException ex) {
                Logger.getLogger(XmlClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            setRunning(false);
            log("Thread Stopped for %s on %s:%s.", name, address, port);

        }
    }
    // constant
    // attribute
    private LiteralClientThread thread;
    private XmlClientService parent;
    // serializable
    private TcpHost tcpHost;
    private MessageDelimiter delimiter;
    private boolean keepAlive;
    private String message;

    // property
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    public void setAddress(String address) {
        tcpHost.setAddress(address);
    }

    public void setPort(int port) {
        tcpHost.setPort(port);
    }

    public String getAddress() {
        return tcpHost.getAddress();
    }

    public int getPort() {
        return tcpHost.getPort();
    }
    
    public MessageDelimiter getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(MessageDelimiter delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isKeptAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public XmlClientService getParent() {
        return parent;
    }

    public void setParent(XmlClientService parent) {
        this.parent = parent;
    }

    public void setStatus() {
        boolean areAllStarted = true;
        boolean areAllStopped = true;

        if (thread == null) {
            setStatus(Status.STOPPED);
        } else {
            if (thread.isRunning()) {
                areAllStopped = false;
            } else {
                areAllStarted = false;
            }
        }

        if (areAllStarted) {
            setStatus(Status.STARTED);
        } else if (areAllStopped) {
            setStatus(Status.STOPPED);
        } else {
            setStatus(Status.MITIGATED);
        }
    }

    public void sendMessage() {
        if (thread != null) {
            thread.sendMessage(message);
        }
    }

    // methode implementation
    @Override
    protected void startAgent() {
        thread = new LiteralClientThread();
        thread.start();
    }

    @Override
    protected void stopAgent() {
        if (thread != null) {
            thread.forceInterrupt();
        }
    }

    @Override
    protected void restartAgent() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void buildDpl(Database db) {
    }

    // constructor
    private LiteralClient() {
        this("", MessageDelimiter.ZERO);
    }

    public LiteralClient(String name, MessageDelimiter delimiter) {
        super(name);
        this.tcpHost = new TcpHost("127.0.0.1", 5000);
        this.delimiter = delimiter;
    }
}
