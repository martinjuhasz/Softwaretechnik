package de.teamrocket.relaxo.util.config;

import com.google.inject.Singleton;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Diese Klasse enthält Konfigurationsparameter der Anwendung.
 */
@Singleton
public class Config {

    // Static

    /**
     * Die instanz der Klasse.
     */
    private static Config config;
    /**
     * Hält die Key-Value-Paare
     */
    private Map<String, String> keyValue;

    // Vars

    /**
     * Erstellt die Config mit der DefaultConfig.
     */
    private Config() {
        this.keyValue = new LinkedHashMap<>(); // LinkedHashMap behält die insert-reihenfolge bei
        this.loadDefaultConfig();
    }


    // Constructor

    /**
     * Gibt die Instanz von der Config zurück.
     *
     * @return Die Config-Instanz.
     */
    public static Config getSingleton() {
        if (config == null) {
            config = new Config();
        }

        return config;
    }

    // Methods

    /**
     * Lädt die Default-Configuration der Config und überschreibt die entsprechenden Key-Value.
     * Nicht in der Default-Config enthaltenden Key-Value werden nicht ersetzt.
     */
    public void loadDefaultConfig() {
        InputStream in = Config.class.getClassLoader().getResourceAsStream("app.config");

        if(in==null){
            throw new ConfigException("URL-Resource app.config nicht gefunden.");
        }

        parseConfigFile(in);
    }

    /**
     * Parste die übergebene Datei und setzt die entsprechenden Configurationsparameter in die KeyValue-Liste
     *
     * @param stream Die zu parsende Datei.
     */
    public void parseConfigFile(InputStream stream) {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

            String zeile;
            while ((zeile = in.readLine()) != null) {
                // ein Kommentar?
                if (zeile.startsWith("#")) {
                    continue;
                }

                // Leerzeile?
                if (zeile.trim().equals("")) {
                    continue;
                }

                // : gefunden? -> aufauf!
                if (zeile.contains(":")) {
                    String[] keyValue = zeile.split(":", 2); // nur ein mal splitten, damit man in den Values auch ein : haben kann!

                    if (keyValue.length == 2) {
                        this.keyValue.put(keyValue[0].trim(), keyValue[1].trim());
                    } else {
                        throw new ConfigFileException("Could not read Line: " + zeile + " i duno wy.");
                    }
                } else {
                    throw new ConfigFileException("Could not read Line: " + zeile + " no : found.");
                }
            }

        } catch (Exception e) {
            throw new ConfigFileException("Could not read File: " + e.getMessage());
        }
    }

    // GETTER

    /**
     * Gibt den Config-Value des übergebenen Config-Schlüssels wieder.
     *
     * @param key Der Config-Schlüssel.
     * @return Der Wert als bool gecastet.
     */
    public boolean getBool(String key) {
        if (this.keyValue.containsKey(key)) {
            try {
                return Boolean.parseBoolean(this.keyValue.get(key));
            } catch (NumberFormatException e) {
                throw new ConfigCastException("Key " + key + " mit dem Wert " + this.keyValue.get(key) + " kann nicht als boolean gecastet werden.");
            }
        } else {
            throw new ConfigException("Key " + key + " not defined.");
        }
    }

    /**
     * Gibt den Config-Value des übergebenen Config-Schlüssels wieder.
     *
     * @param key Der Config-Schlüssel.
     * @return Der Wert als int gecastet.
     */
    public int getInt(String key) {
        if (this.keyValue.containsKey(key)) {
            try {
                return Integer.parseInt(this.keyValue.get(key));
            } catch (NumberFormatException e) {
                throw new ConfigCastException("Key " + key + " mit dem Wert " + this.keyValue.get(key) + " kann nicht als int gecastet werden.");
            }
        } else {
            throw new ConfigException("Key " + key + " not defined.");
        }
    }

    /**
     * Gibt den Config-Value des übergebenen Config-Schlüssels wieder.
     *
     * @param key Der Config-Schlüssel.
     * @return Der Wert als String.
     */
    public String getString(String key) {
        if (this.keyValue.containsKey(key)) {
            return this.keyValue.get(key);
        } else {
            throw new ConfigException("Key " + key + " not defined.");
        }
    }

    /**
     * Gibt den Config-Value des übergebenen Config-Schlüssels wieder.
     *
     * @param key Der Config-Schlüssel.
     * @return Der Wert als double gecastet.
     */
    public double getDouble(String key) {
        if (this.keyValue.containsKey(key)) {
            try {
                return Double.parseDouble(this.keyValue.get(key));
            } catch (NumberFormatException e) {
                throw new ConfigCastException("Key " + key + " mit dem Wert " + this.keyValue.get(key) + " kann nicht als double gecastet werden.");
            }
        } else {
            throw new ConfigException("Key " + key + " not defined.");
        }
    }

    /**
     * Gibt die Key-Value-Map der Config zurück.
     *
     * @return Die Key-Value-Map.
     */
    public Map<String, String> getKeyValueMap() {
        return this.keyValue;
    }

    // SETTER

    /**
     * Überschreibt/Setzt einen Wert der Config und gibt true zurück, falls dieser schon exisiterte.
     *
     * @param key   Der Schlüssel der Config.
     * @param value Der Wert der Config als String.
     * @return True, wenn dieser überschrieben worde, sonst false.
     */
    public boolean set(String key, String value) {
        boolean warSchonDrin = this.keyValue.containsKey(key);
        this.keyValue.put(key, value);
        return warSchonDrin;
    }
}
