/*
 * ISupSimulatorApp.java
 */
package isupsimulator;

import isupsimulator.ch.skyguide.isupSimulator.ui.ISupSimulatorFrame;
import java.awt.SplashScreen;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class ISupSimulatorApp extends SingleFrameApplication {

    private static final int SPLASH_DELAY = 1000;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        // TODO à supprimer
        System.setProperty("java.net.useSystemProxies", "false");

        if (SplashScreen.getSplashScreen() != null) {
            synchronized (this) {
                try {
                    this.wait(SPLASH_DELAY);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ISupSimulatorApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        show(new ISupSimulatorFrame(this));
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of ISupSimulatorApp
     */
    public static ISupSimulatorApp getApplication() {
        return Application.getInstance(ISupSimulatorApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(ISupSimulatorApp.class, args);
    }
}
