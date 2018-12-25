package pl.plajerlair.core.database;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.plajerlair.core.utils.internal.InternalUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * MySQL connection manager, configure, shutdown connection, get connection.
 *
 * @since 1.2.3
 */
public class MySQLConnectionManager {

    private BoneCP connectionPool = null;
    private JavaPlugin plugin;
    private String address;
    private String user;
    private String password;
    private int minConn;
    private int maxConn;

    public MySQLConnectionManager(JavaPlugin plugin, String address, String user, String password, int minConn, int maxConn) {
        this.plugin = plugin;
        this.address = address;
        this.user = user;
        this.password = password;
        this.minConn = minConn;
        this.maxConn = maxConn;
    }

    public void configureConnPool() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); //also you need the MySQL driver
            plugin.getLogger().info("Creating BoneCP Configuration...");
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(address);
            config.setUsername(user);
            config.setPassword(password);
            config.setMinConnectionsPerPartition(minConn); //if you say 5 here, there will be 10 connection available
            config.setMaxConnectionsPerPartition(maxConn);
            config.setPartitionCount(2); //2*5 = 10 connection will be available
            //config.setLazyInit(true); //depends on the application usage you should chose lazy or not
            //setting Lazy true means BoneCP won't open any connections before you request a one from it.
            plugin.getLogger().info("Setting up MySQL Connection pool...");
            connectionPool = new BoneCP(config); // setup the connection pool
            plugin.getLogger().info("Connection pool successfully configured. ");
            plugin.getLogger().info("Total connections ==> " + connectionPool.getTotalCreatedConnections());
        } catch(Exception e) {
            e.printStackTrace();
            InternalUtils.errorOccurred();
            Bukkit.getConsoleSender().sendMessage("Cannot save contents to MySQL database!");
            Bukkit.getConsoleSender().sendMessage("Check configuration of mysql.yml file or disable mysql option in config.yml");
        }
    }

    public void shutdownConnPool() {
        try {
            plugin.getLogger().info("Shutting down connection pool. Trying to close all connections.");
            if(connectionPool != null) {
                connectionPool.shutdown(); //this method must be called only once when the application stops.
                //you don't need to call it every time when you get a connection from the Connection Pool
                plugin.getLogger().info("Pool successfully shutdown. ");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = getConnectionPool().getConnection();
            //will get a thread-safe connection from the BoneCP connection pool.
            //synchronization of the method will be done inside BoneCP source

        } catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection(Connection conn) {
        try {
            if(conn != null) {
                conn.close(); //release the connection - the name is tricky but connection is not closed it is released
                //and it will stay in pool
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public BoneCP getConnectionPool() {
        return connectionPool;
    }

}
