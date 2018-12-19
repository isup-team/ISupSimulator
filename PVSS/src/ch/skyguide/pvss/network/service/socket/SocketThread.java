/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author caronyn
 */
public abstract class SocketThread extends Thread {

    // constant
    private final static Logger LOGGER = Logger.getLogger(SocketThread.class.getName());
    // attributes
    protected boolean forceInterupt = false;
    private boolean running = false;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
        this.statusChanged(running);
    }

    public void forceInterrupt() {
        forceInterupt = true;
        this.interrupt();
    }

    protected void log(final Level level, String msg, Object... args) {
        final String message = String.format(msg, args);

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                LOGGER.log(level, message);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    protected void log(String message, Object... args) {
        log(Level.INFO, message, args);
    }

    private String read(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        int c = reader.read();
        //while (c != -1 && c != (int) getFormater().getDelimiter().getCharacter()) {
        while (c != -1 && sb.toString().endsWith(getFormater().getDelimiter().getCharacter())) {
            sb.append((char) c);
            c = reader.read();
        }

        return sb.toString();
    }

    protected void send(Socket client, String msg) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());

        String message = getFormater().format(msg);
        out.append(message);
        out.flush();
    }

    protected String receive(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String msg = read(in);
        return getFormater().unFormat(msg);
    }

    protected void disconnect(Socket socket) throws IOException {
        if (socket != null) {
            socket.close();
        }
    }

    // abstract
    public abstract MessageFormater getFormater();

    public abstract void statusChanged(boolean isRunning);
}
