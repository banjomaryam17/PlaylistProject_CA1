package persistence;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class MySqlConnector implements Connector{
    private Properties properties;
    private Connection conn = null;

    public MySqlConnector(Connection conn){
        this.conn = conn;
    }
    public MySqlConnector(String propertiesFilename){
        properties = new Properties();
        try{

            String rootPath = Thread.currentThread().getContextClassLoader().getResource(propertiesFilename).getPath();
            properties.load(new FileInputStream(rootPath));
        } catch (IOException e) {
            System.out.println("An exception occured whe trying to load properties from \"" + propertiesFilename + "\": " + e.getMessage());
        }
    }
    public Connection getConnection() {
        conn = null;

        //String driver = "com.mysql.cj.jdbc.Driver";
        String driver = properties.getProperty("driver", "com.mysql.cj.jdbc.Driver");
        //String url = "jdbc:mysql://127.0.0.1:3306/PLAYLISTSMODELS";
        String url = properties.getProperty("url", "jdbc:mysql://127.0.0.1:3306/");
        String database = properties.getProperty("database", "PLAYLISTS");
        //String username = "root";
        String username = properties.getProperty("username", "root");
        //String password = "";
        String password = properties.getProperty("password");

        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url + database, username, password);
        } catch (SQLException e) {
            System.out.println("Connection could not be established, incorrect connection or Database not switched on" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Driver files have not been loaded. Please check pom driver dependencies details." + e.getMessage());
        }
        return conn;
    }
    public void freeConnection(){
        if(conn != null){
            try{
                conn.close();
                conn = null;
            }catch (SQLException e){
                System.out.println("An exception occurred when attempting to close the connection to the database \n " +
                         e.getMessage());
            }
        }
    }
}
