/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author caronyn
 */
public abstract class ServerThread extends SocketThread implements MessageProvider {

    // inner class
    private class Heartbeat extends Thread {

        Socket server;
        long period;

        @Override
        public synchronized void run() {
            log("Heartbeat started for %s on port %s.", server.getInetAddress().toString(), server.getPort());
            Long time = 0L;

            try {

                while (!this.isInterrupted() && !ServerThread.this.forceInterupt) {
                    time = new Date().getTime();

                    String msg = ServerThread.this.getHeartbeatMessage();
                    send(server, msg);
                    log("Heartbeat sent\n%s", getFormater().removeSpecialChar(msg));

                    try {
                        SocketUtil.waitRest(this, period, time);
                    } catch (InterruptedException ex) {
                        if (ServerThread.this.forceInterupt) {
                            this.interrupt();
                        }
                    }
                }

            } catch (IOException ex) {
                log(Level.WARNING, ex.getMessage());
            }

            log("Heartbeat stopped for %s on port %s.", server.getInetAddress().toString(), server.getPort());
        }

        public Heartbeat(ThreadGroup group, Socket server, long period) {
            super(group, Heartbeat.class.getName());
            this.server = server;
            this.period = period;
        }
    }

    private class Receiver extends Thread {

        Socket server;

        @Override
        public synchronized void run() {
            log("Receiver started for %s on port %s.", server.getInetAddress().toString(), server.getPort());

            try {
                while (!ServerThread.this.forceInterupt) {
                    String msg = receive(server);
                    ServerThread.this.messageReceived(msg);
                    log(Level.INFO, "Message received\n%s", msg);
                }
            } catch (IOException ex) {
                log(Level.WARNING, ex.getMessage());
            }

            log("Receiver stopped for %s on port %s.", server.getInetAddress().toString(), server.getPort());
        }

        public Receiver(ThreadGroup group, Socket server) {
            super(group, Receiver.class.getName());
            this.server = server;
        }
    }

    private class StatusSender extends Thread {

        Socket server;
        long period;

        @Override
        public synchronized void run() {
            log("Status sender started for %s on port %s.", server.getInetAddress().toString(), server.getPort());
            Long time = 0L;

            try {

                while (!this.isInterrupted() && !ServerThread.this.forceInterupt) {
                    time = new Date().getTime();

                    String msg = ServerThread.this.getStatusMessage();
                    send(server, msg);
                    log("Status sent\n%s", getFormater().removeSpecialChar(msg));
                    try {
                        SocketUtil.waitRest(this, period, time);
                    } catch (InterruptedException ex) {
                        if (ServerThread.this.forceInterupt) {
                            this.interrupt();
                        }
                    }
                }

            } catch (IOException ex) {
                log(Level.WARNING, ex.getMessage());
            }

            log("Status sender stopped for %s on port %s.", server.getInetAddress().toString(), server.getPort());
        }

        public StatusSender(ThreadGroup group, Socket server, long period) {
            super(group, Receiver.class.getName());
            this.server = server;
            this.period = period;
        }
    }
    // constant
    private static final int TIMEOUT = 2000;
    // attribute
    private List<Socket> servers;
    private List<ThreadGroup> threads;
    // attribute
    protected String name;
    protected int port;
    private long heartbeatPeriod;
    private long statusPeriod;

    public void restartSender() {
        for (ThreadGroup tg : threads) {
            tg.interrupt();
        }
    }

    @Override
    public void run() {
        log("Thread Started for %s on port %s.", name, port);
        setRunning(true);
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(TIMEOUT);
        } catch (IOException ex) {
            log(Level.WARNING, ex.getMessage());
        }

        while (!isInterrupted()) {
            try {

                Socket server = serverSocket.accept();
                servers.add(server);
                log("Connection accepted %s:%s.", server.getInetAddress().toString(), server.getPort());
                ThreadGroup threadGroup = new ThreadGroup(server.getInetAddress().toString() + ":" + server.getPort());
                threads.add(threadGroup);

                // threads
                new Heartbeat(threadGroup, server, heartbeatPeriod).start();
                new Receiver(threadGroup, server).start();
                new StatusSender(threadGroup, server, statusPeriod).start();

            } catch (IOException ex) {
                // do nothing
            }
        }

        this.forceInterupt = true;
        for (ThreadGroup threadGroup : threads) {
            threadGroup.interrupt();
        }
        threads.clear();

        try {
            for (Socket server : servers) {
                server.close();
            }
            serverSocket.close();
            servers.clear();
        } catch (IOException ex) {
            log(Level.WARNING, ex.getMessage());
        }

        setRunning(false);
        log("Thread Stopped for %s on port %s.", name, port);

    }

    public ServerThread(String name, int port, long heartbeatPeriod, long statusPeriod) {
        this.name = name;
        this.port = port;
        this.heartbeatPeriod = heartbeatPeriod;
        this.statusPeriod = statusPeriod;
        threads = new ArrayList<ThreadGroup>();
        servers = new ArrayList<Socket>();
    }
}
