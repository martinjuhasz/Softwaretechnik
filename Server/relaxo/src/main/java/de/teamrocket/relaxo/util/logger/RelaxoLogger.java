package de.teamrocket.relaxo.util.logger;

import de.teamrocket.relaxo.util.config.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Dienst zum Loggen von allen möglichen Ereignissen des Systems.
 */
public class RelaxoLogger {

    // Static

    /**
     * ANSI-Zeichen für Farben.
     */
    private static final String ANSI_RESET = "\u001b[0m";
    private static final String ANSI_RED = "\u001b[31m";
    private static final String ANSI_GREEN = "\u001b[32m";
    private static final String ANSI_CYAN = "\u001b[36m";

    /**
     * Weist das Log-Level aus, was alles ausgegeben werden soll. ALL > FINE > INFO > WARNING
     */
    private static Level logLevel = Level.ALL;

    /**
     * Gibt an, ob Farben in der Ausgabe der Logs-Einträge verwendet werden sollen oder nicht.
     */
    private static boolean useColor;

    /**
     * Eine Map mit der identifiziert, welche Typen von Logger ausgaben machen sollen.
     */
    private static Map<RelaxoLoggerType, Boolean> activeLoggertypes = new HashMap<>();

    // Vars

    /**
     * Gibt an, von welchen Typen dieser Logger ist. @see RelaxoLoggerType
     */
    private RelaxoLoggerType type;

    // Constructor

    /**
     * Setzt den Logger aus RelaxoLoggerType.OTHER.
     */
    public RelaxoLogger() {
        this(RelaxoLoggerType.OTHER);
    }

    /**
     * Übergeben wird ein RelaxoLoggerType um zu identifizieren, um welchen Typen Logger es sich handelt.
     *
     * @param type Der Typ.
     */
    public RelaxoLogger(RelaxoLoggerType type) {
        this.type = type;
        Config config = Config.getSingleton();
        for (RelaxoLoggerType rlt : RelaxoLoggerType.values()) {
            activeLoggertypes.put(rlt, true);
        }

        RelaxoLogger.useColor = config.getBool("logger.use_colors");
    }

    // Methods

    /**
     * Gibt eine Nachricht auf die Konsole aus, sofern dieser Typ-Logger aktiv ist und das Level dem gesetzten
     * Log-Level entspricht oder sogar niedriger ist. (Log-Level INFO gibt INFO und auch WARNING aus)
     *
     * @param level   Das Log-Level, auf das sich dieser Log-Eintrag bezieht.
     * @param message Die Nachricht des Log-Eintrags.
     */
    public void log(Level level, String message) {

        if (RelaxoLogger.logLevel != Level.OFF) {

            Calendar cal = Calendar.getInstance();
            DateFormat formatter = new SimpleDateFormat();

            if (activeLoggertypes.containsKey(this.type) && !activeLoggertypes.get(this.type)) {
                return; // do nothing, dont print, ignore it!
            }

            if (level == Level.WARNING) {
                if (RelaxoLogger.logLevel == Level.WARNING || RelaxoLogger.logLevel == Level.INFO || RelaxoLogger.logLevel == Level.FINE || RelaxoLogger.logLevel == Level.ALL) {
                    if (RelaxoLogger.useColor){
                        this.print(ANSI_RED);
                    }
                } else {
                    return; // do nothing, dont print, ignore it
                }
            } else if (level == Level.INFO) {
                if (RelaxoLogger.logLevel == Level.INFO || RelaxoLogger.logLevel == Level.FINE || RelaxoLogger.logLevel == Level.ALL) {
                    if (RelaxoLogger.useColor) {
                        this.print(ANSI_CYAN);
                    }
                } else {
                    return; // do nothing, dont print, ignore it
                }
            } else if (level == Level.FINE) {
                if (RelaxoLogger.logLevel == Level.FINE || RelaxoLogger.logLevel == Level.ALL) {
                    if (RelaxoLogger.useColor) {
                        this.print(ANSI_GREEN);
                    }
                } else {
                    return; // do nothing, dont print, ignore it
                }
            }

            this.print(formatter.format(cal.getTime()));

            this.print(" [");
            this.print(level.getName());
            this.print("] by [" + this.type.toString() + "]: ");
            this.print(message);

            if (RelaxoLogger.useColor){
                this.println(ANSI_RESET);
            }else{
                this.println("");
            }
        }
    }

    /**
     * Gibt eine Zeile mit Zeilenumbruch in der Konsole aus.
     *
     * @param string Das auszugebene String
     */
    private void println(String string){
        this.print(string + "\n"); // NOSONAR - we want output here
    }

    /**
     * Gibt eine Zeile in der Konsole aus.
     *
     * @param string Das auszugebene String
     */
    private void print(String string){
        System.out.print(string); // NOSONAR - we want output here
    }


    /**
     * Gibt wie log eine Nachricht aus, bezieht sich aber immer auf das Level WARNING
     *
     * @param message Die Nachricht des Log-Eintrags.
     */
    public void warning(String message) {
        this.log(Level.WARNING, message);
    }

    /**
     * Gibt wie log eine Nachricht aus, bezieht sich aber immer auf das Level WARNING
     *
     * @param message Die Nachricht des Log-Eintrags.
     * @param e Die Exception.
     */
    public void warning(String message, Exception e) {
        this.log(Level.WARNING, message);
        if(e!=null){
            this.print(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gibt wie log eine Nachricht aus, bezieht sich aber immer auf das Level INFO
     *
     * @param message Die Nachricht des Log-Eintrags.
     */
    public void info(String message) {
        this.log(Level.INFO, message);
    }

    // Getter / Setter

    public static Level isActive() {
        return logLevel;
    }

    public static void setLoggerActive(RelaxoLoggerType loggerType) {
        activeLoggertypes.put(loggerType, true);
    }

    public static void setLoggerInactive(RelaxoLoggerType loggerType) {
        activeLoggertypes.put(loggerType, false);
    }

    public static void setLogLevel(Level logLevel) {
        RelaxoLogger.logLevel = logLevel;
    }

}
