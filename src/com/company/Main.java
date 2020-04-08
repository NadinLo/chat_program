package com.company;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Connection conn = null;
        Scanner scanner = new Scanner(System.in);
        try {
            String url = "jdbc:mysql://localhost:3306/chat_program?user=root";
            conn = DriverManager.getConnection(url);
            System.out.println("connected");
            Statement stmt;
            String command;
            User user;
            String name = "";

            //register as a new user in DB.
            //login as a user in program
            int successful = 0;
  /*          while (successful != 1) {
                name = scanner.nextLine();
                command = "INSERT INTO `user`(`user_name`) VALUES ('" + name + "')";
                try {
                    stmt = conn.createStatement();
                    successful = stmt.executeUpdate(command);
                } catch (SQLException ex) {
                    System.out.println("The chosen name ist not valid or already in use. Please try again with another name");
                }
            }
            user = new User(name);


   */
            //login as user
            //todo: add passwordOption
            user = null;
            name = null;
            command = "";
            String query = "";
            stmt = null;
            String login;

            while(name == null) {
                login = scanner.nextLine();
                query = "SELECT `user_name` FROM `user` WHERE `user_name` = '" + login + "'";
                try {
                    stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        name = rs.getString("user_name");
                    } else {
                        System.out.println("This user name does not exist. Please try again.");
                    }
                } catch (SQLException ex){
                    System.out.println("Problem");
                }
            }
            user = new User(name);
            System.out.println("you are now logged in as: " + user.getUsername());

            //todo: create directory

            //todo: send message to other user from your directory
            //todo: receive messages
        } catch (SQLException ex){
            throw new Error("Problem", ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }


    }
}
