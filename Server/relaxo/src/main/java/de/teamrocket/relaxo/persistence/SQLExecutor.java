package de.teamrocket.relaxo.persistence;

import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;

/**
 * Klasse zum Aufuehren von SQL Scripten auf der in der mybatis-config eingetragenen Datenbank.
 */
public class SQLExecutor {

    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.DATABASE);

    /**
     * Fuehrt das SQL Script mit dem uebergebenen Dateipfad aus.
     *
     * @param filepath Der Dateipfad
     */
    public void executeSqlScript(String filepath) {
        SqlSession sqlSession = SessionFactory.getSqlSessionFactory().openSession(); //SQL-Session von mybatis erstellen
        Reader reader = null;
        try {
            Connection connection = sqlSession.getConnection();
            ScriptRunner runner = new ScriptRunner(connection);
            reader = Resources.getResourceAsReader(filepath); //Datei einlesen
            //runner.setSendFullScript(true); sendet das komplette Script an die Datenbank und fuehrt es aus, daher keine Ausgabe fuer Befehl pro Zeile
            //runner.setAutoCommit(true); wenn sendFullScript == false, commit nach jeder Zeile
            runner.runScript(reader); //script ausfuehren
        } catch (IOException e) {
            LOGGER.warning(e.getMessage(), e);
        } finally {
            sqlSession.close();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.warning(e.getMessage(), e);
                }
            }
        }
    }
}
