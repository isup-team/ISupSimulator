/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 *
 * @author caronyn
 */
public class SNMPAgentPool {

    // attributes
    private static SNMPAgentPool instance;
    Dictionary<Integer, SNMPAgent> agents;
    Dictionary<Integer, Integer> agentStarted;

    // function
    private void removeAgentInstance(int port) {
        agents.remove(port);
    }

    // attribute
    public SNMPAgent getAgentInstance(SNMPAgentService service) throws UnknownHostException, IOException {
        SNMPAgent agent = agents.get(service.getPort());

        // Lazy initialization
        if (agent == null) {
            agent = new SNMPAgent(service);
            agent.addContext(service.getContext());
            agents.put(service.getPort(), agent);
        } else {
            agent.stop();
        }

        agent.addContext(service.getContext());
        agent.start();
        return agent;

    }

    private Integer getAgentStarter(int port) {
        Integer as = agentStarted.get(port);
        if (as == null) {
            return 0;
        }

        return as;
    }

    // methode
    public void tryStart(int port) throws IOException {
        SNMPAgent agent = agents.get(port);
        if (agent == null) {
            throw new IOException("Agent does not exist !");
        }

        if (!agent.isRunning()) {
            agent.start();
        }

        Integer as = getAgentStarter(port);
        agentStarted.put(port, as + 1);
    }

    public void tryStop(int port) throws IOException {
        SNMPAgent agent = agents.get(port);
        if (agent == null) {
            throw new IOException("Agent does not exist !");
        }

        int as = getAgentStarter(port);

        if (as <= 1) {
            if (agent.isRunning()) {
                agent.stop();
            }
            removeAgentInstance(port);
        }


        agentStarted.put(port, as - 1);



    }

    // Constructor singleton
    public static SNMPAgentPool getInstance() {
        // Lazy initialization
        if (instance == null) {
            instance = new SNMPAgentPool();
        }

        return instance;
    }

    private SNMPAgentPool() {
        agents = new Hashtable<Integer, SNMPAgent>();
        agentStarted = new Hashtable<Integer, Integer>();
    }
}
