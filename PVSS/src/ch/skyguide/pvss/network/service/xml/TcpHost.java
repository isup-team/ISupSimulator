/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml;

/**
 *
 * @author caronyn
 */
public class TcpHost {

    String address;
    int port;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private TcpHost() {
    }

    public TcpHost(String host, int port) {
        this.address = host;
        this.port = port;
    }
}
