package com.company;

import javax.xml.stream.events.EntityReference;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Connection conn = null;
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> contacts = new ArrayList<>();

        try {
            String url = "jdbc:mysql://localhost:3306/chat_program?user=root";
            conn = DriverManager.getConnection(url);
            System.out.println("connected");
            Statement stmt;
            String command;
            String query = "";
            User user;
            String name = "";
            int userID = 0;
/*
            //register as a new user in DB.
            //login as a user in program
            int successful = 0;
            while (successful != 1) {
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
            userID = getUserID(conn, name, userID);
            user.setUser_id(userID);


   */
            //login as user
            //todo: add passwordOption
            user = null;
            name = null;
            command = "";
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

            userID = getUserID(conn, name, userID);
            user.setUser_id(userID);
            System.out.println("you are now logged in as: " + user.getUsername());

            // create contactList
            //todo: add new contact to List
            query = "SELECT user.id, user.user_name FROM `contacts` Inner JOIN user ON contacts.contact_id = user.id WHERE user_id = "
                    + user.getUser_id();
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    contacts.add(name = rs.getString("user.user_name"));
                }
            } catch (SQLException ex){
                throw new Error("Problem", ex);
            }

            //todo: send message to other user from your contactList
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

    private static int getUserID(Connection conn, String name, int userID) {
        String query;
        Statement stmt;
        query = "SELECT `id` FROM `user` WHERE `user_name` = '" + name + "'";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                userID = rs.getInt("id");
            }
        } catch (SQLException ex){
            throw new Error("Problem", ex);
        }
        return userID;
    }
}
