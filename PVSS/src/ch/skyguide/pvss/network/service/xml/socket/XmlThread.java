/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml.socket;

import ch.skyguide.pvss.network.service.Service.Status;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.logging.Level;
import javax.swing.SwingUtilities;

/**
 *
 * @author caronyn
 */
public abstract class XmlThread extends Thread {

    // inner class
    protected class RunLog implements Runnable {

        private Level level;
        private String message;

        @Override
        public void run() {
            simulatorLog(level, message);
        }

        public RunLog(String message, Object... args) {
            this.level = Level.INFO;
            this.message = String.format(message, args);
        }

        public RunLog(Level level, String message, Object... args) {
            this.level = level;
            this.message = String.format(message, args);
        }
    }

    protected class RunStatus implements Runnable {

        @Override
        public void run() {
            setSimulatorStatus();
        }
    }
    // attributes
    private Status status;
    protected boolean forceInterupt = false;
    private XmlMessageFormater formater;

    // property
    public Status getStatus() {
        return status;
    }

    protected void setStatus(Status status) {
        this.status = status;
        SwingUtilities.invokeLater(new RunStatus());
    }

    // methodes
    public void forceInterrupt() {
        forceInterupt = true;
        this.interrupt();
    }

    private String read(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        int c = reader.read();
        while (c != -1 && c != (int) formater.getDelimiter().getCharacter()) {
            sb.append((char) c);
            c = reader.read();
        }

        return sb.toString();
    }

    protected void sendObject(Socket client, Object obj) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());

        String xml = formater.getXstream().toXML(obj);
        String msg = formater.format(xml);
        out.append(msg);
        out.flush();

        SwingUtilities.invokeLater(new RunLog("Message sent\n%s", formater.removeSpecialChar(xml)));
    }

    protected void receiveObject(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String msg = read(in);
        String xml = formater.unFormat(msg);

        // TODO deserializer le message reçu avec XStream
        // Object obj = formater.getXstream().fromXML(msg);

        SwingUtilities.invokeLater(new RunLog(Level.INFO, "Message received\n%s", xml));
    }

    protected void disconnect(Socket socket) throws IOException {
        if (socket != null) {
            socket.close();
        }
    }

    public abstract void setSimulatorStatus();

    public abstract void simulatorLog(Level level, String message);

    public XmlThread(XmlMessageFormater formater) {
        this.formater = formater;
    }
}
