package com.company;

import javax.xml.stream.events.EntityReference;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Connection conn = null;
        Scanner scanner = new Scanner(System.in);
        ArrayList<User> contacts = new ArrayList<>();

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

            //register as a new user in DB.
            //login as a user in program
            int successful = 0;
/*            while (successful != 1) {
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

            while (name == null) {
                System.out.println("Please, enter your name.");
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
                } catch (SQLException ex) {
                    System.out.println("Problem");
                }
            }
            user = new User(name);

            userID = getUserID(conn, name, userID);
            user.setUser_id(userID);
            System.out.println("you are now logged in as: " + user.getUsername());

            // create contactList
            query = "SELECT user.id, user.user_name FROM `contacts` Inner JOIN user ON contacts.contact_id = user.id WHERE user_id = "
                    + user.getUser_id();
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    User contact = new User(name = rs.getString("user.user_name"));
                    userID = getUserID(conn, name, userID);
                    contact.setUser_id(userID);
                    contacts.add(contact);
                }
            } catch (SQLException ex) {
                throw new Error("Problem", ex);
            }
            System.out.println("Your contact list is updated.");
/*
            //add new contact to List
            successful = 0;
            System.out.println("Pleas enter the name of the contact you want to add to your list.");
            while (successful != 1) {
                name = scanner.nextLine();
                command = "INSERT INTO `contacts` (`user_id`,`contact_id`) VALUES( " +
                        "(SELECT user.id FROM user WHERE user.user_name = '" + user.getUsername() + "'), " +
                        "(SELECT user.id FROM user WHERE user.user_name = '" + name + "'))";
                try {
                    stmt = conn.createStatement();
                    successful = stmt.executeUpdate(command);
                } catch (SQLException ex){
                    System.out.println("The chosen name does not exist. Please try again.");
                }
            }
            User contact = new User(name);
            userID = getUserID(conn, name, userID);
            contact.setUser_id(userID);
            contacts.add(contact);

            System.out.println("The contact " + name + "was added to your list.");


 */
            //todo: send message to other user from your contactList
            String sender = "";
            System.out.println("Choose a partner for your conversation from your list");
            for (User contact : contacts) {
                System.out.print(contact.getUsername() + ", ");
            }
            System.out.println("");
            successful = 0;
            while (successful != 1) {
                name = scanner.nextLine();
                for (User contact : contacts) {
                    if (contact.getUsername().equalsIgnoreCase(name)) {
                        successful = 1;
                        System.out.println("previous conversation with " + name + ":");
                        query = "SELECT * FROM `chat` WHERE " +
                                "(`sender_id` = " + user.getUser_id() + " OR `sender_id` = " + contact.getUser_id() +
                                ") AND " +
                                "(`receiver_id` = " + user.getUser_id() + " OR `receiver_id` = " + contact.getUser_id() + ")" +
                                "AND sended_time > DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -2 MONTH)" +
                                "LIMIT 20";
                        ResultSet rs = stmt.executeQuery(query);
                        while (rs.next()) {
                            String time = rs.getTimestamp("sended_time").toString();
                            int sender_id = rs.getInt("sender_id");
                            if (sender_id == user.getUser_id()) {
                                sender = "me";
                            }
                            if (sender_id == contact.getUser_id()) {
                                sender = contact.getUsername();
                            }
                            String text = rs.getString("text");
                            System.out.println(time + "\tfrom " + sender + "\n" + text);
                        }
                        System.out.println("\n\nwrite your message.");
                        String text = scanner.nextLine();
                        command = "INSERT INTO `chat`(`sender_id`, `receiver_id`, `text`) " +
                                "VALUES (" + user.getUser_id() + "," + contact.getUser_id() + ", '" + text + "')";
                        stmt = conn.createStatement();
                        stmt.executeUpdate(command);
                    }


                }

                //todo: receive messages
                if (successful != 1) {
                    System.out.println("This name is not in your contact list. Try again.");
                }



            }
        } catch (SQLException ex) {

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
            while (rs.next()) {
                userID = rs.getInt("id");
            }
        } catch (SQLException ex) {
            throw new Error("Problem", ex);
        }
        return userID;
    }
}
