package pl.plajerlair.core.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * MySQL database handler, used for connecting to database and providing
 * basic methods for executing queries and updates
 *
 * @since 1.2.3
 */
public class MySQLDatabase {

    private MySQLConnectionManager manager;
    private JavaPlugin plugin;

    public MySQLDatabase(JavaPlugin javaPlugin, String address, String user, String password, int minConn, int maxConn) {
        this.plugin = javaPlugin;
        this.manager = new MySQLConnectionManager(plugin, address, user, password, minConn, maxConn);
        plugin.getLogger().log(Level.INFO, "Configuring MySQL connection!");
        manager.configureConnPool();

        Connection connection = manager.getConnection();
        if(connection == null) {
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to database!");
            return;
        }
        manager.closeConnection(connection);
    }

    public void executeUpdate(String query) {
        try {
            Connection connection = manager.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            manager.closeConnection(connection);
        } catch(SQLException e) {
            plugin.getLogger().warning("Failed to execute update: " + query);
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            Connection connection = manager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            manager.closeConnection(connection);
            return rs;
        } catch(SQLException exception) {
            exception.printStackTrace();
            plugin.getLogger().warning("Failed to execute request: " + query);
            return null;
        }
    }

    public MySQLConnectionManager getManager() {
        return manager;
    }
}
