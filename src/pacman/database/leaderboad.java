/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacman.database;

import com.sun.jdi.connect.spi.Connection;

/**
 *
 * @author cex
 */
public class leaderboad {
    

    public Connection openConnection() {

        try{

            String username = "sqlUser";

            String password = "sqlpassword";

            String database = "databaseName";

            Class.forName("com.mysql.jdbc.Driver");

            Connection connection;

            connection = DriverManager.getConnection(

                    "jdbc:mysql://localhost:3306/" + database, username, password

            );

            if(connection == null){

                System.out.println("Database connection fail");

            }else{

                System.out.println("Database connection success");

            }

            return connection;

        }catch(ClassNotFoundException | SQLException e){

            System.out.println(e);

            return null;

        }

    }

    public void closeConnection(Connection conn) {

        try{

            if(conn !=null && !conn.isClosed()){

                conn.close();

                System.out.println("Connection closed");

            }

        }catch(SQLException e){

            System.out.println(e);

        }

    }

    public ResultSet runQuery(Connection conn, String query) {

        try{

            Statement stmt = conn.createStatement();

            ResultSet result = stmt.executeQuery(query);

            return result;

        }catch(SQLException e){

            System.out.println(e);

            return null;

        }

    }

    public int executeUpdate(Connection conn, String query) {

          try{

            Statement stmt = conn.createStatement();

            int result = stmt.executeUpdate(query);

            return result;

        }catch(SQLException e){

            System.out.println(e);

            return -1;

        }

    }    





}
