package de.teamrocket.relaxo.persistence;

import de.teamrocket.relaxo.util.config.ConfigException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Klasse zum bereitstellen einer SQL Session
 */
public class SessionFactory {

    private static SqlSessionFactory factory;

    private SessionFactory() {
    }

    static {
        Reader reader;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
        } catch (IOException e) {
            throw new ConfigException(e.getMessage());
        }
        factory = new SqlSessionFactoryBuilder().build(reader);
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return factory;
    }

}
