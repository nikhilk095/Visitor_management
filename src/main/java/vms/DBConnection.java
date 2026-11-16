package vms;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Central helper that opens a JDBC connection to the PostgreSQL database (nikhil).
 * The defaults are the PG* settings below. Optionally, settings can be read from
 * db.properties (keys: url, user, password), and they can also be overridden with the
 * system properties vms.db.url, vms.db.user and vms.db.password.
 * Needs the PostgreSQL JDBC driver (postgresql-42.x.x.jar) on the classpath.
 */
public final class DBConnection {

    private static final String PGHOST = "127.0.0.1";
    private static final String PGPORT = "5432";
    private static final String PGDATABASE = "nikhil";
    private static final String PGUSER = "postgres";
    private static final String PGPASSWORD = "Nishantnayak2297";

    private static final Properties CONFIG = new Properties();

    static {
        CONFIG.setProperty("url", "jdbc:postgresql://" + PGHOST + ":" + PGPORT + "/" + PGDATABASE);
        CONFIG.setProperty("user", PGUSER);
        CONFIG.setProperty("password", PGPASSWORD);
        try (InputStream in = DBConnection.class.getResourceAsStream("/db.properties")) {
            if (in != null) {
                CONFIG.load(in);
            }
        } catch (Exception ignored) {
            // fall back to the defaults above
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {
            // the driver jar is missing; getConnection() will report "No suitable driver"
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("vms.db.url", CONFIG.getProperty("url"));
        String user = System.getProperty("vms.db.user", CONFIG.getProperty("user"));
        String password = System.getProperty("vms.db.password", CONFIG.getProperty("password"));
        return DriverManager.getConnection(url, user, password);
    }
}