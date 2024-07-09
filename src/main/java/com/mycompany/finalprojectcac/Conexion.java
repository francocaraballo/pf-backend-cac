
package com.mycompany.finalprojectcac;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
    private  Connection connection = null;
    
    public Conexion() {
        if(connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                String URL_DB = "jdbc:mysql://localhost:3306/products";
                String USER = "root";
                String PASSWORD = "4546";
        
                this.connection = DriverManager.getConnection(URL_DB, USER, PASSWORD);

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Este  metodo verifica si la conexion esta cerrada
    public void close() {
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
}
