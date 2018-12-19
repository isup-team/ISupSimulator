/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.conf;

import ch.skyguide.pvss.persistency.NullXStreamFormater;
import ch.skyguide.pvss.persistency.PersistenceStrategy;
import ch.skyguide.pvss.persistency.XmlFilePersistenceStrategy;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Application config file manager.
 * @author caronyn
 */
public class Config {

    // const
    public static final String CONFIG_FILE_NAME = "conf.xml";
    public static final int MAX_RECENT_FILE = 10;
    public static final PersistenceStrategy<Config> PERSISTENCE_STRATEGY = new XmlFilePersistenceStrategy<Config>(new NullXStreamFormater(), CONFIG_FILE_NAME);
    public static final String SIM_EXTENSION = "xml";
    public static final String DPL_EXTENSION = "dpl";
    // attributes
    private static Config singleton;
    private String lastServiceFile, lastExportFile;
    private List<String> recentServiceFile;

    // function
    private static boolean isConfigFileExist() {
        File file = new File(CONFIG_FILE_NAME);
        return file.exists();
    }

    private void serialize() {
        PERSISTENCE_STRATEGY.storeObjectTree(this);
    }

    private static void slideWindow(List list) {
        LinkedList<String> linkedList = (LinkedList<String>) list;
        if (list.size() > MAX_RECENT_FILE) {
            list.remove(0);
        }
    }

    private String fileExist(String filename) {
        if (filename != null && !"".equals(filename)) {
            File file = new File(filename);
            if (file.exists()) {
                return filename;
            }
        }
        return "";
    }

    private void deleteNotExist(List list) {
        LinkedList<String> linkedList = new LinkedList<String>(list);
        boolean found = false;

        for (String filename : linkedList) {
            File file = new File(filename);
            if (!file.exists()) {
                list.remove(filename);
                found = true;
            }
        }

        if (found) {
            serialize();
        }
    }

    private <T> void addUniqueToList(List list, T item) {
        if (list.contains(item)) {
            list.remove(item);
        }

        list.add(item);
        slideWindow(list);
        serialize();
    }

    // property
    /**
     * Restore last service file used.
     * @return Return the last service filename.
     */
    public String getLastServiceFile() {
        return fileExist(lastServiceFile);
    }

    public String getLastExportFile() {
        return fileExist(lastExportFile);
    }

    /**
     * Store last service file used.
     * @param lastServiceFile the last service filename.
     */
    public void setLastServiceFile(String lastServiceFile) {
        this.lastServiceFile = lastServiceFile;
        serialize();
    }

    public void setLastExportFile(String lastExportFile) {
        this.lastExportFile = lastExportFile;
        serialize();
    }

    /**
     * Restore the list of recent service file loaded.
     * @return the list of recent filenames.
     */
    public List<String> getRecentServiceFile() {
        deleteNotExist(recentServiceFile);
        return recentServiceFile;
    }

    // methode
    /**
     * Add a new recent file to the list.
     * @param filename Filename to add.
     */
    public void addRecentServiceFile(String filename) {
        addUniqueToList(recentServiceFile, filename);
    }

    // constructor
    /**
     * Default constructor private for singleton design pattern.
     */
    private Config() {
        recentServiceFile = new LinkedList<String>();
    }

    /**
     * Get the static instance of the singleton.
     * @return The static instance.
     */
    public static Config getInstance() {
        if (singleton == null) {
            try {
                if (isConfigFileExist()) {
                    singleton = PERSISTENCE_STRATEGY.retrieveObjectTree();
                } else {
                    singleton = new Config();
                }
            } catch (Exception ex) {
                singleton = new Config();
            }
        }

        return singleton;
    }
}
