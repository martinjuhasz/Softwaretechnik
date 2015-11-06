package de.teamrocket.relaxo;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.teamrocket.relaxo.controller.CommandExecutor;
import de.teamrocket.relaxo.messaging.Broker;
import de.teamrocket.relaxo.persistence.services.ServiceModule;
import de.teamrocket.relaxo.rest.RestServer;
import de.teamrocket.relaxo.util.keyboardinterface.KeyboardInterface;


/*! \mainpage Relaxo - Server
 * \section server Kurzanleitung zur Inbetriebnahme des Servers
 * - Innerhalb des Projekt Explorers mit Rechtsklick unter „Import -> Import“ unter dem Reiter SVN den Punkt „Checkout Projects from SVN“ auswählen.
 * - Folgende URL einfügen: https://scm.mi.hs-rm.de/svn/2014swtpro/2014swtpro01/Server/
 * - Im darauf folgenden Fenster (evtl. nach Eingabe von Benutzername und Passwort) den Unterordner „relaxo“ auswählen und auf „Next >“ drücken.
 * - Es folgt ein Fenster mit 2 Radio-Buttons in dem der zweite Knopf „Check out as project in the workspace“ ausgewählt werden sollte.
 * - Jetzt auf „Finish“ klicken.
 * - Mit einem Rechtsklick auf das Projekt den Punkt „Configure“ und darunter den Punkt „Convert to Maven Project auswählen“
 * - Einmal „Maven-Test“ ausführen. (Unter Run > Run As > Maven test)
 * \subsection config Konfiguration
 * - Datenbank: Im Paket src/main/resources kann die gewünschte Datenbank konfiguriert werden.
 * - Falls gewünscht kann der Port der REST-Schnittstelle in der Datei app.config konfiguriert werden. Diese befindet sich auch unter src/main/resources
 * \subsection start Server starten
 * - Im Paket de.teamrocket.relaxo.main die Klasse App.class starten
 * - In der Serverkonsole mit dem Command "db create" die Datenbank initialisieren
 * - Falls gewünscht kann man mit dem Command "db testdata" Testdaten in die Datenbank schreiben
 */


/**
 * App ist die Hauptklasse des Servers.
 * Sie enthält die main-Methode
 */
public class App {

    // VARS

    /**
     * Instanz der App
     */
    private static App app;

    /**
     * Instanz des RestServers
     */
    private RestServer restServer;

    /**
     * Instanz des Messagebrokers
     */
    private Broker messageBroker;

    /**
     * Instanz des CommandExecutor, welches den Ablauf der Workflows verwaltet.
     */
    private CommandExecutor commandExecutor;

    /**
     * Der Guice-Injector.
     */
    private Injector injector;

    // STATIC METHODS

    /**
     * main-Methode initialisiert eine neue App
     * Aufruf der Methode init() zum initialisieren des Loggers
     * Aufruf der Methode run() zum initialisieren der MainCollection, des MainControllers und
     * des RestServers
     *
     * @param args Aufrufparameter
     */
    public static void main(String[] args) {
        app = new App();
        app.init();
        app.run();
        System.exit(0);
    }

    // METHODS

    /**
     * Initialisiert Guice, messageBroker, commandExecuter und den restServer
     */
    public void init() {

        injector = Guice.createInjector(new RelaxoModule(), new ServiceModule());

        //Broker starten
        messageBroker = injector.getInstance(Broker.class);
        messageBroker.start();

        // JobMovementManager starten
        commandExecutor = injector.getInstance(CommandExecutor.class);

        // RestServer starten
        restServer = injector.getInstance(RestServer.class);
        restServer.start();
    }

    /**
     * Startet ein KeyboardInterface
     * Nachdem das KeyboadInterface beendet wurde wird der messageBroker,
     * der commandExecutor und der restServer gestoppt
     */
    public void run() {

        // Running
        KeyboardInterface ki = injector.getInstance(KeyboardInterface.class);
        ki.interactWithUser();


        // Stopping
        messageBroker.stop();
        commandExecutor.shutdown();
        restServer.stop();
    }
}
